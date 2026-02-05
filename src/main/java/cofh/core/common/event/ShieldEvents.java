package cofh.core.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.minecraft.world.entity.LivingEntity;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

@EventBusSubscriber(modid = ID_COFH_CORE)
public class ShieldEvents {

    private ShieldEvents() {
    }

    @SubscribeEvent
    public static void handleShieldBlock(LivingShieldBlockEvent event) {
        if (event.isCanceled()) {
            return;
        }

        LivingEntity entity = event.getEntity();
        if (entity == null) {
            return;
        }

        // use entity.getUseItem() to get the shield stack
        // adjust blocked damage or shield durability
        float originalBlocked = event.getOriginalBlockedDamage();
        float customBlocked = /* your logic here */ originalBlocked;

        event.setBlockedDamage(customBlocked);

        // optionally set how much durability is lost
        // event.setShieldDamage(customArmorDamage);
    }
}
