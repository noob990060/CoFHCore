package cofh.core.client.particle.types;

import cofh.core.client.particle.options.BiColorParticleOptions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class PointToPointParticleType extends ParticleType<BiColorParticleOptions> {

    public PointToPointParticleType(boolean overrideLimit) {

        super(overrideLimit);
    }

    public PointToPointParticleType() {

        this(false);
    }

    @Override
    public MapCodec<BiColorParticleOptions> codec() {
        return RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                        Codec.FLOAT.fieldOf("duration").forGetter(o -> o.duration),
                        Codec.FLOAT.fieldOf("delay").forGetter(o -> o.delay),
                        Codec.INT.fieldOf("rgba0").forGetter(o -> o.rgba0),
                        Codec.INT.fieldOf("rgba1").forGetter(o -> o.rgba1)
                ).apply(builder, (size, duration, delay, rgba0, rgba1) -> 
                        new BiColorParticleOptions(this, size, duration, delay, rgba0, rgba1))
        );
    }

    @Override
    public StreamCodec<FriendlyByteBuf, BiColorParticleOptions> streamCodec() {
        return BiColorParticleOptions.biColorStreamCodec(this);
    }

}
