package cofh.core.common.event;

import cofh.core.common.config.CoreCommonConfig;
import cofh.core.common.config.CoreEnchantConfig;
import cofh.core.util.helpers.XpHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.event.entity.player.PlayerXpEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.Map;

import static cofh.core.init.CoreMobEffects.SLIMED;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

@EventBusSubscriber(modid = ID_COFH_CORE)
public class CoreCommonEvents {

    private CoreCommonEvents() {

    }

    @SubscribeEvent
    public static void handleFarmlandTrampleEvent(BlockEvent.FarmlandTrampleEvent event) {

        if (event.isCanceled()) {
            return;
        }
        if (!CoreEnchantConfig.improvedFeatherFalling()) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            int encFeatherFalling = 0; // TODO: Fix FALL_PROTECTION for NeoForge 1.21.1
            if (encFeatherFalling > 0) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void handleLivingFallEvent(LivingFallEvent event) {

        if (event.isCanceled()) {
            return;
        }
        if (event.getDistance() >= 3.0) {
            LivingEntity living = event.getEntity();
            if (living.hasEffect(SLIMED)) {
                Vec3 motion = living.getDeltaMovement();
                living.setDeltaMovement(motion.x, 0.08 * Math.sqrt(event.getDistance() / 0.08), motion.z);
                living.hurtMarked = true;
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent (priority = EventPriority.LOWEST)
    public static void handleItemFishedEvent(ItemFishedEvent event) {

        if (event.isCanceled()) {
            return;
        }
        if (!CoreCommonConfig.enableFishingExhaustion()) {
            return;
        }
        Entity player = event.getHookEntity().getOwner();
        if (!(player instanceof Player) || player instanceof FakePlayer) {
            return;
        }
        ((Player) player).causeFoodExhaustion(CoreCommonConfig.amountFishingExhaustion());
    }

    @SubscribeEvent (priority = EventPriority.LOW)
    public static void handlePickupXpEvent(PlayerXpEvent.PickupXp event) {

        if (event.isCanceled()) {
            return;
        }
        Player player = event.getEntity();
        ExperienceOrb orb = event.getOrb();

        // Improved Mending
        if (CoreEnchantConfig.improvedMending()) {
            player.takeXpDelay = 2;
            player.take(orb, 1);

            Map.Entry<EquipmentSlot, ItemStack> entry = getMostDamagedItem(player);
            if (entry != null) {
                ItemStack itemstack = entry.getValue();
                if (!itemstack.isEmpty() && itemstack.isDamaged()) {
                    int i = Math.min((int) (orb.value * itemstack.getXpRepairRatio()), itemstack.getDamageValue());
                    orb.value -= durabilityToXp(i);
                    itemstack.setDamageValue(itemstack.getDamageValue() - i);
                }
            }
            XpHelper.attemptStoreXP(player, orb);
            if (orb.value > 0) {
                player.giveExperiencePoints(orb.value);
            }
            orb.discard();
            event.setCanceled(true);
            return;
        }
        XpHelper.attemptStoreXP(player, orb);
    }

    // TODO: Fix SaplingGrowTreeEvent for NeoForge 1.21.1
    // @SubscribeEvent (priority = EventPriority.LOWEST)
    // public static void handleSaplingGrowTreeEvent(SaplingGrowTreeEvent event) {
    //
    //     if (!CoreCommonConfig.enableSaplingGrowthMod()) {
    //         return;
    //     }
    //     if (event.getRandomSource().nextInt(CoreCommonConfig.amountSaplingGrowthMod()) != 0) {
    //         event.setCanceled(true);
    //     }
    // }

    // region HELPERS
    private static Map<EquipmentSlot, ItemStack> getMendingSlotItems(Player player) {
        Map<EquipmentSlot, ItemStack> map = new java.util.HashMap<>();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.name().contains("MAINHAND") || slot.name().contains("OFFHAND") || slot.name().contains("FEET") || slot.name().contains("LEGS") || slot.name().contains("CHEST") || slot.name().contains("HEAD")) {
                ItemStack stack = player.getItemBySlot(slot);
                if (!stack.isEmpty()) {
                    map.put(slot, stack);
                }
            }
        }
        return map;
    }

    private static Map.Entry<EquipmentSlot, ItemStack> getMostDamagedItem(Player player) {

        Map<EquipmentSlot, ItemStack> map = getMendingSlotItems(player);
        Map.Entry<EquipmentSlot, ItemStack> mostDamaged = null;
        if (map.isEmpty()) {
            return null;
        }
        double durability = 0.0D;

        for (Map.Entry<EquipmentSlot, ItemStack> entry : map.entrySet()) {
            ItemStack stack = entry.getValue();
            if (!stack.isEmpty() && stack.isDamaged()) {
                if (calcDurabilityRatio(stack) > durability) {
                    mostDamaged = entry;
                    durability = calcDurabilityRatio(stack);
                }
            }
        }
        return mostDamaged;
    }

    private static int durabilityToXp(int durability) {

        return durability / 2;
    }

    private static double calcDurabilityRatio(ItemStack stack) {

        return (double) stack.getDamageValue() / stack.getMaxDamage();
    }
    // endregion
}
