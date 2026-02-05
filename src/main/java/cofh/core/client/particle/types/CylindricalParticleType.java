package cofh.core.client.particle.types;

import cofh.core.client.particle.options.CylindricalParticleOptions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CylindricalParticleType extends ParticleType<CylindricalParticleOptions> {

    public CylindricalParticleType(boolean overrideLimit) {

        super(overrideLimit);
    }

    public CylindricalParticleType() {

        this(true);
    }

    @Override
    public MapCodec<CylindricalParticleOptions> codec() {
        return RecordCodecBuilder.mapCodec(
                builder -> builder.group(
                        Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                        Codec.FLOAT.fieldOf("duration").forGetter(o -> o.duration),
                        Codec.FLOAT.fieldOf("delay").forGetter(o -> o.delay),
                        Codec.INT.fieldOf("rgba0").forGetter(o -> o.rgba0),
                        Codec.FLOAT.fieldOf("height").forGetter(o -> o.height)
                ).apply(builder, (size, duration, delay, rgba, height) -> 
                        new CylindricalParticleOptions(this, size, duration, delay, rgba, height))
        );
    }

    @Override
    public StreamCodec<FriendlyByteBuf, CylindricalParticleOptions> streamCodec() {
        return CylindricalParticleOptions.cylindricalStreamCodec(this);
    }

}
