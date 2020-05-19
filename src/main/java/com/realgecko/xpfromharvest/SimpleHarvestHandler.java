package com.realgecko.xpfromharvest;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Adds easier crop harvesting and replanting with right click and adds XP on
 * successful harvest
 */

public class SimpleHarvestHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {

        if (!(event.getWorld() instanceof WorldServer)) return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (Arrays.asList(ModConfig.crops).contains(state.toString())) {
            handleHarvest(block, world, pos, state, world.rand);
        }
    }

    void handleHarvest(Block block, World world, BlockPos pos, IBlockState state, Random rand) {
        NonNullList<ItemStack> drops = NonNullList.create();
        block.getDrops(drops, world, pos, state, 0);

        boolean foundSeed = false;
        for (ItemStack stack : drops) {
            if ((stack.getItem() instanceof IPlantable) && !foundSeed) {
                foundSeed = true;
                continue;
            }
            EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(entityItem);
        }

        if ((rand.nextInt(100) + 1) <= ModConfig.chance) {
            EntityXPOrb xpOrb = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), ModConfig.xpAmount);
            world.spawnEntity(xpOrb);
        }
        state = block.getDefaultState();
        world.setBlockState(pos, state);
    }
}
