package net.scratch221171.astralenchant.common.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Optional;

public class AEUtils {
    public static Holder<Enchantment> getEnchantmentHolder(ResourceKey<Enchantment> enchantment, Level level) {
        return level.registryAccess().holderOrThrow(enchantment);
    }

    public static Optional<Holder.Reference<Enchantment>> getEnchantmentHolder1(ResourceKey<Enchantment> enchantment, Level level) {
        return level.registryAccess().holder(enchantment);
    }

    // ServerLifecycleHooks.getCurrentServerからレジストリを取得し，エンチャントレベルを取得する
    public static int getEnchantmentLevel(ItemStack stack, ResourceKey<Enchantment> key) {
        return Optional.ofNullable(ServerLifecycleHooks.getCurrentServer())
                .map(MinecraftServer::registryAccess)
                .map(access -> getEnchantmentLevel(stack, access, key))
                .orElse(0);
    }

    public static int getEnchantmentLevel(ItemStack stack, Level level, ResourceKey<Enchantment> key) {
        return getEnchantmentLevel(stack, level.registryAccess(), key);
    }

    public static int getEnchantmentLevel(ItemStack stack, HolderLookup.Provider provider, ResourceKey<Enchantment> key) {
        return getEnchantmentLevel(stack, provider.lookupOrThrow(Registries.ENCHANTMENT), key);
    }

    // ItemStack.getAllEnchantmentsに基づいてエンチャントレベルを取得する
    public static int getEnchantmentLevel(ItemStack stack, HolderLookup.RegistryLookup<Enchantment> lookup, ResourceKey<Enchantment> key) {
        return stack.getAllEnchantments(lookup)
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().is(key))
                .findFirst()
                .map(Object2IntMap.Entry::getIntValue)
                .orElse(0);
    }

    public static int getEnchantmentLevel(ItemEnchantments enchantments, ResourceKey<Enchantment> key) {
        return enchantments
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().is(key))
                .findFirst()
                .map(Object2IntMap.Entry::getIntValue)
                .orElse(0);
    }

    public static ItemEnchantments removeEnchantment(ItemEnchantments enchantments, Holder<Enchantment> enchantment) {
        ItemEnchantments.Mutable newEnchantments = new ItemEnchantments.Mutable(enchantments);
        newEnchantments.removeIf(holder -> holder.value() == enchantment.value());
        return newEnchantments.toImmutable();
    }

    public static ItemEnchantments mergeItemEnchants(ItemEnchantments a, ItemEnchantments b) {
        ItemEnchantments.Mutable result = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);

        a.entrySet().forEach(entry -> result.set(entry.getKey(), entry.getIntValue()));

        for (Object2IntMap.Entry<Holder<Enchantment>> entry : b.entrySet()) {
            Holder<Enchantment> enchant = entry.getKey();
            int levelB = entry.getIntValue();

            if (!result.keySet().contains(enchant)) {
                result.set(enchant, levelB);
                continue;
            }

            int levelA = result.getLevel(enchant);
            if (levelB > levelA) {
                result.set(enchant, levelB);
            } else if (levelA == levelB) {
                if (levelA < enchant.value().getMaxLevel()) {
                    result.set(enchant, levelA + 1);
                }
            }
        }

        return result.toImmutable();
    }

    public static boolean hasEnoughXPPoint(float progress, int level, int required) {
        long total = 0;
        for (int i = 0; i < level; i++) {
            total += getXpNeededForNextLevel(i);
            if (total >= required) {
                return true;
            }
        }
        // ほぼintなのでround
        total += Math.round(progress * getXpNeededForNextLevel(level));
        return total >= required;
    }

    public static int getXpNeededForNextLevel(int j) {
        if (j >= 30) {
            return 112 + (j - 30) * 9;
        } else {
            return j >= 15 ? 37 + (j - 15) * 5 : 7 + j * 2;
        }
    }
}
