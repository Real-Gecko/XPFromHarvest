package com.realgecko.xpfromharvest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarvestHandler {

    // First of all handle standard harvest attempt with left click
    @SubscribeEvent
    public void handleHarvest(BlockEvent.BreakEvent event) {
        if (event.getPlayer() == null || event.getPlayer().world.isRemote)
            return;

        Block block = event.getState().getBlock();

        if (block instanceof BlockCrops) {

            IBlockState state = event.getWorld().getBlockState(event.getPos());

            if (((BlockCrops) block).isMaxAge(state))
                block.dropXpOnBlockBreak(event.getWorld(), event.getPos(), 1);

        }
    }

    // Now let's add some nice harvesting and replanting with right click
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer() == null || event.getEntityPlayer().world.isRemote)
            return;

        World world = event.getWorld();
        IBlockState state = world.getBlockState(event.getPos());
        Block block = state.getBlock();
        BlockPos pos = event.getPos();

        if (block instanceof BlockCrops) {
            if (((BlockCrops) block).isMaxAge(state)) {
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
                EntityXPOrb xpOrb = new EntityXPOrb(world, pos.getX(), pos.getY(), pos.getZ(), 1);
                world.spawnEntity(xpOrb);
                state = block.getDefaultState();
                world.setBlockState(pos, state);
            }
        }
    }
}
