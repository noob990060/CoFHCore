package cofh.core.util.filter;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Map;

import static cofh.core.util.filter.FilterHolderType.*;

public class FilterRegistry {

    public static final String FLUID_FILTER_TYPE = "fluid";
    public static final String ITEM_FILTER_TYPE = "item";

    protected static final Map<String, IFilterFactory<? extends IFilter>> FILTER_FACTORY_MAP = new Object2ObjectOpenHashMap<>();

    static {
        registerFilterFactory(FLUID_FILTER_TYPE, FluidFilter.FACTORY);
        registerFilterFactory(ITEM_FILTER_TYPE, ItemFilter.FACTORY);
    }

    public static boolean registerFilterFactory(String type, IFilterFactory<?> factory) {

        if (type == null || type.isEmpty() || factory == null) {
            return false;
        }
        FILTER_FACTORY_MAP.put(type, factory);
        return true;
    }

    public static IFilter getFilter(String type, CompoundTag nbt, HolderLookup.Provider provider) {

        if (FILTER_FACTORY_MAP.containsKey(type)) {
            return FILTER_FACTORY_MAP.get(type).createFilter(nbt, provider, ITEM, -1, BlockPos.ZERO);
        }
        return EmptyFilter.INSTANCE;
    }

    public static IFilter getFilter(String type, CompoundTag nbt, HolderLookup.Provider provider, BlockEntity tile) {

        if (FILTER_FACTORY_MAP.containsKey(type)) {
            return FILTER_FACTORY_MAP.get(type).createFilter(nbt, provider, TILE, -1, tile.getBlockPos());
        }
        return EmptyFilter.INSTANCE;
    }

    public static IFilter getFilter(String type, CompoundTag nbt, HolderLookup.Provider provider, Entity entity) {

        if (FILTER_FACTORY_MAP.containsKey(type)) {
            return FILTER_FACTORY_MAP.get(type).createFilter(nbt, provider, ENTITY, entity.getId(), BlockPos.ZERO);
        }
        return EmptyFilter.INSTANCE;
    }

}
