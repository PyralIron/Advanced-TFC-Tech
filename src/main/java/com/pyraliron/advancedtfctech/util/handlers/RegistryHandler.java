package com.pyraliron.advancedtfctech.util.handlers;

import blusunrize.immersiveengineering.api.crafting.CrusherRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.common.IEContent;
import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import com.pyraliron.advancedtfctech.init.ModItems;
import com.pyraliron.advancedtfctech.util.IHasModel;
import com.pyraliron.advancedtfctech.util.Reference;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.dries007.tfc.objects.items.food.ItemFoodTFC;
import net.dries007.tfc.objects.items.metal.ItemMetal;
import net.dries007.tfc.util.agriculture.Food;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.pyraliron.advancedtfctech.AdvancedTFCTech.registeredATTBlocks;
import static com.pyraliron.advancedtfctech.AdvancedTFCTech.registeredATTItems;
import static net.dries007.tfc.api.types.Metal.ItemType.DUST;

@EventBusSubscriber(modid = Reference.MOD_ID)
public class RegistryHandler {
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void guiOpen(GuiOpenEvent event){}
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event) {

		//event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
		for (Item item : registeredATTItems)
		{
			if (item.getRegistryName() == null) {
				event.getRegistry().register(item.setRegistryName(createRegistryName(item.getTranslationKey())));
			} else {
				event.getRegistry().register(item);
			}
		}
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event) {
		//event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
		//System.out.println(registeredATTBlocks);

		for (Block block : registeredATTBlocks)
		{
			//System.out.println(block.getTranslationKey());
			event.getRegistry().register(block.setRegistryName(createRegistryName(((BlockATTBase)block).getTranslationKey())));
			//System.out.println(block.getRegistryName());
		}
	}
	private static ResourceLocation createRegistryName(String unlocalized) {
		//System.out.println("REG NAME "+unlocalized+" -----------------------------");
		unlocalized = unlocalized.substring(unlocalized.indexOf("att"));
		unlocalized = unlocalized.replaceFirst("\\.", ":");
		return new ResourceLocation(unlocalized);
	}
	
	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
		//event.getRegistry().registerAll(SoundEvents.NOTE_BLOCK_HIGH_HIGHER,SoundEvents.NOTE_BLOCK_LOWEST_LOWER,SoundEvents.NOTE_BLOCK_LOW_MID,SoundEvents.NOTE_BLOCK_HIGH,SoundEvents.NOTE_BLOCK_HIGHER);
		//System.out.println("registered sounds for att");
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event) {
		//System.out.println("REGISTER MODEL");
		for (Item item : registeredATTItems) {
			//System.out.println("TRY REGISTER MODEL FOR "+item);
			//System.out.println("ITEM INSTANCEOF IHASMODEL "+(item instanceof IHasModel));
			if (item instanceof IHasModel) {
				//System.out.println("REGISTERED MODEL "+item.getUnlocalizedName()+" "+item.getRegistryName());
				((IHasModel)item).registerModel();
			}
		}
		for (Block block : registeredATTBlocks) {
			//System.out.println(block);
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

		for (ModContainer i : Loader.instance().getActiveModList()) {
			//System.out.println("MOD "+ i.getModId());
			if (i.getModId().contentEquals("tfc")) {
				IngredientStack ingredientFiberPirn = new IngredientStack(new ItemStack(ModItems.FIBER_WINDED_PIRN,1));
				IngredientStack ingredientSilkPirn = new IngredientStack(new ItemStack(ModItems.SILK_WINDED_PIRN,1));
				IngredientStack ingredientWoolPirn = new IngredientStack(new ItemStack(ModItems.WOOL_WINDED_PIRN,1));
				IngredientStack ingredientFiber = new IngredientStack(new ItemStack(ItemsTFC.JUTE_FIBER,48));
				IngredientStack ingredientHemp = new IngredientStack(new ItemStack(IEContent.itemMaterial,48,4));
				IngredientStack ingredientString = new IngredientStack(new ItemStack(Items.STRING,48));
				IngredientStack ingredientWoolYarn = new IngredientStack(new ItemStack(ItemsTFC.WOOL_YARN,48));
				//IngredientStack ingredientBarleyFlour = new IngredientStack(new ItemStack(ItemsTFC.getAllSimpleItems().get(),4));

				//PowerLoomRecipe.addRecipe(new ItemStack(ItemsTFC.JUTE_FIBER, 4), goldingot, new ItemStack(Blocks.DIRT,1),100, 100);
				PowerLoomRecipe.addRecipe(new ItemStack(ItemsTFC.BURLAP_CLOTH, 4), ingredientFiberPirn, ingredientFiber, new ItemStack(ModItems.PIRN),500, 256);
				PowerLoomRecipe.addRecipe(new ItemStack(ItemsTFC.BURLAP_CLOTH, 4), ingredientFiberPirn, ingredientHemp, new ItemStack(ModItems.PIRN),500, 256);
				PowerLoomRecipe.addRecipe(new ItemStack(ItemsTFC.SILK_CLOTH, 4), ingredientSilkPirn, ingredientString, new ItemStack(ModItems.PIRN),500, 256);
				PowerLoomRecipe.addRecipe(new ItemStack(ItemsTFC.WOOL_CLOTH, 4), ingredientWoolPirn, ingredientWoolYarn, new ItemStack(ModItems.PIRN),500, 256);

				Food[] raw_grains = {Food.BARLEY,Food.MAIZE,Food.OAT,Food.RICE,Food.RYE,Food.WHEAT};
				Food[] refined_grains = {Food.BARLEY_GRAIN,Food.MAIZE_GRAIN,Food.OAT_GRAIN,Food.RICE_GRAIN,Food.RYE_GRAIN,Food.WHEAT_GRAIN};
				Food[] flours = {Food.BARLEY_FLOUR,Food.CORNMEAL_FLOUR,Food.OAT_FLOUR,Food.RICE_FLOUR,Food.RYE_FLOUR,Food.WHEAT_FLOUR};
				for (int ii = 0; ii < raw_grains.length; ii++) {
					Food raw_grain = raw_grains[ii];
					Food refined_grain = refined_grains[ii];
					Food flour = flours[ii];
					IngredientStack ingredientGrain = new IngredientStack(new ItemStack(ItemFoodTFC.get(raw_grain),1));
					ThresherRecipe.addRecipe(new ItemStack(ItemFoodTFC.get(refined_grain),3), ingredientGrain,new ItemStack(ItemsTFC.STRAW,4),100,256);

					IngredientStack ingredientRefinedGrain = new IngredientStack(new ItemStack(ItemFoodTFC.get(refined_grain),1));
					GristMillRecipe.addRecipe(new ItemStack(ItemFoodTFC.get(flour),3), ingredientRefinedGrain,100,256);

				}
				/*for (QuernRecipe quernRecipe : TFCRegistries.QUERN.getValuesCollection())
				{
					NonNullList<IIngredient<ItemStack>> ingredientlist = quernRecipe.getIngredients();
					IIngredient iingredient = ingredientlist.get(0);
					NonNullList foo = iingredient.getValidIngredients();
					ItemStack input = (ItemStack) foo.get(0);

					if (input.hasCapability(CapabilityFood.CAPABILITY, null)){
						Ingredient ingredient = Ingredient.fromStacks(input);


						ItemStack output = quernRecipe.getOutputs().get(0);
						Item item = output.getItem();

						//The IE Crusher is just super-efficient
						int amount = Math.min(output.getCount() * 2 + 2, 9);
						int meta = output.getMetadata();
						ItemStack newoutput = new ItemStack(item, amount, meta);

						if (ingredient != null)
						{
							ThresherRecipe.addRecipe(newoutput, ingredient,new ItemStack(ItemsTFC.STRAW,4),100,256);
						}
					}
				}*/
				for (Metal metal : TFCRegistries.METALS.getValuesCollection()) {
					//Basic ingot to dust
					if (DUST.hasType(metal)) {
						Ingredient ingredientIngot = Ingredient.fromStacks(new ItemStack(ItemMetal.get(metal, Metal.ItemType.INGOT)));
						CrusherRecipe.addRecipe(new ItemStack(ItemMetal.get(metal, DUST), 1), ingredientIngot, 8000);
					}
				}
			}
		}
		//System.out.println("MOD LIST"+Loader.instance().getActiveModList().get(0).getModId());

	}
	
	
	
}