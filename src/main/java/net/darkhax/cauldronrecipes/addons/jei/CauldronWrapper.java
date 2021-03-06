package net.darkhax.cauldronrecipes.addons.jei;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

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
    
    // int arg0, boolean arg1, T arg2, List<ITextComponent> arg3
    public void getTooltip (int slotIndex, boolean input, ItemStack ingredient, List<ITextComponent> tooltip) {
        
        if (slotIndex == 1) {
            
            tooltip.add(new TranslationTextComponent("tooltip.cauldronrecipes.fluid", this.recipe.getFluidLevel()));
        }
        
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (Minecraft.getInstance().gameSettings.advancedItemTooltips) {
                tooltip.add(new StringTextComponent(this.recipe.getId().toString()).mergeStyle(TextFormatting.GRAY));
            }
        });
    }
    
    public List<ItemStack> getInputs () {
        
        return this.inputs;
    }
    
    public List<ItemStack> getOutputs () {
        
        return this.outputs;
    }
}