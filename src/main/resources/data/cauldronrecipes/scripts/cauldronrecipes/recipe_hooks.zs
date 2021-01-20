// In this example we use recipe hooks to create a new type of cauldron recipe.
// A recipe hook allows you to run parts of your script during the crafting
// process. In this example we create a recipe where using an iron nugget on
// a cauldron that is powered by redstone will turn that cauldron into an block
// of iron.

// Gets the manager for Cauldron Recipes.
val cauldron = <recipetype:cauldronrecipes:cauldron_recipe>;

// Creates a new recipe which accepts an iron nugget and 1 fluid level.
var ironRecipe = cauldron.addRecipe("iron_to_iron_block", <item:minecraft:iron_nugget>, 1);

// Hides the recipe from things like JEI.
ironRecipe.setHidden(true);

// Adds a crafting hook to the recipe. This part of the script will be ran just
// before the recipe is crafted. This allows you to edit the recipe outputs.
// You can also prevent the recipe from being crafted.
ironRecipe.addCraftingHook((event) => {
    
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
    
    // Checks if the cauldron is not recieving redstone power. If it is not the
    // code inside will be ran.
    if (!world.isBlockPowered(pos)) {
    
        // Using the cancel method will prevent the recipe from being crafted.
        // We only want this to be craftable when the cauldron is powered so
        // the recipe should not be craftable.
        event.cancel();
    }
});

// Adds a crafted hook to the recipe. This part of the script will only be used
// when the recipe is successfully crafted. At this stage it is too late to 
// modify the recipe itself, but it is safe to run additional affects now.
ironRecipe.addCraftedHook((event) => {
    
    // These variables are made available in the event context. You don't need
    // all of them, this is just an example.
    var player = event.player; // The player crafting the recipe.
    var recipe = event.recipe; // The recipe that was crafted.
    var world = event.world; // The world/level instance.
    var cauldron = event.cauldron; // The cauldron used to craft the recipe.
    var pos = event.pos; // The position of the cauldron in the world.
    var outputs = event.outputs; // The list of items that the recipe made.
    
    // In general the game state should only be modified on the logic thread.
    // This hook runs on the logic and render thread so you need to filter out
    // the render thread. This can be done safely by checking the value of 
    // world.remote, it will only be true on the render thread.
    if (!world.remote) {
    
        // Sets the block at the cauldron's position to an iron block.
        world.setBlockState(pos, <blockstate:minecraft:iron_block>);
    }
});