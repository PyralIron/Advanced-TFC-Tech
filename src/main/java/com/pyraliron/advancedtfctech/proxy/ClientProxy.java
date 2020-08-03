package com.pyraliron.advancedtfctech.proxy;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import com.pyraliron.advancedtfctech.client.gui.GuiPowerLoom;
import com.pyraliron.advancedtfctech.client.render.TileRenderPowerLoom;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.util.Reference;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(value = {Side.CLIENT},modid = Reference.MOD_ID)
public class ClientProxy extends CommonProxy {
	
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
}
