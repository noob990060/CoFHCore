package cofh.lib.common.item;

import cofh.lib.api.item.ICoFHItem;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nullable;
import static cofh.lib.util.Utils.getItemEnchantmentLevel;

public class ArrowItemCoFH extends ArrowItem implements ICoFHItem {

    protected final IArrowFactory<? extends AbstractArrow> factory;
    protected boolean infinitySupport = false;

    public ArrowItemCoFH(IArrowFactory<? extends AbstractArrow> factory, Properties builder) {

        super(builder);
        this.factory = factory;

        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    public ArrowItemCoFH setInfinitySupport(boolean infinitySupport) {

        this.infinitySupport = infinitySupport;
        return this;
    }

    @Override
    public AbstractArrow createArrow(Level worldIn, ItemStack stack, LivingEntity shooter, @Nullable ItemStack weapon) {

        return factory.createArrow(worldIn, shooter);
    }

    @Override
    public boolean isInfinite(ItemStack bow, ItemStack arrow, LivingEntity shooter) {

        Registry<Enchantment> registry = shooter.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
        Enchantment infinity = registry.get(Enchantments.INFINITY.location());
        return infinitySupport && getItemEnchantmentLevel(infinity, bow) > 0 || super.isInfinite(bow, arrow, shooter);
    }

    // region DISPLAY
    protected String modId = "";

    @Override
    public ArrowItemCoFH setModId(String modId) {

        this.modId = modId;
        return this;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {

        return modId == null || modId.isEmpty() ? super.getCreatorModId(itemStack) : modId;
    }
    // endregion

    // region FACTORY
    public interface IArrowFactory<T extends AbstractArrow> {

        T createArrow(Level world, LivingEntity living);

        T createArrow(Level world, double posX, double posY, double posZ);

    }
    // endregion

    // region DISPENSER BEHAVIOR
    private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {

        private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {

            Direction direction = source.state().getValue(DispenserBlock.FACING);
            Level level = source.level();
            double d0 = source.pos().getX() + ((double) direction.getStepX() * 1.125D);
            double d1 = source.pos().getY() + ((double) direction.getStepY() * 1.125D);
            double d2 = source.pos().getZ() + ((double) direction.getStepZ() * 1.125D);
            Vec3 position = new Vec3(d0, d1, d2);
            
            if (stack.getItem() instanceof ArrowItemCoFH arrowItem) {
                AbstractArrow arrow = arrowItem.factory.createArrow(level, position.x, position.y, position.z);
                arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                level.addFreshEntity(arrow);
                stack.shrink(1);
                return stack;
            }
            return this.behaviourDefaultDispenseItem.dispense(source, stack);
        }
    };
    // endregion
}
