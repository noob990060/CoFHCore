package cofh.core.common.network.data.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ContainerConfigPayload(byte[] data) implements CustomPacketPayload {

    public static final Type<ContainerConfigPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "container_config_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ContainerConfigPayload> STREAM_CODEC = StreamCodec
            .of(ContainerConfigPayload::encode, ContainerConfigPayload::decode);

    private static ContainerConfigPayload decode(RegistryFriendlyByteBuf buf) {
        int len = buf.readVarInt();
        byte[] data = new byte[len];
        buf.readBytes(data);
        return new ContainerConfigPayload(data);
    }

    private static void encode(RegistryFriendlyByteBuf buf, ContainerConfigPayload payload) {
        buf.writeVarInt(payload.data.length);
        buf.writeBytes(payload.data);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
