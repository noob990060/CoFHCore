package cofh.core.common.network.packet.client;

import cofh.core.common.network.data.client.TileGuiPayload;
import cofh.core.util.ProxyUtils;
import cofh.lib.api.block.entity.IPacketHandlerTile;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class TileGuiPacket {

    public static final TileGuiPacket INSTANCE = new TileGuiPacket();

    public static TileGuiPacket get() {
        return INSTANCE;
    }

    public void handle(final TileGuiPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = ProxyUtils.getClientWorld();
            BlockPos pos = payload.pos();
            BlockEntity tile = level.getBlockEntity(pos);
            
            if (tile instanceof IPacketHandlerTile handlerTile) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data()));
                handlerTile.handleGuiPacket(buf);
            }
        });
    }

    public static void sendToClient(IPacketHandlerTile tile, Player player) {
        if (tile == null || tile.world() == null || tile.world().isClientSide) {
            return;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            FriendlyByteBuf tmp = new FriendlyByteBuf(Unpooled.buffer());
            FriendlyByteBuf filled = tile.getGuiPacket(tmp);

            byte[] data = new byte[filled.readableBytes()];
            filled.getBytes(filled.readerIndex(), data);

            PacketDistributor.sendToPlayer(serverPlayer, new TileGuiPayload(tile.pos(), data));
        }
    }
}
