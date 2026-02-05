package cofh.lib.common.item;

import cofh.lib.api.item.ICoFHItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HorseArmorItemCoFH extends Item implements ICoFHItem {

    protected int enchantability = 1;
    protected int protection;

    public HorseArmorItemCoFH(int protection, String texture, Properties builder) {

        super(builder);
        this.protection = protection;
    }

    public HorseArmorItemCoFH(int protection, ResourceLocation texture, Properties builder) {

        super(builder);
        this.protection = protection;
    }

    public HorseArmorItemCoFH setEnchantability(int enchantability) {

        this.enchantability = enchantability;
        return this;
    }

    @Override
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
    public HorseArmorItemCoFH setModId(String modId) {

        this.modId = modId;
        return this;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {

        return modId == null || modId.isEmpty() ? "" : modId;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false; // Horse armor typically not repairable
    }
    // endregion
}
