package cofh.core.common.block.entity;

import cofh.core.common.network.packet.client.TileGuiPacket;
import cofh.core.util.ProxyUtils;
import cofh.core.util.helpers.FluidHelper;
import cofh.lib.api.IConveyableData;
import cofh.lib.api.block.entity.IAreaEffectTile;
import cofh.lib.api.block.entity.IPacketHandlerTile;
import cofh.lib.api.block.entity.ITileCallback;
import cofh.lib.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nullable;

public class BlockEntityCoFH extends BlockEntity implements ITileCallback, IPacketHandlerTile, ITileXpHandler, IConveyableData {

    public BlockEntityCoFH(BlockEntityType<?> tileEntityTypeIn, BlockPos pos, BlockState state) {

        super(tileEntityTypeIn, pos, state);
    }

    @Override
    public void onLoad() {

        super.onLoad();

        if (level != null && Utils.isClientWorld(level)) {
            if (this instanceof IAreaEffectTile) {
                ProxyUtils.addAreaEffectTile((IAreaEffectTile) this);
            }
        }
        clearRemoved();
    }

    @Override
    public void setRemoved() {

        if (this instanceof IAreaEffectTile) {
            ProxyUtils.removeAreaEffectTile((IAreaEffectTile) this);
        }
        super.setRemoved();
    }

    public void markChunkUnsaved() {

        if (this.level != null) {
            if (this.level.isLoaded(this.worldPosition)) {
                this.level.getChunkAt(this.worldPosition).setUnsaved(true);
            }
        }
    }

    public void addPlayerUsing() {

    }

    public void removePlayerUsing() {

    }

    public void receiveGuiNetworkData(int id, int data) {

    }

    public void sendGuiNetworkData(AbstractContainerMenu container, Player player) {

        if (hasGuiPacket() && player instanceof ServerPlayer && (!(player instanceof FakePlayer))) {
            TileGuiPacket.sendToClient(this, player);
        }
    }

    // region HELPERS
    public boolean onActivatedDelegate(Level world, BlockPos pos, BlockState state, Player player, InteractionHand hand, BlockHitResult result) {

        System.out.println("DEBUG: onActivatedDelegate called");
        IFluidHandler handler = world.getCapability(Capabilities.FluidHandler.BLOCK, pos, state, this, result.getDirection());
        System.out.println("DEBUG: Fluid handler: " + (handler != null ? "found" : "null"));
        if (handler != null) {
            // Debug fluid handler state
            System.out.println("DEBUG: Fluid handler tanks: " + handler.getTanks());
            for (int i = 0; i < handler.getTanks(); i++) {
                FluidStack fluid = handler.getFluidInTank(i);
                System.out.println("DEBUG: Tank " + i + ": " + fluid + " / " + handler.getTankCapacity(i));
            }
            System.out.println("DEBUG: Handler can fill 1000mb lava: " + (handler.fill(new FluidStack(net.minecraft.world.level.material.Fluids.LAVA, 1000), FluidAction.SIMULATE) > 0));
            
            ItemStack itemInHand = player.getItemInHand(hand);
            System.out.println("DEBUG: Item in hand: " + itemInHand);
            boolean interactResult = FluidHelper.interactWithHandler(itemInHand, handler, player, hand);
            System.out.println("DEBUG: FluidHelper.interactWithHandler result: " + interactResult);
            return interactResult;
        }
        return false;
    }

    public boolean hasGuiPacket() {

        return true;
    }

    protected Object getSound() {

        return null;
    }

    protected void markDirtyFast() {

        if (this.level != null) {
            this.level.blockEntityChanged(this.worldPosition);
        }
    }
    // endregion

    // region GUI
    public boolean playerWithinDistance(Player player, double distanceSq) {

        return !isRemoved() && worldPosition.distToCenterSqr(player.position()) <= distanceSq;
    }
    // endregion

    // region NETWORK
    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {

        // Prevent crash when provider is null
        if (provider == null) {
            return new CompoundTag();
        }

        return saveWithoutMetadata(provider);
    }
    // endregion

    // region ITileCallback
    @Override
    public Block block() {

        return getBlockState().getBlock();
    }

    @Override
    public BlockState state() {

        return getBlockState();
    }

    @Override
    public BlockPos pos() {

        return worldPosition;
    }

    @Override
    public Level world() {

        return level;
    }
    // endregion
}
