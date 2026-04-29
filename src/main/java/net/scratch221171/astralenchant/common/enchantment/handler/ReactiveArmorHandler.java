package net.scratch221171.astralenchant.common.enchantment.handler;

import java.util.List;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.util.AEUtils;
import net.scratch221171.astralenchant.common.util.IDamageSourceExtension;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class ReactiveArmorHandler {

    @SubscribeEvent
    private static void addDisabledDamageTag(EntityInvulnerabilityCheckEvent event) {
        if (!AEConfig.isEnabled(AEEnchantments.REACTIVE_ARMOR)) return;
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof LivingEntity entity) {
            if (AEUtils.getEnchantmentLevel(AEEnchantments.REACTIVE_ARMOR, entity) > 0) {
                IDamageSourceExtension acc = (IDamageSourceExtension) source;
                List<TagKey<DamageType>> tags = AEConfig.REACTIVE_ARMOR_DISABLED_DAMAGE_TYPE_TAGS.get().stream()
                        .map(id -> TagKey.create(
                                Registries.DAMAGE_TYPE,
                                ResourceLocation.read(id).getOrThrow()))
                        .toList();
                tags.forEach(acc::astralenchant$addDisabledTag);
            }
        }
    }
}
