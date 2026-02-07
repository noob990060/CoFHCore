package cofh.lib.common.inventory;

import cofh.lib.common.inventory.wrapper.InvWrapperCoFH;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static cofh.lib.util.Constants.TRUE;

public class SlotCoFH extends Slot {

    private static final Field SLOT_X;
    private static final Field SLOT_Y;

    static {
        try {
            SLOT_X = Slot.class.getDeclaredField("x");
            SLOT_X.setAccessible(true);
            SLOT_Y = Slot.class.getDeclaredField("y");
            SLOT_Y.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    protected IntSupplier slotStackLimit;
    protected Supplier<Boolean> enabled = TRUE;

    public SlotCoFH(InvWrapperCoFH inventoryIn, int index, int xPosition, int yPosition) {

        super(inventoryIn, index, xPosition, yPosition);
        slotStackLimit = () -> inventoryIn.getSlotLimit(index);
    }

    public SlotCoFH(Container inventoryIn, int index, int xPosition, int yPosition) {

        super(inventoryIn, index, xPosition, yPosition);
        slotStackLimit = () -> inventoryIn.getMaxStackSize();
    }

    public SlotCoFH(Container inventoryIn, int index, int xPosition, int yPosition, int slotStackLimit) {

        super(inventoryIn, index, xPosition, yPosition);
        this.slotStackLimit = () -> slotStackLimit;
    }

    public SlotCoFH(Container inventoryIn, int index, int xPosition, int yPosition, IntSupplier slotStackLimit) {

        super(inventoryIn, index, xPosition, yPosition);
        this.slotStackLimit = slotStackLimit;
    }

    public SlotCoFH setEnabled(Supplier<Boolean> enabled) {

        this.enabled = enabled;
        return this;
    }

    public void setPosition(int xPosition, int yPosition) {

        try {
            SLOT_X.setInt(this, xPosition);
            SLOT_Y.setInt(this, yPosition);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Failed to update slot position.", ex);
        }
    }

    @Override
    public int getMaxStackSize() {

        return slotStackLimit.getAsInt();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {

        return container.canPlaceItem(getSlotIndex(), stack);
    }

    @Override
    public boolean isActive() {

        return enabled.get();
    }

}
