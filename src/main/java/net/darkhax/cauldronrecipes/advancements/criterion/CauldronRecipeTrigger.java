package net.darkhax.cauldronrecipes.advancements.criterion;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;

import net.darkhax.bookshelf.serialization.Serializers;
import net.darkhax.cauldronrecipes.CauldronRecipes;
import net.darkhax.cauldronrecipes.RecipeCauldron;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.loot.ConditionArraySerializer;
import net.minecraft.util.ResourceLocation;

public class CauldronRecipeTrigger extends AbstractCriterionTrigger<CauldronRecipeTrigger.Instance> {
    
    private static final ResourceLocation ID = new ResourceLocation(CauldronRecipes.MOD_ID, "recipe_crafted");
    
    public void trigger (ServerPlayerEntity player, RecipeCauldron recipe) {
        
        this.triggerListeners(player, (instance) -> instance.test(recipe));
    }
    
    @Override
    public ResourceLocation getId () {
        
        return ID;
    }
    
    @Override
    public CauldronRecipeTrigger.Instance deserializeTrigger (JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        
        final List<ResourceLocation> recipes = Serializers.RESOURCE_LOCATION.readList(json, "recipe", Collections.emptyList());
        return new CauldronRecipeTrigger.Instance(entityPredicate, recipes);
    }
    
    public static final class Instance extends CriterionInstance {
        
        private final List<ResourceLocation> recipes;
        
        public Instance(EntityPredicate.AndPredicate player, List<ResourceLocation> recipes) {
            
            super(CauldronRecipeTrigger.ID, player);
            this.recipes = recipes;
        }
        
        @Override
        public JsonObject serialize (ConditionArraySerializer conditions) {
            
            final JsonObject jsonobject = super.serialize(conditions);
            jsonobject.add("recipe", Serializers.RESOURCE_LOCATION.writeList(this.recipes));
            return jsonobject;
        }
        
        public boolean test (RecipeCauldron recipe) {
            
            return this.recipes.isEmpty() || this.recipes.stream().anyMatch(recipeId -> recipeId.equals(recipe.getId()));
        }
    }
}