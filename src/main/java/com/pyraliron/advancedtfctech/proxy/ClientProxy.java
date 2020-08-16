package com.pyraliron.advancedtfctech.proxy;

import blusunrize.immersiveengineering.api.ManualHelper;
import blusunrize.immersiveengineering.api.ManualPageMultiblock;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.lib.manual.ManualPages;
import com.pyraliron.advancedtfctech.client.gui.GuiPowerLoom;
import com.pyraliron.advancedtfctech.client.render.TileRenderPowerLoom;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.init.ModItems;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockPowerLoom;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = {Side.CLIENT},modid = Reference.MOD_ID)
public class ClientProxy extends CommonProxy {
	public static final String CAT_ATT = "att";
	@Override
	public void registerItemRenderer(Item item, int meta, String id) {
		//System.out.println(item.getUnlocalizedName());
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}
	public void registerRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerLoom.class, new TileRenderPowerLoom());
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMultiblockPartATT.class, new TileRenderPowerLoom());
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

	}
	public void renderTile(TileEntity te)
	{
		if (te instanceof TileEntityPowerLoom.TileEntityPowerLoomParent)
		{
			GlStateManager.pushMatrix();
			GlStateManager.rotate(-90, 0, 1, 0);
			//GlStateManager.translate(1, 1, -2);
			GlStateManager.translate(1, 1, -1);

			float pt = 0;
			if (Minecraft.getMinecraft().player != null)
			{
				((TileEntityPowerLoom.TileEntityPowerLoomParent) te).activeTicks = Minecraft.getMinecraft().player.ticksExisted;
				pt = Minecraft.getMinecraft().getRenderPartialTicks();
			}


			TileEntitySpecialRenderer<TileEntity> tesr = TileEntityRendererDispatcher.instance.getRenderer((TileEntity) te);

			tesr.render((TileEntity) te, 0, 0, 0, pt, 0, 0);
			GlStateManager.popMatrix();
		}

	}
}
