package cofh.core.common.block;

import cofh.core.common.block.entity.EnderAirBlockEntity;
import cofh.lib.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource; 
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import static cofh.core.init.CoreBlockEntities.ENDER_AIR_TILE;
import static cofh.core.init.CoreMobEffects.ENDERFERENCE;

public class EnderAirBlock extends AirBlock implements EntityBlock {

    protected static boolean teleport = true;
    protected static int duration = 40;

    public EnderAirBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new EnderAirBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type) {
        return level.isClientSide ? null
                : type == ENDER_AIR_TILE.get()
                        ? (lvl, pos, st, be) -> EnderAirBlockEntity.tick(lvl, pos, st, (EnderAirBlockEntity) be)
                        : null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(8) == 0) {
            Utils.spawnBlockParticlesClient(level, ParticleTypes.PORTAL, pos, random, 2);
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {

        if (!teleport || level.isClientSide) {
            return;
        }
        if (entity instanceof ItemEntity || entity instanceof ExperienceOrb) {
            return;
        }

        BlockPos randPos = pos.offset(
                -128 + level.random.nextInt(257),
                level.random.nextInt(8),
                -128 + level.random.nextInt(257));

        if (!level.getBlockState(randPos).getCollisionShape(level, randPos).isEmpty()) {
            return;
        }

        if (entity instanceof LivingEntity living) {
            if (Utils.teleportEntityTo(entity, randPos)) {

                Holder<MobEffect> eff = level.registryAccess()
                        .lookupOrThrow(Registries.MOB_EFFECT)
                        .getOrThrow(ENDERFERENCE.getKey());

                living.addEffect(new MobEffectInstance(eff, duration, 0));
            }
        }

    }
}
