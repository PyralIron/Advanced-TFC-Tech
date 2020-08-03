package com.pyraliron.advancedtfctech;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import com.pyraliron.advancedtfctech.init.ModItems;
import com.pyraliron.advancedtfctech.multiblocks.BlockATTMetalMultiblocks;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockPowerLoom;
import com.pyraliron.advancedtfctech.proxy.CommonProxy;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION, dependencies = Reference.DEPENDENCIES)
public class AdvancedTFCTech {
	public static BlockATTBase blockMetalMultiblock;
	public static ArrayList<Block> registeredATTBlocks = new ArrayList();
	public static ArrayList<Item> registeredATTItems = new ArrayList();

	@net.minecraftforge.fml.common.Mod.Instance
	public static AdvancedTFCTech Instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;

	public static ModItems modItems = new ModItems();
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		//ModBiomes.init();

		MinecraftForge.EVENT_BUS.register(new com.pyraliron.advancedtfctech.util.handlers.EventHandler());
		blockMetalMultiblock = new BlockATTMetalMultiblocks();
		OBJLoader.INSTANCE.addDomain("immersiveengineering:metalmultiblock/arcfurnace.obj");
		proxy.registerRenderers();
		MultiblockHandler.registerMultiblock(MultiblockPowerLoom.instance);
		//GameRegistry.registerTileEntity(TileEntityAdvancedNote.class, Reference.MOD_ID+"advanced_note_block"/*ModBlocks.ADVANCED_NOTE_BLOCK.getRegistryName()*/);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		registerTile(TileEntityPowerLoom.class);
		registerTile(TileEntityPowerLoom.TileEntityPowerLoomParent.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(Instance, proxy);


	}
	public static void registerTile(Class<? extends TileEntity> tile) {
		String s = tile.getSimpleName();
		s = s.substring(s.indexOf("TileEntity") + "TileEntity".length());
		GameRegistry.registerTileEntity(tile, "att:" + s);

	}




	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
	}

	
}
