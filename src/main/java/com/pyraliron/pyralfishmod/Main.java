package com.pyraliron.pyralfishmod;

import com.pyraliron.pyralfishmod.init.ModBiomes;
import com.pyraliron.pyralfishmod.init.ModBlocks;
import com.pyraliron.pyralfishmod.proxy.CommonProxy;
import com.pyraliron.pyralfishmod.tileentity.TileEntityAdvancedNote;
import com.pyraliron.pyralfishmod.util.Reference;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.NAME, version = Reference.VERSION)
public class Main {

	@net.minecraftforge.fml.common.Mod.Instance
	public static Main Instance;
	
	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		ModBiomes.init();
		
		GameRegistry.registerTileEntity(TileEntityAdvancedNote.class, Reference.MOD_ID+"advanced_note_block"/*ModBlocks.ADVANCED_NOTE_BLOCK.getRegistryName()*/);
	}
	
	@EventHandler
	public static void init(FMLInitializationEvent event) {
		
	}
	
	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		
	}
	
}
