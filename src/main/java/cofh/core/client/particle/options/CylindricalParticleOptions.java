package cofh.core.client.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class CylindricalParticleOptions extends ColorParticleOptions {

    public final float height;

    public CylindricalParticleOptions(ParticleType<? extends CylindricalParticleOptions> type, float size, float duration, float delay, int rgba0, float height) {

        super(type, size, duration, delay, rgba0);
        this.height = height;
    }

    public CylindricalParticleOptions(ParticleType<? extends CylindricalParticleOptions> type, float size, float duration, float delay, float height) {

        this(type, size, duration, delay, 0xFFFFFFFF, height);
    }

    public CylindricalParticleOptions(ParticleType<? extends CylindricalParticleOptions> type, float size, float duration, float height) {

        this(type, size, duration, 0.0F, height);
    }

    public CylindricalParticleOptions(ParticleType<? extends CylindricalParticleOptions> type) {

        this(type, 1.0F, 1.0F, 1.0F);
    }

    protected CylindricalParticleOptions(ParticleType<? extends CylindricalParticleOptions> type, StringReader reader) throws CommandSyntaxException {

        this(type, 1.0F, 1.0F, 1.0F);
        // TODO: Implement command parsing if needed
    }

    public static final Function<ParticleType<CylindricalParticleOptions>, Codec<CylindricalParticleOptions>> CODEC = (type) -> RecordCodecBuilder.create(
            (builder) -> builder.group(
                    Codec.FLOAT.fieldOf("size").forGetter((options) -> options.size),
                    Codec.FLOAT.fieldOf("duration").forGetter((options) -> options.duration),
                    Codec.FLOAT.fieldOf("delay").forGetter((options) -> options.delay),
                    Codec.INT.fieldOf("rgba0").forGetter((options) -> options.rgba0),
                    Codec.FLOAT.fieldOf("height").forGetter((options) -> options.height)
            ).apply(builder, (size, duration, delay, rgba, height) -> new CylindricalParticleOptions(type, size, duration, delay, rgba, height))
    );

    public static StreamCodec<FriendlyByteBuf, CylindricalParticleOptions> cylindricalStreamCodec(ParticleType<? extends CylindricalParticleOptions> type) {
        return StreamCodec.<FriendlyByteBuf, CylindricalParticleOptions>of(
                (buf, o) -> {
                    buf.writeFloat(o.size);
                    buf.writeFloat(o.duration);
                    buf.writeFloat(o.delay);
                    buf.writeInt(o.rgba0);
                    buf.writeFloat(o.height);
                },
                buf -> new CylindricalParticleOptions(
                        type,
                        buf.readFloat(),
                        buf.readFloat(),
                        buf.readFloat(),
                        buf.readInt(),
                        buf.readFloat()));
    }

}
