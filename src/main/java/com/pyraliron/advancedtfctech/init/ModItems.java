package com.pyraliron.advancedtfctech.init;

import com.pyraliron.advancedtfctech.items.ItemFiberWindedPirn;
import com.pyraliron.advancedtfctech.items.ItemPirn;
import com.pyraliron.advancedtfctech.items.ItemSilkWindedPirn;
import com.pyraliron.advancedtfctech.items.ItemWoolWindedPirn;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ModItems {
    public static final CreativeTabs tabAdvancedTFCTech = (new CreativeTabs("advancedTFCTech") {
        @Override public ItemStack getTabIconItem() {
            return new ItemStack(PIRN);
        }
    });
    public static final Item PIRN = new ItemPirn();
    public static final Item FIBER_WINDED_PIRN = new ItemFiberWindedPirn();
    public static final Item SILK_WINDED_PIRN = new ItemSilkWindedPirn();
    public static final Item WOOL_WINDED_PIRN = new ItemWoolWindedPirn();
}
