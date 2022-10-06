package com.pyraliron.advancedtfctech.blocks;

import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class BlockTemperatureGauge extends Block implements IItemSize {
    public static final PropertyInteger TEMP = PropertyInteger.create("power", 0, 3000/50);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool INVERSED = PropertyBool.create("inversed");

    public BlockTemperatureGauge() {
        super(Material.CIRCUITS);
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack) {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack) {
        return Weight.LIGHT;
    }
}
