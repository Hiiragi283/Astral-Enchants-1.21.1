package net.scratch221171.astralenchant.datagen.bootstraps;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.scratch221171.astralenchant.common.worldgen.structure.AEStructureSets;
import net.scratch221171.astralenchant.common.worldgen.structure.AEStructures;

import java.util.List;

public class AEStructureSetBootstrap {

    public static void bootstrap(BootstrapContext<StructureSet> context) {

        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        Holder<Structure> cube = structures.getOrThrow(AEStructures.CUBE);

        StructureSet structureSet = new StructureSet(
                List.of(new StructureSet.StructureSelectionEntry(cube, 1)),
                new RandomSpreadStructurePlacement(
                        2,
                        1,
                        RandomSpreadType.LINEAR,
                        1234567
                )
        );

        context.register(AEStructureSets.CUBE_SET, structureSet);
    }
}
