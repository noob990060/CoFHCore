package cofh.core.init;

import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import static cofh.core.CoFHCore.ENTITY_DATA_SERIALIZERS;

public class CoreEntityDataSerializers {

    private CoreEntityDataSerializers() {

    }

    public static void register() {

    }

    // NeoForge 1.21.1: Use forValueType static method with StreamCodec
    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<FluidStack>> FLUID_STACK_DATA_SERIALIZER = ENTITY_DATA_SERIALIZERS.register("fluid_stack_eds",
            () -> EntityDataSerializer.forValueType(FluidStack.STREAM_CODEC)
    );

}
