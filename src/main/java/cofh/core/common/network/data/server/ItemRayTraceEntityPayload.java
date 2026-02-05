package cofh.core.common.network.data.server;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.Vec3;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ItemRayTraceEntityPayload(
        InteractionHand hand,
        Vec3 origin,
        int targetId,
        Vec3 offset,
        float power) implements CustomPacketPayload {

    public static final Type<ItemRayTraceEntityPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "item_ray_trace_entity_packet"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3_CODEC = StreamCodec.of(
            (buf, v) -> {
                buf.writeDouble(v.x);
                buf.writeDouble(v.y);
                buf.writeDouble(v.z);
            },
            buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemRayTraceEntityPayload> STREAM_CODEC = StreamCodec
            .composite(
                    ByteBufCodecs.VAR_INT, p -> p.hand().ordinal(),
                    VEC3_CODEC, ItemRayTraceEntityPayload::origin,
                    ByteBufCodecs.VAR_INT, ItemRayTraceEntityPayload::targetId,
                    VEC3_CODEC, ItemRayTraceEntityPayload::offset,
                    ByteBufCodecs.FLOAT, ItemRayTraceEntityPayload::power,
                    (handOrd, origin, targetId, offset, power) -> new ItemRayTraceEntityPayload(
                            InteractionHand.values()[handOrd], origin, targetId, offset, power));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
