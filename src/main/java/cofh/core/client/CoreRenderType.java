package cofh.core.client;

import net.minecraft.client.renderer.RenderStateShard;

import java.util.OptionalDouble;

public class CoreRenderType {

    // Dummy class - no longer extends RenderType due to NeoForge 1.21.1 API changes
    private CoreRenderType() {
        // Private constructor to prevent instantiation
    }

    public static final RenderStateShard.LineStateShard THICK_LINES = new RenderStateShard.LineStateShard(OptionalDouble.of(2.5D));

}

