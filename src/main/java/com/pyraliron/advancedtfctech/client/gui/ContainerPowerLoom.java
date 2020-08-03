package com.pyraliron.advancedtfctech.client.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static java.lang.Math.*;

public class ContainerPowerLoom extends ContainerIEBase<TileEntityPowerLoom>
{
    public ContainerPowerLoom(InventoryPlayer inventoryPlayer, TileEntityPowerLoom tile)
    {
        super(inventoryPlayer, tile);

        this.tile=tile;
        //tile.container = this;
        for(int i=0; i<8; i++)
            this.addSlotToContainer(new ATTSlot.LoomInput(this, this.inv, i, (int)round(cos(PI*i/4)*30)+80,(int)round(sin(PI*i/4)*30)+50));

        this.addSlotToContainer(new ATTSlot.Output(this, this.inv, 8, 80-18,98));
        this.addSlotToContainer(new ATTSlot.Output(this, this.inv, 9, 80,98));
        this.addSlotToContainer(new ATTSlot.Output(this, this.inv, 10, 80+18,98));
        this.addSlotToContainer(new ATTSlot.Output(this, this.inv, 11, 80+36+2,98));
        this.addSlotToContainer(new ATTSlot.Output(this, this.inv, 12, 80+54+2,98));

        this.addSlotToContainer(new ATTSlot.WeaveInput(this, this.inv, 13, 18,98));
        this.addSlotToContainer(new ATTSlot.WeaveInput(this, this.inv, 14, 36+2,98));
        this.addSlotToContainer(new ATTSlot.WeaveInput(this, this.inv, 15, 54+2,98));
        this.addSlotToContainer(new ATTSlot.WeaveInput(this, this.inv, 16, 18,18));

        slotCount=16;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(inventoryPlayer, j+i*9+9, 8+j*18, 126+i*18));
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inventoryPlayer, i, 8+i*18, 184));
    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tile.canInteractWith(playerIn);
    }
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot)
    {

        ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = inventorySlots.get(slot);

        if(slotObject != null && slotObject.getHasStack())
        {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            /* We subtract 3 in order to prevent shifting into the warp (unprocessed yarn) slots (3 slots) */
            if(slot < slotCount-3)
            {
                if(!this.mergeItemStack(stackInSlot, slotCount, (slotCount + 36), true))
                    return ItemStack.EMPTY;
            }
            else
            {
                boolean b = false;
                for(int i=0;i<slotCount-3;i++)
                    if(this.getSlot(i).isItemValid(stackInSlot))
                        if(this.mergeItemStack(stackInSlot, i,i+1, false))
                        {
                            b = true;
                            break;
                        }
                if(!b)
                    return ItemStack.EMPTY;
            }

            if(stackInSlot.getCount() == 0)
                slotObject.putStack(ItemStack.EMPTY);
            else
                slotObject.onSlotChanged();

            if(stackInSlot.getCount() == stack.getCount())
                return ItemStack.EMPTY;
            slotObject.onTake(player, stackInSlot);
        }
        return stack;

    }

}
