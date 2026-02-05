package cofh.lib.api.item;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.function.UnaryOperator;

import static cofh.lib.util.constants.NBTTags.TAG_XP;

public interface IXpContainerItem extends IContainerItem {

    int getCapacityXp(ItemStack stack);

    default boolean canStoreXp(ItemStack stack) {

        return true;
    }

    default int getStoredXp(ItemStack stack) {

        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return 0;
        }
        CompoundTag tag = customData.copyTag();
        return tag.getInt(TAG_XP);
    }

    default int getSpaceXp(ItemStack stack) {

        return getCapacityXp(stack) - getStoredXp(stack);
    }

    default int modifyXp(ItemStack stack, int xp) {

        int totalXP = getStoredXp(stack) + xp;

        if (totalXP > getCapacityXp(stack)) {
            totalXP = getCapacityXp(stack);
        } else if (totalXP < 0) {
            totalXP = 0;
        }
        
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            customData = new CustomData();
        }
        CompoundTag tag = new CompoundTag();
        customData.copyTag(tag);
        tag.putInt(TAG_XP, totalXP);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        
        return totalXP;
    }

    static boolean storeXpOrb(Player player, ExperienceOrb orb, ItemStack stack) {

        IXpContainerItem item = (IXpContainerItem) stack.getItem();

        if (!item.canStoreXp(stack)) {
            return false;
        }
        int toAdd = Math.min(item.getSpaceXp(stack), orb.value);

        if (toAdd > 0) {
            stack.setPopTime(5);
            player.level.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.1F, (MathHelper.RANDOM.nextFloat() - MathHelper.RANDOM.nextFloat()) * 0.35F + 0.9F);
            item.modifyXp(stack, toAdd);
            orb.value -= toAdd;
            return true;
        }
        return false;
    }

}

