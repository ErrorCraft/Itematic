package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.network.codec.PacketCodecUtil;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public record ImmuneToDamageComponent(TagKey<DamageType> damage) {
    public static final Codec<ImmuneToDamageComponent> CODEC = TagKey.unprefixedCodec(RegistryKeys.DAMAGE_TYPE)
        .xmap(ImmuneToDamageComponent::new, ImmuneToDamageComponent::damage);
    public static final PacketCodec<ByteBuf, ImmuneToDamageComponent> PACKET_CODEC = PacketCodecUtil.tag(RegistryKeys.DAMAGE_TYPE)
        .xmap(ImmuneToDamageComponent::new, ImmuneToDamageComponent::damage);

    public boolean damage(DamageSource source) {
        return !source.isIn(this.damage);
    }
}
