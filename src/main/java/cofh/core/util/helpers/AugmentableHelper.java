package cofh.core.util.helpers;

import cofh.core.common.item.IAugmentableItem;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cofh.lib.util.Constants.MAX_AUGMENTS;
import static cofh.lib.util.constants.NBTTags.*;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public final class AugmentableHelper {

    private AugmentableHelper() {

    }

    public static boolean isAugmentableItem(ItemStack stack) {

        return !stack.isEmpty() && stack.getItem() instanceof IAugmentableItem;
    }

    public static List<ItemStack> readAugmentsFromItem(ItemStack stack) {

        ListTag augmentTag = getAugmentNBT(stack);
        if (augmentTag.isEmpty()) {
            return Collections.emptyList();
        }
        return getAugments(augmentTag);
    }

    public static void writeAugmentsToItem(ItemStack stack, List<ItemStack> augments) {

        writeAugmentsToItem(stack, convertAugments(augments));
    }

    // region AUGMENTABLE REDIRECTS
    public static List<ItemStack> getAugments(ItemStack augmentable) {

        return !isAugmentableItem(augmentable) ? Collections.emptyList() : ((IAugmentableItem) augmentable.getItem()).getAugments(augmentable);
    }

    public static int getAugmentSlots(ItemStack augmentable) {

        return !isAugmentableItem(augmentable) ? 0 : MathHelper.clamp(((IAugmentableItem) augmentable.getItem()).getAugmentSlots(augmentable), 0, MAX_AUGMENTS);
    }

    public static boolean validAugment(ItemStack augmentable, ItemStack augment, List<ItemStack> augments) {

        return isAugmentableItem(augmentable) && ((IAugmentableItem) augmentable.getItem()).validAugment(augmentable, augment, augments);
    }

    public static void setAugments(ItemStack stack, List<ItemStack> augments) {

        if (!isAugmentableItem(stack)) {
            return;
        }
        ((IAugmentableItem) stack.getItem()).setAugments(stack, augments);
    }
    // endregion

    // region ATTRIBUTES
    public static void setAttribute(CompoundTag subTag, String attribute, float value) {

        subTag.putFloat(attribute, value);
    }

    public static void setAttributeFromAugmentMax(CompoundTag subTag, CompoundTag augmentData, String attribute) {

        float mod = Math.max(getAttributeMod(augmentData, attribute), getAttributeMod(subTag, attribute));
        if (mod > 0.0F) {
            subTag.putFloat(attribute, mod);
        }
    }

    public static void setAttributeFromAugmentAdd(CompoundTag subTag, CompoundTag augmentData, String attribute) {

        float mod = getAttributeMod(augmentData, attribute) + getAttributeMod(subTag, attribute);
        subTag.putFloat(attribute, mod);
    }

    public static void setAttributeFromAugmentString(CompoundTag subTag, CompoundTag augmentData, String attribute) {

        String mod = getAttributeModString(augmentData, attribute);
        if (!mod.isEmpty()) {
            subTag.putString(attribute, mod);
        }
    }

    public static float getAttributeMod(CompoundTag augmentData, String key) {

        return augmentData.getFloat(key);
    }

    public static String getAttributeModString(CompoundTag augmentData, String key) {

        return augmentData.getString(key);
    }

    public static float getAttributeModWithDefault(CompoundTag augmentData, String key, float defaultValue) {

        return augmentData.contains(key) ? augmentData.getFloat(key) : defaultValue;
    }

    public static String getAttributeModWithDefault(CompoundTag augmentData, String key, String defaultValue) {

        return augmentData.contains(key) ? augmentData.getString(key) : defaultValue;
    }

    public static float getPropertyWithDefault(ItemStack container, String key, float defaultValue) {

        CustomData customData = container.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return defaultValue;
        }
        CompoundTag root = customData.copyTag();
        CompoundTag subTag = root.contains(TAG_PROPERTIES) ? root.getCompound(TAG_PROPERTIES) : null;
        return subTag == null ? defaultValue : getAttributeModWithDefault(subTag, key, defaultValue);
    }

    public static String getPropertyWithDefault(ItemStack container, String key, String defaultValue) {

        CustomData customData = container.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return defaultValue;
        }
        CompoundTag root = customData.copyTag();
        CompoundTag subTag = root.contains(TAG_PROPERTIES) ? root.getCompound(TAG_PROPERTIES) : null;
        return subTag == null ? defaultValue : getAttributeModWithDefault(subTag, key, defaultValue);
    }

    // endregion

    // region INTERNAL HELPERS
    private static void writeAugmentsToItem(ItemStack stack, ListTag list) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        CompoundTag root;
        if (customData == null) {
            root = new CompoundTag();
        } else {
            root = customData.copyTag();
        }
        
        if (stack.getItem() instanceof BlockItem) {
            CompoundTag nbt = new CompoundTag();
            nbt.put(TAG_AUGMENTS, list);
            root.put(TAG_BLOCK_ENTITY, nbt);
        } else {
            root.put(TAG_AUGMENTS, list);
        }
        
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(root));
    }

    private static List<ItemStack> getAugments(ListTag list) {

        ArrayList<ItemStack> ret = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            try {
                ret.add(ItemStack.parse(null, list.getCompound(i)).orElse(ItemStack.EMPTY));
            } catch (Exception e) {
                ret.add(ItemStack.EMPTY);
            }
        }
        return ret.isEmpty() ? Collections.emptyList() : ret;
    }

    private static ListTag getAugmentNBT(ItemStack stack) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return new ListTag();
        }
        CompoundTag root = customData.copyTag();
        
        // Check for block entity first
        if (root.contains(TAG_BLOCK_ENTITY)) {
            CompoundTag nbt = root.getCompound(TAG_BLOCK_ENTITY);
            return nbt.contains(TAG_AUGMENTS) ? nbt.getList(TAG_AUGMENTS, TAG_COMPOUND) : new ListTag();
        }
        
        // Check for direct augment tag
        return root.contains(TAG_AUGMENTS) ? root.getList(TAG_AUGMENTS, TAG_COMPOUND) : new ListTag();
    }

    private static ListTag convertAugments(List<ItemStack> augments) {

        ListTag list = new ListTag();
        for (ItemStack augment : augments) {
            // Empty slots are intentionally written.
            //if (!augment.isEmpty()) {
            list.add(augment.save(null));
            //}
        }
        return list;
    }
    // endregion
}
