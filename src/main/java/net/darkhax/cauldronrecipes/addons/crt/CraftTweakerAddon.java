package net.darkhax.cauldronrecipes.addons.crt;

import com.blamejared.crafttweaker.CraftTweaker;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.impl.commands.CTCommandCollectionEvent;
import com.blamejared.crafttweaker.impl.commands.CTCommands.CommandCallerPlayer;
import com.blamejared.crafttweaker.impl.commands.script_examples.ExampleCollectionEvent;

import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CraftTweakerAddon {
    
    @SubscribeEvent
    public static void addDumpCommands (CTCommandCollectionEvent event) {
        
        event.registerDump("cauldronRecipes", "Outputs all known Cauldron Recipes to the log.", (CommandCallerPlayer) CraftTweakerAddon::dumpRecipes);
    }
    
    
    @SubscribeEvent
    public static void addExampleScriptFiles (ExampleCollectionEvent event) {
        
        event.addResource(new ResourceLocation(CauldronRecipes.MOD_ID, "cauldronrecipes/adding_recipes"));
        event.addResource(new ResourceLocation(CauldronRecipes.MOD_ID, "cauldronrecipes/recipe_crafted_event"));
        event.addResource(new ResourceLocation(CauldronRecipes.MOD_ID, "cauldronrecipes/recipe_crafting_event"));
        event.addResource(new ResourceLocation(CauldronRecipes.MOD_ID, "cauldronrecipes/recipe_hooks"));
    }
    
    private static int dumpRecipes (PlayerEntity player, ItemStack stack) {
        
        final RecipeManager recipeManager = player.getEntityWorld().getRecipeManager();
        CraftTweakerAPI.logDump("List of all known Cauldron Recipes:");
        CauldronRecipes.getRecipes(recipeManager).keySet().forEach(id -> CraftTweakerAPI.logDump("- %s", id));
        player.sendMessage(new TranslationTextComponent("cauldronrecipes.log.crt.dump").mergeStyle(TextFormatting.GREEN), CraftTweaker.CRAFTTWEAKER_UUID);
        return 0;
    }
}