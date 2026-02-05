package cofh.core.common.network.packet.client;

import cofh.core.common.network.data.client.TileRenderPayload;
import cofh.core.util.ProxyUtils;
import cofh.lib.api.block.entity.IPacketHandlerTile;
import cofh.lib.util.Utils;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileRenderPacket {

    public static final TileRenderPacket INSTANCE = new TileRenderPacket();

    public static TileRenderPacket get() {
        return INSTANCE;
    }

    public void handle(final TileRenderPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = ProxyUtils.getClientWorld();
            BlockPos pos = payload.pos();
            BlockEntity tile = level.getBlockEntity(pos);
            
            if (tile instanceof IPacketHandlerTile handlerTile) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
                handlerTile.handleRenderPacket(buf);
            }
        });
    }

    public static void sendToClient(IPacketHandlerTile tile) {
        if (tile == null || tile.world() == null || tile.world().isClientSide) {
            return;
        }
        FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());
        FriendlyByteBuf filled = tile.getRenderPacket(tmp);

        byte[] data = new byte[filled.readableBytes()];
        filled.getBytes(filled.readerIndex(), data);

        if (tile.world() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
            // Send to all players tracking the chunk
            serverLevel.getServer().getPlayerList().getPlayers().forEach(player -> {
                if (player.level() == serverLevel && player.distanceToSqr(tile.pos().getCenter()) <= 64.0 * 64.0) {
                    PacketDistributor.sendToPlayer(player, new TileRenderPayload(tile.pos(), data));
                }
            });
        }
    }
}
