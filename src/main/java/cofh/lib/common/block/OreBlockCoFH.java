package cofh.lib.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class OreBlockCoFH extends Block {

    protected int minXp = 0;
    protected int maxXp = 0;

    public static OreBlockCoFH createStoneOre() {

        return new OreBlockCoFH(Properties.of().mapColor(MapColor.STONE).strength(3.0F, 3.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
    }

    public static OreBlockCoFH createDeepslateOre() {

        return new OreBlockCoFH(Properties.of().mapColor(MapColor.DEEPSLATE).strength(4.5F, 3.0F).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops());
    }

    public OreBlockCoFH(Properties properties) {

        super(properties);
    }

    public OreBlockCoFH xp(int minXp, int maxXp) {

        this.minXp = minXp;
        this.maxXp = maxXp;
        return this;
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.level.LevelAccessor level, BlockPos pos, 
                         @org.jetbrains.annotations.Nullable net.minecraft.world.level.block.entity.BlockEntity blockEntity,
                         @org.jetbrains.annotations.Nullable net.minecraft.world.entity.Entity breaker, ItemStack tool) {

        if (maxXp <= 0) {
            return 0;
        }
        if (minXp >= maxXp) {
            return minXp;
        }
        return Mth.nextInt(level.getRandom(), minXp, maxXp);
    }

}
