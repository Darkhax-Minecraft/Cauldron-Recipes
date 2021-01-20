// Gets the manager for Cauldron Recipes
val cauldron = <recipetype:cauldronrecipes:cauldron_recipe>;

// Adds a recipe which crafts a diamond from a dirt.
cauldron.addRecipe("dirt_to_diamonds", <item:minecraft:dirt>, <item:minecraft:diamond>);

// Adds a recipe which turns any item with the forge:gems/lapis tag into dirt.
cauldron.addRecipe("gems_to_dirt", <tag:items:forge:gems/lapis>, <item:minecraft:dirt>);

// Adds a recipe with multiple outputs.
cauldron.addRecipe("stick_to_stones", <item:minecraft:stick>, <item:minecraft:cobblestone>, <item:minecraft:stone>);

// Adds a recipe which requires 2 fluid levels.
cauldron.addRecipe("glass_to_blue_glass", <item:minecraft:glass>, 2, <item:minecraft:light_blue_stained_glass>);

// Adds a recipe with 2 fluid levels and multiple outputs.
cauldron.addRecipe("sand_to_sapplings", <item:minecraft:sand>, 2, <item:minecraft:oak_sapling>, <item:minecraft:birch_sapling>, <item:minecraft:dark_oak_sapling>);