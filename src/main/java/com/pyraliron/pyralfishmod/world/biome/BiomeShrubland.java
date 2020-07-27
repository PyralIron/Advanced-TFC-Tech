package com.pyraliron.pyralfishmod.world.biome;

import java.util.Random;

import com.pyraliron.pyralfishmod.init.ModBiomes;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeShrubland extends BiomePyral {
	public BiomeShrubland() {
        super(new BiomeProperties("Shrubland")
                .setBaseHeight(0.115f)
                .setHeightVariation(0.3f)
                .setTemperature(0.77f)
                .setRainfall(0.53f)
        );

        this.decorator.treesPerChunk = 15;
        this.decorator.grassPerChunk = 7;
        this.decorator.flowersPerChunk = 3;

        //this.spawnableCreatureList.add(new Biome.SpawnListEntry(EntityHorse.class, 2, 2, 4));
    }

    
    public WorldGenAbstractTree getRandomTreeFeature(Random rand)
    {
        return rand.nextInt(100) != 0 ? ModBiomes.Features.OAK_SHRUB : Biome.TREE_FEATURE;
    }
}
