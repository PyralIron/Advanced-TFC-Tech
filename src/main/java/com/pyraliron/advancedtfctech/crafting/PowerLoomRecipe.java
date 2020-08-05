package com.pyraliron.advancedtfctech.crafting;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.util.ListUtils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.client.gui.MultiblockRecipe;
import com.pyraliron.advancedtfctech.init.ModItems;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe.EnumPowerLoomProcessType.WOOL_CLOTH;

public class PowerLoomRecipe extends MultiblockRecipe {
    public static float energyModifier = 1;
    public static float timeModifier = 1;

    public final IngredientStack input;
    public final IngredientStack secondaryInput;
    public final String oreInputString;
    public final ItemStack output;
    public final ItemStack secondaryOutput;
    public final EnumPowerLoomProcessType processType;
    public String specialRecipeType;
    public static ArrayList<String> specialRecipeTypes = new ArrayList<String>();
    public static ArrayList<PowerLoomRecipe> recipeList = new ArrayList<PowerLoomRecipe>();

    public PowerLoomRecipe(ItemStack output, Object input, Object secondaryInput, ItemStack secondaryOutput, int time, int energyPerTick) {
        this.output = output;
        this.input = ApiUtils.createIngredientStack(input);
        this.secondaryInput =  ApiUtils.createIngredientStack(secondaryInput);
        if (this.input.equals(new IngredientStack(new ItemStack(ModItems.SILK_WINDED_PIRN,1)))) {this.processType = EnumPowerLoomProcessType.SILK_CLOTH;}
        else if (this.input.equals(new IngredientStack(new ItemStack(ModItems.WOOL_WINDED_PIRN,1)))) {this.processType = WOOL_CLOTH;}
        else if (this.input.equals(new IngredientStack(new ItemStack(ModItems.FIBER_WINDED_PIRN,1)))) {this.processType = EnumPowerLoomProcessType.BURLAP;}
        else {this.processType = EnumPowerLoomProcessType.SILK_CLOTH;}
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

    public static PowerLoomRecipe loadFromNBT(NBTTagCompound nbt) {
        IngredientStack input = IngredientStack.readFromNBT(nbt.getCompoundTag("input"));

        for (PowerLoomRecipe recipe : recipeList)
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

    public boolean isValidSecondary(ItemStack stack) { return this.secondaryInput != null && this.secondaryInput.matches(stack); }



    public PowerLoomRecipe setSpecialRecipeType(String type) {
        this.specialRecipeType = type;
        if (!specialRecipeTypes.contains(type))
            specialRecipeTypes.add(type);
        return this;
    }

    public static PowerLoomRecipe addRecipe(ItemStack output, Object input, Object secondaryInput, ItemStack secondaryOutput, int time, int energyPerTick) {
        PowerLoomRecipe recipe = new PowerLoomRecipe(output, input, secondaryInput, secondaryOutput, time, energyPerTick);

        if (recipe.input != null)

            recipeList.add(recipe);
        return recipe;
    }

    public static PowerLoomRecipe findRecipe(ItemStack input) {
        for (PowerLoomRecipe recipe : recipeList)
            if (recipe != null && recipe.matches(input))
                return recipe;
        return null;
    }

    public static List<PowerLoomRecipe> removeRecipes(ItemStack stack) {
        List<PowerLoomRecipe> list = new ArrayList();
        Iterator<PowerLoomRecipe> it = recipeList.iterator();
        while (it.hasNext()) {
            PowerLoomRecipe ir = it.next();
            if (OreDictionary.itemMatches(ir.output, stack, true)) {
                list.add(ir);
                it.remove();
            }
        }
        return list;
    }

    public static boolean isValidRecipeInput(ItemStack stack) {
        //System.out.println("RECIPE LIST "+recipeList);
        for (PowerLoomRecipe recipe : recipeList) {
            //System.out.println("RECIPE " + recipe + " " + recipe.isValidInput(stack));
            if (recipe != null && recipe.isValidInput(stack))
                return true;
        }
        return false;
    }
    public static boolean isValidRecipeSecondary(ItemStack stack) {
        //System.out.println("RECIPE LIST "+recipeList);
        for (PowerLoomRecipe recipe : recipeList) {
            //System.out.println("RECIPE " + recipe + " " + recipe.isValidInput(stack));
            if (recipe != null && recipe.isValidSecondary(stack))
                return true;
        }
        return false;
    }
    public static boolean inputMatchesSecondary(ItemStack input, ItemStack secondary, boolean allowEmpty) {
        if (input.isEmpty() || secondary.isEmpty()) {return allowEmpty;}
        //System.out.println("name input "+input.getItem().getUnlocalizedName()+" "+secondary.getItem().getUnlocalizedName()+" "+input.getMetadata()+" "+secondary.getMetadata());
        if (input.getItem().equals(ModItems.FIBER_WINDED_PIRN) && (secondary.getItem().equals(IEContent.itemMaterial) && secondary.getMetadata() == 4 || secondary.getItem().equals(ItemsTFC.JUTE_FIBER))) {return true;}
        if (input.getItem().equals(ModItems.WOOL_WINDED_PIRN)&& (secondary.getItem().equals(ItemsTFC.WOOL_YARN))) {return true;}
        if (input.getItem().equals(ModItems.SILK_WINDED_PIRN)&& (secondary.getItem().equals(Items.STRING))) {return true;}
        return false;
    }
    public enum EnumPowerLoomProcessType {
        WOOL_CLOTH,
        SILK_CLOTH,
        BURLAP,
        BURLAP_HEMP
        ;
        public ItemStack getPirnFromType() {
            return this.ordinal() == WOOL_CLOTH.ordinal() ? new ItemStack(ModItems.WOOL_WINDED_PIRN) : this.ordinal() == EnumPowerLoomProcessType.SILK_CLOTH.ordinal() ? new ItemStack(ModItems.SILK_WINDED_PIRN) : new ItemStack(ModItems.FIBER_WINDED_PIRN);
        }
        public ItemStack getSecondaryFromType() {
            return this.ordinal() == WOOL_CLOTH.ordinal() ? new ItemStack(ItemsTFC.WOOL_YARN) : this.ordinal() == EnumPowerLoomProcessType.SILK_CLOTH.ordinal() ? new ItemStack(Items.STRING) : this.ordinal() == EnumPowerLoomProcessType.BURLAP.ordinal() ? new ItemStack(ItemsTFC.JUTE_FIBER) : new ItemStack(IEContent.itemMaterial,1,4);
        }
        public ItemStack getOutputFromType() {
            return this.ordinal() == WOOL_CLOTH.ordinal() ? new ItemStack(ItemsTFC.WOOL_CLOTH) : this.ordinal() == EnumPowerLoomProcessType.SILK_CLOTH.ordinal() ? new ItemStack(ItemsTFC.SILK_CLOTH) : new ItemStack(ItemsTFC.BURLAP_CLOTH);
        }

    }

}

