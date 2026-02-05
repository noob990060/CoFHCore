package cofh.core.common.network.data.client;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ModelUpdatePayload(BlockPos pos) implements CustomPacketPayload {

    public static final Type<ModelUpdatePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "model_update_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ModelUpdatePayload> STREAM_CODEC = StreamCodec
            .of(ModelUpdatePayload::encode, ModelUpdatePayload::decode);

    private static ModelUpdatePayload decode(RegistryFriendlyByteBuf buf) {
        return new ModelUpdatePayload(buf.readBlockPos());
    }

    private static void encode(RegistryFriendlyByteBuf buf, ModelUpdatePayload payload) {
        buf.writeBlockPos(payload.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
