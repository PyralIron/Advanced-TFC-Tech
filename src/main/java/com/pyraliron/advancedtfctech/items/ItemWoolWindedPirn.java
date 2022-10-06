package com.pyraliron.advancedtfctech.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemWoolWindedPirn extends ItemBase {
    public ItemWoolWindedPirn() {
        super();
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