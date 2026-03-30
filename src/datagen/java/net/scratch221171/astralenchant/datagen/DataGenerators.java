package net.scratch221171.astralenchant.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.datagen.providers.*;

import java.util.concurrent.CompletableFuture;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = AstralEnchant.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        AstralEnchant.LOGGER.info("Loading DataGenerators");
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        lookupProvider = gen.addProvider(event.includeServer(), new AEDatapackProvider(packOutput, lookupProvider)).getRegistryProvider();
        AEBlockTagsProvider blockTags = new AEBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new AEItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));
        gen.addProvider(event.includeServer(), new AELootModifierProvider(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new AERecipeProvider(packOutput, lookupProvider));
        gen.addProvider(event.includeServer(), new AEEnchantmentTagsProvider(packOutput, lookupProvider, existingFileHelper));
        gen.addProvider(event.includeServer(), new AELanguageProviderENUS(packOutput));
        gen.addProvider(event.includeServer(), new AELanguageProviderJAJP(packOutput));
        gen.addProvider(event.includeClient(), new AEItemModelProvider(packOutput, existingFileHelper));
        gen.addProvider(event.includeClient(), new AEBlockStateProvider(packOutput, existingFileHelper));
    }
}
