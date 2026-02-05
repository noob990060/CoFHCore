package cofh.lib.api.item;

import cofh.core.common.item.IAugmentableItem;
import cofh.lib.api.ContainerType;
import cofh.lib.util.Utils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import static cofh.core.util.helpers.AugmentableHelper.getAttributeMod;
import static cofh.core.util.references.CoreIDs.ID_HOLDING;
import static cofh.lib.util.Utils.getEnchantment;
import static cofh.lib.util.Utils.getItemEnchantmentLevel;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;
import static cofh.lib.util.constants.NBTTags.*;

/**
 * Marker interface for anything that supports the "Holding" enchantment. Can
 * also be done via the Enchantable capability, but this is way less overhead.
 */
public interface IContainerItem {

    // Define a new constant for the XP Creative augment tag
    String TAG_AUGMENT_XP_CREATIVE = "AugmentXpCreative";

    default int getMaxStored(ItemStack container, int amount) {
        int holding = getItemEnchantmentLevel(getEnchantment(ID_COFH_CORE, ID_HOLDING), container);
        return Utils.getEnchantedCapacity(amount, holding);
    }

    default boolean isCreative(ItemStack stack, ContainerType type) {

        if (stack.getItem() instanceof IAugmentableItem) {
            CompoundTag subTag = getPropertiesTag(stack);
            if (subTag == null) {
                return false;
            }
            switch (type) {
                case ENERGY -> {
                    return getAttributeMod(subTag, TAG_AUGMENT_RF_CREATIVE) > 0;
                }
                case FLUID -> {
                    return getAttributeMod(subTag, TAG_AUGMENT_FLUID_CREATIVE) > 0;
                }
                case ITEM -> {
                    return getAttributeMod(subTag, TAG_AUGMENT_ITEM_CREATIVE) > 0;
                }
                case EXPERIENCE -> {
                    // Implement the XP creative check using the newly defined augment tag
                    return getAttributeMod(subTag, TAG_AUGMENT_XP_CREATIVE) > 0;
                }
            }
        }
        return false;
    }

    private static CompoundTag getPropertiesTag(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return null;
        }
        // Extract properties from the custom data
        CompoundTag root = customData.copyTag();
        return root.contains(TAG_PROPERTIES) ? root.getCompound(TAG_PROPERTIES) : null;
    }

}
