package cofh.core.common.enchantment;

import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for the Holding enchantment functionality.
 * 
 * In NeoForge 1.21.1, enchantments are defined through data generation
 * in CoreEnchantmentsProvider. This class now serves as a utility
 * for managing valid items that can receive the Holding enchantment.
 */
public class HoldingEnchantment {

    private static final Set<Item> VALID_ITEMS = new HashSet<>();

    /**
     * Adds a valid item that can receive the Holding enchantment.
     * @param container The container item to add
     * @return true if the item was added, false if it was already present
     */
    public static boolean addValidItem(Item container) {
        return VALID_ITEMS.add(container);
    }

    /**
     * Checks if an item is valid for the Holding enchantment.
     * @param item The item to check
     * @return true if the item can receive the Holding enchantment
     */
    public static boolean isValidItem(Item item) {
        return VALID_ITEMS.contains(item);
    }

    /**
     * Gets the set of all valid items for the Holding enchantment.
     * @return An unmodifiable view of the valid items set
     */
    public static Set<Item> getValidItems() {
        return java.util.Collections.unmodifiableSet(VALID_ITEMS);
    }

    private HoldingEnchantment() {
        // Utility class - no instances
    }
}
