package com.pyraliron.advancedtfctech.init;

import com.google.common.collect.ImmutableList;
import com.pyraliron.advancedtfctech.blocks.BlockCharcoalForgeATT;
import com.pyraliron.advancedtfctech.blocks.BlockElectricGrill;
import com.pyraliron.advancedtfctech.te.TileEntityCharcoalForgeATT;
import com.pyraliron.advancedtfctech.te.TileEntityElectricGrill;
import net.dries007.tfc.util.Helpers;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static com.pyraliron.advancedtfctech.util.Reference.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@GameRegistry.ObjectHolder(MOD_ID)
public class ModBlocks {
    @GameRegistry.ObjectHolder("firepit")
    public static final BlockElectricGrill ELECTRIC_GRILL = (BlockElectricGrill) Helpers.getNull();

    @GameRegistry.ObjectHolder("charcoalforge")
    public static final BlockCharcoalForgeATT CHARCOAL_FORGE_ATT = (BlockCharcoalForgeATT) Helpers.getNull();

    private static ImmutableList<ItemBlock> allInventoryItemBlocks;

    public static ImmutableList<ItemBlock> getAllInventoryItemBlocks()
    {
        return allInventoryItemBlocks;
    }
    @SubscribeEvent
    @SuppressWarnings("ConstantConditions")
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();

        ImmutableList.Builder<ItemBlock> inventoryItemBlocks = ImmutableList.builder();

        inventoryItemBlocks.add(new ItemBlock(register(r, "firepit", new BlockElectricGrill())));

        inventoryItemBlocks.add(new ItemBlock(register(r, "charcoalforge", new BlockCharcoalForgeATT())));


        allInventoryItemBlocks = inventoryItemBlocks.build();

        register(TileEntityElectricGrill.class, "fire_pit");

        register(TileEntityCharcoalForgeATT.class, "charcoal_forge");

    }
	/*public static final Block GRASS_BLOCK_PYRAL = new BlockGrassPyral();
	public static final Block DIRT_BLOCK_PYRAL = new BlockDirtPyral();
	public static final Block SAPLING_PYRAL = new BlockSaplingPyral();
	public static final Block ADVANCED_NOTE_BLOCK_LOWEST = new BlockAdvancedNote(0,"advanced_note_block_lowest");
	public static final Block ADVANCED_NOTE_BLOCK_LOWER = new BlockAdvancedNote(1,"advanced_note_block_lower");
	public static final Block ADVANCED_NOTE_BLOCK_LOW = new BlockAdvancedNote(2,"advanced_note_block_low");
	public static final Block ADVANCED_NOTE_BLOCK_MID = new BlockAdvancedNote(3,"advanced_note_block_mid");
	public static final Block ADVANCED_NOTE_BLOCK_HIGH = new BlockAdvancedNote(4,"advanced_note_block_high");
	public static final Block ADVANCED_NOTE_BLOCK_HIGHER = new BlockAdvancedNote(5,"advanced_note_block_higher");*/
    private static <T extends Block> T register(IForgeRegistry<Block> r, String name, T block)
    {
        block.setRegistryName(MOD_ID, name);
        block.setTranslationKey(MOD_ID + "." + name.replace('/', '.'));
        r.register(block);
        return block;
    }
    private static <T extends TileEntity> void register(Class<T> te, String name)
    {
        TileEntity.register(MOD_ID + ":" + name, te);
    }
}
