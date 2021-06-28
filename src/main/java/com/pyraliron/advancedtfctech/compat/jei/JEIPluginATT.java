package com.pyraliron.advancedtfctech.compat.jei;

import com.pyraliron.advancedtfctech.AdvancedTFCTech;
import com.pyraliron.advancedtfctech.compat.jei.category.GristMillCategory;
import com.pyraliron.advancedtfctech.compat.jei.category.PowerLoomCategory;
import com.pyraliron.advancedtfctech.compat.jei.category.ThresherCategory;
import com.pyraliron.advancedtfctech.compat.jei.wrapper.GristMillRecipeWrapper;
import com.pyraliron.advancedtfctech.compat.jei.wrapper.PowerLoomRecipeWrapper;
import com.pyraliron.advancedtfctech.compat.jei.wrapper.ThresherRecipeWrapper;
import com.pyraliron.advancedtfctech.crafting.GristMillRecipe;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static com.pyraliron.advancedtfctech.util.Reference.MOD_ID;

@JEIPlugin
public class JEIPluginATT implements IModPlugin {
    public static final String POWER_LOOM_ID = MOD_ID + ".powerloom";
    public static final String THRESHER_ID = MOD_ID + ".thresher";
    public static final String GRIST_MILL_ID = MOD_ID + ".gristmill";

    private static IModRegistry REGISTRY;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        registry.addRecipeCategories(new PowerLoomCategory(registry.getJeiHelpers().getGuiHelper(), POWER_LOOM_ID));
        registry.addRecipeCategories(new ThresherCategory(registry.getJeiHelpers().getGuiHelper(), THRESHER_ID));
        registry.addRecipeCategories(new GristMillCategory(registry.getJeiHelpers().getGuiHelper(), GRIST_MILL_ID));
    }
    @Override
    public void register(IModRegistry registry)
    {
        //System.out.println("registering powerloom recipes");
        REGISTRY = registry;
        List<PowerLoomRecipeWrapper> powerloomList = (PowerLoomRecipe.recipeList).stream().map(PowerLoomRecipeWrapper::new).collect(Collectors.toList());
        registry.addRecipes(powerloomList, POWER_LOOM_ID);
        registry.addRecipeCatalyst(new ItemStack(AdvancedTFCTech.blockMetalMultiblock,1,1), POWER_LOOM_ID);

        List<ThresherRecipeWrapper> thresherList = (ThresherRecipe.recipeList).stream().map(ThresherRecipeWrapper::new).collect(Collectors.toList());
        registry.addRecipes(thresherList, THRESHER_ID);
        registry.addRecipeCatalyst(new ItemStack(AdvancedTFCTech.blockMetalMultiblock,1,3), THRESHER_ID);

        List<GristMillRecipeWrapper> gristmillList = (GristMillRecipe.recipeList).stream().map(GristMillRecipeWrapper::new).collect(Collectors.toList());
        registry.addRecipes(gristmillList, GRIST_MILL_ID);
        registry.addRecipeCatalyst(new ItemStack(AdvancedTFCTech.blockMetalMultiblock,1,5), GRIST_MILL_ID);
        // registry.addIngredientInfo(new ItemStack(ItemsFL.FRUIT_LEAF, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.tooltip.firmalife.fruit_leaf").getFormattedText());

    }
}
