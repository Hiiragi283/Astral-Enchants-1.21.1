package net.scratch221171.astralenchant.common.enchantment.handler;

import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.util.AEUtils;
import net.scratch221171.astralenchant.common.util.IDamageSourceExtension;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class MitigationPiercingHandler {

    @SubscribeEvent
    private static void addDamageTag(EntityInvulnerabilityCheckEvent event) {
        if (!AEConfig.isEnabled(AEEnchantments.MITIGATION_PIERCING)) return;
        Entity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof LivingEntity attacker) {
            AEUtils.getEnchantmentHolder(AEEnchantments.MITIGATION_PIERCING, attacker)
                    .ifPresent(holder -> {
                        if ((source.getWeaponItem() instanceof ItemStack weapon)
                                && weapon.getEnchantmentLevel(holder) > 0) {
                            IDamageSourceExtension acc = (IDamageSourceExtension) source;
                            List<TagKey<DamageType>> tags =
                                    AEConfig.MITIGATION_PIERCING_ADDED_DAMAGE_TYPE_TAGS.get().stream()
                                            .map(id -> TagKey.create(
                                                    Registries.DAMAGE_TYPE,
                                                    ResourceLocation.read(id).getOrThrow()))
                                            .toList();
                            tags.forEach(acc::astralenchant$addExtraTag);
                        }
                    });
        }
    }

    // パーティクル
    @SubscribeEvent
    private static void onDamage(LivingIncomingDamageEvent event) {
        if (!AEConfig.isEnabled(AEEnchantments.MITIGATION_PIERCING)) return;
        Entity entity = event.getEntity();
        AEUtils.getEnchantmentHolder(AEEnchantments.MITIGATION_PIERCING, entity).ifPresent(holder -> {
            ItemStack weapon = event.getSource().getWeaponItem();
            if (weapon != null
                    && weapon.getEnchantmentLevel(holder) > 0
                    && entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 10, 0f, 1f, 0f, 0.04f);
            }
        });
    }
}
