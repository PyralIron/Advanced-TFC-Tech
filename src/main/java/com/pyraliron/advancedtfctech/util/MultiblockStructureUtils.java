package com.pyraliron.advancedtfctech.util;

import blusunrize.immersiveengineering.common.IEContent;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsAll;
import blusunrize.immersiveengineering.common.blocks.BlockTypes_MetalsIE;
import blusunrize.immersiveengineering.common.blocks.metal.BlockTypes_MetalDecoration1;
import blusunrize.immersiveengineering.common.blocks.wooden.BlockTypes_WoodenDecoration;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public class MultiblockStructureUtils {
    public static boolean oredictCheck(ItemStack blockStack, BlockPos pos, World world) {
        for (BlockTypes_MetalsIE metalType : BlockTypes_MetalsIE.values()) {
            String oredictName = metalType.getName().substring(0,1).toUpperCase(Locale.ENGLISH)+metalType.getName().substring(1);
            //System.out.println(oredictName);
            if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockStorage && blockStack.getMetadata() == metalType.getMeta()) {
                //System.out.println("block state "+world.getBlockState(pos));
                if(!Utils.isOreBlockAt(world, pos, "block"+oredictName))
                    return false;
            }
            if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockStorageSlabs && blockStack.getMetadata() == metalType.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "slab"+oredictName))
                    return false;
            }
        }
        for (BlockTypes_MetalsAll metalType : BlockTypes_MetalsAll.values()) {
            String oredictName = metalType.getName().substring(0,1).toUpperCase(Locale.ENGLISH)+metalType.getName().substring(1);
            //System.out.println(oredictName);
            if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockSheetmetal && blockStack.getMetadata() == metalType.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "blockSheetmetal"+oredictName))
                    return false;
            }
            if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockSheetmetalSlabs && blockStack.getMetadata() == metalType.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "slabSheetmetal"+oredictName))
                    return false;
            }
        }
        //System.out.println("wot");
        if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockMetalDecoration1) {
            //System.out.println("metal dec 1");
            if (blockStack.getMetadata() == BlockTypes_MetalDecoration1.STEEL_SCAFFOLDING_0.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "scaffoldingSteel")) {
                    //System.out.println("bad form");
                    return false;
                }
            } else if (blockStack.getMetadata() == BlockTypes_MetalDecoration1.ALUMINUM_SCAFFOLDING_0.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "scaffoldingAluminum"))
                    return false;
            }
        }
        else if (Block.getBlockFromItem(blockStack.getItem()) == IEContent.blockWoodenDecoration) {
            if (blockStack.getMetadata() == BlockTypes_WoodenDecoration.SCAFFOLDING.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "scaffoldingTreatedWood"))
                    return false;
            } else if (blockStack.getMetadata() == BlockTypes_WoodenDecoration.FENCE.getMeta()) {
                if(!Utils.isOreBlockAt(world, pos, "fenceTreatedWood"))
                    return false;
            }
        }
        //System.out.println("true");
        return true;
    }
}
