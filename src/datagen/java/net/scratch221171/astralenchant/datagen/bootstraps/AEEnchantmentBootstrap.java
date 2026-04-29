package net.scratch221171.astralenchant.datagen.bootstraps;

import java.util.function.BiConsumer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.AstralEnchantmentTags;
import net.scratch221171.astralenchant.common.enchantment.AEEnchantments;
import net.scratch221171.astralenchant.common.enchantment.effect.OverMendingEffect;
import net.scratch221171.astralenchant.common.registries.AEAttributes;

public class AEEnchantmentBootstrap {
    public static void applyConditions(BiConsumer<ResourceKey<?>, ICondition> consumer) {
        consumer.accept(AEEnchantments.SLOT_EXPANSION, new ModLoadedCondition("accessories"));
        consumer.accept(AEEnchantments.ITEM_PROTECTION, new ModLoadedCondition("l2hostility"));
    }

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        HolderLookup.RegistryLookup<Item> itemLookup =
                context.registryLookup(Registries.ITEM).orElseThrow();

        HolderSet<Item> anyHolderSet = new AnyHolderSet<>(itemLookup);
        HolderSet<Item> armorTag = itemLookup.getOrThrow(ItemTags.ARMOR_ENCHANTABLE);
        HolderSet<Item> headTag = itemLookup.getOrThrow(ItemTags.HEAD_ARMOR_ENCHANTABLE);
        HolderSet<Item> chestTag = itemLookup.getOrThrow(ItemTags.CHEST_ARMOR_ENCHANTABLE);
        HolderSet<Item> footTag = itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE);
        HolderSet<Item> weaponTag = itemLookup.getOrThrow(ItemTags.WEAPON_ENCHANTABLE);
        HolderSet<Item> miningTag = itemLookup.getOrThrow(ItemTags.MINING_LOOT_ENCHANTABLE);
        HolderSet<Item> bundleTag = itemLookup.getOrThrow(AstralEnchantmentTags.Items.BUNDLE);
        HolderSet<Item> durabilityTag = itemLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE);

        register(
                context,
                AEEnchantments.MITIGATION_PIERCING,
                Enchantment.enchantment(Enchantment.definition(
                        weaponTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.MAINHAND)));

        register(
                context,
                AEEnchantments.LAST_STAND,
                Enchantment.enchantment(Enchantment.definition(
                        armorTag,
                        1,
                        3,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.ARMOR)));

        register(
                context,
                AEEnchantments.ITEM_PROTECTION,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.ESSENCE_OF_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        5,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.COOLDOWN_REDUCTION,
                Enchantment.enchantment(Enchantment.definition(
                                chestTag,
                                1,
                                3,
                                Enchantment.dynamicCost(100, 10),
                                Enchantment.dynamicCost(150, 10),
                                16,
                                EquipmentSlotGroup.CHEST))
                        .withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        ResourceLocation.fromNamespaceAndPath(AstralEnchant.MOD_ID, "cr_bonus"),
                                        AEAttributes.COOLDOWN_DURATION,
                                        LevelBasedValue.perLevel(-0.15f),
                                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));

        register(
                context,
                AEEnchantments.FEATHER_TOUCH,
                Enchantment.enchantment(Enchantment.definition(
                        miningTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.MAINHAND)));

        register(
                context,
                AEEnchantments.ADVENTURERS_LORE,
                Enchantment.enchantment(Enchantment.definition(
                        footTag,
                        1,
                        3,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        8,
                        EquipmentSlotGroup.FEET)));

        register(
                context,
                AEEnchantments.COMPATIBILITY,
                Enchantment.enchantment(Enchantment.definition(
                        bundleTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.ENDLESS_APPETITE,
                Enchantment.enchantment(Enchantment.definition(
                        chestTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.CHEST)));

        register(
                context,
                AEEnchantments.MOMENTUM,
                Enchantment.enchantment(Enchantment.definition(
                        chestTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.CHEST)));

        register(
                context,
                AEEnchantments.INSTANT_TELEPORT,
                Enchantment.enchantment(Enchantment.definition(
                        headTag,
                        1,
                        4,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.HEAD)));

        register(
                context,
                AEEnchantments.OVERLOAD,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        5,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        32,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.SLOT_EXPANSION,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        3,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.REACTIVE_ARMOR,
                Enchantment.enchantment(Enchantment.definition(
                        chestTag,
                        1,
                        1,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.CHEST)));

        register(
                context,
                AEEnchantments.MYSTIC_REMNANTS,
                Enchantment.enchantment(Enchantment.definition(
                        weaponTag,
                        1,
                        3,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        4,
                        EquipmentSlotGroup.MAINHAND)));

        register(
                context,
                AEEnchantments.CURSE_OF_IGNORANCE,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        1,
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        4,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.CURSE_OF_ENCHANTMENT,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        1,
                        Enchantment.constantCost(25),
                        Enchantment.constantCost(50),
                        4,
                        EquipmentSlotGroup.ANY)));

        register(
                context,
                AEEnchantments.DISTORTION,
                Enchantment.enchantment(Enchantment.definition(
                        anyHolderSet,
                        1,
                        3,
                        Enchantment.dynamicCost(100, 10),
                        Enchantment.dynamicCost(150, 10),
                        16,
                        EquipmentSlotGroup.HAND)));

        register(
                context,
                AEEnchantments.OVER_MENDING,
                Enchantment.enchantment(Enchantment.definition(
                                durabilityTag,
                                1,
                                1,
                                Enchantment.dynamicCost(100, 10),
                                Enchantment.dynamicCost(150, 10),
                                16,
                                EquipmentSlotGroup.ANY))
                        .withEffect(EnchantmentEffectComponents.TICK, new OverMendingEffect()));
    }

    private static void register(
            BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
