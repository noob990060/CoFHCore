package cofh.lib.util.raytracer;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

/**
 * A VoxelShape implementation, produces a {@link VoxelShapeBlockHitResult} when ray traced.
 * {@link IndexedVoxelShape#data} will be passed through to {@link SubHitBlockHitResult#hitInfo} and
 * to {@link SubHitBlockHitResult#subHit} if its an integer.
 * <p>
 * Copied from CCL with permission :)
 * <p>
 * Created by covers1624 on 5/12/20.
 */
public class IndexedVoxelShape extends VoxelShape {

    private static final Field SHAPE_FIELD;

    static {
        try {
            SHAPE_FIELD = VoxelShape.class.getDeclaredField("shape");
            SHAPE_FIELD.setAccessible(true);
        } catch (ReflectiveOperationException ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private final VoxelShape parent;
    private final Object data;

    public IndexedVoxelShape(VoxelShape parent, Object data) {

        super(getShape(parent));
        this.parent = parent;
        this.data = data;
    }

    @Override
    public DoubleList getCoords(Direction.Axis axis) {

        return parent.getCoords(axis);
    }

    @Nullable
    @Override
    public VoxelShapeBlockHitResult clip(Vec3 start, Vec3 end, BlockPos pos) {

        BlockHitResult result = parent.clip(start, end, pos);
        if (result == null) return null;
        double dist = result.getLocation().distanceToSqr(start);
        return new VoxelShapeBlockHitResult(result, this, dist);
    }

    public Object getData() {

        return data;
    }

    private static DiscreteVoxelShape getShape(VoxelShape shape) {

        try {
            return (DiscreteVoxelShape) SHAPE_FIELD.get(shape);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Failed to access voxel shape data.", ex);
        }
    }

}
