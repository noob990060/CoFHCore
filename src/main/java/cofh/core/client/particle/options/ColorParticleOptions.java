package cofh.core.client.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public class ColorParticleOptions extends CoFHParticleOptions {

    public final int rgba0;

    public ColorParticleOptions(ParticleType<? extends ColorParticleOptions> type, float size, float duration, float delay, int rgba) {

        super(type, size, duration, delay);
        this.rgba0 = rgba;
    }

    public ColorParticleOptions(ParticleType<? extends ColorParticleOptions> type, float size, float duration, float delay) {

        this(type, size, duration, delay, 0xFFFFFFFF);
    }

    public ColorParticleOptions(ParticleType<? extends ColorParticleOptions> type, float size, float duration) {

        this(type, size, duration, 0.0F);
    }

    public ColorParticleOptions(ParticleType<? extends ColorParticleOptions> type) {

        this(type, 1.0F, 1.0F);
    }

    protected ColorParticleOptions(ParticleType<? extends ColorParticleOptions> type, StringReader reader) throws CommandSyntaxException {

        this(type, 1.0F, 1.0F, 0.0F);
        // TODO: Implement command parsing if needed
    }

    public static final Function<ParticleType<ColorParticleOptions>, Codec<ColorParticleOptions>> CODEC = (type) -> RecordCodecBuilder.create(
            (builder) -> builder.group(
                    Codec.FLOAT.fieldOf("size").forGetter((options) -> options.size),
                    Codec.FLOAT.fieldOf("duration").forGetter((options) -> options.duration),
                    Codec.FLOAT.fieldOf("delay").forGetter((options) -> options.delay),
                    Codec.INT.fieldOf("rgba0").forGetter((options) -> options.rgba0)
            ).apply(builder, (size, duration, delay, rgba) -> new ColorParticleOptions(type, size, duration, delay, rgba))
    );

    public static StreamCodec<FriendlyByteBuf, ColorParticleOptions> streamCodec(ParticleType<? extends ColorParticleOptions> type) {
        return StreamCodec.<FriendlyByteBuf, ColorParticleOptions>of(
                (buf, o) -> {
                    buf.writeFloat(o.size);
                    buf.writeFloat(o.duration);
                    buf.writeFloat(o.delay);
                    buf.writeInt(o.rgba0);
                },
                buf -> new ColorParticleOptions(
                        type,
                        buf.readFloat(),
                        buf.readFloat(),
                        buf.readFloat(),
                        buf.readInt()));
    }

}
