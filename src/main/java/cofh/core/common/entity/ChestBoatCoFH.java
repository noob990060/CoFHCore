package cofh.core.common.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.function.Supplier;

import static cofh.core.util.references.CoreIDs.ID_HOLDING;
import static cofh.lib.util.Utils.getEnchantment;
import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;
import static cofh.lib.util.constants.NBTTags.TAG_ENCHANTMENTS;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public class ChestBoatCoFH extends ChestBoat implements IOnPlaced {

    protected ListTag enchantments = new ListTag();

    public ChestBoatCoFH(EntityType<? extends Boat> type, Level worldIn) {

        super(type, worldIn);
    }

    public ChestBoatCoFH(Supplier<EntityType<? extends Boat>> type, Level worldIn, double posX, double posY, double posZ) {

        this(type.get(), worldIn);
        this.setPos(posX, posY, posZ);
        this.xo = posX;
        this.yo = posY;
        this.zo = posZ;
    }

    public ChestBoatCoFH onPlaced(ItemStack stack) {

        // For now, keep enchantments empty since the API has changed
        this.enchantments = new ListTag();
        return this;
    }

    protected float getHoldingMod(Map<Enchantment, Integer> enchantmentMap) {

        int holding = enchantmentMap.getOrDefault(getEnchantment(ID_COFH_CORE, ID_HOLDING), 0);
        return 1 + holding / 2F;
    }

    public ItemStack createItemStackTag(ItemStack stack) {

        if (this.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        }
        if (!this.enchantments.isEmpty()) {
            // TODO: Update to use new enchantment component system when available
        }
        return stack;
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {

        super.readAdditionalSaveData(compound);

        enchantments = compound.getList(TAG_ENCHANTMENTS, TAG_COMPOUND);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {

        super.addAdditionalSaveData(compound);

        compound.put(TAG_ENCHANTMENTS, enchantments);
    }

    @Override
    public void destroy(DamageSource source) {

        this.remove(RemovalReason.KILLED);
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack stack = createItemStackTag(getPickResult());
            this.spawnAtLocation(stack);
        }
    }

}
