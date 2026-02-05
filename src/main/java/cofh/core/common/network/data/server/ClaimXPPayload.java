package cofh.core.common.network.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ClaimXPPayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ClaimXPPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "claim_xp_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClaimXPPayload> STREAM_CODEC = StreamCodec
            .of(ClaimXPPayload::encode, ClaimXPPayload::decode);

    private static ClaimXPPayload decode(RegistryFriendlyByteBuf buf) {
        return new ClaimXPPayload(buf.readBlockPos());
    }

    private static void encode(RegistryFriendlyByteBuf buf, ClaimXPPayload payload) {
        buf.writeBlockPos(payload.pos());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
