package com.pyraliron.advancedtfctech.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.pyraliron.advancedtfctech.client.model.ModelThresher;
import com.pyraliron.advancedtfctech.te.TileEntityThresher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileRenderThresher extends TileEntitySpecialRenderer<TileEntityThresher> {
    public TileRenderThresher() {
    }
    private static ModelThresher model = new ModelThresher(false);
    private static ModelThresher modelM = new ModelThresher(true);

    private static String texture = "att:textures/models/thresher.png";

    @Override
    public void render(TileEntityThresher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te != null && te instanceof TileEntityThresher.TileEntityThresherParent)
        {
            //if (te.facing == EnumFacing.NORTH) {System.out.println(te.getPos()+" "+te.getRenderBoundingBox());}
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y - 1, z);

            EnumFacing rotation = te.facing;
            if (rotation == EnumFacing.NORTH)
            {
                GlStateManager.rotate(90F, 0, 1, 0);
                GlStateManager.translate(-1, 0, 0);
            }
            else if (rotation == EnumFacing.WEST)
            {
                GlStateManager.rotate(180F, 0, 1, 0);
                GlStateManager.translate(-1, 0, -1);
            }
            else if (rotation == EnumFacing.SOUTH)
            {
                GlStateManager.rotate(270F, 0, 1, 0);
                GlStateManager.translate(0, 0, -1);
            }
            GlStateManager.translate(-1, 0, -1);

            if (te.mirrored)
            {
            }
            ClientUtils.bindTexture(texture);

            float ticks = te.activeTicks + (te.wasActive ? partialTicks : 0);
            int processType = 0;
            float amountInput = 0;
            float amountOutput = 0;
            float amountPirns = 0;
            float primerCount = 0;
            amountInput = (float)(te.inventory.get(0).getCount());//+te.inventory.get(14).getCount()+te.inventory.get(15).getCount());
            amountOutput = (float)(te.inventory.get(6).getCount());//+te.inventory.get(9).getCount()+te.inventory.get(10).getCount());
            primerCount = 0;//(float)(te.inventory.get(16).getCount());

            //System.out.println("should be ticking ! ");
            //model.empty_pirn_count = 0;//(float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            //modelM.empty_pirn_count = 0;//(float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            //model.isTicking = te.isTicking;
            //modelM.isTicking = te.isTicking;
            //model.ticks = modelM.ticks = 0;
            //System.out.println("inventory for tile "+te.inventory);


            //System.out.println("model ticks "+model.ticks+" "+ticks+" "+te.activeTicks+" "+te.wasActive+" "+partialTicks);
            if (te.mirrored)
            {
                modelM.render(null, processType, amountInput, amountOutput, amountPirns, primerCount, 0.0625F);
            }
            else
            {
                model.render(null, processType, amountInput, amountOutput, amountPirns, primerCount, 0.0625F);
            }
//            IBakedModel outputModel = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(te.getInventory().get(0), te.getWorld(), null);
//            outputModel = ForgeHooksClient.handleCameraTransforms(outputModel, ItemCameraTransforms.TransformType.FIXED, false);
//            GlStateManager.translate(0,1,0);
//            GlStateManager.rotate(90, 0, 0, 1);
//            //System.out.println(te.getInventory().get(0));
//            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//            Minecraft.getMinecraft().getRenderItem().renderItem(te.getInventory().get(0), outputModel);
            GlStateManager.popMatrix();

        }
    }
}
