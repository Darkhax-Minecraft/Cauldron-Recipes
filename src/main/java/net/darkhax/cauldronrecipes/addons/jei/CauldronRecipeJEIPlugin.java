package net.darkhax.cauldronrecipes.addons.jei;

import java.util.Collection;
import java.util.stream.Collectors;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class CauldronRecipeJEIPlugin implements IModPlugin {
    
    @Override
    public ResourceLocation getPluginUid () {
        
        return new ResourceLocation(CauldronRecipes.MOD_ID, "jei");
    }
    
    @Override
    public void registerRecipeCatalysts (IRecipeCatalystRegistration registration) {
        
        registration.addRecipeCatalyst(new ItemStack(Items.CAULDRON), CategoryCauldron.ID);
    }
    
    @Override
    public void registerRecipes (IRecipeRegistration registration) {
        
        final Collection<RecipeCauldron> recipes = CauldronRecipes.getRecipes().values();
        registration.addRecipes(recipes.stream().filter(r -> !r.isHidden()).map(CauldronWrapper::new).collect(Collectors.toList()), CategoryCauldron.ID);
    }
    
    @Override
    public void registerCategories (IRecipeCategoryRegistration registration) {
        
        registration.addRecipeCategories(new CategoryCauldron(registration.getJeiHelpers().getGuiHelper()));
    }
}
