package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.ItemLeftClickPayload;
import cofh.core.util.helpers.ItemHelper;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemLeftClickPacket {

    public static final ItemLeftClickPacket INSTANCE = new ItemLeftClickPacket();

    public static ItemLeftClickPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemLeftClickPayload payload, final IPayloadContext context) {

        context.enqueueWork(() -> {
            Player player = context.player();
            if (player == null) {
                return;
            }

            if (!ItemHelper.isPlayerHoldingLeftClickItem(player)) {
                return;
            }
            ItemHelper.onHeldLeftClickItem(player);
        });
    }

    public static void sendToServer() {
        PacketDistributor.sendToServer(new ItemLeftClickPayload());
    }
}
