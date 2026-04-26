package net.scratch221171.astralenchant.common.enchantment.handler;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.config.RuntimeConfigState;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.registries.AEDataComponents;
import net.scratch221171.astralenchant.common.util.AEUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class OverMendingHandler {

    @SubscribeEvent
    private static void struckByLightning(EntityStruckByLightningEvent event) {
        if (!(RuntimeConfigState.get(AEConfig.OVER_MENDING))) return;
        if (!(event.getEntity() instanceof Player player)) return;
        ServerLevel level = (ServerLevel) player.level();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getOrDefault(AEDataComponents.OVER_MENDING, 0) >= 100) {
                level.playSound(null , player.getOnPos(), SoundEvents.TRIDENT_THUNDER.value(), SoundSource.PLAYERS);
                double multiplier = RuntimeConfigState.get(AEConfig.OVER_MENDING_LIGHTNING_DAMAGE_MULTIPLIER);
                event.getLightning().setDamage((float) (event.getLightning().getDamage() * multiplier));
//                OverMendingCache.CACHE.put(player, slot);
                stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
                stack.set(DataComponents.ENCHANTMENTS, AEUtils.removeEnchantment(stack.get(DataComponents.ENCHANTMENTS), AEUtils.getEnchantmentHolder(AEEnchantments.OVER_MENDING, level)));
                stack.remove(AEDataComponents.OVER_MENDING);
                level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1, player.getZ(), 1000, 0f, 0f, 0f, 0.5f);
                return;
            }
        }
    }

    @SubscribeEvent
    private static void tooltip(ItemTooltipEvent event) {
        if (!(RuntimeConfigState.get(AEConfig.OVER_MENDING))) return;
        ItemStack stack = event.getItemStack();

        if (AEUtils.getEnchantmentLevel(stack, AEEnchantments.OVER_MENDING) > 0) {
            int progress = stack.getOrDefault(AEDataComponents.OVER_MENDING, 0);
            List<Component> tooltip = event.getToolTip();

            for (int i = 0; i < tooltip.size(); i++) {
                Component entry = tooltip.get(i);

                if (!(entry.getContents() instanceof TranslatableContents contents)) continue;
                String key = contents.getKey();

                if (key.equals("enchantment.astralenchant.over_mending")) {
                    {
                        tooltip.add(i + 1,
                                    (progress < 100 ?
                                            Component.translatable("enchantment.astralenchant.over_mending.tooltip.text", progress)
                                            : Component.translatable("enchantment.astralenchant.over_mending.tooltip.hint"))
                                    .setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
                    }
                    return;
                }
            }
        }
    }

//    @SubscribeEvent
//    private static void survivedLightning(LivingDamageEvent.Post event) {
//        if (!(event.getEntity() instanceof Player player)
//            || !player.isAlive()
//            || !(event.getSource() == player.damageSources().lightningBolt())) return;
//        if (OverMendingCache.CACHE.containsKey(player)) {
//            ServerLevel level = (ServerLevel) player.level();
//            ItemStack stack = player.getItemBySlot(OverMendingCache.CACHE.get(player));
//            stack.set(DataComponents.UNBREAKABLE, new Unbreakable(true));
//            stack.set(DataComponents.ENCHANTMENTS, AEUtils.removeEnchantment(stack.get(DataComponents.ENCHANTMENTS), AEUtils.getEnchantmentHolder(AEEnchantments.OVER_MENDING, level)));
//            stack.remove(AEDataComponents.OVER_MENDING);
//            level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1, player.getZ(), 1000, 0f, 0f, 0f, 0.5f);
//            OverMendingCache.CACHE.remove(player);}
//    }

//    @SubscribeEvent
//    private static void onTick(ServerTickEvent.Post event) {
//        OverMendingCache.CACHE.clear();
//    }
//
//    static class OverMendingCache {
//        public static final Map<Player, EquipmentSlot> CACHE = new HashMap<>();
//    }
}
