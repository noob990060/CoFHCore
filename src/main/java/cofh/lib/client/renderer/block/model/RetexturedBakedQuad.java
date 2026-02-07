package cofh.lib.client.renderer.block.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.mojang.blaze3d.vertex.VertexFormatElement;

import java.util.Arrays;

/**
 * Revived from 1.14
 *
 * @author Mojang
 * Thanks tterrag!
 */

public class RetexturedBakedQuad extends BakedQuad {

    private final TextureAtlasSprite texture;

    public RetexturedBakedQuad(BakedQuad quad, TextureAtlasSprite textureIn) {

        super(Arrays.copyOf(quad.getVertices(), quad.getVertices().length), quad.getTintIndex(), FaceBakery.calculateFacing(quad.getVertices()), quad.getSprite(), quad.isShade());
        this.texture = textureIn != null ? textureIn : quad.getSprite();
        if (textureIn != null) {
            this.remapQuad();
        }
    }

    private void remapQuad() {

        if (this.texture == null || this.sprite == null) {
            return;
        }
        int stride = DefaultVertexFormat.BLOCK.getVertexSize() / 4;
        int uvIndex = DefaultVertexFormat.BLOCK.getOffset(VertexFormatElement.UV0) / 4;
        if (stride <= 0 || uvIndex < 0) {
            return;
        }
        for (int i = 0; i < 4; ++i) {
            int j = stride * i;
            if (j + uvIndex + 1 >= this.vertices.length) {
                return;
            }
            this.vertices[j + uvIndex] = Float.floatToRawIntBits(this.texture.getU(getUnInterpolatedU(this.sprite, Float.intBitsToFloat(this.vertices[j + uvIndex]))));
            this.vertices[j + uvIndex + 1] = Float.floatToRawIntBits(this.texture.getV(getUnInterpolatedV(this.sprite, Float.intBitsToFloat(this.vertices[j + uvIndex + 1]))));
        }
    }

    @Override
    public TextureAtlasSprite getSprite() {

        return texture;
    }

    private static float getUnInterpolatedU(TextureAtlasSprite sprite, float u) {

        float f = sprite.getU1() - sprite.getU0();
        return (u - sprite.getU0()) / f;
    }

    private static float getUnInterpolatedV(TextureAtlasSprite sprite, float v) {

        float f = sprite.getV1() - sprite.getV0();
        return (v - sprite.getV0()) / f;
    }

}
