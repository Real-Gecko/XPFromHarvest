package com.realgecko.xpfromharvest;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CuriosityHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity() == null || event.getLevel().isClientSide())
            return;
        if (event.getEntity().isCrouching()) {
            BlockState state = event.getLevel().getBlockState(event.getPos());
            event.getEntity().displayClientMessage(Component.translatable(state.toString()), false);
        }
    }
}
