package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.SecurityPayload;
import cofh.lib.api.control.ISecurable;
import cofh.lib.api.control.ISecurable.AccessMode;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class SecurityPacket {

    public static final SecurityPacket INSTANCE = new SecurityPacket();

    public static SecurityPacket get() {
        return INSTANCE;
    }

    public void handle(final SecurityPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }

            if (player.containerMenu instanceof ISecurable securable) {
                int idx = payload.mode() & 0xFF; // avoid negative byte indexing
                if (idx < 0 || idx >= AccessMode.VALUES.length) {
                    return;
                }
                securable.setAccess(AccessMode.VALUES[idx]);
            }
        });
    }

    public static void sendToServer(AccessMode accessMode) {
        PacketDistributor.sendToServer(new SecurityPayload((byte) accessMode.ordinal()));
    }
}
