package net.scratch221171.astralenchant.compat.accessories;

import io.wispforest.accessories.api.attributes.AccessoryAttributeBuilder;
import io.wispforest.accessories.api.attributes.SlotAttribute;
import io.wispforest.accessories.api.slot.SlotReference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.config.RuntimeConfigState;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.util.AEUtils;

public class SlotExpansionHandler {
    /**
     * {@link AEEnchantments#SLOT_EXPANSION} が付いていた場合スロット数を増やす。
     */
    public static void onAdjustAttributeModifier(ItemStack stack, SlotReference reference, AccessoryAttributeBuilder builder) {
        if (!RuntimeConfigState.get(AEConfig.SLOT_EXPANSION)) return;
        AEUtils.getEnchantmentHolder1(AEEnchantments.SLOT_EXPANSION, reference.entity().level()).ifPresent(holder -> {
            int level = stack.getEnchantmentLevel(holder);
            if (!stack.isEmpty() && level > 0) {
                builder.addStackable(
                        SlotAttribute.getAttributeHolder(reference.slotName()),
                        ResourceLocation.fromNamespaceAndPath(AstralEnchant.MOD_ID, "se_bonus"),
                        level,
                        AttributeModifier.Operation.ADD_VALUE);
            }
        });
    }
}
