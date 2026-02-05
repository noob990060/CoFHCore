package cofh.lib.util.crafting;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Objects;

public final class IngredientWithCount {

    private final Ingredient ingredient;
    private final int count;

    public IngredientWithCount(Ingredient ingredient, int count) {

        this.ingredient = ingredient;
        this.count = count;
    }

    public ItemStack[] getItems() {

        ItemStack[] items = ingredient.getItems();
        if (count <= 1) {
            return items;
        }
        ItemStack[] counted = new ItemStack[items.length];
        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i].copy();
            stack.setCount(count);
            counted[i] = stack;
        }
        return counted;
    }

    public boolean test(@Nullable ItemStack stack) {

        return stack != null && ingredient.test(stack) && stack.getCount() >= count;
    }

    public IntList getStackingIds() {

        return ingredient.getStackingIds();
    }

    public boolean isEmpty() {

        return ingredient.isEmpty();
    }

    public boolean isSimple() {

        return ingredient.isSimple();
    }

    public Ingredient ingredient() {

        return ingredient;
    }

    public int count() {

        return count;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof IngredientWithCount other)) return false;
        return count == other.count && ingredient.equals(other.ingredient);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ingredient, count);
    }

    @Override
    public String toString() {

        return count + "x " + ingredient;
    }
}
