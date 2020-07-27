package com.pyraliron.pyralfishmod.block;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;
import com.pyraliron.pyralfishmod.init.ModBlocks;
import com.pyraliron.pyralfishmod.tileentity.TileEntityAdvancedNote;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityNote;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockAdvancedNote extends BlockContainerPyral {

	private static final List<SoundEvent> INSTRUMENTS = Lists.newArrayList(SoundEvents.BLOCK_NOTE_HARP, SoundEvents.BLOCK_NOTE_BASEDRUM, SoundEvents.BLOCK_NOTE_SNARE, SoundEvents.BLOCK_NOTE_HAT, SoundEvents.BLOCK_NOTE_BASS, SoundEvents.BLOCK_NOTE_FLUTE, SoundEvents.BLOCK_NOTE_BELL, SoundEvents.BLOCK_NOTE_GUITAR, SoundEvents.BLOCK_NOTE_CHIME, SoundEvents.BLOCK_NOTE_XYLOPHONE);
	public static final PropertyInteger NOTE = PropertyInteger.create("note", 0, 12);
	public int octave;
	public BlockAdvancedNote(int octave, String name) {
		super(Material.WOOD, name, CreativeTabs.REDSTONE);
		this.octave = octave;
		this.setDefaultState(this.blockState.getBaseState().withProperty(NOTE, Integer.valueOf(0)));
		setHardness(0.6F);
	}
	public int getOctave() {
		return this.octave;
		//return this.getUnlocalizedName() == "advanced_note_block_lowest" ? 0 : this.getUnlocalizedName() == "advanced_note_block_lower" ? 1 : this.getUnlocalizedName() == "advanced_note_block_lowest" ? 2 : this.getUnlocalizedName() == "advanced_note_block_lower" ? 3 : this.getUnlocalizedName() == "advanced_note_block_lower" ? 4 : 5;
	}
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityAdvancedNote)
        {
            TileEntityAdvancedNote tileentitynote = (TileEntityAdvancedNote)tileentity;

            if (tileentitynote.previousRedstoneState != flag)
            {
                if (flag)
                {
                    tileentitynote.triggerNote(worldIn, pos);
                }

                tileentitynote.previousRedstoneState = flag;
            }
        }
    }

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		TileEntity tileentity = worldIn.getTileEntity(pos);
		
        if (tileentity instanceof TileEntityAdvancedNote)
        {
        	
        	//((TileEntityAdvancedNote) tileentity).forceNote((byte)((this.octave+1)*12));
        }
    }
	public boolean outOfNoteRange(int note, IBlockState state) {
		Block b = state.getBlock();
		if (b instanceof BlockAdvancedNote) {
			//System.out.println(b.getUnlocalizedName());
			BlockAdvancedNote b1 = (BlockAdvancedNote)b;
			//System.out.println(note+" "+b1.getOctave());
			if (note < b1.getOctave()*12-1 || note > b1.getOctave()*12+13) {
				return true;
			}
		}
		return false;
	}
	public int getFirstInRange(IBlockState state) {
		Block b = state.getBlock();
		//System.out.println(b.getUnlocalizedName());
		if (b instanceof BlockAdvancedNote) {
			BlockAdvancedNote b1 = (BlockAdvancedNote)b;
			return b1.getOctave()*12;
		}
		return 0;
	}
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
        	
        	int amount;
        	ItemStack itemstack = playerIn.getHeldItem(hand);
        	if (itemstack.getItem() == Items.STICK) {
               amount = 12;
        	} else if (itemstack.getItem() == Items.ARROW) {
        		amount = -12;
            } else if (itemstack.getItem() == Items.BONE) {
        		amount = -1;
        	} else {
        		amount = 1;
        	}
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityAdvancedNote)
            {
            	
                TileEntityAdvancedNote tileentitynote = (TileEntityAdvancedNote)tileentity;
                int old = tileentitynote.note;
                if (outOfNoteRange(old,state)) {
                	getFirstInRange(state);
            		tileentitynote.forceNote((byte)getFirstInRange(state));
            	}
                //System.out.println("before "+tileentitynote.note);
                tileentitynote.changePitch(amount);
                if (old == tileentitynote.note) return false;
                //System.out.println("true "+tileentitynote.note);
                tileentitynote.triggerNote(worldIn, pos);
                playerIn.addStat(StatList.NOTEBLOCK_TUNED);
                IBlockState newOctaveBlock;
                int param = ((TileEntityAdvancedNote) tileentity).note;
                
                if (param < 12) {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_LOWEST.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("lowest");
                } else if (param < 24) {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_LOWER.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("lower");
                } else if (param < 36) {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_LOW.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("low");
                } else if (param < 48) {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_MID.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("mid");
                } else if (param < 60) {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_HIGH.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("high");
                } else {
                	newOctaveBlock = ModBlocks.ADVANCED_NOTE_BLOCK_HIGHER.getDefaultState().withProperty(NOTE, Integer.valueOf(param % 12));
                	//System.out.println("higher");
                }
                worldIn.setBlockState(pos, newOctaveBlock);
            }

            return true;
        }
    }
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityAdvancedNote)
            {
                ((TileEntityAdvancedNote)tileentity).triggerNote(worldIn, pos);
                playerIn.addStat(StatList.NOTEBLOCK_PLAYED);
            }
        }
    }
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
		//System.out.println("says meta is" + meta);
        return new TileEntityAdvancedNote();
    }
	private SoundEvent getInstrument(int eventId)
    {
        if (eventId < 0 || eventId >= INSTRUMENTS.size())
        {
            eventId = 0;
        }

        return INSTRUMENTS.get(eventId);
    }
	public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        net.minecraftforge.event.world.NoteBlockEvent.Play e = new net.minecraftforge.event.world.NoteBlockEvent.Play(worldIn, pos, state, param, id);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(e)) return false;
        id = e.getInstrument().ordinal();
        param = e.getVanillaNoteId();
        float f = (float)Math.pow(2.0D, (double)(param - 12) / 12.0D);
        float f1;
        System.out.println(param);
        if (param >= 12 && param < 60) {
        	f1 = 1;
        }
        else {
        	f1 = (float)Math.pow(2.0D, (double)(param % 24 - 12) / 12.0D);
        }
        //System.out.println(Math.floor(f*100)/100);
        //System.out.println(this.getProperSoundFromNote(param).getRegistryName());
        worldIn.playSound((EntityPlayer)null, pos, this.getProperSoundFromNote(param)/*this.getInstrument(id)*/, SoundCategory.RECORDS, 3.0F, f1);
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / /*the note limit*/96.0D, 0.0D, 0.0D);
        
        //System.out.println(param);
        
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityAdvancedNote)
        {	
            TileEntityAdvancedNote tileentitynote = (TileEntityAdvancedNote)tileentity;
            tileentitynote.forceNote((byte)param);
        }
        return true;
    }
	public SoundEvent getProperSoundFromNote(int f) {
		//System.out.println("got sound from note");
		if (f >= 12 && f < 60) {
			return com.pyraliron.pyralfishmod.init.SoundEvents.notes[f-12];
		}
		return f < 24 ? com.pyraliron.pyralfishmod.init.SoundEvents.NOTE_BLOCK_LOWEST_LOWER : f < 48 ? com.pyraliron.pyralfishmod.init.SoundEvents.NOTE_BLOCK_LOW_MID : com.pyraliron.pyralfishmod.init.SoundEvents.NOTE_BLOCK_HIGH_HIGHER;
	}
	public IBlockState getStateFromMeta(int meta)
    {
		//System.out.println(meta);
        return this.getDefaultState().withProperty(NOTE, Integer.valueOf(meta));
    }
	
	public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(NOTE).intValue());
    }
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NOTE});
    }
    /**
     * The type of render function called. MODEL for mixed tesr and static model, MODELBLOCK_ANIMATED for TESR-only,
     * LIQUID for vanilla liquids, INVISIBLE to skip all rendering
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	//TODO change to based on octave
        return Item.getItemFromBlock(ModBlocks.ADVANCED_NOTE_BLOCK_LOWEST.getDefaultState().withProperty(NOTE, 0).getBlock());
    }
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    	return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
    	
    }
    public int damageDropped(IBlockState state)
    {
        return 0;
    }


}
