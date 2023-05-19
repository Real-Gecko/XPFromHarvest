package com.realgecko.xpfromharvest;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles harvest attempts with left click (block breaking)
 */

public class BlockBreakHandler {
    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null || event.getLevel().isClientSide())
            return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();
        boolean harvest = false;

        if (ModConfig.crops.get().contains(state.toString()))
            harvest = true;

        if (harvest && (event.getLevel().getRandom().nextInt(100) + 1) <= ModConfig.chance.get())
            block.popExperience((ServerLevel) event.getLevel(), event.getPos(), ModConfig.xpAmount.get());
    }
}
