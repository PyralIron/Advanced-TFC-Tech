package com.pyraliron.advancedtfctech.client.gui;

import blusunrize.immersiveengineering.common.gui.ContainerIEBase;
import com.pyraliron.advancedtfctech.te.TileEntityDoughMixer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDoughMixer extends ContainerIEBase<TileEntityDoughMixer> {
    public ContainerDoughMixer(InventoryPlayer inventoryPlayer, TileEntityDoughMixer tile) {
        super(inventoryPlayer, tile);

        this.tile = tile;
        //tile.container = this;
        for (int i = 0; i < 6; i++)
            this.addSlotToContainer(new ATTSlot.DoughMixerInput(this, this.inv, i, 62 + 18 * (i % 3), 16 + 18 * (i / 3)));
        for (int i = 0; i < 6; i++)
            this.addSlotToContainer(new ATTSlot.Output(this, this.inv, i + 6, 62 + 18 * (i % 3), 74 + 18 * (i / 3)));

        slotCount = 12;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 9; j++)
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
        for (int i = 0; i < 9; i++)
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 184));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return tile.canInteractWith(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {

        ItemStack stack = ItemStack.EMPTY;
        Slot slotObject = inventorySlots.get(slot);

        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            stack = stackInSlot.copy();
            if (slot < slotCount) {
                if (!this.mergeItemStack(stackInSlot, slotCount, (slotCount + 36), true))
                    return ItemStack.EMPTY;
            } else {
                boolean b = false;
                for (int i = 0; i < slotCount; i++)
                    if (this.getSlot(i).isItemValid(stackInSlot))
                        if (this.mergeItemStack(stackInSlot, i, i + 1, false)) {
                            b = true;
                            break;
                        }
                if (!b)
                    return ItemStack.EMPTY;
            }

            if (stackInSlot.getCount() == 0)
                slotObject.putStack(ItemStack.EMPTY);
            else
                slotObject.onSlotChanged();

            if (stackInSlot.getCount() == stack.getCount())
                return ItemStack.EMPTY;
            slotObject.onTake(player, stackInSlot);
        }
        return stack;
    }
}
