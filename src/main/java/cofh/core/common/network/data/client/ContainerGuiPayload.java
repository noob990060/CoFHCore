package cofh.core.common.network.data.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ContainerGuiPayload(byte[] data) implements CustomPacketPayload {

    public static final Type<ContainerGuiPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "container_gui_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerGuiPayload> STREAM_CODEC = StreamCodec
            .of(ContainerGuiPayload::encode, ContainerGuiPayload::decode);

    private static ContainerGuiPayload decode(RegistryFriendlyByteBuf buf) {
        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);
        return new ContainerGuiPayload(data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, ContainerGuiPayload payload) {
        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
