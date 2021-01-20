// This example will heal the player by up to 20 health when they craft any
// cauldron recipe. This is done by using CraftTweaker's event system to listen
// to Cauldron Recipe's Crafted event. An event listener is a bit of code that
// gets run every time an event happens. In this case the Crafted event happens
// after a player crafts a recipe.

// Some classes must be imported to use them in your script.
import crafttweaker.api.events.CTEventManager;
import mods.cauldronrecipes.events.Crafted; 

// Registers an event listener for the Crafted event. This is a function that
// accepts an event parameter containing context about the crafting event.
CTEventManager.register<Crafted>((event) => {
    
    // These variables are made available in the event context. You don't need
    // all of them, this is just an example.
    var player = event.player; // The player crafting the recipe.
    var recipe = event.recipe; // The recipe that was crafted.
    var world = event.world; // The world/level instance.
    var cauldron = event.cauldron; // The cauldron used to craft the recipe.
    var pos = event.pos; // The position of the cauldron in the world.
    var outputs = event.outputs; // The list of items that the recipe made.
    
    // This event is ran on both the rendering thread and the logic thread.
    // Generally you only modify the game state on the logic thread. This line 
    // of code prevents the inner code from running on the rendering thread.
    if (!world.remote) {
    
        // Heals the player when they craft a cauldron recipe.
        player.heal(20);
    }
});