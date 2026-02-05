package cofh.core.common.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static cofh.core.init.CoreBlockEntities.ENDER_AIR_TILE;

public class EnderAirBlockEntity extends BlockEntity {

    protected int duration = 200;

    public EnderAirBlockEntity(BlockPos pos, BlockState state) {
        super(ENDER_AIR_TILE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EnderAirBlockEntity be) {

        if (level.isClientSide) {
            return;
        }
        if (--be.duration <= 0) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            level.removeBlockEntity(pos);
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
