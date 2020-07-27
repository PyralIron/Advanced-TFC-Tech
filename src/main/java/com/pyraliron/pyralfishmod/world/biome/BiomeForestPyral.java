package com.pyraliron.pyralfishmod.world.biome;

import java.util.Random;

import com.pyraliron.pyralfishmod.init.ModBiomes;
import com.pyraliron.pyralfishmod.init.ModBlocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeForestPyral extends BiomePyral {

	public BiomeForestPyral() {
		super(new BiomeProperties("Forest")
                .setBaseHeight(0.115f)
                .setHeightVariation(0.3f)
                .setTemperature(0.77f)
                .setRainfall(0.53f)
        );
		
		this.decorator = this.createBiomeDecorator();

        this.decorator.treesPerChunk = 15;
        this.decorator.grassPerChunk = 7;
        this.decorator.flowersPerChunk = 3;
        this.topBlock = ModBlocks.GRASS_BLOCK_PYRAL.getDefaultState();
        this.fillerBlock = ModBlocks.DIRT_BLOCK_PYRAL.getDefaultState();
	}
	@Override
	public BiomeDecorator createBiomeDecorator()
    {
        return getModdedBiomeDecorator(new BiomeDecoratorPyral());
    }
	
	public WorldGenAbstractTree getRandomTreeFeature(Random rand, BlockPos pos)
    {
        return pos.getX() > 500 ? ModBiomes.Features.PYRAL_TREE : ModBiomes.Features.OAK_SHRUB;
    }

}
