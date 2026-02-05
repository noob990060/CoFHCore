package cofh.lib.common.item;

import cofh.lib.api.item.ICoFHItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;

public class PickaxeItemCoFH extends PickaxeItem implements ICoFHItem {

    public PickaxeItemCoFH(Tier tier, Properties builder) {

        super(tier, builder);
    }

    // region DISPLAY
    protected String modId = "";

    @Override
    public PickaxeItemCoFH setModId(String modId) {

        this.modId = modId;
        return this;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {

        return modId == null || modId.isEmpty() ? super.getCreatorModId(itemStack) : modId;
    }
    // endregion
}
