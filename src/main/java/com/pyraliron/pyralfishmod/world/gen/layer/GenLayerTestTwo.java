package com.pyraliron.pyralfishmod.world.gen.layer;

import com.pyraliron.pyralfishmod.world.gen.noise.OpenSimplexNoise;
import com.pyraliron.pyralfishmod.world.gen.noise.PerlinNoiseTwo;

import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayerTestTwo extends GenLayer {
	public static int island_size = 200;
	public static float octaves = 20;
	public static double freq = 45;
	public OpenSimplexNoise opn;
	public PerlinNoiseTwo pln;
	public GenLayerTestTwo(long seed) {
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
            	
            	
            	int realX = areaX*4 + j;
            	int realY = areaY*4 + i;
            	float i1 = (realX % island_size) - island_size/2;
            	float j1 = (realY % island_size) - island_size/2;
            	
            	double d1 = Math.sqrt(i1*i1+j1*j1);
            	double logistic_d1 = (2/(1+Math.pow(2.718281828,(-0.040*(d1-island_size/4))))-1)*200;
            	double value = (pln.snoise2((float)(realX / freq), (float)(realY / freq), octaves) * 127.0 + 128.0)-logistic_d1;
            	
            	int c = 0;
            	
            	if (value > 50) {
                    c = 1;
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
