package net.scratch221171.astralenchant.client;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.scratch221171.astralenchant.client.itemRenderer.XPbarDecorator;
import net.scratch221171.astralenchant.common.AstralEnchant;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class Setup {

    @SubscribeEvent
    public static void registerItemDecorators(RegisterItemDecorationsEvent event) {
        // BuiltInRegistriesやRegistries経由で全アイテムを取得して登録
        for (Item item : BuiltInRegistries.ITEM) {
            if (item != Items.AIR) {
                event.register(item, new XPbarDecorator());
            }
        }
    }
}
