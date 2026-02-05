package cofh.lib.common.enchantment;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Utility class for enchantment override functionality.
 * 
 * In NeoForge 1.21.1, enchantments are defined through data generation
 * and Enchantment is now a record class that cannot be extended.
 * This class provides utility methods for enchantment behavior overrides
 * and compatibility checks.
 */
public class EnchantmentOverride {

    /**
     * Checks if an enchantment should always be enabled.
     * Override enchantments are typically always enabled unless specifically disabled.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment should be enabled
     */
    public static boolean isEnabled(Enchantment enchantment) {
        return true;
    }

    /**
     * Checks if an enchantment can be applied at the enchanting table.
     * This provides a basic compatibility check for override enchantments.
     * 
     * @param stack The item stack to check
     * @param enchantment The enchantment to apply
     * @return true if the enchantment can be applied at the enchanting table
     */
    public static boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // Basic check - if the item is enchantable and the enchantment is supported
        return stack.isEnchantable() && enchantment.isSupportedItem(stack);
    }

    /**
     * Checks if an enchantment is allowed on books.
     * Override enchantments typically allow books by default.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment can be applied to books
     */
    public static boolean isAllowedOnBooks(Enchantment enchantment) {
        // TODO: Implement configuration-based book allowance logic
        return true;
    }

    /**
     * Checks if an enchantment is discoverable in loot.
     * Override enchantments typically allow loot generation by default.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment can be found in loot
     */
    public static boolean isDiscoverable(Enchantment enchantment) {
        // TODO: Implement configuration-based discoverability logic
        return true;
    }

    /**
     * Checks if an enchantment is tradeable by villagers.
     * Override enchantments typically allow villager trading by default.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment can be traded
     */
    public static boolean isTradeable(Enchantment enchantment) {
        // TODO: Implement configuration-based tradeability logic
        return true;
    }

    private EnchantmentOverride() {
        // Utility class - no instances
    }
}
