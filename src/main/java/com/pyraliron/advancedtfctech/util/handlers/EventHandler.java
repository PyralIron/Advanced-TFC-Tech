package com.pyraliron.advancedtfctech.util.handlers;

import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    @SubscribeEvent
    public void onBlockRightClicked(PlayerInteractEvent event) {
        event.getItemStack();
        event.getWorld();
        event.getPos();
        event.getEntityPlayer();
        //System.out.println(event+ " "+event.getSide());
        //TODO: note that this check for right-click-block is necessary because this function is called with a left-click-block event as well
        if (event instanceof PlayerInteractEvent.RightClickBlock && event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockATTBase) {
            //System.out.println(event.getWorld().getBlockState(event.getPos()).getBlock());
            event.getWorld().getBlockState(event.getPos()).getBlock().onBlockActivated(event.getWorld(),event.getPos(),event.getWorld().getBlockState(event.getPos()),event.getEntityPlayer(),event.getHand(),event.getFace(),event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
        }

    }
}
