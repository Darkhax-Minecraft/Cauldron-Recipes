package net.darkhax.cauldronrecipes.addons.crt;

import java.util.List;

import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.logger.ILogger;

import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ActionCreateRecipe extends CauldronRecipeAction {
    
    private final String zenId;
    private final IIngredient zenIngredient;
    private final IItemStack[] zenOutputs;
    
    private final int fluidLevel;
    
    private ResourceLocation recipeId;
    private Ingredient recipeIngredient;
    private ItemStack[] recipeOutputs;
    
    public ActionCreateRecipe(String id, IIngredient ingredient, int fluidLevel, IItemStack... outputs) {
        
        this.zenId = id;
        this.zenIngredient = ingredient;
        this.fluidLevel = fluidLevel;
        this.zenOutputs = outputs;
    }
    
    @Override
    public void apply () {
        
        final RecipeCauldron recipe = new RecipeCauldron(this.recipeId, this.recipeIngredient, this.fluidLevel, this.recipeOutputs);
        CauldronRecipes.getRecipes().put(this.recipeId, recipe);
    }
    
    @Override
    public String getDescription () {
        
        return "Adding recipe for " + this.asString(this.recipeOutputs) + " with input " + this.asString(this.recipeIngredient.getMatchingStacks()) + " and id " + this.zenId;
    }
    
    @Override
    public boolean validate (ILogger logger) {
        
        this.recipeId = ResourceLocation.tryCreate(this.zenId);
        
        if (this.recipeId == null) {
            
            this.logError(logger, "Could not create recipe ID from " + this.zenId + ". It is not a valid namespace ID!");
            return false;
        }
        
        this.recipeIngredient = this.zenIngredient.asVanillaIngredient();
        
        if (this.recipeIngredient.hasNoMatchingItems()) {
            
            this.logWarn(logger, "The recipe " + this.zenId + " has no matching inputs.");
        }
        
        if (this.zenOutputs.length == 0) {
            
            this.logError(logger, "The recipe " + this.zenId + " has no outputs. At least one is needed.");
            return false;
        }
        
        final List<ItemStack> outputs = NonNullList.create();
        
        for (final IItemStack zenStack : this.zenOutputs) {
            
            final ItemStack outputStack = zenStack.getInternal();
            
            if (outputStack.isEmpty()) {
                
                this.logError(logger, "Could not create recipe with invalid output. Output: " + zenStack.getCommandString());
                return false;
            }
            
            else {
                
                outputs.add(outputStack);
            }
        }
        
        this.recipeOutputs = outputs.toArray(new ItemStack[0]);
        
        return true;
    }
}