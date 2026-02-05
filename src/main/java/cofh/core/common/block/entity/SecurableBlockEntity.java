package cofh.core.common.block.entity;

import cofh.core.util.control.ISecurableTile;
import cofh.core.util.control.SecurityControlModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static cofh.lib.util.constants.NBTTags.TAG_BLOCK_ENTITY;

public class SecurableBlockEntity extends BlockEntityCoFH implements ISecurableTile {

    protected SecurityControlModule securityControl = new SecurityControlModule(this);

    public SecurableBlockEntity(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {

        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public ItemStack createItemStackTag(ItemStack stack) {

        // TODO: Fix ItemStack tag system for NeoForge 1.21.1
        // The ItemStack tag API has changed significantly in NeoForge 1.21.1
        // For now, skip tag functionality to avoid compilation errors
        return super.createItemStackTag(stack);
    }

    // region NBT
    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {

        super.loadAdditional(nbt, provider);

        securityControl.read(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider) {

        super.saveAdditional(nbt, provider);

        securityControl.write(nbt);
    }
    // endregion

    // region NETWORK

    // CONTROL
    @Override
    public FriendlyByteBuf getControlPacket(FriendlyByteBuf buffer) {

        super.getControlPacket(buffer);

        securityControl.writeToBuffer(buffer);

        return buffer;
    }

    @Override
    public void handleControlPacket(FriendlyByteBuf buffer) {

        super.handleControlPacket(buffer);

        securityControl.readFromBuffer(buffer);
    }
    // endregion

    // region MODULES
    @Override
    public SecurityControlModule securityControl() {

        return securityControl;
    }
    // endregion
}
