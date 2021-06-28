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

public class GristMillRecipe extends MultiblockRecipe {
    public static float energyModifier = 1;
    public static float timeModifier = 1;

    public final IngredientStack input;
    public final String oreInputString;
    public final ItemStack output;
    public String specialRecipeType;
    public static ArrayList<String> specialRecipeTypes = new ArrayList<String>();
    public static ArrayList<GristMillRecipe> recipeList = new ArrayList<GristMillRecipe>();

    public GristMillRecipe(ItemStack output, Object input, int time, int energyPerTick) {
        this.output = output;
        this.input = ApiUtils.createIngredientStack(input);
        //System.out.println(this.input);



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

        return nbt;
    }

    public static GristMillRecipe loadFromNBT(NBTTagCompound nbt) {
        IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));

        for (GristMillRecipe recipe : recipeList)
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




    public GristMillRecipe setSpecialRecipeType(String type) {
        this.specialRecipeType = type;
        if (!specialRecipeTypes.contains(type))
            specialRecipeTypes.add(type);
        return this;
    }

    public static GristMillRecipe addRecipe(ItemStack output, Object input, int time, int energyPerTick) {
        GristMillRecipe recipe = new GristMillRecipe(output, input, time, energyPerTick);

        if (recipe.input != null)

            recipeList.add(recipe);
        return recipe;
    }

    public static GristMillRecipe findRecipe(ItemStack input) {
        for (GristMillRecipe recipe : recipeList)
            if (recipe != null && recipe.matches(input))
                return recipe;
        return null;
    }

    public static List<GristMillRecipe> removeRecipes(ItemStack stack) {
        List<GristMillRecipe> list = new ArrayList();
        Iterator<GristMillRecipe> it = recipeList.iterator();
        while (it.hasNext()) {
            GristMillRecipe ir = it.next();
            if (OreDictionary.itemMatches(ir.output, stack, true)) {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    public static boolean isValidRecipeInput(ItemStack stack) {
        //System.out.println("RECIPE LIST "+recipeList);
        for (GristMillRecipe recipe : recipeList) {
            //System.out.println("RECIPE " + recipe + " " + recipe.isValidInput(stack));
            if (recipe != null && recipe.isValidInput(stack))
                return true;
        }
        return false;
    }
}
