package cofh.core.common.item;

import cofh.lib.common.item.PickaxeItemCoFH;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;

public class HammerItem extends PickaxeItemCoFH {

    private static final int DEFAULT_BASE_AREA = 1;

    public final int radius;

    public HammerItem(Tier tier, int radius, Properties builder) {

        super(tier, builder.durability(tier.getUses() * 4));
        this.radius = radius;
    }

    public HammerItem(Tier tier, Properties builder) {

        this(tier, DEFAULT_BASE_AREA, builder);
    }



    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {

        return true;
    }

}
