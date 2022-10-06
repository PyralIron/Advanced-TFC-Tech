package com.pyraliron.advancedtfctech.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.client.gui.MultiblockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DoughMixerRecipe extends MultiblockRecipe {
    public static float energyModifier = 1;
    public static float timeModifier = 1;

    public final IngredientStack input;
    public final IngredientStack fluidInput;
    public final String oreInputString;
    public final ItemStack output;
    public String specialRecipeType;
    public static ArrayList<String> specialRecipeTypes = new ArrayList<String>();
    public static ArrayList<DoughMixerRecipe> recipeList = new ArrayList<DoughMixerRecipe>();

    public DoughMixerRecipe(ItemStack output, Object input, FluidStack inputFluid, int time, int energyPerTick) {
        this.output = output;
        this.input = ApiUtils.createIngredientStack(input);
        this.fluidInput = new IngredientStack(inputFluid);
        //System.out.println(this.input);



        //System.out.println("INPUT FOR RECIPE "+this.input);
        this.oreInputString = input instanceof String ? (String) input : null;
        this.totalProcessTime = (int) Math.floor(time * timeModifier);
        this.totalProcessEnergy = (int) Math.floor(energyPerTick * energyModifier) * totalProcessTime;


        this.inputList = Lists.newArrayList(this.input);
        this.fluidInputList = Lists.newArrayList(inputFluid);
        this.outputList = ListUtils.fromItem(this.output);
    }

    @Override
    public void setupJEI() {
        super.setupJEI();
//		List<ItemStack>[] newJeiItemOutputList = new ArrayList[jeiItemOutputList.length+1];
//		System.arraycopy(jeiItemOutputList,0, newJeiItemOutputList,0, jeiItemOutputList.length);
//		newJeiItemOutputList[jeiItemOutputList.length] = Lists.newArrayList(slag);
//		jeiItemOutputList = newJeiItemOutputList;
        //this.jeiTotalItemOutputList.add(slag);
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));
        //fluidInput.getValidIngredients().get(0).writeToNBT()
        return nbt;
    }

    public static DoughMixerRecipe loadFromNBT(NBTTagCompound nbt) {
        IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));
        for (DoughMixerRecipe recipe : recipeList)
            if (recipe.input.equals(input)) {
                return recipe;
            }
        return null;
    }

    public NonNullList<ItemStack> getOutputs(ItemStack input) {
        NonNullList<ItemStack> outputs = NonNullList.create();
        outputs.add(output);
        return outputs;
    }

    public boolean matches(ItemStack input, FluidStack inputFluid) {
        return this.input != null && this.input.matches(input) && this.fluidInput.matches(inputFluid);
    }

    public boolean isValidInput(ItemStack stack) { return this.input != null && this.input.matches(stack); }




    public DoughMixerRecipe setSpecialRecipeType(String type) {
        this.specialRecipeType = type;
        if (!specialRecipeTypes.contains(type))
            specialRecipeTypes.add(type);
        return this;
    }

    public static DoughMixerRecipe addRecipe(ItemStack output, Object input, FluidStack inputFluid, int time, int energyPerTick) {
        DoughMixerRecipe recipe = new DoughMixerRecipe(output, input, inputFluid, time, energyPerTick);

        if (recipe.input != null)

            recipeList.add(recipe);
        return recipe;
    }

    public static DoughMixerRecipe findRecipe(ItemStack input, FluidStack inputFluid) {
        for (DoughMixerRecipe recipe : recipeList)
            if (recipe != null && recipe.matches(input, inputFluid))
                return recipe;
        return null;
    }

    public static List<DoughMixerRecipe> removeRecipes(ItemStack stack) {
        List<DoughMixerRecipe> list = new ArrayList();
        Iterator<DoughMixerRecipe> it = recipeList.iterator();
        while (it.hasNext()) {
            DoughMixerRecipe ir = it.next();
            if (OreDictionary.itemMatches(ir.output, stack, true)) {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    public static boolean isValidRecipeInput(ItemStack stack) {
        //System.out.println("RECIPE LIST "+recipeList);
        for (DoughMixerRecipe recipe : recipeList) {
            //System.out.println("RECIPE " + recipe + " " + recipe.isValidInput(stack));
            if (recipe != null && recipe.isValidInput(stack))
                return true;
        }
        return false;
    }
}
