package net.darkhax.cauldronrecipes;

import java.util.List;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.darkhax.bookshelf.item.crafting.RecipeDataBase;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeCauldron extends RecipeDataBase {
    
    public static final IRecipeSerializer<RecipeCauldron> SERIALIZER = new Serializer();
    
    private final ResourceLocation id;
    private final Ingredient input;
    private final int fluidLevel;
    private final ItemStack[] results;
    private final ResourceLocation loot;
    
    public RecipeCauldron(ResourceLocation id, Ingredient input, int fluidLevel, ItemStack[] result, @Nullable ResourceLocation loot) {
        
        this.id = id;
        this.input = input;
        this.fluidLevel = fluidLevel;
        this.results = result;
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
        
        if (results != null) {
            
            for (ItemStack stack : this.results) {
                
                final ItemStack resultDrop = stack.copy();
                
                if (!player.inventory.addItemStackToInventory(resultDrop)) {
                    
                    player.dropItem(resultDrop, false);
                }
            }
        }
        
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
    }
    
    private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<RecipeCauldron> {
        
        public static ItemStack readItemStack(JsonElement element, boolean acceptNBT) {
                        
            if (JSONUtils.isString(element)) {
                
                final String identifier = element.getAsString();
                final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(identifier));
                
                if (item != null) {
                    
                    return new ItemStack(item);
                }
                
                else {
                    
                    throw new JsonSyntaxException("Could not find Item with ID " + identifier);
                }
            }
            
            else if (element.isJsonObject()) {
                
                return CraftingHelper.getItemStack(element.getAsJsonObject(), acceptNBT);
            }
            
            throw new JsonSyntaxException("Could not read ItemStack from element. Must be String or Object");
        }
        
        public static List<ItemStack> readItemStacks(JsonElement element, boolean acceptNBT) {
            
            List<ItemStack> items = NonNullList.create();
            
            if (element.isJsonArray()) {
                
                for (JsonElement subElement : element.getAsJsonArray()) {
                    
                    items.add(readItemStack(subElement, acceptNBT));
                }
            }
            
            else {
                
                items.add(readItemStack(element, acceptNBT));
            }
            
            return items;
        }
        
        public static ItemStack[] readItemStackArray(PacketBuffer buffer) {
            
            final ItemStack[] items = new ItemStack[buffer.readInt()];
            
            for (int i = 0; i < items.length; i++) {
                
                items[i] = buffer.readItemStack();
            }
            
            return items;
        }
        
        public static void writeItemStackArray(PacketBuffer buffer, ItemStack[] items) {
            
            buffer.writeInt(items.length);
            
            for (ItemStack stack : items) {
                
                buffer.writeItemStack(stack);
            }
        }
        
        @Override
        public RecipeCauldron read (ResourceLocation recipeId, JsonObject json) {
            
            final Ingredient input = Ingredient.deserialize(json.get("input"));
            final int fluidLevel = JSONUtils.getInt(json, "fluidLevel", 1);            
            final ItemStack[] results = json.has("result") ? readItemStacks(json.get("result"), true).toArray(new ItemStack[0]) : new ItemStack[0];            
            final ResourceLocation loot = ResourceLocation.tryCreate(JSONUtils.getString(json, "loot", "minecraft:empty"));
            return new RecipeCauldron(recipeId, input, fluidLevel, results, loot);
        }
        
        @Override
        public RecipeCauldron read (ResourceLocation recipeId, PacketBuffer buffer) {
            
            final Ingredient input = Ingredient.read(buffer);
            final int fluidLevel = buffer.readInt();            
            final ItemStack[] results = readItemStackArray(buffer);
            final ResourceLocation loot = ResourceLocation.tryCreate(buffer.readString());
            return new RecipeCauldron(recipeId, input, fluidLevel, results, loot);
        }
        
        @Override
        public void write (PacketBuffer buffer, RecipeCauldron recipe) {
            
            recipe.input.write(buffer);
            buffer.writeInt(recipe.fluidLevel);
            writeItemStackArray(buffer, recipe.results);
            buffer.writeString(recipe.loot.toString());
        }
    }
}