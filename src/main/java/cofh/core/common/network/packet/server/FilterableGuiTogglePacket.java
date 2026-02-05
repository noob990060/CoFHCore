package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.FilterableGuiTogglePayload;
import cofh.core.util.filter.FilterHolderType;
import cofh.core.util.filter.IFilterable;
import cofh.core.util.filter.IFilterableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static cofh.core.util.filter.FilterHolderType.*;

public class FilterableGuiTogglePacket {

    public static final FilterableGuiTogglePacket INSTANCE = new FilterableGuiTogglePacket();

    public static FilterableGuiTogglePacket get() {
        return INSTANCE;
    }

    public static final byte FILTER_GUI = 0;
    public static final byte GUI = 1;

    public void handle(final FilterableGuiTogglePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player sender = context.player(); // <-- non-Optional
            if (!(sender instanceof ServerPlayer player)) {
                return;
            }

            Level level = player.level(); // <-- method, not field

            FilterHolderType type = FilterHolderType.from(payload.holderType());
            int entityId = payload.entityId();
            BlockPos pos = payload.pos();
            byte mode = payload.mode();

            switch (type) {
                case ITEM -> {
                    ItemStack held = player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (held.getItem() instanceof IFilterableItem filterable) {
                        if (mode == GUI)
                            filterable.openGui(player, held);
                        else if (mode == FILTER_GUI)
                            filterable.openFilterGui(player, held);
                    }
                }
                case ENTITY -> {
                    Entity entity = level.getEntity(entityId);
                    if (entity == null || entity.isRemoved())
                        return;

                    if (entity instanceof IFilterable filterable) {
                        if (mode == GUI)
                            filterable.openGui(player);
                        else if (mode == FILTER_GUI)
                            filterable.openFilterGui(player);
                    }
                }
                case TILE -> {
                    if (!level.isLoaded(pos))
                        return;

                    BlockEntity be = level.getBlockEntity(pos);
                    if (be instanceof IFilterable filterable) {
                        if (mode == GUI)
                            filterable.openGui(player);
                        else if (mode == FILTER_GUI)
                            filterable.openFilterGui(player);
                    }
                }
                case SELF -> {
                    return;
                }
            }
        });
    }

    public static void openGui(IFilterable filterable) {
        if (filterable instanceof BlockEntity tile) {
            openGui(tile);
        } else if (filterable instanceof Entity entity) {
            openGui(entity);
        }
    }

    // region ITEMS
    public static void openFilterGui(ItemStack stack) {
        sendToServer(FILTER_GUI);
    }

    public static void openGui(ItemStack stack) {
        sendToServer(GUI);
    }

    protected static void sendToServer(byte mode) {
        PacketDistributor.sendToServer(new FilterableGuiTogglePayload(ITEM.ordinal(), -1, BlockPos.ZERO, mode));
    }
    // endregion

    // region TILES
    public static void openFilterGui(BlockEntity tile) {
        sendToServer(tile.getBlockPos(), FILTER_GUI);
    }

    public static void openGui(BlockEntity tile) {
        sendToServer(tile.getBlockPos(), GUI);
    }

    protected static void sendToServer(BlockPos pos, byte mode) {
        PacketDistributor.sendToServer(new FilterableGuiTogglePayload(TILE.ordinal(), -1, pos, mode));
    }
    // endregion

    // region ENTITIES
    public static void openFilterGui(Entity entity) {
        sendToServer(entity.getId(), FILTER_GUI);
    }

    public static void openGui(Entity entity) {
        sendToServer(entity.getId(), GUI);
    }

    protected static void sendToServer(int entityId, byte mode) {
        PacketDistributor.sendToServer(new FilterableGuiTogglePayload(ENTITY.ordinal(), entityId, BlockPos.ZERO, mode));
    }
    // endregion
}
