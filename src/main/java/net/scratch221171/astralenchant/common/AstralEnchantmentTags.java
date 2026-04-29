package net.scratch221171.astralenchant.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class AstralEnchantmentTags {
    public static final String COMMON = "c";

    public static ResourceLocation commonId(String path) {
        return ResourceLocation.fromNamespaceAndPath(COMMON, path);
    }

    // Block
    public static final class Blocks {
        public static final TagKey<Block> STORAGE_BLOCKS_ARCANIUM =
                BlockTags.create(commonId("storage_blocks/arcanium"));
    }

    // Item
    public static final class Items {
        public static final TagKey<Item> BUNDLE = ItemTags.create(AstralEnchant.id("bundle"));

        public static final TagKey<Item> GEMS_ARCANE_QUARTZ = ItemTags.create(commonId("gems/arcane_quartz"));
        public static final TagKey<Item> INGOTS_ARCANIUM = ItemTags.create(commonId("ingots/arcanium"));
        public static final TagKey<Item> STORAGE_BLOCKS_ARCANIUM = ItemTags.create(commonId("storage_blocks/arcanium"));
    }
}
