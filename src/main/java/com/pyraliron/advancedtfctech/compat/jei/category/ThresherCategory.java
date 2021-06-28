package com.pyraliron.advancedtfctech.compat.jei.category;

import blusunrize.immersiveengineering.common.util.compat.jei.MultiblockRecipeWrapper;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.dries007.tfc.compat.jei.BaseRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import static com.pyraliron.advancedtfctech.util.Reference.MOD_ID;

public class ThresherCategory extends BaseRecipeCategory<MultiblockRecipeWrapper> {
    private static final ResourceLocation ICONS = new ResourceLocation(MOD_ID, "textures/gui/jei/jei.png");
    private final IDrawableStatic slot;
    private final IDrawableStatic gears;
    private final IDrawableAnimated gearsAnimated;

    public ThresherCategory(IGuiHelper helper, String id)
    {
        super(helper.createBlankDrawable(120, 38), id);
        gears = helper.createDrawable(ICONS, 0, 134, 22, 16);
        IDrawableStatic arrowAnimated = helper.createDrawable(ICONS, 22, 134, 22, 16);
        this.gearsAnimated = helper.createAnimatedDrawable(arrowAnimated, 80, IDrawableAnimated.StartDirection.LEFT, false);
        this.slot = helper.getSlotDrawable();
    }
    @Override
    public void drawExtras(Minecraft minecraft)
    {
        gears.draw(minecraft, 50, 12);
        gearsAnimated.draw(minecraft, 50, 12);
        slot.draw(minecraft, 20, 10);
        slot.draw(minecraft, 84, 0);
        slot.draw(minecraft, 84, 20);

    }
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, MultiblockRecipeWrapper recipeWrapper, IIngredients ingredients)
    {
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        itemStackGroup.init(0, true, 20, 10);
        itemStackGroup.init(1, false, 84, 0);
        itemStackGroup.init(2, false, 84, 20);
        itemStackGroup.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        itemStackGroup.set(1, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
        itemStackGroup.set(2, ingredients.getOutputs(VanillaTypes.ITEM).get(1));
    }
    @Override
    public String getModName() {
        return "Advanced TFC Tech";
    }
}
