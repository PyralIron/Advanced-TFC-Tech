package com.pyraliron.pyralfishmod.world;

import net.minecraft.world.DimensionType;

public class DimensionPyral {
	public static DimensionType SURFACE = DimensionType.register("Surface", "_surf", 0, WorldProviderPyralSurface.class, true);
}
