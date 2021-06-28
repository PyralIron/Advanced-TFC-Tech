package com.pyraliron.advancedtfctech.compat.jei.wrapper;

import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GristMillRecipeWrapper extends MultiblockRecipeWrapper {
    private final GristMillRecipe recipe;
    public GristMillRecipeWrapper(GristMillRecipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }
    @Override
    public void getIngredients(IIngredients ingredients)
    {
        if(!inputs.isEmpty())
            ingredients.setInputs(ItemStack.class, inputs);
        if(!outputs.isEmpty())
            ingredients.setOutputs(ItemStack.class, outputs);
        if(!fluidInputs.isEmpty())
            ingredients.setInputs(FluidStack.class, fluidInputs);
        if(!fluidOutputs.isEmpty())
            ingredients.setOutputs(FluidStack.class, fluidOutputs);
    }
}
