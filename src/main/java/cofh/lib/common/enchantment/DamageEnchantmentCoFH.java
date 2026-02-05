package cofh.lib.common.enchantment;

import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Utility class for damage enchantment functionality.
 * 
 * In NeoForge 1.21.1, enchantments are defined through data generation
 * and Enchantment is now a record class that cannot be extended.
 * This class provides utility methods for damage enchantment calculations
 * and compatibility checks.
 */
public class DamageEnchantmentCoFH {

    /**
     * Calculates the minimum enchantment cost for a damage enchantment.
     * 
     * @param level The enchantment level
     * @return The minimum cost in experience levels
     */
    public static int getMinCost(int level) {
        return 10 + (level - 1) * 8;
    }

    /**
     * Calculates the maximum enchantment cost for a damage enchantment.
     * 
     * @param level The enchantment level
     * @return The maximum cost in experience levels
     */
    public static int getMaxCost(int level) {
        return getMinCost(level) + 20;
    }

    /**
     * Checks if a damage enchantment is compatible with another enchantment.
     * Damage enchantments are incompatible with other damage enchantments.
     * 
     * @param ench The enchantment to check compatibility with
     * @return true if the enchantments are compatible
     */
    public static boolean checkCompatibility(Enchantment ench) {
        // In NeoForge 1.21.1, damage enchantments should be incompatible with each other
        // This logic would typically be handled through the exclusiveSet in the enchantment definition
        return !isDamageEnchantment(ench);
    }

    /**
     * Calculates the extra damage provided by a damage enchantment.
     * 
     * @param level The enchantment level
     * @return The extra damage amount
     */
    public static float getExtraDamage(int level) {
        return level * 2.5F;
    }

    /**
     * Checks if an enchantment is a damage enchantment.
     * This is a placeholder method - in practice, this would be determined
     * by checking the enchantment's definition or registry entry.
     * 
     * @param ench The enchantment to check
     * @return true if the enchantment is a damage enchantment
     */
    private static boolean isDamageEnchantment(Enchantment ench) {
        // TODO: Implement proper damage enchantment detection
        // This would typically check the enchantment's registry ID or definition
        // For now, return false as a safe default
        return false;
    }

    private DamageEnchantmentCoFH() {
        // Utility class - no instances
    }
}
