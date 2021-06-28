package com.pyraliron.advancedtfctech.items;

import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.util.IHasModel;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.items.ItemTFC;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemBase extends ItemTFC implements IHasModel {

    public ItemBase(String name, CreativeTabs tab) {
        System.out.println(ItemTFC.class);
        setTranslationKey(name);
        setRegistryName("att:"+name);
        setCreativeTab(tab);
        //System.out.println("ITEM ADDED-----------------------------------------------------------------------");
        AdvancedTFCTech.registeredATTItems.add(this);

    }
    public void registerModel() {
        AdvancedTFCTech.proxy.registerItemRenderer(this, 0, "inventory");
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

