package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.StorageClearPayload;
import cofh.lib.api.block.entity.ITileCallback;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class StorageClearPacket {

    public static final StorageClearPacket INSTANCE = new StorageClearPacket();

    public static StorageClearPacket get() {
        return INSTANCE;
    }

    public void handle(final StorageClearPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }

            Level world = player.level();
            if (!world.isLoaded(payload.pos())) {
                return;
            }
            BlockEntity tile = world.getBlockEntity(payload.pos());
            if (tile instanceof ITileCallback callback) {
                // Use payload.storageType directly and ensure it's valid
                int storageTypeIndex = payload.storageType();
                if (storageTypeIndex < 0 || storageTypeIndex >= StorageType.VALUES.length) {
                    return; // Invalid storage type index
                }
                StorageType storageType = StorageType.VALUES[storageTypeIndex];

                switch (storageType) {
                    case ENERGY -> callback.clearEnergy(payload.index());
                    case FLUID -> callback.clearTank(payload.index());
                    case ITEM -> callback.clearSlot(payload.index());
                }
            }
        });
    }

    public static boolean sendToServer(ITileCallback tile, StorageType storageType, int storageIndex) {
        if (tile == null) {
            return false;
        }
        // Send the packet to the server with the relevant information
        PacketDistributor.sendToServer(new StorageClearPayload(tile.pos(), storageType.ordinal(), storageIndex));
        return true;
    }

    // STORAGE TYPE ENUM
    public enum StorageType {
        ENERGY, FLUID, ITEM;

        public static final StorageType[] VALUES = values();
    }
}
