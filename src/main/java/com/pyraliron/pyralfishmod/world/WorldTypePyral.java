package com.pyraliron.pyralfishmod.world;

import com.pyraliron.pyralfishmod.world.biome.BiomeProviderPyral;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypePyral extends WorldType {
	
	public WorldTypePyral() {
		super ("PYRAL");
	}
	
	@Override
	public BiomeProvider getBiomeProvider(World world) {
		return new BiomeProviderPyral(world);
	}
	
	@Override
    public IChunkGenerator getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkGeneratorOverworldPyral(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
    }

}
