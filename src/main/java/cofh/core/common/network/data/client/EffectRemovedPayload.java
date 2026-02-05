package cofh.core.common.network.data.client;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record EffectRemovedPayload(int entityId, ResourceLocation effect) implements CustomPacketPayload {

    public static final Type<EffectRemovedPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "effect_removed_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, EffectRemovedPayload> STREAM_CODEC = StreamCodec
            .of(EffectRemovedPayload::encode, EffectRemovedPayload::decode);

    private static EffectRemovedPayload decode(RegistryFriendlyByteBuf buf) {
        return new EffectRemovedPayload(buf.readVarInt(), buf.readResourceLocation());
    }

    private static void encode(RegistryFriendlyByteBuf buf, EffectRemovedPayload payload) {
        buf.writeVarInt(payload.entityId);
        buf.writeResourceLocation(payload.effect);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
