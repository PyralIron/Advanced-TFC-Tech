package com.pyraliron.advancedtfctech.util.handlers;

import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import com.pyraliron.advancedtfctech.blocks.BlockCharcoalForgeATT;
import com.pyraliron.advancedtfctech.blocks.BlockElectricGrill;
import net.dries007.tfc.objects.blocks.devices.BlockCharcoalForge;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.pyraliron.advancedtfctech.init.ModBlocks.CHARCOAL_FORGE_ATT;
import static com.pyraliron.advancedtfctech.init.ModBlocks.ELECTRIC_GRILL;
import static net.dries007.tfc.objects.blocks.property.ILightableBlock.LIT;

public class EventHandler {
    @SubscribeEvent
    public void onBlockRightClicked(PlayerInteractEvent event) {
//        event.getItemStack();
//        event.getWorld();
//        event.getPos();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        EntityPlayer p = event.getEntityPlayer();
        //System.out.println(event+ " "+event.getSide());
        //TODO: note that this check for right-click-block is necessary because this function is called with a left-click-block event as well
        if (event instanceof PlayerInteractEvent.RightClickBlock && event.getWorld().getBlockState(event.getPos()).getBlock() instanceof BlockATTBase) {
            //System.out.println(event.getWorld().getBlockState(event.getPos()).getBlock());
            event.getWorld().getBlockState(event.getPos()).getBlock().onBlockActivated(event.getWorld(),event.getPos(),event.getWorld().getBlockState(event.getPos()),event.getEntityPlayer(),event.getHand(),event.getFace(),event.getPos().getX(),event.getPos().getY(),event.getPos().getZ());
        } else if (event instanceof PlayerInteractEvent.RightClickBlock && p.isSneaking() && p.getHeldItem(EnumHand.MAIN_HAND) == ItemStack.EMPTY) {
            Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
            if (block instanceof BlockFirePit && !(block instanceof BlockElectricGrill)) {

//                System.out.println(ELECTRIC_GRILL +" " + pos + " " + world);
                world.getBlockState(pos).getBlock().breakBlock(world,pos,world.getBlockState(pos));
                world.setBlockState(pos, ELECTRIC_GRILL.getDefaultState().withProperty(LIT, false));
//                TEFirePit te = Helpers.getTE(world, pos, TEFirePit.class);
//                if (te != null)
//                {
//                    te.onCreate(log.getItem());
//                }
//                stuffToUse.forEach(Entity::setDead);
//                log.getItem().shrink(1);
//                if (log.getItem().getCount() == 0)
//                {
//                    log.setDead();
//                }
//                TFCTriggers.LIT_TRIGGER.trigger((EntityPlayerMP) player, world.getBlockState(pos).getBlock()); // Trigger lit block
            } else if (block instanceof BlockCharcoalForge && !(block instanceof BlockCharcoalForgeATT)) {
                world.getBlockState(pos).getBlock().breakBlock(world,pos,world.getBlockState(pos));
                world.setBlockState(pos, CHARCOAL_FORGE_ATT.getDefaultState().withProperty(LIT, false));
            }
        }

    }
}
