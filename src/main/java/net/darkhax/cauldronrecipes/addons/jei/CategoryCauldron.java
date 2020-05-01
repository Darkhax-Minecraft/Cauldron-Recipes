package net.darkhax.cauldronrecipes.addons.jei;

import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

public class CategoryCauldron implements IRecipeCategory<CauldronWrapper> {
    
    public static final ResourceLocation ID = new ResourceLocation(CauldronRecipes.MOD_ID, "cauldron");
    
    private final IDrawable icon;
    private final IDrawableStatic background;
    private final IDrawableStatic slotDrawable;
    
    public CategoryCauldron(IGuiHelper guiHelper) {
        
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(Items.CAULDRON));
        this.background = guiHelper.createBlankDrawable(130, 20);
        this.slotDrawable = guiHelper.getSlotDrawable();
    }
    
    @Override
    public ResourceLocation getUid () {
        
        return ID;
    }
    
    @Override
    public Class<? extends CauldronWrapper> getRecipeClass () {
        
        return CauldronWrapper.class;
    }
    
    @Override
    public String getTitle () {
        
        return I18n.format("cauldronrecipes.title");
    }
    
    @Override
    public IDrawable getBackground () {
        
        return this.background;
    }
    
    @Override
    public IDrawable getIcon () {
        
        return this.icon;
    }
    
    @Override
    public void setIngredients (CauldronWrapper recipe, IIngredients ingredients) {
        
        recipe.setIngredients(ingredients);
    }
    
    @Override
    public void draw (CauldronWrapper recipe, double mouseX, double mouseY) {
        
        // Input & Cauldron
        this.slotDrawable.draw(0, 0);
        this.slotDrawable.draw(20, 0);
        
        for (int nextSlotId = 2; nextSlotId < 6; nextSlotId++) {
            
            final int relativeSlotId = nextSlotId - 2;
            this.slotDrawable.draw(55 + 19 * (relativeSlotId % 4), 19 * (relativeSlotId / 4));
        }
    }
    
    @Override
    public void setRecipe (IRecipeLayout recipeLayout, CauldronWrapper recipe, IIngredients ingredients) {
        
        final IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        
        // Input
        stacks.init(0, true, 0, 0);
        stacks.set(0, recipe.getInputs());
        
        // Cauldron
        stacks.init(1, true, 20, 0);
        stacks.set(1, new ItemStack(Items.CAULDRON));
        
        int nextSlotId = 2;
        
        for (final ItemStack output : recipe.getOutputs()) {
            
            if (nextSlotId <= 4) {
                
                final int relativeSlotId = nextSlotId - 2;
                stacks.init(nextSlotId, false, 55 + 19 * (relativeSlotId % 4), 19 * (relativeSlotId / 4));
                stacks.set(nextSlotId, output);
                nextSlotId++;
            }
        }
        
        stacks.addTooltipCallback(recipe::getTooltip);
    }
}