package com.realgecko.xpfromharvest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handles harvest attempts with left click (block breaking)
 */

public class BlockBreakHandler {
    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null || event.getPlayer().world.isRemote)
            return;

        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        boolean harvest = false;

        if (block instanceof BlockCrops) {
            if (((BlockCrops) block).isMaxAge(state))
                harvest = true;
        }

        if (block instanceof BlockNetherWart) {
            int meta = block.getMetaFromState(state);
            if (meta == 3)
                harvest = true;
        }

        if (harvest && (event.getWorld().rand.nextInt(100) + 1) <= ModConfig.chance)
            block.dropXpOnBlockBreak(event.getWorld(), event.getPos(), ModConfig.xpAmount);
    }
}
