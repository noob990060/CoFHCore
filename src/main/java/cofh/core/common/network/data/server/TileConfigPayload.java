package cofh.core.common.network.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record TileConfigPayload(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<TileConfigPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "tile_config_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TileConfigPayload> STREAM_CODEC = StreamCodec
            .of(TileConfigPayload::encode, TileConfigPayload::decode);

    private static TileConfigPayload decode(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();

        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);

        return new TileConfigPayload(pos, data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, TileConfigPayload payload) {
        buf.writeBlockPos(payload.pos());

        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
