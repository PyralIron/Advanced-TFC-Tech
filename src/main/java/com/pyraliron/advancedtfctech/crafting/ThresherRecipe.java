package com.pyraliron.advancedtfctech.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.client.gui.MultiblockRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ThresherRecipe extends MultiblockRecipe {
    public static float energyModifier = 1;
    public static float timeModifier = 1;

    public final IngredientStack input;
    public final String oreInputString;
    public final ItemStack output;
    public final ItemStack secondaryOutput;
    public String specialRecipeType;
    public static ArrayList<String> specialRecipeTypes = new ArrayList<String>();
    public static ArrayList<ThresherRecipe> recipeList = new ArrayList<ThresherRecipe>();

    public ThresherRecipe(ItemStack output, Object input, ItemStack secondaryOutput, int time, int energyPerTick) {
        this.output = output;
        this.input = ApiUtils.createIngredientStack(input);
        //System.out.println(this.input);
        this.secondaryOutput = secondaryOutput;



        //System.out.println("INPUT FOR RECIPE "+this.input);
        this.oreInputString = input instanceof String ? (String) input : null;
        this.totalProcessTime = (int) Math.floor(time * timeModifier);
        this.totalProcessEnergy = (int) Math.floor(energyPerTick * energyModifier) * totalProcessTime;


        this.inputList = Lists.newArrayList(this.input);

        this.outputList = ListUtils.fromItem(this.output);
    }

    @Override
    public void setupJEI() {
        super.setupJEI();
        List<ItemStack>[] newJeiItemOutputList = new ArrayList[jeiItemOutputList.length+1];
        System.arraycopy(jeiItemOutputList,0, newJeiItemOutputList,0, jeiItemOutputList.length);
        newJeiItemOutputList[jeiItemOutputList.length] = Lists.newArrayList(this.secondaryOutput);
        jeiItemOutputList = newJeiItemOutputList;
        this.jeiTotalItemOutputList.add(this.secondaryOutput);
        //System.out.println(this.jeiTotalItemOutputList);
    }

    @Override
    public int getMultipleProcessTicks() {
        return 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("input", input.writeToNBT(new NBTTagCompound()));

        return nbt;
    }

    public static ThresherRecipe loadFromNBT(NBTTagCompound nbt) {
        IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));

        for (ThresherRecipe recipe : recipeList)
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

    public boolean matches(ItemStack input) {
        return this.input != null && this.input.matches(input);
    }

    public boolean isValidInput(ItemStack stack) { return this.input != null && this.input.matches(stack); }




    public ThresherRecipe setSpecialRecipeType(String type) {
        this.specialRecipeType = type;
        if (!specialRecipeTypes.contains(type))
            specialRecipeTypes.add(type);
        return this;
    }

    public static ThresherRecipe addRecipe(ItemStack output, Object input, ItemStack secondaryOutput, int time, int energyPerTick) {
        ThresherRecipe recipe = new ThresherRecipe(output, input, secondaryOutput, time, energyPerTick);

        if (recipe.input != null)

            recipeList.add(recipe);
        return recipe;
    }

    public static ThresherRecipe findRecipe(ItemStack input) {
        for (ThresherRecipe recipe : recipeList)
            if (recipe != null && recipe.matches(input))
                return recipe;
        return null;
    }

    public static List<ThresherRecipe> removeRecipes(ItemStack stack) {
        List<ThresherRecipe> list = new ArrayList();
        Iterator<ThresherRecipe> it = recipeList.iterator();
        while (it.hasNext()) {
            ThresherRecipe ir = it.next();
            if (OreDictionary.itemMatches(ir.output, stack, true)) {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    public static boolean isValidRecipeInput(ItemStack stack) {
        //System.out.println("RECIPE LIST "+recipeList);
        for (ThresherRecipe recipe : recipeList) {
            //System.out.println("RECIPE " + recipe + " " + recipe.isValidInput(stack));
            if (recipe != null && recipe.isValidInput(stack))
                return true;
        }
        return false;
    }
}
