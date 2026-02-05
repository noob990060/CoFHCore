package cofh.core.client.particle.impl;

import cofh.core.client.particle.SpriteParticle;
import cofh.core.client.particle.options.ColorParticleOptions;
import cofh.core.util.helpers.RenderHelper;
import cofh.lib.util.helpers.MathHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;

public class BlastParticle extends SpriteParticle {

    private BlastParticle(ColorParticleOptions data, ClientLevel level, SpriteSet sprites, double x, double y, double z, double dx, double dy, double dz) {

        super(data, level, sprites, x, y, z, dx, dy, dz);
        gravity = -0.1F;
        friction = 0.9F;
        oRoll = roll = 0.5F * (float) random.nextGaussian();
    }

    @Override
    public ParticleRenderType getRenderType() {

        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float pTicks, double x, double y, double z) {

        return RenderHelper.FULL_BRIGHT;
    }

    @Override
    public void move(double dx, double dy, double dz) {

    }

    @Override
    protected void updateVelocity() {

    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, VertexConsumer consumer, int packedLight, float time, float pTicks) {
        // Use the same rendering logic as SpriteParticle but with the provided parameters
        Vec3 camPos = new Vec3(0, 0, 0); // Camera position is already accounted for in the CoFHParticle.render method
        
        float x = (float) (Mth.lerp(pTicks, xo, this.x) - camPos.x);
        float y = (float) (Mth.lerp(pTicks, yo, this.y) - camPos.y);
        float z = (float) (Mth.lerp(pTicks, zo, this.z) - camPos.z) + 0.1F;

        float rot = MathHelper.interpolate(oRoll, roll, pTicks);
        float sin = MathHelper.sin(rot);
        float cos = MathHelper.cos(rot);
        float w = size * 0.5F;
        float a = w * (cos - sin);
        float b = w * (sin + cos);

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV1();

        int light = getLightColor(pTicks);

        consumer.addVertex(x + a, y + b, z);
        consumer.setUv(u1, v0);
        consumer.setColor(c0.r, c0.g, c0.b, c0.a);
        consumer.setLight(light);

        consumer.addVertex(x - b, y + a, z);
        consumer.setUv(u0, v0);
        consumer.setColor(c0.r, c0.g, c0.b, c0.a);
        consumer.setLight(light);

        consumer.addVertex(x - a, y - b, z);
        consumer.setUv(u0, v1);
        consumer.setColor(c0.r, c0.g, c0.b, c0.a);
        consumer.setLight(light);

        consumer.addVertex(x + b, y - a, z);
        consumer.setUv(u1, v1);
        consumer.setColor(c0.r, c0.g, c0.b, c0.a);
        consumer.setLight(light);
    }

    @Nonnull
    public static ParticleProvider<ColorParticleOptions> factory(SpriteSet spriteSet) {

        return (data, level, x, y, z, dx, dy, dz) -> new BlastParticle(data, level, spriteSet, x, y, z, dx, dy, dz);
    }

}
