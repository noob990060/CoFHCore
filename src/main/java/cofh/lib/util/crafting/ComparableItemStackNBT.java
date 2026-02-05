package cofh.lib.util.crafting;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Extension of {@link ComparableItemStack} except NBT sensitive.
 * <p>
 * It is expected that this will have limited use, so this is a child class for overhead performance reasons.
 *
 * @author King Lemming
 */
public class ComparableItemStackNBT extends ComparableItemStack {

    public CompoundTag tag;

    public ComparableItemStackNBT(ItemStack stack) {

        super(stack);

        if (!stack.isEmpty()) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                tag = customData.copyTag();
            }
        }
    }

    @Override
    public boolean isStackEqual(ComparableItemStack other) {

        return super.isStackEqual(other) && isStackTagEqual((ComparableItemStackNBT) other);
    }

    private boolean isStackTagEqual(ComparableItemStackNBT other) {

        return tag == null ? other.tag == null : other.tag != null && tag.equals(other.tag);
    }

    @Override
    public ItemStack toItemStack() {

        ItemStack ret = super.toItemStack();

        if (!ret.isEmpty() && tag != null) {
            ret.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {

        return o instanceof ComparableItemStackNBT && isItemEqual((ComparableItemStack) o) && isStackTagEqual((ComparableItemStackNBT) o);
    }

    @Override
    public int hashCode() {

        return tag != null ? tag.hashCode() * 31 + super.hashCode() : super.hashCode();
    }

}
