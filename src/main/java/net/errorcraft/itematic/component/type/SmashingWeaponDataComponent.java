package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public record SmashingWeaponDataComponent(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
    public static final Codec<SmashingWeaponDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HitSounds.CODEC.fieldOf("hit_sounds").forGetter(SmashingWeaponDataComponent::hitSounds),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("smash_attack_fall_distance").forGetter(SmashingWeaponDataComponent::smashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("heavy_smash_attack_fall_distance").forGetter(SmashingWeaponDataComponent::heavySmashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("knockback_power").forGetter(SmashingWeaponDataComponent::knockbackPower)
    ).apply(instance, SmashingWeaponDataComponent::new));
    public static final PacketCodec<RegistryByteBuf, SmashingWeaponDataComponent> PACKET_CODEC = PacketCodec.tuple(
        HitSounds.PACKET_CODEC, SmashingWeaponDataComponent::hitSounds,
        PacketCodecs.DOUBLE, SmashingWeaponDataComponent::smashAttackFallDistance,
        PacketCodecs.DOUBLE, SmashingWeaponDataComponent::heavySmashAttackFallDistance,
        PacketCodecs.DOUBLE, SmashingWeaponDataComponent::knockbackPower,
        SmashingWeaponDataComponent::new
    );

    public static SmashingWeaponDataComponent of(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
        return new SmashingWeaponDataComponent(hitSounds, smashAttackFallDistance, heavySmashAttackFallDistance, knockbackPower);
    }

    public boolean canSmash(LivingEntity attacker) {
        return attacker.fallDistance > this.smashAttackFallDistance && !attacker.isGliding();
    }

    public DamageSource damageSource(LivingEntity attacker) {
        if (this.canSmash(attacker)) {
            return attacker.getDamageSources().maceSmash(attacker);
        }

        return null;
    }

    public record HitSounds(RegistryEntry<SoundEvent> inAir, RegistryEntry<SoundEvent> onGroundSmallFallDistance, RegistryEntry<SoundEvent> onGroundLargeFallDistance) {
        public static final Codec<HitSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.ENTRY_CODEC.fieldOf("in_air").forGetter(HitSounds::inAir),
            SoundEvent.ENTRY_CODEC.fieldOf("on_ground_small_fall_distance").forGetter(HitSounds::onGroundSmallFallDistance),
            SoundEvent.ENTRY_CODEC.fieldOf("on_ground_large_fall_distance").forGetter(HitSounds::onGroundLargeFallDistance)
        ).apply(instance, HitSounds::new));
        public static final PacketCodec<RegistryByteBuf, HitSounds> PACKET_CODEC = PacketCodec.tuple(
            SoundEvent.ENTRY_PACKET_CODEC, HitSounds::inAir,
            SoundEvent.ENTRY_PACKET_CODEC, HitSounds::onGroundSmallFallDistance,
            SoundEvent.ENTRY_PACKET_CODEC, HitSounds::onGroundLargeFallDistance,
            HitSounds::new
        );

        public static HitSounds of(RegistryEntry<SoundEvent> inAir, RegistryEntry<SoundEvent> onGroundSmallFallDistance, RegistryEntry<SoundEvent> onGroundLargeFallDistance) {
            return new HitSounds(inAir, onGroundSmallFallDistance, onGroundLargeFallDistance);
        }
    }
}
