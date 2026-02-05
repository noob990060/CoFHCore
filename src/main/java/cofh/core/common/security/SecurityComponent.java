package cofh.core.common.security;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record SecurityComponent(UUID owner, boolean isPublic) {

    public static final Codec<SecurityComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            UUIDUtil.STRING_CODEC.fieldOf("owner").forGetter(SecurityComponent::owner),
            Codec.BOOL.fieldOf("public").forGetter(SecurityComponent::isPublic)).apply(inst, SecurityComponent::new));
}
