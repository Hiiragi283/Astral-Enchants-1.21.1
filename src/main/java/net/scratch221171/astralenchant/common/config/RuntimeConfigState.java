package net.scratch221171.astralenchant.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.scratch221171.astralenchant.common.AstralEnchant;
import org.jetbrains.annotations.Nullable;

public class RuntimeConfigState {

    private static final Map<String, Object> VALUES = new HashMap<>();

    // こういうの好きじゃない
    static Path path = FMLPaths.CONFIGDIR.get().resolve(AstralEnchant.MOD_ID + "-server.toml");
    static CommentedFileConfig config = CommentedFileConfig.builder(path).build();

    // Config Condition用
    @SuppressWarnings("unchecked")
    @Nullable public static <T> T get(String key) {
        return (T) VALUES.get(key);
    }

    public static <T> T get(ModConfigSpec.ConfigValue<T> configValue) {
        return get(AEConfig.get(configValue));
    }
}
