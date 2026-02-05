package cofh.lib.common.item;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class ItemTierCoFH implements Tier {

    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final Supplier<Ingredient> repairIngredient;

    public ItemTierCoFH(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {

        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = repairIngredient;
    }

    // region IItemTier
    @Override
    public int getUses() {

        return this.uses;
    }

    @Override
    public float getSpeed() {

        return this.speed;
    }

    @Override
    public float getAttackDamageBonus() {

        return this.damage;
    }

    @Override
    public int getEnchantmentValue() {

        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {

        return this.repairIngredient.get();
    }

    @Override
    public TagKey<net.minecraft.world.level.block.Block> getIncorrectBlocksForDrops() {
        return null; // Default implementation - can be customized if needed
    }
    // endregion
}
