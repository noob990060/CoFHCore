package cofh.core.common.network.data.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record EffectAddedPayload(int entityId, ResourceLocation effect, int duration) implements CustomPacketPayload {

    public static final Type<EffectAddedPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "effect_added_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EffectAddedPayload> STREAM_CODEC = StreamCodec
            .of(EffectAddedPayload::encode, EffectAddedPayload::decode);

    private static EffectAddedPayload decode(RegistryFriendlyByteBuf buf) {
        return new EffectAddedPayload(buf.readVarInt(), buf.readResourceLocation(), buf.readInt());
    }

    private static void encode(RegistryFriendlyByteBuf buf, EffectAddedPayload payload) {
        buf.writeVarInt(payload.entityId);
        buf.writeResourceLocation(payload.effect);
        buf.writeInt(payload.duration);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
