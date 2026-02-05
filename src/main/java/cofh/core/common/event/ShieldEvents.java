package cofh.core.common.event;

// TODO: Fix ShieldBlockEvent for NeoForge 1.21.1

import net.neoforged.fml.common.EventBusSubscriber;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

@EventBusSubscriber(modid = ID_COFH_CORE)
public class ShieldEvents {

    private ShieldEvents() {

    }

    // TODO: Fix ShieldBlockEvent for NeoForge 1.21.1
    // @SubscribeEvent (priority = HIGH)
    // public static void handleShieldBlock(ShieldBlockEvent event) {
    //
    //     if (event.isCanceled()) {
    //         return;
    //     }
    //     LivingEntity entity = event.getEntity();
    //     var shield = entity.getUseItem().getCapability(CoreCapabilities.ShieldHandler.ITEM);
    //     if (shield != null) {
    //         event.setBlockedDamage(shield.onBlock(entity, event.getDamageSource(), event.getBlockedDamage()));
    //     }
    // }

}