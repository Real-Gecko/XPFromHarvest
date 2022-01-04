package com.realgecko.xpfromharvest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Adds easier crop harvesting and replanting with right click and adds XP on
 * successful harvest
 */

public class SimpleHarvestHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {

        if (event.getPlayer() == null || event.getWorld().isClientSide())
            return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (ModConfig.crops.get().contains(state.toString())) {
            handleHarvest(block, world, pos, state, event.getPlayer(), world.random);
        }
    }

    void handleHarvest(Block block, World world, BlockPos pos, BlockState state, PlayerEntity player, Random rand) {
        List<ItemStack> drops = Block.getDrops(state, (ServerWorld) world, pos, null);
        List<ItemStack> toRemove = new ArrayList<ItemStack>();
        boolean foundSeed = false;
        for (ItemStack stack : drops) {
            // Seeds are BlockNamedItem whose block is equal to crop it's able to produce
            if (stack.getItem() instanceof BlockNamedItem && !foundSeed) {
                if (((BlockNamedItem) stack.getItem()).getBlock() == block) {
                    // So, we've found a seed for this particular crop, let's take it away
                    stack.shrink(1);
                    if (stack.getCount() == 0)
                        toRemove.add(stack);
                    foundSeed = true;
                }
            }
        }

        drops.removeAll(toRemove);

        // Now let's spawn remaining drops
        for (ItemStack stack : drops) {
            ItemEntity entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.addFreshEntity(entityItem);
        }

        if ((rand.nextInt(100) + 1) <= ModConfig.chance.get()) {
            ExperienceOrbEntity xpOrb = new ExperienceOrbEntity(world, (double) pos.getX(), (double) pos.getY(),
                    (double) pos.getZ(), ModConfig.xpAmount.get());
            world.addFreshEntity(xpOrb);
        }
        world.setBlockAndUpdate(pos, block.defaultBlockState());
    }
}