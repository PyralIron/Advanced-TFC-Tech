package com.pyraliron.pyralfishmod.init;

import com.pyraliron.pyralfishmod.Main;
import com.pyraliron.pyralfishmod.util.Reference;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RegisterBiomes {
	public static void registerOverworldBiomes(Biome biome, BiomeType type, boolean isSpawnBiome, int weight) {
        
        BiomeManager.addBiome(type, new BiomeEntry(biome, weight));
        if(isSpawnBiome){
            BiomeManager.addSpawnBiome(biome);
        }
        BiomeManager.addStrongholdBiome(biome);
    }

 

 

	@SubscribeEvent
    public void registerBiome(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
            //================================
            ModBiomes.shrubland.setRegistryName(Reference.MOD_ID, "Shrubland"), ModBiomes.forest.setRegistryName(Reference.MOD_ID, "Forest")
            
            //================================
        );

  }
}
