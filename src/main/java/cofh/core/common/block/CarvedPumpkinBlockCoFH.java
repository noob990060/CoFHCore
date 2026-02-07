package cofh.core.common.block;

import cofh.core.util.ProxyUtils;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class CarvedPumpkinBlockCoFH extends CarvedPumpkinBlock {

    protected String translationKey = "";

    public CarvedPumpkinBlockCoFH setTranslationKey(String translationKey) {

        this.translationKey = translationKey;
        return this;
    }

    /**
     * This ensures that the predicate check isn't stupid. Can't do this for other hardcoded cases unfortunately.
     */
    public static void updatePredicate() {

        Predicate<BlockState> predicate = (state) -> state != null && (state.is(Blocks.CARVED_PUMPKIN) || state.is(Blocks.JACK_O_LANTERN) || state.getBlock() instanceof CarvedPumpkinBlockCoFH);
        try {
            Field field = CarvedPumpkinBlock.class.getDeclaredField("PUMPKINS_PREDICATE");
            field.setAccessible(true);
            field.set(null, predicate);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Failed to update pumpkin predicate.", ex);
        }
    }

    public CarvedPumpkinBlockCoFH(Properties properties) {

        super(properties);
    }

    @Override
    public String getDescriptionId() {

        String specificTranslation = Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(this));
        if (ProxyUtils.canLocalize(specificTranslation)) {
            return specificTranslation;
        }
        return translationKey;
    }

}
