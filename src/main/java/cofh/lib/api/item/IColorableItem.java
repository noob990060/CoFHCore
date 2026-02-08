package cofh.lib.api.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;

public interface IColorableItem {

    default int getColor(ItemStack item, int colorIndex) {

        if (colorIndex == 0) {
            // Use the new DyedItemColor component (used by leather armor, etc.)
            DyedItemColor dyedColor = item.get(DataComponents.DYED_COLOR);
            if (dyedColor != null) {
                return net.minecraft.util.FastColor.ARGB32.opaque(dyedColor.rgb());
            }
        }
        return -1;
    }

}
