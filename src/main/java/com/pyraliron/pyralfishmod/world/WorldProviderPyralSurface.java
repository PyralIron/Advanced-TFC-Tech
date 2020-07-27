package com.pyraliron.pyralfishmod.world;

import com.pyraliron.pyralfishmod.world.biome.BiomeProviderPyral;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;

public class WorldProviderPyralSurface extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		// TODO Auto-generated method stub
		return DimensionPyral.SURFACE;
	}
	
	@Override
	protected void init() {
		this.hasSkyLight = true;
		this.biomeProvider = new BiomeProviderPyral(this.world);
	}
	
	public boolean canDropChunk(int x, int z)
    {
        return !this.world.isSpawnChunk(x, z) || !this.world.provider.getDimensionType().shouldLoadSpawn();
    }

	public BiomeProvider getBiomeProvider()
    {
        return this.biomeProvider;
    }
}
