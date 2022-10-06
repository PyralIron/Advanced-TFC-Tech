package com.pyraliron.advancedtfctech;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import static com.pyraliron.advancedtfctech.init.ModItems.PIRN;

public class CreativeTabsATT {
    public static final CreativeTabs tabAdvancedTFCTech = (new CreativeTabs("advancedTFCTech") {
        @Override public ItemStack createIcon() {
            return new ItemStack(PIRN);
        }
    });
}
