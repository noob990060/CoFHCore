package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.SideConfigPayload;
import cofh.core.util.control.IReconfigurableTile;
import cofh.lib.api.control.IReconfigurable.SideConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static cofh.lib.api.control.IReconfigurable.SideConfig.SIDE_NONE;

public class SideConfigPacket {

    public static final SideConfigPacket INSTANCE = new SideConfigPacket();

    public static SideConfigPacket get() {
        return INSTANCE;
    }

    public void handle(final SideConfigPayload payload, final IPayloadContext context) {
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
            if (!(be instanceof IReconfigurableTile tile)) {
                return;
            }

            byte[] bSides = payload.sides();
            SideConfig[] sides = { SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE, SIDE_NONE };

            if (bSides.length == 6) {
                for (int i = 0; i < 6; i++) {
                    int idx = bSides[i] & 0xFF; // avoid negative byte indexing
                    if (idx >= SideConfig.VALUES.length) {
                        idx = 0;
                    }
                    sides[i] = SideConfig.VALUES[idx];
                }
            }

            tile.reconfigControl().setSideConfig(sides);
        });
    }

    public static void sendToServer(IReconfigurableTile tile) {
        if (tile == null) {
            return;
        }

        byte[] bSides = new byte[6];
        SideConfig[] current = tile.reconfigControl().getSideConfig();
        for (int i = 0; i < 6; i++) {
            bSides[i] = (byte) current[i].ordinal();
        }

        PacketDistributor.sendToServer(new SideConfigPayload(tile.pos(), bSides));
    }
}
