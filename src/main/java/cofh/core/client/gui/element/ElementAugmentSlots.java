package cofh.core.client.gui.element;

import cofh.core.client.gui.IGuiAccess;
import cofh.core.util.helpers.GuiHelper;
import cofh.lib.common.inventory.SlotCoFH;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.client.gui.GuiGraphics;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static cofh.core.util.helpers.GuiHelper.SLOT_SIZE;
import static cofh.lib.util.Constants.MAX_AUGMENTS;
import static cofh.lib.util.Constants.TRUE;

public class ElementAugmentSlots extends ElementBase {

    private final IntSupplier numSlots;
    private final List<SlotCoFH> augmentSlots;
    private final List<ElementSlot> slots = new ArrayList<>(MAX_AUGMENTS);

    public ElementAugmentSlots(IGuiAccess gui, int posX, int posY, @Nonnull IntSupplier numSlots, @Nonnull List<SlotCoFH> augmentSlots) {

        this(gui, posX, posY, numSlots, augmentSlots, null, TRUE);
    }

    public ElementAugmentSlots(IGuiAccess gui, int posX, int posY, @Nonnull IntSupplier numSlots, @Nonnull List<SlotCoFH> augmentSlots, String texture, Supplier<Boolean> drawUnderlay) {

        super(gui, posX, posY);

        this.numSlots = numSlots;
        this.augmentSlots = augmentSlots;

        for (int i = 0; i < augmentSlots.size(); ++i) {
            int slotIndex = i;
            this.augmentSlots.get(i).setEnabled(() -> slotIndex < this.numSlots.getAsInt() && this.visible());
        }
        for (int i = 0; i < MAX_AUGMENTS; ++i) {
            int slotIndex = i;
            ElementSlot slot = GuiHelper.createSlot(gui, 18 * i, 0);
            slot.setVisible(() -> slotIndex < this.numSlots.getAsInt());
            if (texture != null && drawUnderlay != null) {
                slot.setUnderlayTexture(texture, drawUnderlay);
            }
            slots.add(slot);
        }
    }

    @Override
    public void drawBackground(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {

        for (ElementBase slot : slots) {
            if (slot.visible()) {
                slot.drawBackground(pGuiGraphics, mouseX, mouseY);
            }
        }
    }

    @Override
    public void drawForeground(GuiGraphics pGuiGraphics, int mouseX, int mouseY) {

        for (ElementBase slot : slots) {
            if (slot.visible()) {
                slot.drawForeground(pGuiGraphics, mouseX, mouseY);
            }
        }
    }

    @Override
    public void update(int mouseX, int mouseY) {

        int activeSlots = MathHelper.clamp(numSlots.getAsInt(), 0, MAX_AUGMENTS);

        int absX = posX() + offsetX();
        int absY = posY() + offsetY();
        int[] slotX = new int[activeSlots];
        int[] slotY = new int[activeSlots];

        switch (activeSlots) {
            case 1:
                setSlotPosition(0, absX + SLOT_SIZE, absY + SLOT_SIZE, slotX, slotY);
                break;
            case 2:
                for (int i = 0; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i % 2), absY + SLOT_SIZE, slotX, slotY);
                }
                break;
            case 3:
                for (int i = 0; i < 2; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i % 2), absY + 9, slotX, slotY);
                }
                setSlotPosition(2, absX + SLOT_SIZE, absY + 9 + SLOT_SIZE, slotX, slotY);
                break;
            case 4:
                for (int i = 0; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i % 2), absY + 9 + SLOT_SIZE * (i / 2), slotX, slotY);
                }
                break;
            case 5:
                for (int i = 0; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * (i % 3) + 9 * (i / 3), absY + 9 + SLOT_SIZE * (i / 3), slotX, slotY);
                }
                break;
            case 6:
                for (int i = 0; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * (i % 3), absY + 9 + SLOT_SIZE * (i / 3), slotX, slotY);
                }
                break;
            case 7:
                for (int i = 0; i < 2; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i), absY, slotX, slotY);
                }
                for (int i = 2; i < 5; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * (i - 2), absY + SLOT_SIZE, slotX, slotY);
                }
                for (int i = 5; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i - 5), absY + SLOT_SIZE * 2, slotX, slotY);
                }
                break;
            case 8:
                for (int i = 0; i < 3; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * i, absY, slotX, slotY);
                }
                for (int i = 3; i < 5; ++i) {
                    setSlotPosition(i, absX + 9 + SLOT_SIZE * (i - 3), absY + SLOT_SIZE, slotX, slotY);
                }
                for (int i = 5; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * (i - 5), absY + SLOT_SIZE * 2, slotX, slotY);
                }
                break;
            case 9:
                for (int i = 0; i < activeSlots; ++i) {
                    setSlotPosition(i, absX + SLOT_SIZE * (i % 3), absY + SLOT_SIZE * (i / 3), slotX, slotY);
                }
            default:
        }
        for (int i = 0; i < activeSlots; ++i) {
            slots.get(i).setPosition(slotX[i] - 1 - offsetX(), slotY[i] - 1 - offsetY());
        }
    }

    private void setSlotPosition(int index, int x, int y, int[] slotX, int[] slotY) {

        augmentSlots.get(index).setPosition(x, y);
        slotX[index] = x;
        slotY[index] = y;
    }

}
