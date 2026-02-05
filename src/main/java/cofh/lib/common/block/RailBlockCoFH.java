package cofh.lib.common.block;

import cofh.lib.api.block.IDismantleable;
import cofh.lib.api.block.IWrenchable;
import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import static cofh.core.common.config.CoreCommonConfig.returnDismantleDrops;

public class RailBlockCoFH extends RailBlock implements IDismantleable, IWrenchable {

    protected float maxSpeed = 0.4F;

    public RailBlockCoFH(Properties builder) {

        super(builder);
    }

    public RailBlockCoFH speed(float maxSpeed) {

        this.maxSpeed = MathHelper.clamp(maxSpeed, 0F, 1F);
        return this;
    }

    @Override
    public float getRailMaxSpeed(BlockState state, Level world, BlockPos pos, AbstractMinecart cart) {

        return maxSpeed;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {

        if (Utils.isWrench(player.getMainHandItem())) {
            if (player.isSecondaryUseActive()) {
                if (canDismantle(worldIn, pos, state, player)) {
                    dismantleBlock(worldIn, pos, state, hit, player, returnDismantleDrops());
                    return InteractionResult.SUCCESS;
                }
            } else {
                BlockState rotState = rotate(state, worldIn, pos, Rotation.CLOCKWISE_90);
                if (rotState != state) {
                    worldIn.setBlockAndUpdate(pos, rotState);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

}
