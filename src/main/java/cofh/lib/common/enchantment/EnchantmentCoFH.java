package cofh.lib.common.enchantment;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Utility class for CoFH enchantment functionality.
 * 
 * In NeoForge 1.21.1, enchantments are defined through data generation
 * and Enchantment is now a record class that cannot be extended.
 * This class provides utility methods for enchantment configuration
 * and compatibility checks.
 */
public class EnchantmentCoFH {

    /**
     * Checks if an enchantment is enabled based on configuration.
     * This method should be used by enchantment effect components
     * to determine if the enchantment should be active.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment should be enabled
     */
    public static boolean isEnabled(Enchantment enchantment) {
        // TODO: Implement configuration-based enable/disable logic
        // This would typically check a configuration system
        return true;
    }

    /**
     * Checks if an enchantment is allowed on books.
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
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment can be traded
     */
    public static boolean isTradeable(Enchantment enchantment) {
        // TODO: Implement configuration-based tradeability logic
        return true;
    }

    /**
     * Checks if an enchantment is a treasure enchantment.
     * 
     * @param enchantment The enchantment to check
     * @return true if the enchantment is a treasure enchantment
     */
    public static boolean isTreasureOnly(Enchantment enchantment) {
        // TODO: Implement configuration-based treasure logic
        return false;
    }

    /**
     * Gets the maximum level for an enchantment.
     * 
     * @param enchantment The enchantment to check
     * @return The maximum level, or 1 if not specified
     */
    public static int getMaxLevel(Enchantment enchantment) {
        // TODO: Implement configuration-based max level logic
        return enchantment.definition().maxLevel();
    }

    /**
     * Gets the description for an enchantment, with support for disabled state.
     * 
     * @param enchantment The enchantment to get the description for
     * @return The description Component, or disabled message if not enabled
     */
    public static Component getDescription(Enchantment enchantment) {
        return isEnabled(enchantment) ? enchantment.description() : Component.literal("enchantment.cofh_core.disabled");
    }

    private EnchantmentCoFH() {
        // Utility class - no instances
    }
}
