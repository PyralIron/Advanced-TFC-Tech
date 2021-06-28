package com.pyraliron.advancedtfctech.proxy;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.client.IECustomStateMapper;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.lib.manual.ManualPages;
import com.pyraliron.advancedtfctech.client.gui.GuiGristMill;
import com.pyraliron.advancedtfctech.client.gui.GuiPowerLoom;
import com.pyraliron.advancedtfctech.client.gui.GuiThresher;
import com.pyraliron.advancedtfctech.client.render.TileRenderGristMill;
import com.pyraliron.advancedtfctech.client.render.TileRenderPowerLoom;
import com.pyraliron.advancedtfctech.client.render.TileRenderThresher;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import com.pyraliron.advancedtfctech.init.ModItems;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockGristMill;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockPowerLoom;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockThresher;
import com.pyraliron.advancedtfctech.te.TileEntityGristMill;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.te.TileEntityThresher;
import com.pyraliron.advancedtfctech.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.pyraliron.advancedtfctech.AdvancedTFCTech.registeredATTBlocks;

@Mod.EventBusSubscriber(value = {Side.CLIENT},modid = Reference.MOD_ID)
public class ClientProxy extends CommonProxy {
	public static final String CAT_ATT = "att";
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		//System.out.println(item);
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerLoom.TileEntityPowerLoomParent.class, new TileRenderPowerLoom());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityThresher.TileEntityThresherParent.class, new TileRenderThresher());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGristMill.TileEntityGristMillParent.class, new TileRenderGristMill());
		// ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMultiblockPartATT.class, new TileRenderPowerLoom());

		//OBJLoader.INSTANCE.addDomain("immersiveengineering");
	}
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt)
	{
		for(Block block : registeredATTBlocks)
		{
			final ResourceLocation loc = Block.REGISTRY.getNameForObject(block);
			Item blockItem = Item.getItemFromBlock(block);
			if(blockItem==null)
				throw new RuntimeException("ITEMBLOCK FOR "+loc+" : "+block+" IS NULL");
			if(block instanceof IEBlockInterfaces.IIEMetaBlock)
			{
				IEBlockInterfaces.IIEMetaBlock ieMetaBlock = (IEBlockInterfaces.IIEMetaBlock)block;
				//System.out.println(ieMetaBlock+" "+ieMetaBlock.getMetaEnums());
				if(ieMetaBlock.useCustomStateMapper())
					ModelLoader.setCustomStateMapper(block, IECustomStateMapper.getStateMapper(ieMetaBlock));
				ModelLoader.setCustomMeshDefinition(blockItem, new ItemMeshDefinition()
				{
					@Override
					public ModelResourceLocation getModelLocation(ItemStack stack)
					{
						return new ModelResourceLocation(loc, "inventory");
					}
				});
				for(int meta = 0; meta < ieMetaBlock.getMetaEnums().length; meta++)
				{
					String location = loc.toString();
					String prop = ieMetaBlock.appendPropertiesToState()?("inventory,"+ieMetaBlock.getMetaProperty().getName()+"="+ieMetaBlock.getMetaEnums()[meta].toString().toLowerCase(Locale.US)): null;
					//System.out.println(location+" "+prop);
					if(ieMetaBlock.useCustomStateMapper())
					{
						String custom = ieMetaBlock.getCustomStateMapping(meta, true);
						if(custom!=null)
							location += "_"+custom;
					}
					try
					{
						ModelLoader.setCustomModelResourceLocation(blockItem, meta, new ModelResourceLocation(location, prop));
					} catch(NullPointerException npe)
					{
						throw new RuntimeException("WELP! apparently "+ieMetaBlock+" lacks an item!", npe);
					}
				}
			}
			else
				ModelLoader.setCustomModelResourceLocation(blockItem, 0, new ModelResourceLocation(loc, "inventory"));
		}
	}
	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		//System.out.println("CLIENT GUI ELEMENT");
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof IEBlockInterfaces.IGuiTile) {
			Object gui = null;
			if (ID == Reference.GUIID_PowerLoom && te instanceof TileEntityPowerLoom)
				gui = new GuiPowerLoom(player.inventory, (TileEntityPowerLoom) te);
			else if (ID == Reference.GUIID_Thresher && te instanceof TileEntityThresher)
				//System.out.println("THRESHER");
				gui = new GuiThresher(player.inventory, (TileEntityThresher) te);
			else if (ID == Reference.GUIID_GristMill && te instanceof TileEntityGristMill)
				gui = new GuiGristMill(player.inventory, (TileEntityGristMill) te);
			if(gui!=null)
				((IEBlockInterfaces.IGuiTile)te).onGuiOpened(player, true);
			return gui;
		}
		return null;
	}
	@Override
	public void postInit() {
		ArrayList<PowerLoomRecipe> recipeList = PowerLoomRecipe.recipeList;
		List<String[]> l = new ArrayList<String[]>();
		for (PowerLoomRecipe recipe : recipeList)
		{
			recipe.input.getExampleStack();
			String inputName = recipe.input.getExampleStack().getDisplayName();
			String secondaryName = recipe.secondaryInput.getExampleStack().getDisplayName();
			String outputName = recipe.output.getDisplayName();
			String[] test = new String[]{
					recipe.input.inputSize + " "+inputName,
					secondaryName
			};
			l.add(test);
			test = new String[]{recipe.output.getCount() + " "+outputName,""};
			l.add(test);
		}

		String[][] table = l.toArray(new String[0][]);
		ManualHelper.addEntry("powerLoom", CAT_ATT,
				new ManualPageMultiblock(ManualHelper.getManual(), "powerLoom0",MultiblockPowerLoom.instance),
				new ManualPages.Text(ManualHelper.getManual(), "powerLoom1"),
				new ManualPages.Table(ManualHelper.getManual(), "powerLoom2", table, true),
				new ManualPages.Crafting(ManualHelper.getManual(),"pirn0",new ItemStack(ModItems.PIRN, 1)),
				new ManualPages.Crafting(ManualHelper.getManual(),"pirn1",new ItemStack(ModItems.FIBER_WINDED_PIRN, 1)),
				new ManualPages.Crafting(ManualHelper.getManual(),"pirn2",new ItemStack(ModItems.SILK_WINDED_PIRN, 1)),
				new ManualPages.Crafting(ManualHelper.getManual(),"pirn3",new ItemStack(ModItems.WOOL_WINDED_PIRN, 1))
		);
		l = new ArrayList<>();
		for (ThresherRecipe recipe : ThresherRecipe.recipeList) {
			l.add(new String[]{recipe.input.inputSize+" "+recipe.input.getExampleStack().getDisplayName(),""});
			l.add(new String[]{recipe.output.getCount()+" "+recipe.output.getDisplayName(),recipe.secondaryOutput.getDisplayName()});
		}
		ManualHelper.addEntry("thresher", CAT_ATT,
				new ManualPageMultiblock(ManualHelper.getManual(), "thresher0", MultiblockThresher.instance),
				new ManualPages.Table(ManualHelper.getManual(), "thresher1", l.toArray(new String[0][]), true)
		);
		l = new ArrayList<>();
		for (GristMillRecipe recipe : GristMillRecipe.recipeList) {
			l.add(new String[]{recipe.input.inputSize+" "+recipe.input.getExampleStack().getDisplayName()});
			l.add(new String[]{recipe.output.getCount()+" "+recipe.output.getDisplayName()});
		}
		ManualHelper.addEntry("gristMill", CAT_ATT,
				new ManualPageMultiblock(ManualHelper.getManual(), "gristMill0", MultiblockGristMill.instance),
				new ManualPages.Table(ManualHelper.getManual(), "gristMill1", l.toArray(new String[0][]), true)
		);

	}
	@SideOnly(Side.CLIENT)
	public void renderTile(TileEntity te)
	{
		//System.out.println("render tile");
		if (te instanceof TileEntityPowerLoom.TileEntityPowerLoomParent)
		{
			//System.out.println("render parent");
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-90, 0, 1, 0);
			//GlStateManager.translate(1, 1, -2);
			GlStateManager.translate(1, 1, -1);

			float pt = 0;
			if (Minecraft.getMinecraft().player != null)
			{
				((TileEntityPowerLoom.TileEntityPowerLoomParent) te).activeTicks = Minecraft.getMinecraft().player.ticksExisted;
				pt = Minecraft.getMinecraft().getRenderPartialTicks();
			} else {
				//System.out.println("no player");
			}


			TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer((TileEntity) te);

			tesr.render((TileEntity) te, 0, 0, 0, pt, 0, 0);
			GlStateManager.popMatrix();
		} else if (te instanceof TileEntityThresher.TileEntityThresherParent) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-90, 0, 1, 0);
			//GlStateManager.translate(1, 1, -2);
			GlStateManager.translate(1, 1, -1);

			float pt = 0;
			if (Minecraft.getMinecraft().player != null)
			{
				((TileEntityThresher.TileEntityThresherParent) te).activeTicks = Minecraft.getMinecraft().player.ticksExisted;
				pt = Minecraft.getMinecraft().getRenderPartialTicks();
			}


			TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer((TileEntity) te);

			tesr.render((TileEntity) te, 0, 0, 0, pt, 0, 0);
			GlStateManager.popMatrix();
		} else if (te instanceof TileEntityGristMill.TileEntityGristMillParent) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-90, 0, 1, 0);
			//GlStateManager.translate(1, 1, -2);
			GlStateManager.translate(1, 1, -2);

			float pt = 0;
			if (Minecraft.getMinecraft().player != null)
			{
				((TileEntityGristMill.TileEntityGristMillParent) te).activeTicks = Minecraft.getMinecraft().player.ticksExisted;
				pt = Minecraft.getMinecraft().getRenderPartialTicks();
			}


			TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer((TileEntity) te);

			tesr.render((TileEntity) te, 0, 0, 0, pt, 0, 0);
			GlStateManager.popMatrix();
		}

	}
}
