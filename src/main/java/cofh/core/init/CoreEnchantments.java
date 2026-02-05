package cofh.core.init;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public class CoreEnchantments {

    private CoreEnchantments() {

    }

    public static void register() {

        // In NeoForge 1.21.1, enchantments are registered through data generation
        // See CoreEnchantmentsProvider for the actual enchantment definition
        Types.register();
    }

    public static class Types {

        public static void register() {
            // Enchantment types are now defined through JSON data generation
            // The actual enchantment is registered in CoreEnchantmentsProvider
        }
    }

    // In NeoForge 1.21.1, enchantments are registered through data generation
    // The holding enchantment is now defined in CoreEnchantmentsProvider
    // We can keep this as a reference for the enchantment ID
    public static final String HOLDING_ID = ID_COFH_CORE + ":holding";

}
