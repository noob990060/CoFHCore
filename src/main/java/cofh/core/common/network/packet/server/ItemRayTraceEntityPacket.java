package cofh.core.common.network.packet.server;

import cofh.core.common.item.IEntityRayTraceItem;
import cofh.core.common.network.data.server.ItemRayTraceEntityPayload;
import cofh.core.util.ProxyUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ItemRayTraceEntityPacket {

    public static final ItemRayTraceEntityPacket INSTANCE = new ItemRayTraceEntityPacket();

    public static ItemRayTraceEntityPacket get() {
        return INSTANCE;
    }

    public void handle(final ItemRayTraceEntityPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (!(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            ItemStack stack = serverPlayer.getItemInHand(payload.hand());
            if (!(stack.getItem() instanceof IEntityRayTraceItem item)) {
                return;
            }

            Entity target = serverPlayer.serverLevel().getEntity(payload.targetId());
            if (target == null) {
                return;
            }

            Vec3 hitPos = target.position().add(payload.offset());

            item.handleEntityRayTrace(
                    serverPlayer.serverLevel(),
                    serverPlayer,
                    payload.hand(),
                    stack,
                    payload.origin(),
                    target,
                    hitPos,
                    payload.power());
        });
    }

    public static void sendToServer(Player player, InteractionHand hand, Vec3 origin, Entity target, Vec3 hit,
            float power) {
        Player client = ProxyUtils.getClientPlayer();
        if (client == null || !client.equals(player)) {
            return;
        }

        Vec3 offset = hit.subtract(target.position());

        PacketDistributor.sendToServer(
                new ItemRayTraceEntityPayload(hand, origin, target.getId(), offset, power));
    }
}
