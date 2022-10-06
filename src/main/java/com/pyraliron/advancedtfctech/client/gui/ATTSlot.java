package com.pyraliron.advancedtfctech.client.gui;

import com.pyraliron.advancedtfctech.crafting.DoughMixerRecipe;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public abstract class ATTSlot extends Slot {
    final Container container;

    public ATTSlot(Container container, IInventory inv, int id, int x, int y) {
        super(inv, id, x, y);
        this.container = container;
    }
    public static class Output extends ATTSlot
    {
        public Output(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return false;
        }
    }
    public static class LoomInput extends ATTSlot
    {
        public LoomInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            //System.out.println(itemStack.getItem()+" IS VALID ? : "+PowerLoomRecipe.isValidRecipeInput(itemStack));
            //System.out.println(PowerLoomRecipe.isValidRecipeInput(itemStack)+" "+itemStack);
            return !itemStack.isEmpty() && PowerLoomRecipe.isValidRecipeInput(itemStack);
        }
        @Override
        public int getSlotStackLimit()
        {
            return 1;
        }
    }
    public static class WeaveInput extends ATTSlot
    {
        public WeaveInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return !itemStack.isEmpty() && PowerLoomRecipe.isValidRecipeInput(itemStack);
        }

    }
    public static class ThresherInput extends ATTSlot
    {
        public ThresherInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return !itemStack.isEmpty() && ThresherRecipe.isValidRecipeInput(itemStack);
        }
    }
    public static class GristMillInput extends ATTSlot
    {
        public GristMillInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return !itemStack.isEmpty() && GristMillRecipe.isValidRecipeInput(itemStack);
        }
    }
    public static class DoughMixerInput extends ATTSlot
    {
        public DoughMixerInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            return !itemStack.isEmpty() && DoughMixerRecipe.isValidRecipeInput(itemStack);
        }
    }
    public static class WinderInput extends ATTSlot
    {
        public WinderInput(Container container, IInventory inv, int id, int x, int y)
        {
            super(container, inv, id, x, y);
        }
        @Override
        public boolean isItemValid(ItemStack itemStack)
        {
            //TODO: Fix this
            //return !itemStack.isEmpty() && PirnWinderRecipe.isValidRecipeInput(itemStack);
            return false;
        }
    }
}
