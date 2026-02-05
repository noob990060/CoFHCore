package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.SecurityControlPayload;
import cofh.core.util.control.ISecurableTile;
import cofh.lib.api.control.ISecurable.AccessMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SecurityControlPacket {

    public static final SecurityControlPacket INSTANCE = new SecurityControlPacket();

    public static SecurityControlPacket get() {
        return INSTANCE;
    }

    public void handle(final SecurityControlPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }

            Level level = player.level();
            if (!level.isLoaded(payload.pos())) {
                return;
            }

            BlockEntity be = level.getBlockEntity(payload.pos());
            if (!(be instanceof ISecurableTile tile)) {
                return;
            }

            int idx = payload.mode() & 0xFF; // avoid negative byte indexing
            if (idx < 0 || idx >= AccessMode.VALUES.length) {
                return;
            }

            tile.setAccess(AccessMode.VALUES[idx]);
        });
    }

    public static void sendToServer(ISecurableTile tile) {
        if (tile == null) {
            return;
        }

        PacketDistributor.sendToServer(new SecurityControlPayload(
                tile.pos(),
                (byte) tile.securityControl().getAccess().ordinal()));
    }
}
