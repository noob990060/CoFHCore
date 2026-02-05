package cofh.lib.api.item;

import cofh.core.common.item.IMultiModeItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.component.CustomData;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.core.client.CoreKeys.MULTIMODE_DECREMENT;
import static cofh.core.client.CoreKeys.MULTIMODE_INCREMENT;
import static cofh.lib.util.constants.NBTTags.TAG_ACTIVE;
import static cofh.lib.util.helpers.StringHelper.*;
import static net.minecraft.ChatFormatting.RED;
import static net.minecraft.ChatFormatting.YELLOW;

/**
 * Hacky default interface to reduce boilerplate. :)
 */
public interface ICoFHItem extends IItemExtension {

    ICoFHItem setModId(String modId);

    default void addEnergyTooltip(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn, int extract, int receive, boolean creative) {

        if (extract == receive && extract > 0 || creative) {
            tooltip.add(getTextComponent(localize("info.cofh.transfer") + ": " + getScaledNumber(extract) + " RF/t"));
        } else if (extract > 0) {
            if (receive > 0) {
                tooltip.add(getTextComponent(localize("info.cofh.send") + "|" + localize("info.cofh.receive") + ": " + getScaledNumber(extract) + "|" + getScaledNumber(receive) + " RF/t"));
            } else {
                tooltip.add(getTextComponent(localize("info.cofh.send") + ": " + getScaledNumber(extract) + " RF/t"));
            }
        } else if (receive > 0) {
            tooltip.add(getTextComponent(localize("info.cofh.receive") + ": " + getScaledNumber(receive) + " RF/t"));
        }
    }

    default void addModeChangeTooltip(IMultiModeItem item, ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        if (item.getNumModes(stack) <= 2) {
            addIncrementModeChangeTooltip(item, stack, worldIn, tooltip, flagIn);
            return;
        }
        tooltip.add(Component.translatable("info.cofh.mode_change", Component.keybind("key.cofh.mode_change_increment"), Component.keybind("key.cofh.mode_change_decrement")).withStyle(YELLOW));

        if (MULTIMODE_INCREMENT.getKey().getValue() == -1) {
            tooltip.add(Component.translatable("info.cofh.key_not_bound", localize("key.cofh.mode_change_increment")).withStyle(RED));
        }
        if (MULTIMODE_DECREMENT.getKey().getValue() == -1) {
            tooltip.add(Component.translatable("info.cofh.key_not_bound", localize("key.cofh.mode_change_decrement")).withStyle(RED));
        }
    }

    default void addIncrementModeChangeTooltip(IMultiModeItem item, ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        tooltip.add(Component.translatable("info.cofh.mode_toggle", Component.keybind("key.cofh.mode_change_increment")).withStyle(YELLOW));

        if (MULTIMODE_INCREMENT.getKey().getValue() == -1) {
            tooltip.add(Component.translatable("info.cofh.key_not_bound", localize("key.cofh.mode_change_increment")).withStyle(RED));
        }
    }

    default boolean isActive(ItemStack stack) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().getBoolean(TAG_ACTIVE);
    }

    default void setActive(ItemStack stack, boolean state) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null || customData.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
            customData = stack.get(DataComponents.CUSTOM_DATA);
        }
        var tag = customData.copyTag();
        tag.putBoolean(TAG_ACTIVE, state);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    default boolean hasActiveTag(ItemStack stack) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.copyTag().contains(TAG_ACTIVE);
    }

    default void setActive(ItemStack stack, LivingEntity entity) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null || customData.isEmpty()) {
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(new CompoundTag()));
            customData = stack.get(DataComponents.CUSTOM_DATA);
        }
        var tag = customData.copyTag();
        tag.putLong(TAG_ACTIVE, entity.level().getGameTime() + 20);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

}
