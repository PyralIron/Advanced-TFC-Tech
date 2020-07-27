package com.pyraliron.pyralfishmod.tileentity;

import com.pyraliron.pyralfishmod.events.AdvancedNoteBlockEvent;
import com.pyraliron.pyralfishmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.NoteBlockEvent;

public class TileEntityAdvancedNote extends TileEntity {
	/** Note to play */
    public byte note;
    /** stores the latest redstone state */
    public boolean previousRedstoneState;
    
    public static int noteLimit = 72;
    
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("note", this.note);
        compound.setBoolean("powered", this.previousRedstoneState);
        return compound;
    }

    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.note = compound.getByte("note");
        this.note = (byte)MathHelper.clamp(this.note, 0, noteLimit-1);
        this.previousRedstoneState = compound.getBoolean("powered");
    }
    public void changePitch(int amount)
    {
        byte old = note;
        if (this.note+amount < 0) {
        	this.note = (byte)((this.note + amount+noteLimit) % (noteLimit));
        }
        else {
        	this.note = (byte)((this.note + amount) % (noteLimit));
        }
        
        if (!onNoteChange(this, old)) return;
        //System.out.println("marked dirty");
        this.markDirty();
    }
    public void forceNote(byte newNote) {
    	this.note = newNote;
    }
    public static boolean onNoteChange(TileEntityAdvancedNote te, byte old)
    {
        AdvancedNoteBlockEvent.Change e = new AdvancedNoteBlockEvent.Change(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()), old, te.note);
        if (MinecraftForge.EVENT_BUS.post(e))
        {
            te.note = old;
            //System.out.println("post");
            return false;
        }
        te.note = (byte)e.getVanillaNoteId();
        return true;
    }
    public void triggerNote(World worldIn, BlockPos posIn)
    {
        if (worldIn.getBlockState(posIn.up()).getMaterial() == Material.AIR)
        {
            IBlockState iblockstate = worldIn.getBlockState(posIn.down());
            Material material = iblockstate.getMaterial();
            int i = 0;

            if (material == Material.ROCK)
            {
                i = 1;
            }

            if (material == Material.SAND)
            {
                i = 2;
            }

            if (material == Material.GLASS)
            {
                i = 3;
            }

            if (material == Material.WOOD)
            {
                i = 4;
            }

            Block block = iblockstate.getBlock();

            if (block == Blocks.CLAY)
            {
                i = 5;
            }

            if (block == Blocks.GOLD_BLOCK)
            {
                i = 6;
            }

            if (block == Blocks.WOOL)
            {
                i = 7;
            }

            if (block == Blocks.PACKED_ICE)
            {
                i = 8;
            }

            if (block == Blocks.BONE_BLOCK)
            {
                i = 9;
            }
            
            if (this.note < 12) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_LOWEST, i, this.note);
            } else if (this.note < 24) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_LOWER, i, this.note);
            } else if (this.note < 36) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_LOW, i, this.note);
            } else if (this.note < 48) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_MID, i, this.note);
            } else if (this.note < 60) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_HIGH, i, this.note);
            } else if (this.note < 72) {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_HIGHER, i, this.note);
            } else {
            	worldIn.addBlockEvent(posIn, ModBlocks.ADVANCED_NOTE_BLOCK_LOWEST, i, this.note);
            }
            
        }
    }
}
