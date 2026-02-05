package cofh.core.common.network.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record SideConfigPayload(BlockPos pos, byte[] sides) implements CustomPacketPayload {

    public static final Type<SideConfigPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "side_config_packet"));

    // Exactly 6 bytes (one per face)
    public static final StreamCodec<RegistryFriendlyByteBuf, SideConfigPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SideConfigPayload::pos,
            ByteBufCodecs.byteArray(6), SideConfigPayload::sides,
            SideConfigPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
