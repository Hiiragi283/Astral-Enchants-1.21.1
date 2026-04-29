package net.scratch221171.astralenchant.common.registries;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.loot.MysticRemnantsLootModifier;

public class AELootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> REGISTER =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AstralEnchant.MOD_ID);

    public static final Supplier<MapCodec<MysticRemnantsLootModifier>> MYSTIC_REMNANTS_LOOT_MODIFIER =
            REGISTER.register("mystic_remnants_loot_modifier", () -> MysticRemnantsLootModifier.CODEC);

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
