package net.scratch221171.astralenchant.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.AstralEnchantmentTags;
import net.scratch221171.astralenchant.common.registries.AEBlocks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AEBlockTagsProvider extends BlockTagsProvider {
    public AEBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, AstralEnchant.MOD_ID, null);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider lookupProvider) {
        tag(AstralEnchantmentTags.Blocks.STORAGE_BLOCKS_ARCANIUM).add(AEBlocks.ARCANIUM_BLOCK.get());
        tag(Tags.Blocks.STORAGE_BLOCKS).addTag(AstralEnchantmentTags.Blocks.STORAGE_BLOCKS_ARCANIUM);
    }
}
