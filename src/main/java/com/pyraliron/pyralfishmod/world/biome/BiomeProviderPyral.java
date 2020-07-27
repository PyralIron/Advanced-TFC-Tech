package com.pyraliron.pyralfishmod.world.biome;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.pyraliron.pyralfishmod.world.WorldTypePyral;
import com.pyraliron.pyralfishmod.world.gen.layer.GenLayerTest;
import com.pyraliron.pyralfishmod.world.gen.layer.GenLayerTestTwo;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;
import net.minecraft.world.gen.layer.IntCache;

public class BiomeProviderPyral extends BiomeProvider {
	
	private GenLayer genBiomes;
	private GenLayer biomeIndexLayer;
	private ChunkGeneratorSettings settings;
	private final BiomeCache biomeCache;
	
	public BiomeProviderPyral(long seed, WorldType worldType, String chunkProviderSettings) {
		
		super();
		this.biomeCache = new BiomeCache(this);
		if (!(worldType instanceof WorldTypePyral))
        {
            throw new RuntimeException("WorldChunkManagerPyral requires a world of type WorldTypePyral");
        }
		if (worldType == WorldType.CUSTOMIZED && !chunkProviderSettings.isEmpty())
        {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(chunkProviderSettings).build();
        }
		/*
		GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldType, this.settings);
        agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
		*/
        GenLayer[] agenlayer = setupPyralGenLayers(seed, settings, worldType);
        agenlayer = getModdedBiomeGenerators(worldType, seed, agenlayer);
		this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
		
	}
	
	public static GenLayer allocateBiomes(long worldSeed, GenLayer mainBranch,WorldType worldType) {
		GenLayer biomesLayer = new GenLayerBiome(200L, mainBranch, worldType, null);
		//biomesLayer = new GenLayerZoom(1000L, biomesLayer);
		//biomesLayer = new GenLayerZoom(1000L, biomesLayer);
		//biomesLayer = new GenLayerBiomeEdge(1000L, biomesLayer);
		return biomesLayer;
	}
	
	public BiomeProviderPyral(World world)
    {
		//super(world.getWorldInfo());
        this(world.getSeed(), world.getWorldInfo().getTerrainType(), world.getWorldInfo().getGeneratorOptions());
    }
	
	

	public static GenLayer initialLandAndSeaLayer()
	{
		GenLayer stack;
		stack = new GenLayerIsland(1L);
	    /*stack = new GenLayerFuzzyZoom(2000L, stack);
	    
	    stack = new GenLayerZoom(2001L, stack);
	    
	    stack = new GenLayerSmooth(30L, stack);
	    stack = new GenLayerRemoveTooMuchOcean(2L, stack); // <--- this is the layer which does 90% of the work, the ones before it are almost pointless
	    
	    stack = new GenLayerZoom(2002L, stack);
	    stack = new GenLayerZoom(2003L, stack);
	    */
	    return stack;
	}
	
	public static int getModdedBiomeSize(WorldType worldType, int original)
    {
        net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize event = new net.minecraftforge.event.terraingen.WorldTypeEvent.BiomeSize(worldType, original);
        net.minecraftforge.common.MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.getNewSize();
    }
	
	public static GenLayer[] setupPyralGenLayers (long worldSeed, ChunkGeneratorSettings settings, WorldType worldType) {
		/*int biomeSize = 40;//settings.biomeSize.getValue();
        int riverSize = 4;
        
     // first few layers just create areas of land and sea, continents and islands
        GenLayer mainBranch = initialLandAndSeaLayer();
        
        // add mushroom islands and deep oceans        
        //mainBranch = new GenLayerAddMushroomIsland(5L, mainBranch);
        //mainBranch = new GenLayerLargeIsland(5L, mainBranch);
       // mainBranch = new GenLayerDeepOcean(4L, mainBranch);
        
        GenLayer riversAndSubBiomesInit = new GenLayerRiverInit(100L, mainBranch);
        
        mainBranch = allocateBiomes(worldSeed, mainBranch, worldType);

        //mainBranch = new GenLayerSmooth(1000L, mainBranch);

        // develop the rivers branch
        GenLayer riversBranch = GenLayerZoom.magnify(1000L, riversAndSubBiomesInit, 2);
        //riversBranch = GenLayerZoom.magnify(1000L, riversBranch, riverSize);
        //riversBranch = new GenLayerRiver(1L, riversBranch);
        //riversBranch = new GenLayerSmooth(1000L, riversBranch);
        
        riversBranch.initWorldGenSeed(worldSeed);
        mainBranch.initWorldGenSeed(worldSeed);
        return new GenLayer[] {riversBranch, mainBranch};*/
		GenLayer genlayer = new GenLayerIsland(1L);
        genlayer = new GenLayerFuzzyZoom(2000L, genlayer);
        GenLayer genlayeraddisland = new GenLayerAddIsland(1L, genlayer);
        GenLayer genlayerzoom = new GenLayerZoom(2001L, genlayeraddisland);
        GenLayer genlayeraddisland1 = new GenLayerAddIsland(2L, genlayerzoom);
        genlayeraddisland1 = new GenLayerAddIsland(50L, genlayeraddisland1);
        genlayeraddisland1 = new GenLayerAddIsland(70L, genlayeraddisland1);
        GenLayer genlayerremovetoomuchocean = new GenLayerRemoveTooMuchOcean(2L, genlayeraddisland1);
        GenLayer genlayeraddsnow = new GenLayerAddSnow(2L, genlayerremovetoomuchocean);
        GenLayer genlayeraddisland2 = new GenLayerAddIsland(3L, genlayeraddsnow);
        GenLayer genlayeredge = new GenLayerEdge(2L, genlayeraddisland2, GenLayerEdge.Mode.COOL_WARM);
        genlayeredge = new GenLayerEdge(2L, genlayeredge, GenLayerEdge.Mode.HEAT_ICE);
        genlayeredge = new GenLayerEdge(3L, genlayeredge, GenLayerEdge.Mode.SPECIAL);
        GenLayer genlayerzoom1 = new GenLayerZoom(2002L, genlayeredge);
        genlayerzoom1 = new GenLayerZoom(2003L, genlayerzoom1);
        GenLayer genlayeraddisland3 = new GenLayerAddIsland(4L, genlayerzoom1);
        GenLayer genlayeraddmushroomisland = new GenLayerAddMushroomIsland(5L, genlayeraddisland3);
        GenLayer genlayerdeepocean = new GenLayerDeepOcean(4L, genlayeraddmushroomisland);
        GenLayer genlayer4 = GenLayerZoom.magnify(1000L, genlayerdeepocean, 0);
        int i = 4;
        int j = i;

        if (settings != null)
        {
            i = settings.biomeSize;
            j = settings.riverSize;
        }

        

        i = getModdedBiomeSize(worldType, i);

        GenLayer lvt_7_1_ = GenLayerZoom.magnify(1000L, genlayer4, 0);
        GenLayer genlayerriverinit = new GenLayerRiverInit(100L, lvt_7_1_);
        GenLayer genlayerbiomeedge = worldType.getBiomeLayer(worldSeed, genlayer4, settings);
        GenLayer lvt_9_1_ = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        GenLayer genlayerhills = new GenLayerHills(1000L, genlayerbiomeedge, lvt_9_1_);
        GenLayer genlayer5 = GenLayerZoom.magnify(1000L, genlayerriverinit, 2);
        //genlayer5 = GenLayerZoom.magnify(1000L, genlayer5, j);
        GenLayer genlayerriver = new GenLayerRiver(1L, genlayer5);
        GenLayer genlayersmooth = new GenLayerSmooth(1000L, genlayerriver);
        genlayerhills = new GenLayerRareBiome(1001L, genlayerhills);

        for (int k = 0; k < i; ++k)
        {
            genlayerhills = new GenLayerZoom((long)(1000 + k), genlayerhills);

            if (k == 0)
            {
                genlayerhills = new GenLayerAddIsland(3L, genlayerhills);
            }

            if (k == 1 || i == 1)
            {
                genlayerhills = new GenLayerShore(1000L, genlayerhills);
            }
        }

        GenLayer genlayersmooth1 = new GenLayerSmooth(1000L, genlayerhills);
        GenLayer genlayerrivermix = new GenLayerRiverMix(100L, genlayersmooth1, genlayersmooth);
        GenLayer genlayer3 = new GenLayerVoronoiZoom(10L, genlayerrivermix);
        genlayerrivermix.initWorldGenSeed(worldSeed);
        genlayer3.initWorldGenSeed(worldSeed);
        GenLayer gentest = new GenLayerTest(2L);
        GenLayer gentest2 = new GenLayerVoronoiZoom(10L, gentest);
        return new GenLayer[] {gentest/*genlayerrivermix*/, gentest2/*genlayer3*/, /*genlayerrivermix*/gentest};
	}
	
	@Override
    
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random)
    {
        IntCache.resetIntCache();
        int xmin = x - (range >> 2);
        int zmin = z - (range >> 2);
        int xmax = x + (range >> 2);
        int zmax = z + (range >> 2);
        int xdiff = xmax - xmin + 1;
        int zdiff = zmax - zmin + 1;
        Biome[] genbiomes = this.getBiomes(null, xmin,zmin,xdiff,zdiff, true);
        BlockPos blockpos = null;
        int k1 = 0;

        for (int index = 0; index < xdiff * zdiff; ++index)
        {
            int i2 = xmin + index % xdiff << 2;
            int j2 = zmin + index / xdiff << 2;
            Biome biome = genbiomes[index];

            if (biomes.contains(biome) && (blockpos == null || random.nextInt(k1 + 1) == 0))
            {
                blockpos = new BlockPos(i2, 0, j2);
                ++k1;
            }
        }

        return blockpos;
    }
	public Biome[] getBiomes(@Nullable Biome[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
	    {
	        IntCache.resetIntCache();

	        if (listToReuse == null || listToReuse.length < width * length)
	        {
	            listToReuse = new Biome[width * length];
	        }

	        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
	        {
	            Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
	            System.arraycopy(abiome, 0, listToReuse, 0, width * length);
	            return listToReuse;
	        }
	        else
	        {
	            int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

	            for (int i = 0; i < width * length; ++i)
	            {
	                listToReuse[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
	            }

	            return listToReuse;
	        }
	    }
	 
	 public Biome[] getBiomesForGeneration(Biome[] biomes, int x, int z, int width, int height)
	    {
	        IntCache.resetIntCache();

	        if (biomes == null || biomes.length < width * height)
	        {
	            biomes = new Biome[width * height];
	        }

	        int[] aint = this.genBiomes.getInts(x, z, width, height);

	        try
	        {
	            for (int i = 0; i < width * height; ++i)
	            {
	                biomes[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
	            }

	            return biomes;
	        }
	        catch (Throwable throwable)
	        {
	            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
	            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
	            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
	            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
	            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
	            crashreportcategory.addCrashSection("w", Integer.valueOf(width));
	            crashreportcategory.addCrashSection("h", Integer.valueOf(height));
	            throw new ReportedException(crashreport);
	        }
	    }
	 public boolean areBiomesViable(int x, int z, int radius, List<Biome> allowed)
	    {
	        IntCache.resetIntCache();
	        int i = x - radius >> 2;
	        int j = z - radius >> 2;
	        int k = x + radius >> 2;
	        int l = z + radius >> 2;
	        int i1 = k - i + 1;
	        int j1 = l - j + 1;
	        int[] aint = this.genBiomes.getInts(i, j, i1, j1);

	        try
	        {
	            for (int k1 = 0; k1 < i1 * j1; ++k1)
	            {
	                Biome biome = Biome.getBiome(aint[k1]);

	                if (!allowed.contains(biome))
	                {
	                    return false;
	                }
	            }

	            return true;
	        }
	        catch (Throwable throwable)
	        {
	            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
	            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
	            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
	            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
	            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
	            crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
	            crashreportcategory.addCrashSection("allowed", allowed);
	            throw new ReportedException(crashreport);
	        }
	    }
	
	
    
}
