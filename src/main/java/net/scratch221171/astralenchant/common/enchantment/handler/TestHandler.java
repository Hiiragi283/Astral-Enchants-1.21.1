package net.scratch221171.astralenchant.common.enchantment.handler;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.scratch221171.astralenchant.common.AstralEnchant;
import net.scratch221171.astralenchant.common.event.ItemEnchantmentSetEvent;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class TestHandler {

    @SubscribeEvent
    private static void test(ItemEnchantmentSetEvent event) {
    }
}
