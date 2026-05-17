package net.scratch221171.astralenchant.common.enchantment.handler;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityInvulnerabilityCheckEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.AstralEnchantmentTags;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.util.AEUtils;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class ReactiveArmorHandler {

    @SubscribeEvent
    private static void addDisabledDamageTag(EntityInvulnerabilityCheckEvent event) {
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof LivingEntity entity) {
            if (AEUtils.getEnchantmentLevel(AEEnchantments.REACTIVE_ARMOR, entity) > 0) {
                if (source.is(AstralEnchantmentTags.DamageTypes.DISABLE_REACTIVE_ARMOR)) {
                    event.setInvulnerable(false);
                }
            }
        }
    }
}
