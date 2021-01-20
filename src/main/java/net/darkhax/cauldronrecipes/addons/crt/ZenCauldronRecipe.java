package net.darkhax.cauldronrecipes.addons.crt;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.events.CTEventManager;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import net.darkhax.cauldronrecipes.CauldronRecipeEvent;
import net.darkhax.cauldronrecipes.RecipeCauldron;

@ZenRegister
@NativeTypeRegistration(value = RecipeCauldron.class, zenCodeName = "mods.cauldronrecipes.CauldronRecipe")
public class ZenCauldronRecipe {
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("fluidLevel")
    public static int getFluidLevel (RecipeCauldron recipe) {
        
        return recipe.getFluidLevel();
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("input")
    public static IIngredient getInput (RecipeCauldron recipe) {
        
        return IIngredient.fromIngredient(recipe.getInput());
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("outputs")
    public static List<IItemStack> getOutputs (RecipeCauldron recipe) {
        
        return Collections.unmodifiableList(CraftTweakerHelper.getIItemStacks(Arrays.stream(recipe.getOutputs()).collect(Collectors.toList())));
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("hidden")
    public static boolean isHidden (RecipeCauldron recipe) {
        
        return recipe.isHidden();
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Setter("hidden")
    public static void setHidden (RecipeCauldron recipe, boolean hidden) {
        
        recipe.setHidden(hidden);
    }
    
    @ZenCodeType.Method
    public static void addCraftingHook (RecipeCauldron recipe, Consumer<CauldronRecipeEvent.AboutToCraft> hook) {
        
        CTEventManager.register(CauldronRecipeEvent.AboutToCraft.class, event -> {
            
            if (event.getRecipe().getId().equals(recipe.getId())) {
                
                hook.accept(event);
            }
        });
    }
    
    @ZenCodeType.Method
    public static void addCraftedHook (RecipeCauldron recipe, Consumer<CauldronRecipeEvent.Crafted> hook) {
        
        CTEventManager.register(CauldronRecipeEvent.Crafted.class, event -> {
            
            if (event.getRecipe().getId().equals(recipe.getId())) {
                
                hook.accept(event);
            }
        });
    }
}