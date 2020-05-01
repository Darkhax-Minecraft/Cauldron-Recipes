package net.darkhax.cauldronrecipes.addons.jei;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class CauldronWrapper implements IRecipeCategoryExtension {
    
    private final RecipeCauldron recipe;
    private final List<ItemStack> inputs;
    private final List<ItemStack> outputs;
    
    public CauldronWrapper(RecipeCauldron recipe) {
        
        this.recipe = recipe;
        this.inputs = Arrays.asList(this.recipe.getInput().getMatchingStacks());
        this.outputs = Arrays.asList(this.recipe.getOutputs());
    }
    
    @Override
    public void setIngredients (IIngredients ingredients) {
        
        ingredients.setInputs(VanillaTypes.ITEM, this.inputs);
        ingredients.setOutputs(VanillaTypes.ITEM, this.outputs);
    }
    
    public void getTooltip (int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        
        if (slotIndex == 1) {
            
            tooltip.add(I18n.format("tooltip.cauldronrecipes.fluid", recipe.getFluidLevel()));
        }
    }
    
    public List<ItemStack> getInputs () {
        
        return this.inputs;
    }
    
    public List<ItemStack> getOutputs () {
        
        return this.outputs;
    }
}