package net.scratch221171.astralenchant.common.mixin.minecraft;

import java.util.Optional;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.config.RuntimeConfigState;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.util.AEUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {
    /** {@link AEEnchantments#MOMENTUM} が付いている場合は，アイテム使用中の移動速度低下を無効にし，アイテム使用中でも走り始められるようにする。 */
    @Redirect(
            method = {"aiStep", "canStartSprinting"},
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean astralenchant$disableUsingItemSlowdown(LocalPlayer instance) {
        if (RuntimeConfigState.get(AEConfig.MOMENTUM)) {
            Optional<Holder.Reference<Enchantment>> holder =
                    AEUtils.getEnchantmentHolder1(AEEnchantments.MOMENTUM, instance.level());
            if (holder.isPresent() && EnchantmentHelper.getEnchantmentLevel(holder.get(), instance) > 0) return false;
        }
        return instance.isUsingItem();
    }
}
