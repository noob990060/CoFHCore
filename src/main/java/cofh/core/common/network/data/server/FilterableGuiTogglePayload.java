package cofh.core.common.network.data.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record FilterableGuiTogglePayload(int holderType, int entityId, BlockPos pos, byte mode)
        implements CustomPacketPayload {

    public static final Type<FilterableGuiTogglePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "filterable_gui_toggle_packet"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FilterableGuiTogglePayload> STREAM_CODEC = StreamCodec
            .of(FilterableGuiTogglePayload::encode, FilterableGuiTogglePayload::decode);

    private static FilterableGuiTogglePayload decode(RegistryFriendlyByteBuf buf) {
        int holderType = buf.readVarInt();
        int entityId = buf.readVarInt();
        BlockPos pos = buf.readBlockPos();
        byte mode = buf.readByte();
        return new FilterableGuiTogglePayload(holderType, entityId, pos, mode);
    }

    private static void encode(RegistryFriendlyByteBuf buf, FilterableGuiTogglePayload payload) {
        buf.writeVarInt(payload.holderType());
        buf.writeVarInt(payload.entityId());
        buf.writeBlockPos(payload.pos());
        buf.writeByte(payload.mode());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
