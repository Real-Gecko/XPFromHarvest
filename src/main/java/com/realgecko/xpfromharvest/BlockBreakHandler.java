package com.realgecko.xpfromharvest;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

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

        if (Config.cropList.contains(state.toString()))
            harvest = true;

        if (harvest && (event.getLevel().getRandom().nextInt(100) + 1) <= Config.chance)
            block.popExperience((ServerLevel) event.getLevel(), event.getPos(), Config.xpAmount);
    }
}
