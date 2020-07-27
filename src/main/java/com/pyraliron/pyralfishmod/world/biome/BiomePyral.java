package com.pyraliron.pyralfishmod.world.biome;

import java.util.Random;

import com.pyraliron.pyralfishmod.init.ModBiomes;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomePyral extends Biome {

	public BiomePyral(BiomeProperties properties) {
		super(properties);
		
	}
	
	public WorldGenAbstractTree getRandomTreeFeature(Random rand, BlockPos pos)
    {
		return null;
    }

}
