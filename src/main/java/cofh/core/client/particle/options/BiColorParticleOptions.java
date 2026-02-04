package cofh.core.client.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class BiColorParticleOptions extends ColorParticleOptions {

    public final int rgba1;

    public BiColorParticleOptions(ParticleType<? extends BiColorParticleOptions> type, float size, float duration,
            float delay, int rgba0, int rgba1) {
        super(type, size, duration, delay, rgba0);
        this.rgba1 = rgba1;
    }

    public BiColorParticleOptions(ParticleType<? extends BiColorParticleOptions> type, float size, float duration,
            float delay) {
        this(type, size, duration, delay, 0xFFFFFFFF, 0xFFFFFFFF);
    }

    public BiColorParticleOptions(ParticleType<? extends BiColorParticleOptions> type, float size, float duration) {
        this(type, size, duration, 0.0F);
    }

    public BiColorParticleOptions(ParticleType<? extends BiColorParticleOptions> type) {
        this(type, 1.0F, 1.0F, 0.0F);
    }

    public static final Function<ParticleType<BiColorParticleOptions>, Codec<BiColorParticleOptions>> CODEC = type -> RecordCodecBuilder
            .create(builder -> builder.group(
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.FLOAT.fieldOf("duration").forGetter(o -> o.duration),
                    Codec.FLOAT.fieldOf("delay").forGetter(o -> o.delay),
                    Codec.INT.fieldOf("rgba0").forGetter(o -> o.rgba0),
                    Codec.INT.fieldOf("rgba1").forGetter(o -> o.rgba1)).apply(builder,
                            (size, duration, delay, rgba0, rgba1) -> new BiColorParticleOptions(type, size, duration,
                                    delay, rgba0, rgba1)));

    public static final StreamCodec<FriendlyByteBuf, BiColorParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, o) -> {
                buf.writeFloat(o.size);
                buf.writeFloat(o.duration);
                buf.writeFloat(o.delay);
                buf.writeInt(o.rgba0);
                buf.writeInt(o.rgba1);
            },
            buf -> new BiColorParticleOptions(
                    ModParticles.BICOLOR.get(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readInt(),
                    buf.readInt()));
}
