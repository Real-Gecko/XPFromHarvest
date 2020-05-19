package com.realgecko.xpfromharvest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles harvest attempts with left click (block breaking)
 */

public class BlockBreakHandler {
    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null || event.getWorld().isRemote())
            return;

        BlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        boolean harvest = false;

        if (ModConfig.crops.get().contains(state.toString()))
            harvest = true;

        if (harvest && (event.getWorld().getRandom().nextInt(100) + 1) <= ModConfig.chance.get())
            block.dropXpOnBlockBreak(event.getWorld().getWorld(), event.getPos(), ModConfig.xpAmount.get());
    }
}
