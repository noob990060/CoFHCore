package cofh.core.common.network.data.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record PlayerMotionPayload(double motionX, double motionY, double motionZ) implements CustomPacketPayload {

    public static final Type<PlayerMotionPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "player_motion_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerMotionPayload> STREAM_CODEC = StreamCodec
            .of(PlayerMotionPayload::encode, PlayerMotionPayload::decode);

    private static PlayerMotionPayload decode(RegistryFriendlyByteBuf buf) {
        return new PlayerMotionPayload(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    private static void encode(RegistryFriendlyByteBuf buf, PlayerMotionPayload payload) {
        buf.writeDouble(payload.motionX);
        buf.writeDouble(payload.motionY);
        buf.writeDouble(payload.motionZ);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
