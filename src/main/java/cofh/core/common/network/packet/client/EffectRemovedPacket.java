package cofh.core.common.network.packet.client;

import cofh.core.common.network.data.client.EffectRemovedPayload;
import cofh.core.util.ProxyUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EffectRemovedPacket {

    public static final EffectRemovedPacket INSTANCE = new EffectRemovedPacket();

    public static EffectRemovedPacket get() {
        return INSTANCE;
    }

    public void handle(final EffectRemovedPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            int id = payload.entityId();
            Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(payload.effect()).orElse(null);

            if (ProxyUtils.getClientWorld().getEntity(id) instanceof LivingEntity entity && !entity.equals(ProxyUtils.getClientPlayer())) {
                if (effectHolder != null) {
                    entity.removeEffect(effectHolder);
                }
            }
        });
    }

    public static void sendToClient(LivingEntity entity, MobEffectInstance effect) {
        if (entity == null || effect == null) {
            return;
        }
        Holder<MobEffect> effectHolder = effect.getEffect();
        ResourceLocation effectId = effectHolder.unwrapKey().orElseThrow().location();
        if (entity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel && entity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayersNear(serverLevel, serverPlayer, entity.getX(), entity.getY(), entity.getZ(), 64.0, new EffectRemovedPayload(entity.getId(), effectId));
        }
    }

    public static void sendToClient(LivingEntity entity, MobEffect effect) {
        if (entity == null || effect == null) {
            return;
        }
        Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect);
        ResourceLocation effectId = effectHolder.unwrapKey().orElseThrow().location();
        if (entity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel && entity instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayersNear(serverLevel, serverPlayer, entity.getX(), entity.getY(), entity.getZ(), 64.0, new EffectRemovedPayload(entity.getId(), effectId));
        }
    }
}
