package net.scratch221171.astralenchant.common.registries;

import com.mojang.serialization.MapCodec;
import java.util.function.Supplier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.condition.ConfigCondition;

public class AEConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> REGISTER =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, AstralEnchant.MOD_ID);

    public static final Supplier<MapCodec<ConfigCondition>> CONFIG_CONDITION =
            REGISTER.register("config_condition", () -> ConfigCondition.CODEC);

    public static void register(IEventBus eventBus) {
        REGISTER.register(eventBus);
    }
}
