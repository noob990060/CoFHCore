package cofh.lib.common.item;

import cofh.lib.api.item.ICoFHItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;

public class ShovelItemCoFH extends ShovelItem implements ICoFHItem {

    public ShovelItemCoFH(Tier tier, float attackDamageIn, float attackSpeedIn, Properties builder) {

        super(tier, builder.component(DataComponents.TOOL, tier.createToolProperties(BlockTags.MINEABLE_WITH_SHOVEL))
                .component(DataComponents.ATTRIBUTE_MODIFIERS, DiggerItem.createAttributes(tier, attackDamageIn, attackSpeedIn)));
    }

    // region DISPLAY
    protected String modId = "";

    @Override
    public ShovelItemCoFH setModId(String modId) {

        this.modId = modId;
        return this;
    }

    @Override
    public String getCreatorModId(ItemStack itemStack) {

        return modId == null || modId.isEmpty() ? super.getCreatorModId(itemStack) : modId;
    }
    // endregion
}
