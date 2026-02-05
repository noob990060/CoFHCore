package cofh.lib.init.data.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

public abstract class EntityLootSubProviderCoFH extends EntityLootSubProvider {

    protected EntityLootSubProviderCoFH(HolderLookup.Provider provider) {

        super(FeatureFlags.REGISTRY.allFlags(), provider);
    }

}
