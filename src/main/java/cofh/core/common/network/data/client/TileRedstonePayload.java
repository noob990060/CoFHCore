package cofh.core.common.network.data.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record TileRedstonePayload(BlockPos pos, byte[] data) implements CustomPacketPayload {

    public static final Type<TileRedstonePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "tile_redstone_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TileRedstonePayload> STREAM_CODEC = StreamCodec
            .of(TileRedstonePayload::encode, TileRedstonePayload::decode);

    private static TileRedstonePayload decode(RegistryFriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();

        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);

        return new TileRedstonePayload(pos, data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, TileRedstonePayload payload) {
        buf.writeBlockPos(payload.pos);

        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
