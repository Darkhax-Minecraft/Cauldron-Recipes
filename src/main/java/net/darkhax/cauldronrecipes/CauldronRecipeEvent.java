package net.darkhax.cauldronrecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Events related to the crafting of cauldron recipes.
 */
public class CauldronRecipeEvent extends PlayerEvent {
    
    /**
     * The recipe being crafted.
     */
    private final RecipeCauldron recipe;
    
    /**
     * The world instance.
     */
    private final World world;
    
    /**
     * The cauldron block state.
     */
    private final BlockState cauldron;
    
    /**
     * The position of the cauldron block.
     */
    private final BlockPos pos;
    
    private CauldronRecipeEvent(PlayerEntity player, RecipeCauldron recipe, World world, BlockState cauldron, BlockPos pos) {
        
        super(player);
        this.recipe = recipe;
        this.world = world;
        this.cauldron = cauldron;
        this.pos = pos;
    }
    
    /**
     * Gets the recipe being crafted.
     * 
     * @return The recipe being crafted.
     */
    public RecipeCauldron getRecipe () {
        
        return this.recipe;
    }
    
    /**
     * Gets the world instance.
     * 
     * @return The world instance.
     */
    public World getWorld () {
        
        return this.world;
    }
    
    /**
     * Gets the cauldron block state.
     * 
     * @return The cauldron block state.
     */
    public BlockState getCauldron () {
        
        return this.cauldron;
    }
    
    /**
     * Gets the position of the cauldron block.
     * 
     * @return The cauldron block pos.
     */
    public BlockPos getPos () {
        
        return this.pos;
    }
    
    /**
     * This event is fired when the cauldron recipe is about to be crafted. This is a mutable
     * event meaning that you can cancel the crafting or modify the output.
     */
    @Cancelable
    public static class AboutToCraft extends CauldronRecipeEvent {
        
        /**
         * The item being used to craft the recipe.
         */
        private final ItemStack input;
        
        /**
         * The initial output. This is not mutable.
         */
        private final List<ItemStack> initialOutput;
        
        /**
         * The actual output.
         */
        private final List<ItemStack> output;
        
        public AboutToCraft(PlayerEntity player, RecipeCauldron recipe, World world, BlockState cauldron, BlockPos pos, ItemStack input, ItemStack[] output) {
            
            super(player, recipe, world, cauldron, pos);
            this.input = input;
            this.output = Arrays.stream(output).collect(Collectors.toCollection(ArrayList::new));
            this.initialOutput = Collections.unmodifiableList(new ArrayList<>(this.output));
        }
        
        /**
         * Gets the item used to craft this recipe.
         * 
         * @return The item used to craft this recipe.
         */
        public ItemStack getInput () {
            
            return this.input;
        }
        
        /**
         * Gets the initial outputs for the recipe. This is not mutable. See
         * {@link #getOutputs()} for the actual output.
         * 
         * @return The initial outputs for the recipe.
         */
        public List<ItemStack> getInitialOutputs () {
            
            return this.initialOutput;
        }
        
        /**
         * Gets the actual outputs for the recipe. This is a mutable list that can be modified.
         * 
         * @return A modifiable list of output items.
         */
        public List<ItemStack> getOutputs () {
            
            return this.output;
        }
    }
    
    /**
     * This event is fired when a cauldron recipe has already been crafted. This is not mutable
     * or preventable.
     */
    public static class Crafted extends CauldronRecipeEvent {
        
        /**
         * The item that was crafted.
         */
        private final List<ItemStack> outputs;
        
        public Crafted(PlayerEntity player, RecipeCauldron recipe, World world, BlockState cauldron, BlockPos pos, List<ItemStack> outputs) {
            
            super(player, recipe, world, cauldron, pos);
            this.outputs = Collections.unmodifiableList(outputs);
        }
        
        /**
         * Gets the item that was crafted.
         * 
         * @return The item that was crafted.
         */
        public List<ItemStack> getOutputs () {
            
            return this.outputs;
        }
    }
}