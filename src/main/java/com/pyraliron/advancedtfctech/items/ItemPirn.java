package com.pyraliron.advancedtfctech.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

import static com.pyraliron.advancedtfctech.init.ModItems.tabAdvancedTFCTech;

public class ItemPirn extends ItemBase {
    public ItemPirn() {
        super("pirn",tabAdvancedTFCTech);
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack itemStack) {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack itemStack) {
        return Weight.VERY_LIGHT;
    }
}
