package com.pyraliron.pyralfishmod.world.gen.layer;

import com.pyraliron.pyralfishmod.init.ModBiomes;
import com.pyraliron.pyralfishmod.world.gen.noise.OpenSimplexNoise;
import com.pyraliron.pyralfishmod.world.gen.noise.PerlinNoise;
import com.pyraliron.pyralfishmod.world.gen.noise.PerlinNoiseTwo;

import net.minecraft.init.Biomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTest extends GenLayer {

	public static int island_size = 200;
	public static float octaves = 20;
	public static double freq = 45;
	public OpenSimplexNoise opn;
	public PerlinNoiseTwo pln;
	public GenLayerTest(long seed) {
		super(seed);
		opn = new OpenSimplexNoise(seed);
		
	}

	@Override
	public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight) {
		int[] aint = IntCache.getIntCache(areaWidth * areaHeight);
		for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
            	
            	
            	int realX = areaX + j;
            	int realY = areaY + i;
            	float i1 = (Math.abs(realX) % island_size) - island_size/2;
            	float j1 = (Math.abs(realY) % island_size) - island_size/2;
            	
            	double d1 = Math.sqrt(i1*i1+j1*j1);
            	double logistic_d1 = (2/(1+Math.pow(2.718281828,(-0.040*(Math.abs(d1)-island_size/4))))-1)*200;
            	double value = (pln.snoise2((float)(realX / freq), (float)(realY / freq), octaves) * 127.0 + 128.0)-logistic_d1;
            	
            	//temp_seed = random.randint(-1200, 1200);
            	float temp_seed = 0F;
            	float tree_seed = 0F;
            	int temp_distance = 600;
            	int min_temp = -10;
                int max_temp = 90;
       	        double temp_noise = pln.snoise2((float)(realX / freq), (float)(realY / freq), octaves)*(max_temp-min_temp)/40;
                double temp = (1/(1+Math.pow(2.718281828,(-0.040*200/temp_distance*(-Math.abs(realY)+temp_distance/2)))))*(max_temp-min_temp)+min_temp+temp_noise;
            	
                double tree_noise = pln.snoise2((float)(realX / freq), (float)(realY / freq),octaves);
            	
            	int c = 0;
            	if (value > 240) {
            		c = 3;
            	} else if (value > 50) {
            		if (temp < -8) {
            			if (tree_noise > 0.1) {
            				c = Biome.getIdForBiome(Biomes.COLD_TAIGA);
            			} else {
            				c = Biome.getIdForBiome(Biomes.ICE_PLAINS);
            			}
            		} else if (temp < 0) {
            			c = Biome.getIdForBiome(Biomes.TAIGA);
            		} else if (temp > 85) {
            			c = Biome.getIdForBiome(Biomes.DESERT);
            		} else if (temp > 80) {
            			c = Biome.getIdForBiome(Biomes.SAVANNA);
            		} else {
            			if (tree_noise > 0.1) {
            				c = Biome.getIdForBiome(ModBiomes.forest);
            			} else {
            				c = Biome.getIdForBiome(Biomes.PLAINS);
            			}
            		}
                    
            	} else if (value > 40) {
                    c = 16;
            	} else {
                    c = 0;
            	}   		
            	//double distFromOrigin = Math.sqrt(realX*realX+realY*realY)/4;
                
            	this.initChunkSeed((long)(areaX + j), (long)(areaY + i));
                
                aint[j + i * areaWidth] = (int) c;
            }
        }
		return aint;
	}

}
