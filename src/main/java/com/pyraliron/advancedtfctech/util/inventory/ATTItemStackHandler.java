package com.pyraliron.advancedtfctech.util.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ATTItemStackHandler extends ItemStackHandler {
    private int[] inputSlots;
    private int[] outputSlots;
    public ATTItemStackHandler()
    {
        super(1);
    }

    public ATTItemStackHandler(int size)
    {
        stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }
    public ATTItemStackHandler(int size, int[] inputSlots, int[] outputSlots) {
        this(size);
        this.inputSlots = inputSlots;
        this.outputSlots = outputSlots;
    }
    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        boolean flag = false;
        for (int i = 0; i < inputSlots.length; i++) {
            if (inputSlots[i] == slot) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return ItemStack.EMPTY;
        }
        return super.insertItem(slot,stack,simulate);
    }
    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        return super.extractItem(slot,amount,simulate);
    }
}
