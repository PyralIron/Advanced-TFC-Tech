package com.pyraliron.advancedtfctech.util.handlers;

import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import com.pyraliron.advancedtfctech.util.Reference;
import com.pyraliron.pyralfishmod.init.ModBlocks;
import com.pyraliron.pyralfishmod.init.ModItems;
import com.pyraliron.pyralfishmod.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static com.pyraliron.advancedtfctech.AdvancedTFCTech.registeredATTBlocks;
import static com.pyraliron.advancedtfctech.AdvancedTFCTech.registeredATTItems;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class RegistryHandler {
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {
		System.out.println("F************************************************************");
		//event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		for (Item item : registeredATTItems)
		{
			event.getRegistry().register(item.setRegistryName(createRegistryName(item.getUnlocalizedName())));
		}
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		//event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
		System.out.println(registeredATTBlocks);
		for (Block block : registeredATTBlocks)
		{
			System.out.println(block.getUnlocalizedName());
			event.getRegistry().register(block.setRegistryName(createRegistryName(block.getUnlocalizedName())));
		}
	}
	private static ResourceLocation createRegistryName(String unlocalized) {
		System.out.println("REG NAME "+unlocalized+" -----------------------------");
		unlocalized = unlocalized.substring(unlocalized.indexOf("att"));
		unlocalized = unlocalized.replaceFirst("\\.", ":");
		return new ResourceLocation(unlocalized);
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		//event.getRegistry().registerAll(SoundEvents.NOTE_BLOCK_HIGH_HIGHER,SoundEvents.NOTE_BLOCK_LOWEST_LOWER,SoundEvents.NOTE_BLOCK_LOW_MID,SoundEvents.NOTE_BLOCK_HIGH,SoundEvents.NOTE_BLOCK_HIGHER);
		System.out.println("registered sounds for att");
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		for (Item item : ModItems.ITEMS) {
			if (item instanceof IHasModel) {
				((IHasModel)item).registerModel();
			}
		}
		for (Block block : ModBlocks.BLOCKS) {
			if (block instanceof IHasModel) {
				((IHasModel)block).registerModel();
			}
		}
	}
	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event)
	{
		IngredientStack goldingot = new IngredientStack(new ItemStack(Items.GOLD_INGOT,1));
		MetalPressRecipe.addRecipe(new ItemStack(Blocks.BONE_BLOCK, 1), goldingot, new ItemStack(IEContent.blockStorage, 1, 8), 2400);


	}
	
	
	
}