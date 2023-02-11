package com.realgecko.xpfromharvest;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Handles harvest attempts with left click (block breaking)
 */

@SuppressWarnings("deprecation")
public class BlockBreakHandler {
    @SubscribeEvent
    public void handleBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null || event.getLevel().isClientSide())
            return;

        BlockState state = event.getLevel().getBlockState(event.getPos());
        Block block = state.getBlock();
        ResourceLocation rLoc = Registry.BLOCK.getKey(block);
        boolean harvest = false;

        if (ModConfig.cropMap.containsKey(rLoc.toString())) {
        	CropData data = ModConfig.cropMap.get(rLoc.toString());
        	if (data.getAge() == CropData.getAgeFromBlockState(state) && !data.getRightClick())
        		harvest = true;
        }

        if (harvest && (event.getLevel().getRandom().nextInt(100) + 1) <= ModConfig.chance.get())
            block.popExperience((ServerLevel) event.getLevel(), event.getPos(), ModConfig.xpAmount.get());
    }
}
