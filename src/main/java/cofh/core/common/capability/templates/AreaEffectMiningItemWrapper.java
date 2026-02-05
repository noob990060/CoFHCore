package cofh.core.common.capability.templates;

import cofh.core.util.helpers.AreaEffectHelper;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class AreaEffectMiningItemWrapper extends AreaEffectItemWrapper {

    private final int radius;
    private final int depth;
    private final Type type;

    public enum Type {
        EXCAVATOR, HAMMER, SICKLE
    }

    public AreaEffectMiningItemWrapper(ItemStack containerIn, int radius, int depth, Type type) {

        super(containerIn);

        this.radius = radius;
        this.depth = depth;
        this.type = type;
    }

    public AreaEffectMiningItemWrapper(ItemStack containerIn, int radius, Type type) {

        this(containerIn, radius, 1, type);
    }

    @Override
    public ImmutableList<BlockPos> getAreaEffectBlocks(BlockPos pos, Player player, Level level) {

        if (type == Type.SICKLE) {
            return AreaEffectHelper.getBlocksCentered(areaEffectItem, pos, player, radius, depth);
        }
        // TODO: Fix Excavating enchantment lookup for NeoForge 1.21.1
        int excavatingLevel = 0; // getItemEnchantmentLevel(getEnchantment(ID_ENSORCELLATION, ID_EXCAVATING), areaEffectItem);
        return AreaEffectHelper.getBreakableBlocksRadius(areaEffectItem, pos, player, radius + excavatingLevel);
    }

}
