package cofh.core.common.entity;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.Map;

import static cofh.lib.util.constants.NBTTags.TAG_ENCHANTMENTS;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public abstract class AbstractMinecartCoFH extends AbstractMinecart {

    protected ListTag enchantments = new ListTag();

    protected AbstractMinecartCoFH(EntityType<?> type, Level worldIn) {

        super(type, worldIn);
    }

    protected AbstractMinecartCoFH(EntityType<?> type, Level worldIn, double posX, double posY, double posZ) {

        super(type, worldIn, posX, posY, posZ);
    }

    public AbstractMinecartCoFH onPlaced(ItemStack stack) {

        // TODO: Update enchantment handling for NeoForge 1.21.1
        // For now, preserve existing enchantments in NBT format
        this.enchantments = new ListTag();
        return this;
    }

    protected float getHoldingMod(Map<Enchantment, Integer> enchantmentMap) {

        // TODO: Update this method for NeoForge 1.21.1 enchantment system
        return 1.0F;
    }

    public ItemStack createItemStackTag(ItemStack stack) {

        if (this.hasCustomName()) {
            stack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        }
        if (!this.enchantments.isEmpty()) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData == null) {
                CompoundTag newTag = new CompoundTag();
                newTag.put(TAG_ENCHANTMENTS, enchantments);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
            } else {
                CompoundTag tag = customData.copyTag();
                tag.put(TAG_ENCHANTMENTS, enchantments);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
            }
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

        this.remove(Entity.RemovalReason.KILLED);
        if (this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack stack = createItemStackTag(getPickResult());
            this.spawnAtLocation(stack);
        }
    }

}
