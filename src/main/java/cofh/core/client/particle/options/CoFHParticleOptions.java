package cofh.core.client.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class CoFHParticleOptions implements ParticleOptions {

    protected final ParticleType<? extends CoFHParticleOptions> type;
    public final float size;
    public final float duration;
    public final float delay;

    public CoFHParticleOptions(ParticleType<? extends CoFHParticleOptions> type,
            float size, float duration, float delay) {
        this.type = type;
        this.size = size;
        this.duration = duration;
        this.delay = delay;
    }

    public CoFHParticleOptions(ParticleType<? extends CoFHParticleOptions> type,
            float size, float duration) {
        this(type, size, duration, 0.0F);
    }

    public CoFHParticleOptions(ParticleType<? extends CoFHParticleOptions> type) {
        this(type, 1.0F, 1.0F, 0.0F);
    }

    @Override
    public ParticleType<? extends CoFHParticleOptions> getType() {
        return type;
    }

    /* ---------------- Serialization ---------------- */

    public static final Function<ParticleType<CoFHParticleOptions>, Codec<CoFHParticleOptions>> CODEC = type -> RecordCodecBuilder
            .create(builder -> builder.group(
                    Codec.FLOAT.fieldOf("size").forGetter(o -> o.size),
                    Codec.FLOAT.fieldOf("duration").forGetter(o -> o.duration),
                    Codec.FLOAT.fieldOf("delay").forGetter(o -> o.delay)).apply(builder,
                            (size, duration, delay) -> new CoFHParticleOptions(type, size, duration, delay)));

    public static final StreamCodec<FriendlyByteBuf, CoFHParticleOptions> STREAM_CODEC = StreamCodec.of(
            (buf, o) -> {
                buf.writeFloat(o.size);
                buf.writeFloat(o.duration);
                buf.writeFloat(o.delay);
            },
            buf -> new CoFHParticleOptions(
                    ModParticles.COFH.get(), // registry object
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat()));
}
