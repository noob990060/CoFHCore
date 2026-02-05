package cofh.core.common.network.data.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ItemLeftClickPayload() implements CustomPacketPayload {

    public static final Type<ItemLeftClickPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "item_left_click_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemLeftClickPayload> STREAM_CODEC = StreamCodec.of(
            (buf, payload) -> {
            },
            buf -> new ItemLeftClickPayload()
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
