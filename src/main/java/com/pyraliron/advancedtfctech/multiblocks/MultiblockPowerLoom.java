/*
 * Large potions of this code were taken from Immersive Petroleum created by Flaxbeard
 * https://github.com/Flaxbeard/ImmersivePetroleum/
 * as well as from Immersive Engineering created by BluSunrize
 * https://github.com/BluSunrize/ImmersiveEngineering/
 */
package com.pyraliron.advancedtfctech.multiblocks;

import blusunrize.immersiveengineering.api.MultiblockHandler;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration0;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import flaxbeard.immersivepetroleum.common.IPContent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MultiblockPowerLoom implements MultiblockHandler.IMultiblock {
    public static MultiblockPowerLoom instance = new MultiblockPowerLoom();
    static ItemStack[][][] structure = new ItemStack[3][5][3];
    static final IngredientStack[] materials;
    static boolean needFix = true;
    @Override
    public String getUniqueName()
    {
        return "att:powerloom";
    }

    @Override
    public boolean isBlockTrigger(IBlockState iBlockState) {
        return iBlockState.getBlock() == IEContent.blockMetalDecoration0 && iBlockState.getBlock().getMetaFromState(iBlockState) == BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta();

    }

    @Override
    public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
        if (this.needFix) {MultiblockPowerLoom.fixStructure();this.needFix = false;}
        side = side.getOpposite();
        if (side == EnumFacing.UP || side == EnumFacing.DOWN)
            side = EnumFacing.fromAngle(player.rotationYaw);
        boolean mirror = false;
        boolean b = this.structureCheck(world, pos.offset(side.getOpposite()), side, mirror);
        if (!b) {
            //System.out.println("b false");
            mirror = true;
            b = structureCheck(world, pos.offset(side.getOpposite()), side, mirror);
        }
        if (!b)
            return false;
        if (b)
            for (int h = -1; h <= 1; h++) {
                for (int l = 0; l <= 4; l++) {
                    for (int w = -1; w <= 1; w++) {
                        int ww = mirror ? -w : w;
                        BlockPos pos2 = pos.offset(side, l).offset(side.rotateY(), ww).add(0, h, 0);

                        if (l == 0 && w == 0 && h == 0) {
                            world.setBlockState(pos2, AdvancedTFCTech.blockMetalMultiblock.getStateFromMeta(BlockTypes_ATTMetalMultiblock.POWERLOOM_PARENT.getMeta()));
                        } else if (this.structure[h+1][l][w+1] != ItemStack.EMPTY) {
                            world.setBlockState(pos2, AdvancedTFCTech.blockMetalMultiblock.getStateFromMeta(BlockTypes_ATTMetalMultiblock.POWERLOOM.getMeta()));
                        }
                        TileEntity curr = world.getTileEntity(pos2);
                        if (curr instanceof TileEntityPowerLoom) {
                            TileEntityPowerLoom tile = (TileEntityPowerLoom) curr;
                            tile.facing = side;
                            tile.formed = true;
                            //tile.pos = (h + 1) * 18 + (l + 1) * 3 + (w + 1);

                            /* obfuscated field ? why ? this is pos */
                            tile.field_174879_c = ((h + 1) * 15 + (l) * 3 + (w + 1));
                            tile.setPos((h + 1) * 15 + (l) * 3 + (w + 1));
                            tile.offset = new int[]{(side == EnumFacing.WEST ? -l : side == EnumFacing.EAST ? l : side == EnumFacing.NORTH ? ww : -ww), h, (side == EnumFacing.NORTH ? -l : side == EnumFacing.SOUTH ? l : side == EnumFacing.EAST ? ww : -ww)};
                            //System.out.println(tile.offset);
                            tile.mirrored = mirror;
                            tile.markDirty();
                            world.addBlockEvent(pos2, IPContent.blockMetalMultiblock, 255, 0);
                        }
                    }
                }
            }
        return false;
   }
    @Override
    public IBlockState getBlockstateFromStack(int index, ItemStack stack)
    {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock)
        {
            return ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getItemDamage());
        }
        return null;
    }


    public boolean structureCheck(World world, BlockPos pos, EnumFacing side, Boolean mirror) {

        /*System.out.println("check structure");
        System.out.println(world.getBlockState(pos.add(1,0,0)).toString());
        System.out.println(world.getBlockState(pos.add(0,1,0)).toString());
        System.out.println(world.getBlockState(pos.add(0,0,1)).toString());
        System.out.println(world.getBlockState(pos.add(1,1,1)).toString());
        System.out.println(side.toString());
        System.out.println(pos.toString());*/
        boolean formed = true;
        for (int h = -1; h <= 1; h++) {
            for (int l = 0; l <= 4; l++) {
                for (int w = -1; w <= 1; w++) {
                    int ww = mirror ? -w : w;
                    BlockPos pos2 = pos.offset(side, l+1).offset(side.rotateY(), ww).add(0, h, 0);
                    //if (h == 1 && l == 0 w == 0)
                    /*System.out.println(h+" "+l+" "+w+" "+MultiblockPowerLoom.structure[h+1][1-l][w+1].toString());
                    System.out.println("ie block "+MultiblockPowerLoom.structure[1][1][1].getItem().toString());
                    System.out.println(new ItemStack(IEContent.blockMetalDecoration0, 1).getItem());
                    System.out.println(pos2+(new ItemStack(world.getBlockState(pos2).getBlock(),1,world.getBlockState(pos2).getBlock().getMetaFromState(world.getBlockState(pos2)))).getItem().toString()+world.getBlockState(pos2));
                    //formed = formed && ((new ItemStack(world.getBlockState(pos2).getBlock(),1)).toString() == MultiblockPowerLoom.structure[h+1][1-l][w+1].toString());
                    System.out.println("light test block eng"+Utils.isBlockAt(world,pos2,IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta()));
                    //if (h == 0 && l == 0 && w == 0 && !Utils.isBlockAt(world,pos2,IEContent.blockMetalDecoration0, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())) {formed = false;}
                    System.out.println("------------------");
                    System.out.println(Block.getBlockFromItem(MultiblockPowerLoom.structure[h+1][1-l][w+1].getItem()));
                    System.out.println(world.getBlockState(pos2).getBlock());
                    System.out.println(MultiblockPowerLoom.structure[h+1][l][w+1].getMetadata());
                    System.out.println(world.getBlockState(pos2).getBlock().getMetaFromState(world.getBlockState(pos2)));*/
                    if (structure[h+1][l][w+1].isEmpty()) {continue;}
                    if (!(Block.getBlockFromItem(MultiblockPowerLoom.structure[h+1][l][w+1].getItem()) == world.getBlockState(pos2).getBlock()
                            &&MultiblockPowerLoom.structure[h+1][l][w+1].getMetadata() == world.getBlockState(pos2).getBlock().getMetaFromState(world.getBlockState(pos2)))) {formed = false;}

                }
            }
        }
        return formed;

    }

    @Override
    public ItemStack[][][] getStructureManual()
    {
        this.fixStructure();
        return structure;
    }

    @Override
    public IngredientStack[] getTotalMaterials() {
        return materials;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean overwriteBlockRender(ItemStack itemStack, int i) {
        return false;
    }

    @Override
    public float getManualScale()
    {
        return 12;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canRenderFormedStructure()
    {
        return true;
    }

    Object te;

    @Override
    @SideOnly(Side.CLIENT)
    public void renderFormedStructure()
    {
        if (te == null)
        {
            te = new TileEntityPowerLoom.TileEntityPowerLoomParent();
        }

        //ImmersivePetroleum.proxy.renderTile((TileEntity) te);
        AdvancedTFCTech.proxy.renderTile((TileEntity) te);
    }
    public static void fixStructure() {
        if (MultiblockPowerLoom.needFix) {MultiblockPowerLoom.needFix = false;}
        else {return;}
        for (int h = 0; h < 3; h++) {
            for (int l = 0; l < 5; l++) {
                for (int w = 0; w < 3; w++) {
                    if (l > 0 && l < 4 && h == 0 && (w == 0 || w == 2) || w == 2 && h == 1 && (l == 1 || l == 3) || w == 0 && (h == 1 || h == 2) && (l == 1 || l == 3)) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                    } else if (l > 0 && l < 4 && h == 0) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta());
                    } else if ((l == 1 || l == 3) && (h == 2 || h == 1) && w == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsIE.STEEL.getMeta());
                    } else if (l == 2 && h == 2 && w == 1) {
                        //TODO: Figure out how to make this a top slab like the distillation tower does :/ is a full block for now
                        structure[h][l][w] = new ItemStack(IEContent.blockSheetmetal, 1, BlockTypes_MetalsIE.STEEL.getMeta());
                    } else if (l == 0 && h == 0 && w == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta());
                    } else if ((l == 0 && h == 1 && w == 1) || (l == 4 && w == 1 && h < 2)) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
                    } else if (l == 0 && h == 0 && w == 0) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta());
                    } else if (l == 2 && h == 1 && w == 1) {
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta());
                    } else if (h == 2 && w == 0 && l == 2) {
                        //TODO: Figure out how to make this a top slab like the distillation tower does :/
                        structure[h][l][w] = new ItemStack(IEContent.blockMetalDecoration1, 1, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta());
                    }
                    if (structure[h][l][w] == null) {
                        structure[h][l][w] = ItemStack.EMPTY;
                    }
                }
            }
        }
        /*structure[0][0][0] = new ItemStack(Blocks.LOG,1, 0);
        structure[0][0][1] = new ItemStack(Blocks.LOG,1,1);
        structure[0][0][2] = new ItemStack(Blocks.LOG,1,2);
        structure[1][0][0] = new ItemStack(Blocks.DIRT,1);
        structure[1][0][1] = new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
        structure[1][0][2] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][0] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][1] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][2] = new ItemStack(Blocks.DIRT,1);
        structure[1][1][0] = new ItemStack(Blocks.DIRT,1);
        structure[1][1][1] = new ItemStack(Blocks.DIRT,1);
        structure[1][1][2] = new ItemStack(Blocks.DIRT,1);*/
    }
    static {
        /*structure[0][0][0] = new ItemStack(Blocks.LOG,1, 0);
        structure[0][0][1] = new ItemStack(Blocks.LOG,1,1);
        structure[0][0][2] = new ItemStack(Blocks.LOG,1,2);
        structure[1][0][0] = new ItemStack(Blocks.DIRT,1);
        structure[1][0][1] = new ItemStack(Blocks.DIRT,1);
        structure[1][0][2] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][0] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][1] = new ItemStack(Blocks.DIRT,1);
        structure[0][1][2] = new ItemStack(Blocks.DIRT,1);
        structure[1][1][0] = new ItemStack(Blocks.DIRT,1);
        structure[1][1][1] = new ItemStack(IEContent.blockMetalDecoration0, 1,0);//, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta());
        System.out.println("SHIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIT"+new ItemStack(IEContent.blockMetalDecoration0, 1,0));
        structure[1][1][2] = new ItemStack(Blocks.DIRT,1);
        materials = new IngredientStack[]{new IngredientStack("scaffoldingSteel", 11), new IngredientStack("fenceTreatedWood", 6), new IngredientStack(new ItemStack(IEContent.blockMetalDevice1, 4, BlockTypes_MetalDevice1.FLUID_PIPE.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())), new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 2, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())), new IngredientStack("blockSteel", 2), new IngredientStack("blockSheetmetalSteel", 4)};*/
        materials = new IngredientStack[]{
                new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 6, BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta())),
                new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 3, BlockTypes_MetalDecoration0.HEAVY_ENGINEERING.getMeta())),
                new IngredientStack(new ItemStack(Blocks.PISTON, 3, EnumFacing.DOWN.ordinal())),
                new IngredientStack(new ItemStack(IEContent.blockMetalDecoration1, 3, BlockTypes_MetalDecoration1.STEEL_FENCE.getMeta())),
                new IngredientStack(new ItemStack(IEContent.blockStorage, 1, BlockTypes_MetalsIE.STEEL.getMeta())),
                new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 3, BlockTypes_MetalDecoration0.LIGHT_ENGINEERING.getMeta())),
                new IngredientStack(new ItemStack(IEContent.blockMetalDecoration0, 1, BlockTypes_MetalDecoration0.RS_ENGINEERING.getMeta()))
        };


    }

}
