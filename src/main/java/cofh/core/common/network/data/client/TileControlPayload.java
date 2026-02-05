package cofh.core.common.network.data.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record TileControlPayload(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<TileControlPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "tile_control_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TileControlPayload> STREAM_CODEC = StreamCodec
            .of(TileControlPayload::encode, TileControlPayload::decode);

    private static TileControlPayload decode(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();

        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);

        return new TileControlPayload(pos, data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, TileControlPayload payload) {
        buf.writeBlockPos(payload.pos);

        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
