package net.darkhax.cauldronrecipes.addons.crt;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import org.openzen.zencode.java.ZenCodeType;

import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.impl.helper.CraftTweakerHelper;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import net.darkhax.cauldronrecipes.CauldronRecipeEvent;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@ZenRegister
@NativeTypeRegistration(value = CauldronRecipeEvent.class, zenCodeName = "mods.cauldronrecipes.events.CauldronRecipeEvent")
public class ZenCauldronRecipeEvent {
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("recipe")
    public static RecipeCauldron getRecipe (CauldronRecipeEvent event) {
        
        return event.getRecipe();
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("world")
    public static World getWorld (CauldronRecipeEvent event) {
        
        return event.getWorld();
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("cauldron")
    public static BlockState getCauldron (CauldronRecipeEvent event) {
        
        return event.getCauldron();
    }
    
    @Nonnull
    @ZenCodeType.Method
    @ZenCodeType.Getter("pos")
    public static BlockPos getPos (CauldronRecipeEvent event) {
        
        return event.getPos();
    }
    
    @ZenRegister
    @NativeTypeRegistration(value = CauldronRecipeEvent.AboutToCraft.class, zenCodeName = "mods.cauldronrecipes.events.AboutToCraft")
    public static class ZenAboutToCraftEvent {
        
        @Nonnull
        @ZenCodeType.Method
        @ZenCodeType.Getter("input")
        public static ItemStack getInput (CauldronRecipeEvent.AboutToCraft event) {
            
            return event.getInput();
        }
        
        @Nonnull
        @ZenCodeType.Method
        @ZenCodeType.Getter("initialOutputs")
        public static List<ItemStack> getInitialOutputs (CauldronRecipeEvent.AboutToCraft event) {
            
            return event.getInitialOutputs();
        }
        
        @Nonnull
        @ZenCodeType.Method
        @ZenCodeType.Getter("outputs")
        public static List<ItemStack> getOutputs (CauldronRecipeEvent.AboutToCraft event) {
            
            return event.getOutputs();
        }
        
        @Nonnull
        @ZenCodeType.Method
        public static void cancel (CauldronRecipeEvent.AboutToCraft event) {
            
            event.setCanceled(true);
        }
    }
    
    @ZenRegister
    @NativeTypeRegistration(value = CauldronRecipeEvent.Crafted.class, zenCodeName = "mods.cauldronrecipes.events.Crafted")
    public static class ZenCraftedEvent {
        
        @Nonnull
        @ZenCodeType.Method
        @ZenCodeType.Getter("outputs")
        public static List<IItemStack> getOutputs (CauldronRecipeEvent.Crafted event) {
            
            return Collections.unmodifiableList(CraftTweakerHelper.getIItemStacks(event.getOutputs()));
        }
    }
}