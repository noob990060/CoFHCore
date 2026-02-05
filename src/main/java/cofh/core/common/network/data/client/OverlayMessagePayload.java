package cofh.core.common.network.data.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record OverlayMessagePayload(String message) implements CustomPacketPayload {

    public static final Type<OverlayMessagePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "overlay_message_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OverlayMessagePayload> STREAM_CODEC = StreamCodec
            .of(OverlayMessagePayload::encode, OverlayMessagePayload::decode);

    private static OverlayMessagePayload decode(RegistryFriendlyByteBuf buf) {
        return new OverlayMessagePayload(buf.readUtf());
    }

    private static void encode(RegistryFriendlyByteBuf buf, OverlayMessagePayload payload) {
        buf.writeUtf(payload.message);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
