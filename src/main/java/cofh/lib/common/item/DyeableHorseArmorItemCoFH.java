package cofh.lib.common.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DyeableHorseArmorItemCoFH extends HorseArmorItemCoFH {

    protected int enchantability = 1;

    public DyeableHorseArmorItemCoFH(int protection, String texture, Properties builder) {

        super(protection, texture, builder);
    }

    public DyeableHorseArmorItemCoFH(int protection, ResourceLocation texture, Properties builder) {

        super(protection, texture, builder);
    }

    public DyeableHorseArmorItemCoFH setEnchantability(int enchantability) {

        this.enchantability = enchantability;
        return this;
    }

    public boolean isEnchantable(ItemStack stack) {

        return enchantability > 0;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {

        return enchantability;
    }

    // region DISPLAY
    protected String modId = "";

    @Override
    public DyeableHorseArmorItemCoFH setModId(String modId) {

        this.modId = modId;
        return this;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {

        return modId == null || modId.isEmpty() ? super.getCreatorModId(itemStack) : modId;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false; // Horse armor typically not repairable
    }
    // endregion
}
