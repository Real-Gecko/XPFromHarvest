package com.realgecko.xpfromharvest;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
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

@SuppressWarnings("deprecation")
public class SimpleHarvestHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {

        if (event.getEntity() == null || event.getLevel().isClientSide())
            return;

        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        
		ResourceLocation rLoc = Registry.BLOCK.getKey(block);
        
		if (ModConfig.cropMap.containsKey(rLoc.toString())) {
			CropData data = ModConfig.cropMap.get(rLoc.toString());
			
			if (data.getAge() == CropData.getAgeFromBlockState(state)) {
				if (data.getRightClick())
					handleRightclick(block, world, pos, state, event.getEntity(), world.random, data);
				else
					handleHarvest(block, world, pos, state, event.getEntity(), world.random, data);
			}
		}
    }
    
    void handleRightclick(Block block, Level world, BlockPos pos, BlockState state, Player player, RandomSource rand, CropData data) {
    	if (data.getForceFortune())
    		dropItems(block, world, pos, state, player, data);
    	giveExperience(rand, player);
    }

    void handleHarvest(Block block, Level world, BlockPos pos, BlockState state, Player player, RandomSource rand, CropData data) {
    	dropItems(block, world, pos, state, player, data);
        giveExperience(rand, player);
        
        // Reset block
        world.setBlockAndUpdate(pos, block.defaultBlockState());
    }
    
    void giveExperience(RandomSource rand, Player player) {
    	if ((rand.nextInt(100) + 1) <= ModConfig.chance.get()) {
            player.giveExperiencePoints(ModConfig.xpAmount.get());
        }
    }
    
    void dropItems(Block block, Level world, BlockPos pos, BlockState state, Player player, CropData data) {
    	ItemStack tool = player.getItemBySlot(EquipmentSlot.MAINHAND);
        List<ItemStack> drops = Block.getDrops(state, (ServerLevel) world, pos, null, player, tool);
        List<ItemStack> toRemove = new ArrayList<ItemStack>();
        List<String> blacklist = data.getDropBlacklist();
        
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

            // Check blacklist
    		if (blacklist.contains(Registry.ITEM.getKey(stack.getItem()).toString()))
    			toRemove.add(stack);
        }

        drops.removeAll(toRemove);

        // Now let's spawn remaining drops
        for (ItemStack stack : drops) {
            ItemEntity entityItem = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.addFreshEntity(entityItem);
        }
    }
}
