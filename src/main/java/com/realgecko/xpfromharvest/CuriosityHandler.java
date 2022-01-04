package com.realgecko.xpfromharvest;

import java.util.UUID;

import net.minecraft.block.BlockState;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CuriosityHandler {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (event.getPlayer() == null || event.getWorld().isClientSide())
            return;
        if (event.getPlayer().isCrouching()) {
            BlockState state = event.getWorld().getBlockState(event.getPos());
            event.getPlayer().sendMessage(new TranslationTextComponent(state.toString()), new UUID(0, 0));
        }
    }
}
