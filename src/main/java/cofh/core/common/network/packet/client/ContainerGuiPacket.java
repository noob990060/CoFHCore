package cofh.core.common.network.packet.client;

import cofh.core.common.inventory.ContainerMenuCoFH;
import cofh.core.common.network.data.client.ContainerGuiPayload;
import cofh.core.util.ProxyUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ContainerGuiPacket {

    public static final ContainerGuiPacket INSTANCE = new ContainerGuiPacket();

    public static ContainerGuiPacket get() {

        return INSTANCE;
    }

    public void handle(final ContainerGuiPayload payload, final IPayloadContext context) {

        context.enqueueWork(() -> {
            Player player = ProxyUtils.getClientPlayer();
            if (player.containerMenu instanceof ContainerMenuCoFH container) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
                container.handleGuiPacket(buf);
            }
        });
    }

    public static void sendToClient(ContainerMenuCoFH container, Player player) {

        if (container == null) {
            return;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf filled = container.getGuiPacket(tmp);

            byte[] data = new byte[filled.readableBytes()];
            filled.getBytes(filled.readerIndex(), data);

            PacketDistributor.sendToPlayer(serverPlayer, new ContainerGuiPayload(data));
        }
    }

}
