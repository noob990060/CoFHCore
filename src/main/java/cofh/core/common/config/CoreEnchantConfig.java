package cofh.core.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.function.Supplier;

import static cofh.lib.util.Constants.MAX_ENCHANT_LEVEL;
import static cofh.lib.util.Constants.TRUE;

public class CoreEnchantConfig implements IBaseConfig {

    @Override
    public void apply(ModConfigSpec.Builder builder) {

        builder.push("Enchantments");

        improvedFeatherFalling = builder
                .comment("If TRUE, Feather Falling will prevent Farmland from being trampled. This option will work with alternative versions (overrides) of Feather Falling.")
                .define("Improved Feather Falling", improvedFeatherFalling);

        improvedMending = builder
                .comment("If TRUE, Mending behavior is altered so that Experience Orbs always repair items if possible, and the most damaged item is prioritized. This option may not work with alternative versions (overrides) of Mending.")
                .define("Improved Mending", improvedMending);

        builder.push("Holding");
        enableHolding = builder
                .comment("If TRUE, the Holding Enchantment is available for various Storage Items and Blocks.")
                .define("Enable", true);
        treasureHolding = builder
                .comment("This sets whether or not the Holding Enchantment is considered a 'treasure' enchantment.")
                .define("Treasure", false);
        levelHolding = builder
                .comment("This option adjusts the maximum allowable level for the Holding Enchantment.")
                .defineInRange("Max Level", 4, 1, MAX_ENCHANT_LEVEL);
        builder.pop();

        builder.pop();
    }

    @Override
    public void refresh() {
        // In NeoForge 1.21.1, enchantments are defined through data generation
        // and cannot be configured through runtime methods.
        // Configuration values are stored for potential use in data generation.
    }

    public static boolean improvedFeatherFalling() {

        return improvedFeatherFalling.get();
    }

    public static boolean improvedMending() {

        return improvedMending.get();
    }

    public static boolean enableHolding() {

        return enableHolding.get();
    }

    public static boolean treasureHolding() {

        return treasureHolding.get();
    }

    public static int levelHolding() {

        return levelHolding.get();
    }

    private static Supplier<Boolean> improvedFeatherFalling = TRUE;
    private static Supplier<Boolean> improvedMending = TRUE;

    private static Supplier<Boolean> enableHolding;
    private static Supplier<Boolean> treasureHolding;
    private static Supplier<Integer> levelHolding;

}
