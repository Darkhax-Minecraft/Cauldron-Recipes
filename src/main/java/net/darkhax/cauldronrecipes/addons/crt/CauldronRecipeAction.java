package net.darkhax.cauldronrecipes.addons.crt;

import java.util.StringJoiner;

import com.blamejared.crafttweaker.api.actions.IRuntimeAction;
import com.blamejared.crafttweaker.api.logger.ILogger;

import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.minecraft.item.ItemStack;

public abstract class CauldronRecipeAction implements IRuntimeAction {
    
    @Override
    public String describe () {
        
        final String description = this.getDescription();
        
        CauldronRecipes.LOGGER.info(" [CraftTweaker] {}", description);
        return "[Cauldron Recipes] " + description;
    }
    
    abstract String getDescription ();
    
    public String asString (ItemStack[] stacks) {
        
        final StringJoiner joiner = new StringJoiner(",", "[", "]");
        
        for (final ItemStack stack : stacks) {
            
            joiner.add(stack.toString());
        }
        
        return joiner.toString();
    }
    
    public void logError (ILogger logger, String msg) {
        
        logger.error("[Cauldron Recipes] " + msg);
        CauldronRecipes.LOGGER.error(msg);
    }
    
    public void logWarn (ILogger logger, String msg) {
        
        logger.error("[Cauldron Recipes] " + msg);
        CauldronRecipes.LOGGER.error(msg);
    }
}
