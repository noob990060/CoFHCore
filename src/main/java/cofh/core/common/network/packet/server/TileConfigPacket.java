package cofh.core.common.network.packet.server;

import cofh.core.common.network.data.server.TileConfigPayload;
import cofh.lib.api.block.entity.IPacketHandlerTile;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileConfigPacket {

    public static final TileConfigPacket INSTANCE = new TileConfigPacket();

    public static TileConfigPacket get() {

        return INSTANCE;
    }

    public void handle(final TileConfigPayload payload, final IPayloadContext context) {

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
            if (be instanceof IPacketHandlerTile handlerTile) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
                handlerTile.handleConfigPacket(buf);
            }
        });
    }

    public static void sendToServer(IPacketHandlerTile tile) {

        if (tile == null) {
            return;
        }

        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf filled = tile.getConfigPacket(tmp);

        byte[] data = new byte[filled.readableBytes()];
        filled.getBytes(filled.readerIndex(), data);

        PacketDistributor.sendToServer(new TileConfigPayload(tile.pos(), data));
    }

}
