package cofh.core.common.event;

import cofh.core.common.capability.CoreCapabilities;
import cofh.core.common.capability.templates.ArcheryBowItemWrapper;
import cofh.lib.api.capability.IArcheryBowItem;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent;
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent;

import static cofh.core.util.helpers.ArcheryHelper.findAmmo;
import static cofh.core.util.helpers.ArcheryHelper.validBow;
import static cofh.lib.util.Constants.DAMAGE_ARROW;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

@EventBusSubscriber(modid = ID_COFH_CORE)
public class ArcheryEvents {

    private ArcheryEvents() {

    }

    @SubscribeEvent (priority = EventPriority.HIGHEST)
    public static void handleArrowLooseEvent(ArrowLooseEvent event) {

        ItemStack bow = event.getBow();
        if (!validBow(bow)) {
            return;
        }
        Player shooter = event.getEntity();
        IArcheryBowItem bowCap = bow.getCapability(CoreCapabilities.ArcheryHandler.BOW);
        if (bowCap == null) {
            bowCap = new ArcheryBowItemWrapper(bow);
        }
        event.setCanceled(bowCap.fireArrow(findAmmo(shooter, bow), shooter, event.getCharge(), event.getLevel()));
    }

    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handleArrowNockEvent(ArrowNockEvent event) {

        ItemStack bow = event.getBow();
        if (!validBow(bow)) {
            return;
        }
        Player shooter = event.getEntity();
        ItemStack ammo = findAmmo(shooter, bow);

        if (!ammo.isEmpty()) {
            shooter.startUsingItem(event.getHand());
            event.setAction(InteractionResultHolder.consume(bow));
        } else if (!shooter.getAbilities().instabuild) {
            event.setAction(InteractionResultHolder.fail(bow));
        }
    }

    @SubscribeEvent
    public static void handleItemUseTickEvent(LivingEntityUseItemEvent.Tick event) {

    }

    @SubscribeEvent (priority = EventPriority.HIGH)
    public static void handleLivingDamageEvent(net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre event) {

        Entity entity = event.getEntity();
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (!(attacker instanceof LivingEntity)) {
            return;
        }
        if (source.getMsgId().equals(DAMAGE_ARROW)) {
        }
    }

}
