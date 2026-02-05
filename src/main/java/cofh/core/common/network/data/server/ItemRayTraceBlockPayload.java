package cofh.core.common.network.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ItemRayTraceBlockPayload(InteractionHand hand, Vec3 origin, BlockHitResult result)
        implements CustomPacketPayload {

    public static final Type<ItemRayTraceBlockPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "item_ray_trace_block_packet"));

    private static final StreamCodec<RegistryFriendlyByteBuf, Vec3> VEC3_CODEC = StreamCodec.of(
            (buf, v) -> {
                buf.writeDouble(v.x);
                buf.writeDouble(v.y);
                buf.writeDouble(v.z);
            },
            buf -> new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));

    private static final StreamCodec<RegistryFriendlyByteBuf, BlockHitResult> BLOCK_HIT_RESULT_CODEC = StreamCodec.of(
            (buf, r) -> {
                VEC3_CODEC.encode(buf, r.getLocation());
                buf.writeBlockPos(r.getBlockPos());
                buf.writeEnum(r.getDirection());
                buf.writeBoolean(r.isInside());
            },
            buf -> {
                Vec3 loc = VEC3_CODEC.decode(buf);
                BlockPos pos = buf.readBlockPos();
                Direction dir = buf.readEnum(Direction.class);
                boolean inside = buf.readBoolean();
                return new BlockHitResult(loc, dir, pos, inside);
            });

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemRayTraceBlockPayload> STREAM_CODEC = StreamCodec
            .composite(
                    ByteBufCodecs.VAR_INT, p -> p.hand().ordinal(),
                    VEC3_CODEC, ItemRayTraceBlockPayload::origin,
                    BLOCK_HIT_RESULT_CODEC, ItemRayTraceBlockPayload::result,
                    (handOrd, origin, result) -> new ItemRayTraceBlockPayload(InteractionHand.values()[handOrd], origin,
                            result));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
