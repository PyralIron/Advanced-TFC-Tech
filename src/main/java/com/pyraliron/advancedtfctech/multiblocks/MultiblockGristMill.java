package com.pyraliron.advancedtfctech.multiblocks;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.te.TileEntityGristMill;
import com.pyraliron.advancedtfctech.util.MultiblockStructureUtils;
import flaxbeard.immersivepetroleum.common.IPContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockGristMill implements MultiblockHandler.IMultiblock {
    public static MultiblockGristMill instance = new MultiblockGristMill();
    static ItemStack[][][] structure = new ItemStack[3][3][4];
    static boolean needFix = true;
    @Override
    public ItemStack[][][] getStructureManual()
    {
        this.fixStructure();
        return structure;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean overwriteBlockRender(ItemStack stack, int iterator)
    {
        return false;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderFormedStructure()
    {
        return true;
    }
    //@SideOnly(Side.CLIENT)
    Object te;
    static ItemStack renderStack = ItemStack.EMPTY;
    @Override
    @SideOnly(Side.CLIENT)
    public void renderFormedStructure()
    {
        if (te == null)
        {
            te = new TileEntityGristMill.TileEntityGristMillParent();
        }
        AdvancedTFCTech.proxy.renderTile((TileEntity) te);
        /*if(renderStack.isEmpty())
            renderStack = new ItemStack(IEContent.blockMetalMultiblock,1, BlockTypes_MetalMultiblock.CRUSHER.getMeta());
        GlStateManager.translate(1.5, 1.5, 2.5);
        GlStateManager.rotate(-45, 0, 1, 0);
        GlStateManager.rotate(-20, 1, 0, 0);
        GlStateManager.scale(5.5, 5.5, 5.5);

        GlStateManager.disableCull();
        ClientUtils.mc().getRenderItem().renderItem(renderStack, ItemCameraTransforms.TransformType.GUI);
        GlStateManager.enableCull();*/
    }
    @Override
    public float getManualScale()
    {
        return 12;
    }

    @Override
    public String getUniqueName()
    {
        return "att:gristmill";
    }

    @Override
    public boolean isBlockTrigger(IBlockState state)
    {
        return state.getBlock()== IEContent.blockStorage && (state.getBlock().getMetaFromState(state)== BlockTypes_MetalsIE.STEEL.getMeta());
    }

    @Override
    public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player)
    {
        if (this.needFix) {MultiblockGristMill.fixStructure();this.needFix = false;}
        if(side.getAxis()== EnumFacing.Axis.Y)
            return false;
        BlockPos startPos = pos;
        side = side.getOpposite();

        boolean mirrored = false;
        boolean b = structureCheck(world,startPos, side, mirrored);
        if(!b)
        {
            mirrored = true;
            b = structureCheck(world,startPos, side, mirrored);
        }

        if(b)
        {

            for(int l=-1;l<2;l++)
                for(int w=-1;w<=2;w++)
                    for(int h=-1;h<=1;h++)
                    {
                        if (structure[h+1][l+1][w+1] == null || structure[h+1][l+1][w+1].isEmpty()) {continue;}
                        int ww = mirrored?-w:w;
                        BlockPos pos2 = startPos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

                        if (l == 0 && w == 0 && h == 0) {
                            world.setBlockState(pos2, AdvancedTFCTech.blockMetalMultiblock.getStateFromMeta(BlockTypes_ATTMetalMultiblock.GRISTMILL_PARENT.getMeta()));
                        } else if (this.structure[h+1][l+1][w+1] != ItemStack.EMPTY) {
                            world.setBlockState(pos2, AdvancedTFCTech.blockMetalMultiblock.getStateFromMeta(BlockTypes_ATTMetalMultiblock.GRISTMILL.getMeta()));
                        }
                        TileEntity curr = world.getTileEntity(pos2);
                        //System.out.println("curr "+curr);

                        if(curr instanceof TileEntityGristMill)
                        {
                            TileEntityGristMill tile = (TileEntityGristMill)curr;

                            // dist = target position - current position
                            tile.facing = side;
                            tile.pos = (h+1)*12 + (l+1)*4 + (w+1);

                            //System.out.println("facing "+tile.facing+" side "+side);

                            //System.out.println("hsould be origin "+tile.getPos().offset(tile.facing, distL).offset(tile.facing.rotateY(), w2).add(0, distH, 0));

                            tile.formed=true;

                            //System.out.println("pos "+tile.field_174879_c);
                            //System.out.println("get origin "+tile.getOrigin()+" start pos "+startPos);
                            tile.offset = new int[]{(side==EnumFacing.WEST?-l: side==EnumFacing.EAST?l: side==EnumFacing.NORTH?ww: -ww),h,(side==EnumFacing.NORTH?-l: side==EnumFacing.SOUTH?l: side==EnumFacing.EAST?ww : -ww)};
                            tile.mirrored = mirrored;
                            tile.markDirty();
                            world.addBlockEvent(pos2, IPContent.blockMetalMultiblock, 255, 0);
                        }
                    }
        }
        return b;
    }

    boolean structureCheck(World world, BlockPos startPos, EnumFacing dir, boolean mirror)
    {
        for(int l=-1;l<2;l++)
            for(int w=-1;w<=2;w++)
                for(int h=-1;h<=1;h++)
                {
                    int ww = mirror?-w:w;
                    BlockPos pos = startPos.offset(dir, l).offset(dir.rotateY(), ww).add(0, h, 0);
                    if (structure[h+1][l+1][w+1] == null || structure[h+1][l+1][w+1].isEmpty()) {continue;}
                    if (!MultiblockStructureUtils.oredictCheck(structure[h+1][l+1][w+1],pos,world)) {return false;}
                    else if (!(Block.getBlockFromItem(MultiblockGristMill.structure[h+1][l+1][w+1].getItem()) == world.getBlockState(pos).getBlock()
                            &&MultiblockGristMill.structure[h+1][l+1][w+1].getMetadata() == world.getBlockState(pos).getBlock().getMetaFromState(world.getBlockState(pos)))) {return false;}

                }
        return true;
    }

    static private IngredientStack[] materials;
    @Override
    public IngredientStack[] getTotalMaterials()
    {
        if (materials == null) {
            materials = new IngredientStack[]{
                    new IngredientStack(new ItemStack(Blocks.HOPPER, 1)),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 9, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockSheetmetal, 3, BlockTypes_MetalsIE.STEEL.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockSheetmetalSlabs, 4, BlockTypes_MetalsIE.STEEL.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 4, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
                    new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
            };
        }
        return materials;
    }

    public static void fixStructure() {
        if (MultiblockGristMill.needFix) {MultiblockGristMill.needFix = false;}
        else {return;}
        for(int h=0;h<3;h++)
            for(int l=0;l<3;l++)
                for(int w=0;w<4;w++)
                {
                    if (w == 0 && h == 1 && l == 1 || w == 2 && h == 0 && l == 0 || (w > 1) && h == 1 && l == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0,1,BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                    }
                    else if ((l == 0 || l == 2) && w < 2 && h == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockSheetmetalSlabs,1, BlockTypes_MetalsIE.STEEL.getMeta());
                    }
                    else if (w == 2 && l == 0 && h == 1 || (w == 0 || w == 3) && h == 0 && l == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal,1, BlockTypes_MetalsIE.STEEL.getMeta());
                    }
                    else if (h == 0) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1,1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                    }
                    else if (l == 1 && w == 1 && h == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockStorage,1, BlockTypes_MetalsIE.STEEL.getMeta());
                    }
                    else if (w == 2 && h == 2 && l == 1) {
                        structure[h][l][w] = new ItemStack(Blocks.HOPPER,1);
                    }
                    else if (w == 2 && h == 1 && l == 2) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0,1,BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
                    }
                }
    }
}