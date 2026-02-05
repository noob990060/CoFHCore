package cofh.core.compat.jei;

import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import java.util.Optional;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.neoforge.fluids.FluidStack;

/**
 * NeoForge 1.21.1: Updated for JEI 19.9.0+ API and NeoForge Data Components
 * Uses the new Data Components system instead of NBT tags
 */
public class FluidPotionSubtypeInterpreter implements IIngredientSubtypeInterpreter<FluidStack> {

    public static final FluidPotionSubtypeInterpreter INSTANCE = new FluidPotionSubtypeInterpreter();

    private FluidPotionSubtypeInterpreter() {

    }

    @Override
    public String apply(FluidStack ingredient, UidContext context) {

        // NeoForge 1.21.1: Check if fluid has potion contents using Data Components
        if (!ingredient.has(DataComponents.POTION_CONTENTS)) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        
        // NeoForge 1.21.1: Get potion contents from Data Components
        PotionContents potionContents = ingredient.get(DataComponents.POTION_CONTENTS);
        if (potionContents == null) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        
        // NeoForge 1.21.1: Get potion from Optional Holder
        Optional<Holder<Potion>> potionTypeOptional = potionContents.potion();
        if (potionTypeOptional.isEmpty()) {
            return IIngredientSubtypeInterpreter.NONE;
        }
        Holder<Potion> potionType = potionTypeOptional.get();
        String potionTypeString = Potion.getName(potionTypeOptional, "potion");

        StringBuilder stringBuilder = new StringBuilder(potionTypeString);
        
        // NeoForge 1.21.1: Get custom effects from potion contents
        for (MobEffectInstance effect : potionContents.customEffects()) {
            stringBuilder.append(";").append(effect);
        }
        
        // NeoForge 1.21.1: Get potion effects from the potion type
        for (MobEffectInstance effect : potionType.value().getEffects()) {
            stringBuilder.append(";").append(effect);
        }
        
        return stringBuilder.toString();
    }

}
