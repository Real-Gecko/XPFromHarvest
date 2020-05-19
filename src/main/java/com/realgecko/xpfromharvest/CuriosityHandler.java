package com.realgecko.xpfromharvest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CuriosityHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getWorld() instanceof WorldServer)) return;
        if (event.getEntityPlayer().isSneaking()) {
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            event.getEntityPlayer().sendMessage(new TextComponentString(state.toString()));
        }
    }
}
