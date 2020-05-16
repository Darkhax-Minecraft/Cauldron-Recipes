package net.darkhax.cauldronrecipes;

import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.RecipeUtils;
import net.darkhax.bookshelf.util.SidedExecutor;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod(CauldronRecipes.MOD_ID)
public class CauldronRecipes {
    
    public static final String MOD_ID = "cauldronrecipes";
    public static final Logger LOGGER = LogManager.getLogger("Cauldron Recipes");
    private final RegistryHelper registry = new RegistryHelper("cauldronrecipes", LOGGER, null);
    
    public static IRecipeType<RecipeCauldron> recipeType;
    
    public CauldronRecipes() {
        
        recipeType = this.registry.registerRecipeType("cauldron_recipe");
        this.registry.registerRecipeSerializer(RecipeCauldron.SERIALIZER, "cauldron_recipe");
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClickBlock);
        
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onRecipesSynced));
    }
    
    private void onPlayerClickBlock (PlayerInteractEvent.RightClickBlock event) {
        
        final World world = event.getWorld();
        final PlayerEntity player = event.getPlayer();
        final BlockPos pos = event.getPos();
        final BlockState state = world.getBlockState(pos);
        final ItemStack stack = player.getHeldItem(event.getHand());
        
        if (state.getBlock() instanceof CauldronBlock) {
            
            final int initialFluidLevel = state.get(CauldronBlock.LEVEL);
            
            if (initialFluidLevel > 0) {
                
                final RecipeCauldron recipe = findRecipe(stack, initialFluidLevel);
                
                if (recipe != null) {
                    
                    if (!player.isCreative()) {
                        
                        recipe.consume(world, stack, pos, state, initialFluidLevel);
                    }
                    
                    event.setCanceled(true);
                    
                    if (player instanceof ServerPlayerEntity) {
                        
                        recipe.giveItems(pos, state, (ServerPlayerEntity) player);
                    }
                }
            }
        }
    }
    
    private void onRecipesSynced (RecipesUpdatedEvent event) {
        
        final Map<ResourceLocation, RecipeCauldron> recipes = getRecipes(event.getRecipeManager());
        final int namespaces = recipes.keySet().stream().map(ResourceLocation::getNamespace).collect(Collectors.toSet()).size();
        LOGGER.info("Loaded {} cauldron recipes from {} namespaces", recipes.size(), namespaces);
    }
    
    @Nullable
    public static RecipeCauldron findRecipe (ItemStack item, int currentFluid) {
        
        for (final RecipeCauldron recipe : getRecipes().values()) {
            
            if (recipe.matches(item, currentFluid)) {
                
                return recipe;
            }
        }
        
        return null;
    }
    
    public static Map<ResourceLocation, RecipeCauldron> getRecipes () {
        
        return getRecipes(null);
    }
    
    public static Map<ResourceLocation, RecipeCauldron> getRecipes (@Nullable RecipeManager manager) {
        
        return RecipeUtils.getRecipes(recipeType, getManager(manager));
    }
    
    public static RecipeManager getManager (@Nullable RecipeManager manager) {
        
        return manager != null ? manager : SidedExecutor.callForSide( () -> () -> Minecraft.getInstance().player.connection.getRecipeManager(), () -> () -> ServerLifecycleHooks.getCurrentServer().getRecipeManager());
    }
}