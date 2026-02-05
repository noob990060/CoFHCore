package cofh.core.util.filter;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class EmptyFilter implements IFilter {

    public static final EmptyFilter INSTANCE = new EmptyFilter();

    @Override
    public IFilter read(CompoundTag nbt, HolderLookup.Provider provider) {

        return INSTANCE;
    }

    @Override
    public CompoundTag write(CompoundTag nbt, HolderLookup.Provider provider) {

        return nbt;
    }

}
