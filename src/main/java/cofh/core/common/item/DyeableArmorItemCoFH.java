package cofh.core.common.item;

import cofh.lib.api.item.IColorableItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public class DyeableArmorItemCoFH extends ArmorItemCoFH implements IColorableItem {

    public DyeableArmorItemCoFH(ArmorMaterial pMaterial, ArmorItem.Type pType, Item.Properties pProperties) {

        super(pMaterial, pType, pProperties);
    }

    @Override
    public int getColor(ItemStack item, int colorIndex) {
        return IColorableItem.super.getColor(item, colorIndex);
    }

    public boolean hasCustomColor(ItemStack stack) {
        return stack.has(DataComponents.DYED_COLOR);
    }

    public void clearColor(ItemStack stack) {
        stack.remove(DataComponents.DYED_COLOR);
    }

    public void setColor(ItemStack stack, int color) {
        stack.set(DataComponents.DYED_COLOR, new DyedItemColor(color, false));
    }

}
