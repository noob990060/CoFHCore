package cofh.core.common.network.data.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public record ContainerGuiPayload(FriendlyByteBuf buf) implements CustomPacketPayload {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ID_COFH_CORE, "container_gui_packet");

    public ContainerGuiPayload(final FriendlyByteBuf buf) {

        this.buf = buf;
    }

    @Override
    public void write(FriendlyByteBuf buf) {

        buf.writeBytes(this.buf);
    }

    @Override
    public ResourceLocation id() {

        return ID;
    }

}
