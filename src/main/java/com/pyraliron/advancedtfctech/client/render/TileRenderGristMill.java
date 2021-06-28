package com.pyraliron.advancedtfctech.client.render;

import blusunrize.immersiveengineering.client.ClientUtils;
import com.pyraliron.advancedtfctech.client.model.ModelGristMill;
import com.pyraliron.advancedtfctech.te.TileEntityGristMill;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileRenderGristMill extends TileEntitySpecialRenderer<TileEntityGristMill> {
    public TileRenderGristMill() {
    }
    private static ModelGristMill model = new ModelGristMill(false);
    private static ModelGristMill modelM = new ModelGristMill(true);

    private static String texture = "att:textures/models/gristmill.png";
    @Override
    public void render(TileEntityGristMill te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
        if (te != null && te instanceof TileEntityGristMill.TileEntityGristMillParent) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y - 1, z);

            EnumFacing rotation = te.facing;
            if (rotation == EnumFacing.NORTH) {
                GlStateManager.rotate(90F, 0, 1, 0);
                GlStateManager.translate(-1, 0, 0);
            } else if (rotation == EnumFacing.WEST) {
                GlStateManager.rotate(180F, 0, 1, 0);
                GlStateManager.translate(-1, 0, -1);
            } else if (rotation == EnumFacing.SOUTH) {
                GlStateManager.rotate(270F, 0, 1, 0);
                GlStateManager.translate(0, 0, -1);
            }
            GlStateManager.translate(-1, 0, -1);

            if (te.mirrored) {
            }
            ClientUtils.bindTexture(texture);

            float ticks = te.activeTicks + (te.wasActive ? partialTicks : 0);
            int processType = 0;
            float amountInput = 0;
            float amountOutput = 0;
            float amountPirns = 0;
            float primerCount = 0;
            amountInput = (float) (te.inventory.get(0).getCount());//+te.inventory.get(14).getCount()+te.inventory.get(15).getCount());
            amountOutput = (float) (te.inventory.get(6).getCount());//+te.inventory.get(9).getCount()+te.inventory.get(10).getCount());
            primerCount = 0;//(float)(te.inventory.get(16).getCount());

            //System.out.println("should be ticking ! ");
            //model.empty_pirn_count = 0;//(float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            //modelM.empty_pirn_count = 0;//(float)(te.inventory.get(11).getCount()+te.inventory.get(12).getCount());
            //model.isTicking = te.isTicking;
            //modelM.isTicking = te.isTicking;
            //model.ticks = modelM.ticks = 0;
            //System.out.println("inventory for tile "+te.inventory);


            //System.out.println("model ticks "+ticks+" "+te.activeTicks+" "+te.wasActive+" "+partialTicks);
            float active = 0;//(((TileEntityGristMill.TileEntityGristMillParent) te).processQueue.size() > 0 && te.energyStorage.getEnergyStored() >= 256 ? 1 : 0);
            if (((TileEntityGristMill.TileEntityGristMillParent) te).processQueue.size() > 0) {
                active = ((TileEntityGristMill.TileEntityGristMillParent) te).processQueue.get(0).processTick / (float)((TileEntityGristMill.TileEntityGristMillParent) te).processQueue.get(0).maxTicks;
            }

            //System.out.println(amountInput+" "+amountOutput+" "+active);
            if (te.mirrored) {
                modelM.render(null, processType, amountInput, amountOutput, amountPirns, active, 0.0625F);
            } else {
                model.render(null, processType, amountInput, amountOutput, amountPirns, active, 0.0625F);
            }
            GlStateManager.popMatrix();
        }
    }
}
