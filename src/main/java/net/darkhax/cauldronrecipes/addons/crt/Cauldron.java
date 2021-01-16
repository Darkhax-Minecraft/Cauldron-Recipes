package net.darkhax.cauldronrecipes.addons.crt;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;

import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

@ZenRegister
@ZenCodeType.Name("mods.cauldronrecipes.Cauldron")
public class Cauldron implements IRecipeManager {
    
    private static final ResourceLocation REGISTRY_LOCATION = new ResourceLocation("cauldronrecipes", "cauldron_recipe");
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, IItemStack outputs) {
        
        this.addRecipe(id, input, 1, outputs);
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, int fluidLevel, IItemStack outputs) {
        
        this.addRecipe(id, input, fluidLevel, new IItemStack[] { outputs });
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, IItemStack[] outputs) {
        
        this.addRecipe(id, input, 1, outputs);
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, int fluidLevel, IItemStack[] outputs) {
        
        id = this.validateRecipeName(id);
        final ResourceLocation recipeId = new ResourceLocation(CraftTweaker.MODID, id);
        final RecipeCauldron recipe = new RecipeCauldron(recipeId, input.asVanillaIngredient(), fluidLevel, CraftTweakerHelper.getItemStacks(outputs));
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
    }
    
    @Override
    public ResourceLocation getBracketResourceLocation () {
        
        return REGISTRY_LOCATION;
    }
    
    @Override
    public IRecipeType<?> getRecipeType () {
        
        return CauldronRecipes.recipeType;
    }
}