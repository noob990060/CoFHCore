package cofh.core.util.crafting;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import static cofh.core.init.CoreRecipeSerializers.SHAPED_POTION_RECIPE_SERIALIZER;

public class ShapedPotionNBTRecipe implements CraftingRecipe {

    private final ShapedRecipe wrappedRecipe;

    public ShapedPotionNBTRecipe(String pGroup, CraftingBookCategory pCategory, ShapedRecipePattern pattern, ItemStack pResult) {

        wrappedRecipe = new ShapedRecipe(pGroup, pCategory, pattern, pResult);
    }

    @Override
    public boolean matches(CraftingInput input, Level worldIn) {

        // TODO: Update to use new potion API when available
        return wrappedRecipe.matches(input, worldIn);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registryAccess) {

        ItemStack result = wrappedRecipe.getResultItem(registryAccess).copy();

        // TODO: Update to use new potion API when available
        return result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {

        return wrappedRecipe.canCraftInDimensions(width, height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {

        return wrappedRecipe.getResultItem(registryAccess);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {

        return wrappedRecipe.getIngredients();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {

        return SHAPED_POTION_RECIPE_SERIALIZER.get();
    }

    @Override
    public CraftingBookCategory category() {

        return wrappedRecipe.category();
    }

    // region SERIALIZER
    public static class Serializer implements RecipeSerializer<ShapedPotionNBTRecipe> {

        // TODO: Update to use new codec system when available
        @Override
        public MapCodec<ShapedPotionNBTRecipe> codec() {
            // TODO: Implement when codec system is available
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ShapedPotionNBTRecipe> streamCodec() {
            // TODO: Implement when codec system is available
            return null;
        }
    }

    //    public static class Serializer implements RecipeSerializer<ShapedPotionNBTRecipe> {
    //
    //        public ShapedPotionNBTRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
    //
    //            String s = GsonHelper.getAsString(json, "group", "");
    //            CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", (String) null), CraftingBookCategory.MISC);
    //            Map<String, Ingredient> map = ShapedRecipeInternal.keyFromJson(GsonHelper.getAsJsonObject(json, "key"));
    //            String[] astring = ShapedRecipeInternal.shrink(ShapedRecipeInternal.patternFromJson(GsonHelper.getAsJsonArray(json, "pattern")));
    //            int i = astring[0].length();
    //            int j = astring.length;
    //            NonNullList<Ingredient> nonnulllist = ShapedRecipeInternal.dissolvePattern(astring, map, i, j);
    //            ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
    //            return new ShapedPotionNBTRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack);
    //        }
    //
    //        @Override
    //        public ShapedPotionNBTRecipe fromNetwork(FriendlyByteBuf buffer) {
    //
    //            int i = buffer.readVarInt();
    //            int j = buffer.readVarInt();
    //            String s = buffer.readUtf(32767);
    //            CraftingBookCategory craftingbookcategory = buffer.readEnum(CraftingBookCategory.class);
    //            NonNullList<Ingredient> nonnulllist = NonNullList.withSize(i * j, Ingredient.EMPTY);
    //
    //            for (int k = 0; k < nonnulllist.size(); ++k) {
    //                nonnulllist.set(k, Ingredient.fromNetwork(buffer));
    //            }
    //            ItemStack itemstack = buffer.readItem();
    //            return new ShapedPotionNBTRecipe(recipeId, s, craftingbookcategory, i, j, nonnulllist, itemstack);
    //        }
    //
    //        @Override
    //        public void toNetwork(FriendlyByteBuf buffer, ShapedPotionNBTRecipe recipe) {
    //
    //            buffer.writeVarInt(recipe.getRecipeWidth());
    //            buffer.writeVarInt(recipe.getRecipeHeight());
    //            buffer.writeUtf(recipe.getGroup());
    //
    //            for (Ingredient ingredient : recipe.getIngredients()) {
    //                ingredient.toNetwork(buffer);
    //            }
    //            buffer.writeItem(recipe.wrappedRecipe.result);
    //        }
    //
    //    }
    // endregion
}
