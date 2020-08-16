package com.pyraliron.advancedtfctech.proxy;

import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.client.gui.ContainerPowerLoom;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import com.pyraliron.advancedtfctech.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CommonProxy implements IGuiHandler {
	public void registerItemRenderer(Item item, int meta, String id) {}
	public void registerRenderers() {
	}

	@Nullable
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		//System.out.println("TILE ENTITY"+te);
		if(te instanceof IEBlockInterfaces.IGuiTile)
		{
			Object gui = null;
			if(ID== Reference.GUIID_PowerLoom && te instanceof TileEntityPowerLoom)
				gui = new ContainerPowerLoom(player.inventory, (TileEntityPowerLoom) te);
			if(gui!=null)
				((IEBlockInterfaces.IGuiTile)te).onGuiOpened(player, false);
			return gui;

		}
		return null;
	}
	public static <T extends TileEntity & IEBlockInterfaces.IGuiTile> void openGuiForTile(@Nonnull EntityPlayer player, @Nonnull T tile)
	{
		//System.out.println("OPENING GUI FOR TILE");
		player.openGui(AdvancedTFCTech.Instance, tile.getGuiID(), tile.getWorld(), tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());
	}

	@Nullable
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}
	public void renderTile(TileEntity te) {

	}
	public void postInit() {

	}
}

