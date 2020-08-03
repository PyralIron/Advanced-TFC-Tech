package com.pyraliron.pyralfishmod.util.handlers;

import com.pyraliron.pyralfishmod.init.ModBlocks;
import com.pyraliron.pyralfishmod.init.ModItems;
import com.pyraliron.pyralfishmod.init.SoundEvents;
import com.pyraliron.pyralfishmod.util.IHasModel;
import com.pyraliron.pyralfishmod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		event.getRegistry().registerAll(SoundEvents.NOTE_BLOCK_HIGH_HIGHER,SoundEvents.NOTE_BLOCK_LOWEST_LOWER,SoundEvents.NOTE_BLOCK_LOW_MID,SoundEvents.NOTE_BLOCK_HIGH,SoundEvents.NOTE_BLOCK_HIGHER);
		//System.out.println("registered sounds for pyralfish");
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ModItems.ITEMS) {
			if (item instanceof IHasModel) {
				//System.out.println("REGISTERED MODEL "+item.getUnlocalizedName()+" "+item.getRegistryName());
				((IHasModel)item).registerModel();
			}
		}
		for (Block block : ModBlocks.BLOCKS) {
			if (block instanceof IHasModel) {
				//System.out.println("REGISTERED MODEL "+block.getUnlocalizedName()+" "+block.getRegistryName());
				((IHasModel)block).registerModel();
			}
		}
	}
	
	
	
}
