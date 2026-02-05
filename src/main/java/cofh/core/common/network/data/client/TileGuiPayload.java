package cofh.core.common.network.data.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record TileGuiPayload(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<TileGuiPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "tile_gui_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TileGuiPayload> STREAM_CODEC = StreamCodec
            .of(TileGuiPayload::encode, TileGuiPayload::decode);

    private static TileGuiPayload decode(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();

        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);

        return new TileGuiPayload(pos, data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, TileGuiPayload payload) {
        buf.writeBlockPos(payload.pos);

        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
