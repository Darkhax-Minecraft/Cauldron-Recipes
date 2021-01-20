// This example will add an iron nugget to the outputs of all cauldron recipes.
// It will also prevent all recipe from being crafted if it is raining. This is
// done by using CraftTweaker's event system to listen to Cauldron Recipe's 
// AboutToCraft event. An event listener is a bit of code that runs every time 
// a specific event happens. In this case the AboutToCraft event is fired just 
// before the recipe is crafted, allowing us to change the outcome.

// Some classes must be imported to use them in your script.
import crafttweaker.api.events.CTEventManager;
import mods.cauldronrecipes.events.AboutToCraft; 

// Registers an event listener for the AboutToCraft event. This is a function
// that accepts an event parameter containing context about the event.
CTEventManager.register<AboutToCraft>((event) => {
    
    // These variables are made available in the event context. You don't need
    // all of them, this is just an example.
    var player = event.player; // The player crafting the recipe.
    var recipe = event.recipe; // The recipe that was crafted.
    var world = event.world; // The world/level instance.
    var cauldron = event.cauldron; // The cauldron used to craft the recipe.
    var pos = event.pos; // The position of the cauldron in the world.
    var input = event.input; // The item used to craft this recipe.
    var initialOutputs = event.initialOutputs; // The original outputs.
    var outputs = event.outputs; // The list of items that the recipe made.
    
    // This event is ran on both the rendering thread and the logic thread.
    // Generally you only modify the game state on the logic thread. This line 
    // of code prevents the inner code from running on the rendering thread.
    if (!world.remote) {
    
        // Adds a single iron nugget to the recipe output.
        event.outputs.add(<item:minecraft:iron_nugget>);
    }
    
    // Checks if it is raining on the cauldron being used. Note that this is
    // being done on both the rendering and logic thread. This is because the
    // rendering thread may get out of sync if it still thinks it can craft it.
    if (!world.isRainingAt(pos.up())) {
        
        // Using the cancel method will cancel the crafting.
        event.cancel();
    }
});