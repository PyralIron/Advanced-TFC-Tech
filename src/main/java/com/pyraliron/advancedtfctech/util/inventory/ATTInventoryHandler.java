package com.pyraliron.advancedtfctech.util.inventory;

import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import blusunrize.immersiveengineering.common.util.inventory.IIEInventory;

public class ATTInventoryHandler extends IEInventoryHandler {
    public int stackLimit;
    public ATTInventoryHandler(int slots, IIEInventory inventory, int stackLimit) {
        super(slots, inventory);
        this.stackLimit = stackLimit;
    }
    public ATTInventoryHandler(int slots, IIEInventory inventory, int slotOffset, boolean canInsert, boolean canExtract, int stackLimit) {
        super(slots, inventory, slotOffset, canInsert, canExtract);
        this.stackLimit = stackLimit;
    }
    public ATTInventoryHandler(int slots, IIEInventory inventory, int slotOffset, boolean canInsert[], boolean canExtract[], int stackLimit) {
        super(slots, inventory, slotOffset, canInsert, canExtract);
        this.stackLimit = stackLimit;
    }
    @Override
    public int getSlotLimit(int slot) {
        return stackLimit;
    }
}
