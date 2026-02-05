package cofh.core.common.network.packet.client;

import cofh.core.common.network.data.client.EffectAddedPayload;
import cofh.core.util.ProxyUtils;
import cofh.lib.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class EffectAddedPacket {

    public static final EffectAddedPacket INSTANCE = new EffectAddedPacket();

    public static EffectAddedPacket get() {
        return INSTANCE;
    }

    public void handle(final EffectAddedPayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            int id = payload.entityId();
            Holder<MobEffect> effectHolder = BuiltInRegistries.MOB_EFFECT.getHolder(payload.effect()).orElse(null);
            int effectDur = payload.duration();

            MobEffectInstance effect = effectHolder != null ? new MobEffectInstance(effectHolder, effectDur) : null;

            if (effect == null) {
                return;
            }
            Level level = ProxyUtils.getClientWorld();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(id);
            if (entity instanceof LivingEntity living && !entity.equals(ProxyUtils.getClientPlayer())) {
                living.forceAddEffect(effect, null);
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
            PacketDistributor.sendToPlayersNear(serverLevel, serverPlayer, entity.getX(), entity.getY(), entity.getZ(), 64.0, new EffectAddedPayload(entity.getId(), effectId, effect.getDuration()));
        }
    }

    public static void sendToClient(LivingEntity entity, MobEffectInstance effect, Player player) {
        if (entity == null || effect == null) {
            return;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            Holder<MobEffect> effectHolder = effect.getEffect();
            ResourceLocation effectId = effectHolder.unwrapKey().orElseThrow().location();
            PacketDistributor.sendToPlayer(serverPlayer, new EffectAddedPayload(entity.getId(), effectId, effect.getDuration()));
        }
    }
}
