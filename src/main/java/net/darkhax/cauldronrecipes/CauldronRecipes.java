package net.darkhax.cauldronrecipes;

import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.bookshelf.util.ModUtils;
import net.darkhax.bookshelf.util.RecipeUtils;
import net.darkhax.bookshelf.util.SidedExecutor;
import net.darkhax.cauldronrecipes.CauldronRecipeEvent.AboutToCraft;
import net.darkhax.cauldronrecipes.addons.crt.CraftTweakerAddon;
import net.darkhax.cauldronrecipes.advancements.criterion.CauldronRecipeTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@Mod(CauldronRecipes.MOD_ID)
public class CauldronRecipes {
    
    public static final String MOD_ID = "cauldronrecipes";
    public static final Logger LOGGER = LogManager.getLogger("Cauldron Recipes");
    public static IRecipeType<RecipeCauldron> recipeType;
    
    private final RegistryHelper registry = new RegistryHelper("cauldronrecipes", LOGGER);
    private final CauldronRecipeTrigger cauldronTrigger;
    
    public CauldronRecipes() {
        
        recipeType = this.registry.recipeTypes.register("cauldron_recipe");
        this.registry.recipeSerializers.register(RecipeCauldron.SERIALIZER, "cauldron_recipe");
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerClickBlock);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        
        this.cauldronTrigger = CriteriaTriggers.register(new CauldronRecipeTrigger());
    }
    
    private void setup (FMLCommonSetupEvent event) {
        
        if (ModUtils.isInModList("crafttweaker")) {
            
            MinecraftForge.EVENT_BUS.register(CraftTweakerAddon.class);
        }
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
                    
                    final AboutToCraft craftEvent = new AboutToCraft(player, recipe, world, state, pos, stack, recipe.getOutputs());
                    
                    if (!MinecraftForge.EVENT_BUS.post(craftEvent)) {
                        
                        if (!player.isCreative()) {
                            
                            stack.shrink(1);
                        }
                        
                        world.setBlockState(pos, state.with(CauldronBlock.LEVEL, initialFluidLevel - recipe.getFluidLevel()));
                        event.setCanceled(true);
                        event.setCancellationResult(ActionResultType.CONSUME);
                        
                        if (player instanceof ServerPlayerEntity) {
                            
                            for (final ItemStack outputEntry : craftEvent.getOutputs()) {
                                
                                final ItemStack resultDrop = outputEntry.copy();
                                
                                if (!player.inventory.addItemStackToInventory(resultDrop)) {
                                    
                                    player.dropItem(resultDrop, false);
                                }
                            }
                            
                            this.cauldronTrigger.trigger((ServerPlayerEntity) player, recipe);
                        }
                        
                        MinecraftForge.EVENT_BUS.post(new CauldronRecipeEvent.Crafted(player, recipe, world, state, pos, craftEvent.getOutputs()));
                    }
                }
            }
        }
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