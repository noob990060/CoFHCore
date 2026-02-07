package cofh.lib.api.item;

import cofh.lib.util.helpers.MathHelper;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import static cofh.lib.api.ContainerType.FLUID;
import static cofh.lib.util.constants.NBTTags.TAG_AMOUNT;
import static cofh.lib.util.constants.NBTTags.TAG_FLUID;

/**
 * Implement this interface on Item classes that support external manipulation of their internal fluid storage.
 * <p>
 * NOTE: Use of NBT data on the containing ItemStack is encouraged.
 *
 * @author King Lemming
 */
public interface IFluidContainerItem extends IContainerItem {

    default CompoundTag getOrCreateTankTag(ItemStack container) {

        CustomData customData = container.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            customData = CustomData.EMPTY;
        }
        CompoundTag tag = customData.copyTag();
        container.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return tag;
    }

    default int getSpace(ItemStack container) {

        return getCapacity(container) - getFluidAmount(container);
    }

    default int getScaledFluidStored(ItemStack container, int scale) {

        return MathHelper.round((double) getFluidAmount(container) * scale / getCapacity(container));
    }

    default int getFluidAmount(ItemStack container) {

        return getFluid(container).getAmount();
    }

    default FluidStack getFluid(ItemStack container) {

        CompoundTag tag = getOrCreateTankTag(container);
        if (!tag.contains(TAG_FLUID)) {
            return FluidStack.EMPTY;
        }
        return getFluid(container, null);
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param level     Level for registry access (required for NeoForge 1.21.1+)
     * @return FluidStack representing the fluid in the container, EMPTY if the container is empty.
     */
    default FluidStack getFluid(ItemStack container, Level level) {

        CompoundTag tag = getOrCreateTankTag(container);
        if (!tag.contains(TAG_FLUID)) {
            return FluidStack.EMPTY;
        }
        HolderLookup.Provider lookupProvider = getLookupProvider(level);
        return FluidStack.parseOptional(lookupProvider, tag.getCompound(TAG_FLUID));
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack being queried.
     * @return TRUE if the fluid is valid in this particular container.
     */
    default boolean isFluidValid(ItemStack container, FluidStack resource) {

        return true;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @return Capacity of this fluid container.
     */
    int getCapacity(ItemStack container);

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param action    If SIMULATE, the fill will only be simulated.
     * @return Amount of fluid that was (or would have been, if simulated) filled into the container.
     */
    default int fill(ItemStack container, FluidStack resource, FluidAction action) {

        return fill(container, resource, action, null);
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param resource  FluidStack attempting to fill the container.
     * @param action    If SIMULATE, the fill will only be simulated.
     * @param level     Level for registry access (required for NeoForge 1.21.1+)
     * @return Amount of fluid that was (or would have been, if simulated) filled into the container.
     */
    default int fill(ItemStack container, FluidStack resource, FluidAction action, Level level) {

        CompoundTag containerTag = getOrCreateTankTag(container);
        if (resource.isEmpty() || !isFluidValid(container, resource)) {
            return 0;
        }
        int capacity = getCapacity(container);

        if (isCreative(container, FLUID)) {
            if (action.execute()) {
                CompoundTag fluidTag = new CompoundTag();
                HolderLookup.Provider lookupProvider = getLookupProvider(level);
                resource.save(lookupProvider, fluidTag);
                fluidTag.putInt(TAG_AMOUNT, capacity);
                containerTag.put(TAG_FLUID, fluidTag);
            }
            return resource.getAmount();
        }
        if (action.simulate()) {
            if (!containerTag.contains(TAG_FLUID)) {
                return Math.min(capacity, resource.getAmount());
            }
            FluidStack stack = getFluid(container, null);
            if (stack.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!stack.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity - stack.getAmount(), resource.getAmount());
        }
        if (!containerTag.contains(TAG_FLUID)) {
            CompoundTag newFluidTag = new CompoundTag();
            HolderLookup.Provider lookupProvider = getLookupProvider(level);
            resource.save(lookupProvider, newFluidTag);
            if (capacity < resource.getAmount()) {
                newFluidTag.putInt(TAG_AMOUNT, capacity);
                containerTag.put(TAG_FLUID, newFluidTag);
                return capacity;
            }
            newFluidTag.putInt(TAG_AMOUNT, resource.getAmount());
            containerTag.put(TAG_FLUID, newFluidTag);
            return resource.getAmount();
        }
        CompoundTag fluidTag = containerTag.getCompound(TAG_FLUID);
        FluidStack stack = getFluid(container, null);
        if (stack.isEmpty() || !stack.isFluidEqual(resource)) {
            return 0;
        }
        int filled = capacity - stack.getAmount();
        if (resource.getAmount() < filled) {
            stack.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            stack.setAmount(capacity);
        }
        CompoundTag updatedFluidTag = new CompoundTag();
        HolderLookup.Provider lookupProvider = getLookupProvider(level);
        stack.save(lookupProvider, updatedFluidTag);
        containerTag.put(TAG_FLUID, updatedFluidTag);
        return filled;
    }

    /**
     * @param container ItemStack which is the fluid container.
     * @param maxDrain  Maximum amount of fluid to be removed from the container.
     * @param action    If SIMULATE, the drain will only be simulated.
     * @return Fluidstack holding the amount of fluid that was (or would have been, if simulated) drained from the
     * container.
     */
    default FluidStack drain(ItemStack container, int maxDrain, FluidAction action) {

        CompoundTag containerTag = getOrCreateTankTag(container);
        if (maxDrain <= 0 || !containerTag.contains(TAG_FLUID)) {
            return FluidStack.EMPTY;
        }
        FluidStack stack = getFluid(container, null);
        if (stack.isEmpty()) {
            return FluidStack.EMPTY;
        }
        boolean creative = isCreative(container, FLUID);
        int drained = creative ? maxDrain : Math.min(stack.getAmount(), maxDrain);
        if (action.execute() && !creative) {
            if (maxDrain >= stack.getAmount()) {
                containerTag.remove(TAG_FLUID);
                return stack;
            }
            CompoundTag fluidTag = containerTag.getCompound(TAG_FLUID);
            fluidTag.putInt(TAG_AMOUNT, fluidTag.getInt(TAG_AMOUNT) - drained);
            containerTag.put(TAG_FLUID, fluidTag);
        }
        stack.setAmount(drained);
        return stack;
    }

    private static HolderLookup.Provider getLookupProvider(Level level) {

        return level != null
                ? level.registryAccess()
                : RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY);
    }

}
