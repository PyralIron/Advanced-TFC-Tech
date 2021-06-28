/*
 * Large potions of this code were taken from Immersive Petroleum created by Flaxbeard
 * https://github.com/Flaxbeard/ImmersivePetroleum/
 * as well as from Immersive Engineering created by BluSunrize
 * https://github.com/BluSunrize/ImmersiveEngineering/
 */
package com.pyraliron.advancedtfctech.multiblocks;

import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import flaxbeard.immersivepetroleum.common.blocks.BlockIPBase;
import net.minecraft.util.IStringSerializable;

import java.util.Locale;

public enum BlockTypes_ATTMetalMultiblock implements IStringSerializable, BlockIPBase.IBlockEnum, BlockATTBase.IBlockEnum {
    POWERLOOM(false),
    POWERLOOM_PARENT(false),
    THRESHER(false),
    THRESHER_PARENT(false),
    GRISTMILL(false),
    GRISTMILL_PARENT(false),
    DOUGHMIXER(false),
    DOUGHMIXER_PARENT(false);

    private boolean needsCustomState;

    private BlockTypes_ATTMetalMultiblock(boolean needsCustomState) {
        this.needsCustomState = needsCustomState;
    }

    public String getName() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public int getMeta() {
        return this.ordinal();
    }

    public boolean listForCreative() {
        return false;
    }

    public boolean needsCustomState() {
        return this.needsCustomState;
    }

    public String getCustomState() {
        String[] split = this.getName().split("_");
        String s = split[0].toLowerCase(Locale.ENGLISH);

        for(int i = 1; i < split.length; ++i) {
            s = s + split[i].substring(0, 1).toUpperCase(Locale.ENGLISH) + split[i].substring(1).toLowerCase(Locale.ENGLISH);
        }

        return s;
    }
}