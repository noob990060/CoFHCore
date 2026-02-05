package cofh.core.common.registry;

import cofh.core.common.security.SecurityComponent;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static cofh.lib.util.constants.ModIds.ID_COFH_CORE;

public class CoreDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister
            .create(Registries.DATA_COMPONENT_TYPE, ID_COFH_CORE);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SecurityComponent>> SECURITY = COMPONENTS.register(
            "security",
            () -> DataComponentType.<SecurityComponent>builder()
                    .persistent(SecurityComponent.CODEC) // saved to disk
                    .build());
}
