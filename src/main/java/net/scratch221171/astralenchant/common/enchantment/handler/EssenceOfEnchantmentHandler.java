package net.scratch221171.astralenchant.common.enchantment.handler;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.config.AEConfig;
import net.scratch221171.astralenchant.common.config.RuntimeConfigState;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.registries.AEDataComponents;
import net.scratch221171.astralenchant.common.util.AEUtils;
import net.scratch221171.astralenchant.common.util.IAttributeSentimentExtension;

import java.util.Set;
import java.util.function.BiConsumer;

//@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class EssenceOfEnchantmentHandler {

//    @SubscribeEvent
//    private static void ApplyAttributeModifier(ItemAttributeModifierEvent event) {
//        if (!RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANTMENT)) return;
//
//        ItemStack stack = event.getItemStack();
//        int level = AEUtils.getEnchantmentLevelFromNBT(stack, AEEnchantments.ESSENCE_OF_ENCHANTMENT);
//        if (stack.isEmpty() || level <= 0) return;
//
//        int totalLevel = 0;
//        Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = stack.getTagEnchantments().entrySet();
//        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments) {
//            if (!entry.getKey().is(AEEnchantments.ESSENCE_OF_ENCHANTMENT)) totalLevel += entry.getIntValue();
//        }
//        if (RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANT_INCLUDE_OVERLOAD_IN_CALCULATION)) totalLevel += stack.getOrDefault(AEDataComponents.OVERLOAD, 0) * (enchantments.size() - 1);
//
//        ItemAttributeModifiers attributeModifiers = event.getDefaultModifiers();
//        double multiplier = RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANT_LEVEL_MULTIPLIER);
//
//        for (ItemAttributeModifiers.Entry entry : attributeModifiers.modifiers()) {
//            ResourceLocation id = entry.modifier().id();
//            ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(AstralEnchant.MOD_ID, "eoe_bonus_" + id.getPath() + "_" + entry.slot().getSerializedName());
//
//            Attribute.Sentiment sentiment = ((IAttributeSentimentExtension)entry.attribute().value()).astralenchant$getSentiment();
//            AttributeModifier newBonusModifier;
//
//            switch (sentiment) {
//                // *(1 + a)
//                case POSITIVE -> newBonusModifier = new AttributeModifier(newId, totalLevel * level * multiplier / 100f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
//                // /(1 + a)
//                case NEGATIVE -> newBonusModifier = new AttributeModifier(newId, - 1 + 1 / (totalLevel * level * multiplier / 100f + 1), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
//                default -> {
//                    AstralEnchant.LOGGER.info("default");
//                    continue;
//                }
//            }
//
//            event.addModifier(entry.attribute(), newBonusModifier, entry.slot());
//        }
//    }

    public static void handle(ItemStack stack, Holder<Attribute> attribute, BiConsumer<Holder<Attribute>, AttributeModifier> consumer, ResourceLocation id, String slotName) {
        if (!RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANTMENT)) return;

        int level = AEUtils.getEnchantmentLevelFromNBT(stack, AEEnchantments.ESSENCE_OF_ENCHANTMENT);
        if (stack.isEmpty() || level <= 0) return;

        int totalLevel = 0;
        Set<Object2IntMap.Entry<Holder<Enchantment>>> enchantments = stack.getTagEnchantments().entrySet();
        for (Object2IntMap.Entry<Holder<Enchantment>> entry : enchantments) {
            if (!entry.getKey().is(AEEnchantments.ESSENCE_OF_ENCHANTMENT)) totalLevel += entry.getIntValue();
        }
        if (RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANT_INCLUDE_OVERLOAD_IN_CALCULATION)) totalLevel += stack.getOrDefault(AEDataComponents.OVERLOAD, 0) * (enchantments.size() - 1);

        double multiplier = RuntimeConfigState.get(AEConfig.ESSENCE_OF_ENCHANT_LEVEL_MULTIPLIER);

        ResourceLocation newId = ResourceLocation.fromNamespaceAndPath(AstralEnchant.MOD_ID, "eoe_bonus_" + id.getPath() + "_" + slotName);

        Attribute.Sentiment sentiment = ((IAttributeSentimentExtension)attribute.value()).astralenchant$getSentiment();
        AttributeModifier newModifier;

        switch (sentiment) {
            // *(1 + a)
            case POSITIVE -> newModifier = new AttributeModifier(newId, totalLevel * level * multiplier / 100f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            // /(1 + a)
            case NEGATIVE -> newModifier = new AttributeModifier(newId, - 1 + 1 / (totalLevel * level * multiplier / 100f + 1), AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
            default -> {
                return;
            }
        }

        consumer.accept(attribute, newModifier);
    }
}
