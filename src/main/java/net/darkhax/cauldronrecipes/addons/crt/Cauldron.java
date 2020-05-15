package net.darkhax.cauldronrecipes.addons.crt;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;

import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

@ZenRegister
@ZenCodeType.Name("mods.cauldronrecipes.Cauldron")
public class Cauldron implements IRecipeManager {
    
    private static final ResourceLocation REGISTRY_LOCATION = new ResourceLocation("cauldronrecipes", "cauldron_recipe");
    
    public Cauldron() {
        
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, IItemStack outputs) {
        
        CraftTweakerAPI.apply(new ActionCreateRecipe(id, input, 1, outputs));
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, int fluidLevel, IItemStack outputs) {
        
        CraftTweakerAPI.apply(new ActionCreateRecipe(id, input, fluidLevel, outputs));
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, IItemStack[] outputs) {
        
        CraftTweakerAPI.apply(new ActionCreateRecipe(id, input, 1, outputs));
    }
    
    @ZenCodeType.Method
    public void addRecipe (String id, IIngredient input, int fluidLevel, IItemStack[] outputs) {
        
        CraftTweakerAPI.apply(new ActionCreateRecipe(id, input, fluidLevel, outputs));
    }
    
    @Override
    public ResourceLocation getBracketResourceLocation () {
        
        return REGISTRY_LOCATION;
    }
    
    @Override
    public IRecipeType getRecipeType () {
        
        return CauldronRecipes.recipeType;
    }
}