package cofh.lib.common.item;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ArmorMaterialCoFH {

    protected static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    public static ArmorMaterial create(String nameIn, int maxDamageFactorIn, int[] damageReductionAmountsIn, int enchantabilityIn, Holder<SoundEvent> equipSoundIn, float toughnessIn, float knockbackResistanceIn, Supplier<Ingredient> repairMaterialSupplier) {

        return new ArmorMaterial(
            createDefenseMap(damageReductionAmountsIn),
            enchantabilityIn,
            equipSoundIn,
            repairMaterialSupplier,
            List.of(new ArmorMaterial.Layer(net.minecraft.resources.ResourceLocation.withDefaultNamespace(nameIn))),
            toughnessIn,
            knockbackResistanceIn
        );
    }

    public static int getDurabilityForType(ArmorItem.Type pType, int maxDamageFactor) {
        return pType.getDurability(maxDamageFactor);
    }

    public static int getDurabilityForType(ArmorItem.Type pType) {
        return getDurabilityForType(pType, 15); // Default multiplier
    }

    private static EnumMap<ArmorItem.Type, Integer> createDefenseMap(int[] damageReductionAmounts) {
        EnumMap<ArmorItem.Type, Integer> defenseMap = new EnumMap<>(ArmorItem.Type.class);
        ArmorItem.Type[] types = ArmorItem.Type.values();
        for (int i = 0; i < Math.min(damageReductionAmounts.length, types.length); i++) {
            defenseMap.put(types[i], damageReductionAmounts[i]);
        }
        return defenseMap;
    }
}
