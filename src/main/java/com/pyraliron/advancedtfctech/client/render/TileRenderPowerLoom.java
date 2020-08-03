package com.pyraliron.advancedtfctech.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.pyraliron.advancedtfctech.client.model.ModelPowerLoom;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileRenderPowerLoom extends TileEntitySpecialRenderer<TileEntityPowerLoom> {
    public TileRenderPowerLoom() {
    }
    private static ModelPowerLoom model = new ModelPowerLoom(false);
    private static ModelPowerLoom modelM = new ModelPowerLoom(true);

    private static String texture = "att:textures/models/powerloom.png";

    @Override
    public void render(TileEntityPowerLoom te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te != null && te instanceof TileEntityPowerLoom.TileEntityPowerLoomParent)
        {
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
            amountInput = (float)(te.inventory.get(13).getCount()+te.inventory.get(14).getCount()+te.inventory.get(15).getCount());
            amountOutput = (float)(te.inventory.get(8).getCount()+te.inventory.get(9).getCount()+te.inventory.get(10).getCount());
            primerCount = (float)(te.inventory.get(16).getCount());
            for (int i = 0; i < 8; i++) {amountPirns += (float)(te.inventory.get(i).getCount());}
            ;
            //System.out.println("should be ticking ! ");
            model.empty_pirn_count = (float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            modelM.empty_pirn_count = (float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            model.isTicking = te.isTicking;
            modelM.isTicking = te.isTicking;
            if (((TileEntityPowerLoom.TileEntityPowerLoomParent) te).isTicking) {
                //System.out.println("is ticking");
                //System.out.println("tick "+((TileEntityPowerLoom.TileEntityPowerLoomParent) te).tick +" maxticks "+((TileEntityPowerLoom.TileEntityPowerLoomParent) te).maxTicks);
                model.ticks = modelM.ticks = ((float)((TileEntityPowerLoom.TileEntityPowerLoomParent) te).tick)/(float)((TileEntityPowerLoom.TileEntityPowerLoomParent) te).maxTicks;
                //IMultiblockRecipe recipe = ((TileEntityPowerLoom.TileEntityPowerLoomParent) te).processQueue.get(0).recipe;
                //if (recipe instanceof PowerLoomRecipe) {
                    processType = te.processType;
                //}
            }
            else {
                model.ticks = modelM.ticks = 0F;
            }
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
            GlStateManager.popMatrix();

        }
    }
}
