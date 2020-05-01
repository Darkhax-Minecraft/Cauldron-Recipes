package net.darkhax.cauldronrecipes;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.item.crafting.RecipeDataBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeCauldron extends RecipeDataBase {
    
    public static final IRecipeSerializer<RecipeCauldron> SERIALIZER = new Serializer();
    
    private final ResourceLocation id;
    private final Ingredient input;
    private final int fluidLevel;
    private final ResourceLocation loot;
    
    public RecipeCauldron(ResourceLocation id, Ingredient input, int fluidLevel, ResourceLocation loot) {
        
        this.id = id;
        this.input = input;
        this.fluidLevel = fluidLevel;
        this.loot = loot;
    }
    
    @Override
    public ResourceLocation getId () {
        
        return this.id;
    }
    
    @Override
    public IRecipeSerializer<?> getSerializer () {
        
        return SERIALIZER;
    }
    
    @Override
    public IRecipeType<?> getType () {
        
        return CauldronRecipes.recipeType;
    }
    
    public boolean matches (ItemStack item, int level) {
        
        return this.input.test(item) && this.fluidLevel <= level;
    }
    
    public void consume (World world, ItemStack stack, BlockPos pos, BlockState state, int currentFluidLevel) {
        
        stack.shrink(1);
        world.setBlockState(pos, state.with(CauldronBlock.LEVEL, currentFluidLevel - this.fluidLevel));
    }
    
    public void giveLoot (BlockPos pos, BlockState state, ServerPlayerEntity player) {
        
        final LootTable table = player.server.getLootTableManager().getLootTableFromLocation(this.loot);
        
        if (table != null && table != LootTable.EMPTY_LOOT_TABLE) {
            
            final LootContext.Builder ctxBuilder = new LootContext.Builder(player.getServerWorld());
            ctxBuilder.withParameter(LootParameters.POSITION, pos);
            ctxBuilder.withParameter(LootParameters.THIS_ENTITY, player);
            ctxBuilder.withLuck(player.getLuck());
            ctxBuilder.withRandom(player.world.rand);
            
            for (final ItemStack stack : table.generate(ctxBuilder.build(LootParameterSets.GIFT))) {
                
                if (!player.inventory.addItemStackToInventory(stack)) {
                    
                    player.dropItem(stack, false);
                }
            }
        }
        
        else {
            
            CauldronRecipes.LOGGER.error("Loot table {} is missing or empty!", this.loot);
        }
    }
    
    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeCauldron> {
        
        @Override
        public RecipeCauldron read (ResourceLocation recipeId, JsonObject json) {
            
            final Ingredient input = Ingredient.deserialize(json.get("input"));
            final int fluidLevel = JSONUtils.getInt(json, "fluidLevel", 1);
            final ResourceLocation loot = ResourceLocation.tryCreate(JSONUtils.getString(json, "loot"));
            return new RecipeCauldron(recipeId, input, fluidLevel, loot);
        }
        
        @Override
        public RecipeCauldron read (ResourceLocation recipeId, PacketBuffer buffer) {
            
            final Ingredient input = Ingredient.read(buffer);
            final int fluidLevel = buffer.readInt();
            final ResourceLocation loot = ResourceLocation.tryCreate(buffer.readString());
            return new RecipeCauldron(recipeId, input, fluidLevel, loot);
        }
        
        @Override
        public void write (PacketBuffer buffer, RecipeCauldron recipe) {
            
            recipe.input.write(buffer);
            buffer.writeInt(recipe.fluidLevel);
            buffer.writeString(recipe.loot.toString());
        }
    }
}