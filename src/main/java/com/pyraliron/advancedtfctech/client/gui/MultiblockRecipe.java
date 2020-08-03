package com.pyraliron.advancedtfctech.client.gui;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IJEIRecipe;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class MultiblockRecipe implements IMultiblockRecipe, IJEIRecipe {
    protected List<IngredientStack> inputList;
    protected NonNullList<ItemStack> outputList;
    protected List<FluidStack> fluidInputList;
    protected List<FluidStack> fluidOutputList;
    protected int totalProcessTime;
    protected int totalProcessEnergy;
    public List<ItemStack>[] jeiItemInputList;
    protected List<ItemStack> jeiTotalItemInputList;
    public List<ItemStack>[] jeiItemOutputList;
    protected List<ItemStack> jeiTotalItemOutputList;
    protected List<FluidStack> jeiFluidInputList;
    protected List<FluidStack> jeiFluidOutputList;

    public MultiblockRecipe() {
    }

    public List<IngredientStack> getItemInputs() {
        return this.inputList;
    }

    public NonNullList<ItemStack> getItemOutputs() {
        return this.outputList;
    }

    public List<FluidStack> getFluidInputs() {
        return this.fluidInputList;
    }

    public List<FluidStack> getFluidOutputs() {
        return this.fluidOutputList;
    }

    public int getTotalProcessTime() {
        return this.totalProcessTime;
    }

    public int getTotalProcessEnergy() {
        return this.totalProcessEnergy;
    }

    public void setupJEI() {
        int i;
        ArrayList list;
        if (this.inputList != null) {
            this.jeiItemInputList = new ArrayList[this.inputList.size()];
            this.jeiTotalItemInputList = new ArrayList();

            for(i = 0; i < this.inputList.size(); ++i) {
                IngredientStack ingr = (IngredientStack)this.inputList.get(i);
                list = new ArrayList();
                Iterator var4;
                ItemStack stack;
                if (ingr.oreName != null) {
                    var4 = OreDictionary.getOres(ingr.oreName).iterator();

                    while(var4.hasNext()) {
                        stack = (ItemStack)var4.next();
                        list.add(ApiUtils.copyStackWithAmount(stack, ingr.inputSize));
                    }
                } else if (ingr.stackList != null) {
                    var4 = ingr.stackList.iterator();

                    while(var4.hasNext()) {
                        stack = (ItemStack)var4.next();
                        list.add(ApiUtils.copyStackWithAmount(stack, ingr.inputSize));
                    }
                } else {
                    list.add(ApiUtils.copyStackWithAmount(ingr.stack, ingr.inputSize));
                }

                this.jeiItemInputList[i] = list;
                this.jeiTotalItemInputList.addAll(list);
            }
        } else {
            this.jeiTotalItemInputList = Collections.emptyList();
        }

        if (this.outputList != null) {
            this.jeiItemOutputList = new ArrayList[this.outputList.size()];
            this.jeiTotalItemOutputList = new ArrayList();

            for(i = 0; i < this.outputList.size(); ++i) {
                ItemStack s = outputList.get(i);
                list = Lists.newArrayList(!s.isEmpty()?s.copy():ItemStack.EMPTY);
                this.jeiItemOutputList[i] = list;
                this.jeiTotalItemOutputList.addAll(list);
            }
        } else {
            this.jeiTotalItemOutputList = Collections.emptyList();
        }

        FluidStack fs;
        if (this.fluidInputList != null) {
            this.jeiFluidInputList = new ArrayList();

            for(i = 0; i < this.fluidInputList.size(); ++i) {
                fs = (FluidStack)this.fluidInputList.get(i);
                if (fs != null) {
                    this.jeiFluidInputList.add(fs.copy());
                }
            }
        } else {
            this.jeiFluidInputList = Collections.emptyList();
        }

        if (this.fluidOutputList != null) {
            this.jeiFluidOutputList = new ArrayList();

            for(i = 0; i < this.fluidOutputList.size(); ++i) {
                fs = (FluidStack)this.fluidOutputList.get(i);
                if (fs != null) {
                    this.jeiFluidOutputList.add(fs.copy());
                }
            }
        } else {
            this.jeiFluidOutputList = Collections.emptyList();
        }

    }

    public List<ItemStack> getJEITotalItemInputs() {
        return this.jeiTotalItemInputList;
    }

    public List<ItemStack> getJEITotalItemOutputs() {
        return this.jeiTotalItemOutputList;
    }

    public List<FluidStack> getJEITotalFluidInputs() {
        return this.jeiFluidInputList;
    }

    public List<FluidStack> getJEITotalFluidOutputs() {
        return this.jeiFluidOutputList;
    }
}

