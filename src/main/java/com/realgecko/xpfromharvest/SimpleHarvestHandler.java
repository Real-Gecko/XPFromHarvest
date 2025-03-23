package com.realgecko.xpfromharvest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
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

        if (event.getEntity() == null || event.getLevel().isClientSide())
            return;

        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (Config.cropList.contains(state.toString())) {
            handleHarvest(block, world, pos, state, event, world.random);
        }
    }

    void handleHarvest(Block block, Level world, BlockPos pos, BlockState state,
            PlayerInteractEvent.RightClickBlock event, RandomSource rand) {
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) world, pos, null);
        List<ItemStack> toRemove = new ArrayList<ItemStack>();
        boolean foundSeed = false;
        for (ItemStack stack : drops) {
            // Seeds are BlockNamedItem whose block is equal to crop it's able to produce
            if (stack.getItem() instanceof BlockItem && !foundSeed) {
                if (((BlockItem) stack.getItem()).getBlock() == block) {
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

        if ((rand.nextInt(100) + 1) <= Config.chance) {
            // player.giveExperiencePoints(Config.xpAmount);
            block.popExperience((ServerLevel) event.getLevel(), event.getPos(), Config.xpAmount);
        }
        world.setBlockAndUpdate(pos, block.defaultBlockState());
    }
}
