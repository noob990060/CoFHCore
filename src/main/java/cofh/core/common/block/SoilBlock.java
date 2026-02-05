package cofh.core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.function.Supplier;

public class SoilBlock extends Block {

    protected static final VoxelShape SHAPE_TILLED = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

    protected Supplier<Block> otherBlock = () -> Blocks.DIRT;

    public SoilBlock(Properties properties) {

        super(properties);
    }

    public SoilBlock otherBlock(Supplier<Block> dirt) {

        this.otherBlock = dirt;
        return this;
    }

    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, net.minecraft.world.level.block.Block plantBlock) {

        return canSustainPlant(state, world, pos, facing, plantBlock, false);
    }

    protected boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, Block plantBlock, boolean tilled) {

        if (plantBlock instanceof AttachedStemBlock) {
            return true;
        }
        // In NeoForge 1.21.1, PlantType system was replaced
        // Use tag-based checks for plant types
        
        // Check if it's a crop (requires tilled soil)
        if (isCropPlant(plantBlock, world, pos.above())) {
            return tilled;
        }
        
        // Check if it's a beach plant (needs water nearby)
        if (isBeachPlant(plantBlock, world, pos.above())) {
            return !tilled;
        }
        
        // Default behavior for other plants
        return !tilled;
    }
    
    private boolean isCropPlant(Block plantBlock, BlockGetter world, BlockPos pos) {
        // Simplified crop detection - check if the plant is a typical crop
        return plantBlock.defaultBlockState().is(net.minecraft.tags.BlockTags.CROPS);
    }
    
    private boolean isBeachPlant(Block plantBlock, BlockGetter world, BlockPos pos) {
        // Simplified beach plant detection
        return plantBlock.defaultBlockState().is(net.minecraft.tags.BlockTags.SAND);
    }

    @Override
    public boolean isFertile(BlockState state, BlockGetter world, BlockPos pos) {

        return true;
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility toolAction, boolean simulate) {

        if (ItemAbilities.HOE_TILL == toolAction && context.getItemInHand().canPerformAction(ItemAbilities.HOE_TILL)) {
            if (context.getLevel().getBlockState(context.getClickedPos().above()).isAir()) {
                return otherBlock.get().defaultBlockState();
            }
        }
        return state;
    }

}
