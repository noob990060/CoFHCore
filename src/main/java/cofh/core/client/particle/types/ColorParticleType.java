package cofh.core.client.particle.types;

import cofh.core.client.particle.options.ColorParticleOptions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ColorParticleType extends ParticleType<ColorParticleOptions> {

    public ColorParticleType(boolean overrideLimit) {

        super(overrideLimit);
    }

    public ColorParticleType() {

        this(false);
    }

    @Override
    public MapCodec<ColorParticleOptions> codec() {
        return RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                        Codec.FLOAT.fieldOf("duration").forGetter(o -> o.duration),
                        Codec.FLOAT.fieldOf("delay").forGetter(o -> o.delay),
                        Codec.INT.fieldOf("rgba0").forGetter(o -> o.rgba0)
                ).apply(builder, (size, duration, delay, rgba) -> 
                        new ColorParticleOptions(this, size, duration, delay, rgba))
        );
    }

    @Override
    public StreamCodec<FriendlyByteBuf, ColorParticleOptions> streamCodec() {
        return ColorParticleOptions.streamCodec(this);
    }

}
