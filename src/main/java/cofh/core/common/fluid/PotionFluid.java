package cofh.core.common.fluid;

import cofh.lib.common.fluid.FluidCoFH;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static cofh.core.CoFHCore.FLUIDS;
import static cofh.core.CoFHCore.FLUID_TYPES;
import static cofh.core.util.references.CoreIDs.ID_FLUID_POTION;

public class PotionFluid extends FluidCoFH {

    private static PotionFluid INSTANCE;

    public static PotionFluid create() {

        if (INSTANCE == null) {
            INSTANCE = new PotionFluid();
        }
        return INSTANCE;
    }

    protected PotionFluid() {

        super(FLUIDS, ID_FLUID_POTION);

        // This is only used for testing.
        // bucket = toolsTab(1000, ITEMS.register(bucket(key), () -> new
        // BucketItem(stillFluid,
        // properties().containerItem(Items.BUCKET).maxStackSize(1).group(ItemGroup.BREWING)));
    }

    @Override
    protected BaseFlowingFluid.Properties fluidProperties() {

        return new BaseFlowingFluid.Properties(type(), stillFluid, flowingFluid);
    }

    @Override
    protected Supplier<FluidType> type() {

        return TYPE;
    }

    public static final DeferredHolder<FluidType, FluidType> TYPE = FLUID_TYPES.register(ID_FLUID_POTION,
            () -> new FluidType(FluidType.Properties.create()
                    .density(1100)
                    .viscosity(1100)
                    .sound(SoundActions.BUCKET_FILL, SoundEvents.BOTTLE_FILL)
                    .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BOTTLE_EMPTY)) {

                @Override
                public Component getDescription(FluidStack stack) {

                    // TODO: Update to use new potion API when available
                    return super.getDescription(stack);
                }

                @Override
                public Rarity getRarity(FluidStack stack) {

                    // TODO: Update to use new potion API when available
                    return Rarity.COMMON;
                }

                @Override
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {

                    consumer.accept(new IClientFluidTypeExtensions() {

                        private static final ResourceLocation
                                STILL = ResourceLocation.parse("cofh_core:block/fluids/potion_still"),
                                FLOW = ResourceLocation.parse("cofh_core:block/fluids/potion_flow");

                        @Override
                        public int getTintColor(FluidStack stack) {

                            return 0xFF000000 | getPotionColor(stack);
                        }

                        @Override
                        public ResourceLocation getStillTexture() {

                            return STILL;
                        }

                        @Override
                        public ResourceLocation getFlowingTexture() {

                            return FLOW;
                        }
                    });
                }
            });

    // region HELPERS
    public static int DEFAULT_COLOR = 0xF800F8;

    public static int getPotionColor(FluidStack stack) {

        // TODO: Implement using new PotionContents system when fluid components are available
        return DEFAULT_COLOR;
    }

    public static FluidStack getPotionAsFluid(int amount, Potion type, boolean hasCustom) {

        if (type == null) {
            return FluidStack.EMPTY;
        }
        if (type == Potions.WATER && !hasCustom) {
            return new FluidStack(Fluids.WATER, amount);
        }
        return addPotionToFluidStack(new FluidStack(INSTANCE.stillFluid.get(), amount), type);
    }

    public static FluidStack getPotionAsFluid(int amount, Potion type) {

        return getPotionAsFluid(amount, type, false);
    }

    public static FluidStack addPotionToFluidStack(FluidStack stack, Potion type) {

        ResourceLocation resourceLoc = BuiltInRegistries.POTION.getKey(type);
        // NOTE: This can actually happen.
        if (resourceLoc == null) {
            return FluidStack.EMPTY;
        }
        // TODO: Update to use new fluid tag API when available
        return stack;
    }

    public static FluidStack setCustomEffects(FluidStack stack, Collection<MobEffectInstance> effects) {

        // TODO: Update to use new fluid tag API when available
        return stack;
    }

    public static Collection<MobEffectInstance> getCustomEffects(FluidStack stack) {

        // TODO: Update to use new fluid tag API when available
        return Collections.emptyList();
    }

    public static FluidStack setCustomColor(FluidStack stack, int color) {

        // TODO: Update to use new fluid tag API when available
        return stack;
    }

    public static ItemStack setCustomColor(ItemStack stack, int color) {

        // TODO: Update to use new item tag API when available
        return stack;
    }

    public static FluidStack getPotionFluidFromItem(int amount, ItemStack stack) {

        if (stack.getItem() == Items.POTION) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents != null && contents.potion().isPresent() && contents.potion().get() != Potions.WATER) {
                // TODO: Convert PotionContents to fluid when fluid components are available
                return new FluidStack(INSTANCE.stillFluid.get(), amount);
            }
        }
        return FluidStack.EMPTY;
    }

    public static ItemStack getItemFromPotionFluid(FluidStack fluid) {

        ItemStack stack = new ItemStack(Items.POTION);
        // TODO: Convert fluid to PotionContents when fluid components are available
        return stack;
    }
    // endregion

    // protected static class PotionFluidAttributes extends FluidAttributes {
    //
    // protected PotionFluidAttributes(Builder builder, Fluid fluid) {
    //
    // super(builder, fluid);
    // }
    //
    // @Override
    // public Component getDisplayName(FluidStack stack) {
    //
    // Potion potion = PotionUtils.getPotion(stack.getTag());
    // if (potion == Potions.EMPTY || potion == Potions.WATER) {
    // return super.getDisplayName(stack);
    // }
    // return new
    // Component.translatable(potion.getName(Items.POTION.getDescriptionId() +
    // ".effect."));
    // }
    //
    // public Rarity getRarity(FluidStack stack) {
    //
    // return
    // FluidHelper.getPotionFromFluidTag(stack.getTag()).getEffects().isEmpty() ?
    // Rarity.COMMON : Rarity.UNCOMMON;
    // }
    //
    // @Override
    // public int getColor(FluidStack stack) {
    //
    // return 0xFF000000 | getPotionColor(stack);
    // }
    //
    // public static Builder builder(ResourceLocation stillTexture, ResourceLocation
    // flowingTexture) {
    //
    // return new Builder(stillTexture, flowingTexture, PotionFluidAttributes::new)
    // {};
    // }
    //
    // }

}
