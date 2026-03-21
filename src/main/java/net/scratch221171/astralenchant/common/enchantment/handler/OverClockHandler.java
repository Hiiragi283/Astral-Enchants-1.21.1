/*
 * Based on code from JustDireThings
 * https://github.com/Direwolf20-MC/JustDireThings
 *
 * Licensed under the MIT License.
 *
 * Original copyright (c) 2023 Direwolf20-MC
 */

package net.scratch221171.astralenchant.common.enchantment.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.scratch221171.astralenchant.common.AstralEnchant;

import java.util.ArrayList;

@EventBusSubscriber(modid = AstralEnchant.MOD_ID)
public class OverClockHandler {

    static ArrayList<OverClockEntry> entries = new ArrayList<>();

    @SubscribeEvent
    private static void onUseClock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.CLOCK)) return;

        if (event.getLevel().isClientSide) return;

        AstralEnchant.LOGGER.info("using clock");

        int rate = event.getItemStack().getCount();

        entries.add(new OverClockEntry(
                event.getPos(),
                event.getLevel().dimension(),
                rate,
                rate * 100L
        ));

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    @SubscribeEvent
    private static void onTick(ServerTickEvent.Post event) {
        for (OverClockEntry entry : entries) {
            AstralEnchant.LOGGER.info("entry: " + entry);
            ServerLevel serverLevel = event.getServer().getLevel(entry.dimension);
            if (serverLevel == null) continue;
            BlockState blockState = serverLevel.getBlockState(entry.blockPos);
            BlockEntity blockEntity = serverLevel.getBlockEntity(entry.blockPos);
            AstralEnchant.LOGGER.info("check");
            if (!isValidTickAccelBlock(serverLevel, blockState, blockEntity)) {
                entry.remainingTick = 0;
                continue;
            }
            AstralEnchant.LOGGER.info("valid");
            for (int i = 0; i < entry.rate; i++) {
                AstralEnchant.LOGGER.info("remaining: " + entry.remainingTick);
                if (entry.remainingTick <= 0) break;
                if (blockEntity != null) {
                    BlockEntityTicker<BlockEntity> ticker = blockEntity.getBlockState().getTicker(serverLevel, (BlockEntityType<BlockEntity>) blockEntity.getType());
                    if (ticker != null) {
                        ticker.tick(serverLevel, entry.blockPos, blockEntity.getBlockState(), blockEntity);
                    }
                } else if (blockState.isRandomlyTicking()) {
                    if (serverLevel.random.nextInt(1365) == 0) { //Average Random Tick Rate
                        blockState.randomTick(serverLevel, entry.blockPos, serverLevel.random);
                    }
                }
                entry.remainingTick--;
            }
            AstralEnchant.LOGGER.info("ending entry: " + entry);
        }

        entries.removeIf(entry -> entry.remainingTick <= 0);
    }

    private static boolean isValidTickAccelBlock(ServerLevel serverLevel, BlockState blockState, BlockEntity blockEntity) {
        if (blockEntity == null && !blockState.isRandomlyTicking())
            return false;
        if (blockEntity != null) {
            BlockEntityTicker<BlockEntity> ticker = blockEntity.getBlockState().getTicker(serverLevel, (BlockEntityType<BlockEntity>) blockEntity.getType());
            if (ticker == null) {
                AstralEnchant.LOGGER.info("null ticker");
                return false;
            }
        }
        return true;
    }

    static class OverClockEntry {
        private final BlockPos blockPos;
        private final ResourceKey<Level> dimension;
        private int rate;
        private long remainingTick;


        OverClockEntry(BlockPos blockPos, ResourceKey<Level> dimension, int rate, long remaining) {
            this.blockPos = blockPos;
            this.dimension = dimension;
            this.rate = rate;
            this.remainingTick = remaining;
        }
    }
}
