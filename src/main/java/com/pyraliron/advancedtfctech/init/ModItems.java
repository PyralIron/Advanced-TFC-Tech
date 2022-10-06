package com.pyraliron.advancedtfctech.init;

import com.google.common.collect.ImmutableList;
import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.items.ItemFiberWindedPirn;
import com.pyraliron.advancedtfctech.items.ItemPirn;
import com.pyraliron.advancedtfctech.items.ItemSilkWindedPirn;
import com.pyraliron.advancedtfctech.items.ItemWoolWindedPirn;
import net.dries007.tfc.util.Helpers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static com.pyraliron.advancedtfctech.CreativeTabsATT.tabAdvancedTFCTech;
import static com.pyraliron.advancedtfctech.util.Reference.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@GameRegistry.ObjectHolder(MOD_ID)
public class ModItems {

    @GameRegistry.ObjectHolder("pirn")
    public static final Item PIRN = Helpers.getNull();// = new ItemPirn();
    @GameRegistry.ObjectHolder("fiber_winded_pirn")
    public static final Item FIBER_WINDED_PIRN = Helpers.getNull();//= new ItemFiberWindedPirn();
    @GameRegistry.ObjectHolder("silk_winded_pirn")
    public static final Item SILK_WINDED_PIRN = Helpers.getNull();//= new ItemSilkWindedPirn();
    @GameRegistry.ObjectHolder("wool_winded_pirn")
    public static final Item WOOL_WINDED_PIRN = Helpers.getNull();//= new ItemWoolWindedPirn();

    private static ImmutableList<Item> allEasyItems;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> r = event.getRegistry();

        ImmutableList.Builder<Item> easyItems = ImmutableList.builder();
        easyItems.add(register(r, "pirn", new ItemPirn(), tabAdvancedTFCTech));
        easyItems.add(register(r, "fiber_winded_pirn", new ItemFiberWindedPirn(), tabAdvancedTFCTech));
        easyItems.add(register(r, "silk_winded_pirn", new ItemSilkWindedPirn(), tabAdvancedTFCTech));
        easyItems.add(register(r, "wool_winded_pirn", new ItemWoolWindedPirn(), tabAdvancedTFCTech));

        ModBlocks.getAllInventoryItemBlocks().forEach(x -> registerItemBlock(r, x));
    }

    private static <T extends Item> T register(IForgeRegistry<Item> r, String name, T item, CreativeTabs ct)
    {
        item.setRegistryName(MOD_ID, name);
        item.setCreativeTab(ct);
        item.setTranslationKey(MOD_ID + "." + name.replace('/', '.'));
        r.register(item);
        AdvancedTFCTech.registeredATTItems.add(item);
        return item;
    }
    @SuppressWarnings("ConstantConditions")
    private static void registerItemBlock(IForgeRegistry<Item> r, ItemBlock item)
    {
        item.setRegistryName(item.getBlock().getRegistryName());
        item.setCreativeTab(item.getBlock().getCreativeTab());
        r.register(item);
    }
}
