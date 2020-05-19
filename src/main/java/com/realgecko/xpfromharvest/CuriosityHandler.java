package com.realgecko.xpfromharvest;

import net.minecraft.block.BlockState;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CuriosityHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer() == null || event.getWorld().isRemote())
            return;
        if (event.getPlayer().isSneaking()) {
            BlockState state = event.getWorld().getBlockState(event.getPos());
            event.getPlayer().sendMessage(new StringTextComponent(state.toString()));
        }
    }
}
