package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.RedstoneControlPayload;
import cofh.core.util.control.IRedstoneControllableTile;
import cofh.lib.api.control.IRedstoneControllable.ControlMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class RedstoneControlPacket {

    public static final RedstoneControlPacket INSTANCE = new RedstoneControlPacket();

    public static RedstoneControlPacket get() {
        return INSTANCE;
    }

    public void handle(final RedstoneControlPayload payload, final IPayloadContext context) {
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
            if (!(be instanceof IRedstoneControllableTile tile)) {
                return;
            }

            int idx = payload.mode() & 0xFF; // avoid negative byte indexing
            if (idx < 0 || idx >= ControlMode.VALUES.length) {
                return;
            }

            tile.setControl(payload.threshold(), ControlMode.VALUES[idx]);
        });
    }

    public static void sendToServer(IRedstoneControllableTile tile) {
        if (tile == null) {
            return;
        }

        PacketDistributor.sendToServer(new RedstoneControlPayload(
                tile.pos(),
                tile.redstoneControl().getThreshold(),
                (byte) tile.redstoneControl().getMode().ordinal()));
    }
}
