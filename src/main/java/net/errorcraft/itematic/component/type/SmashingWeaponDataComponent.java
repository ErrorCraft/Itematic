package net.errorcraft.itematic.component.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public record SmashingWeaponDataComponent(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
    public static final Codec<SmashingWeaponDataComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HitSounds.CODEC.fieldOf("hit_sounds").forGetter(SmashingWeaponDataComponent::hitSounds),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("smash_attack_fall_distance").forGetter(SmashingWeaponDataComponent::smashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("heavy_smash_attack_fall_distance").forGetter(SmashingWeaponDataComponent::heavySmashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("knockback_power").forGetter(SmashingWeaponDataComponent::knockbackPower)
    ).apply(instance, SmashingWeaponDataComponent::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SmashingWeaponDataComponent> PACKET_CODEC = StreamCodec.composite(
        HitSounds.PACKET_CODEC, SmashingWeaponDataComponent::hitSounds,
        ByteBufCodecs.DOUBLE, SmashingWeaponDataComponent::smashAttackFallDistance,
        ByteBufCodecs.DOUBLE, SmashingWeaponDataComponent::heavySmashAttackFallDistance,
        ByteBufCodecs.DOUBLE, SmashingWeaponDataComponent::knockbackPower,
        SmashingWeaponDataComponent::new
    );

    public static SmashingWeaponDataComponent of(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
        return new SmashingWeaponDataComponent(hitSounds, smashAttackFallDistance, heavySmashAttackFallDistance, knockbackPower);
    }

    public boolean canSmash(LivingEntity attacker) {
        return attacker.fallDistance > this.smashAttackFallDistance && !attacker.isFallFlying();
    }

    public DamageSource damageSource(LivingEntity attacker) {
        if (this.canSmash(attacker)) {
            return attacker.damageSources().mace(attacker);
        }

        return null;
    }

    public record HitSounds(Holder<SoundEvent> inAir, Holder<SoundEvent> onGroundSmallFallDistance, Holder<SoundEvent> onGroundLargeFallDistance) {
        public static final Codec<HitSounds> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SoundEvent.CODEC.fieldOf("in_air").forGetter(HitSounds::inAir),
            SoundEvent.CODEC.fieldOf("on_ground_small_fall_distance").forGetter(HitSounds::onGroundSmallFallDistance),
            SoundEvent.CODEC.fieldOf("on_ground_large_fall_distance").forGetter(HitSounds::onGroundLargeFallDistance)
        ).apply(instance, HitSounds::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, HitSounds> PACKET_CODEC = StreamCodec.composite(
            SoundEvent.STREAM_CODEC, HitSounds::inAir,
            SoundEvent.STREAM_CODEC, HitSounds::onGroundSmallFallDistance,
            SoundEvent.STREAM_CODEC, HitSounds::onGroundLargeFallDistance,
            HitSounds::new
        );

        public static HitSounds of(Holder<SoundEvent> inAir, Holder<SoundEvent> onGroundSmallFallDistance, Holder<SoundEvent> onGroundLargeFallDistance) {
            return new HitSounds(inAir, onGroundSmallFallDistance, onGroundLargeFallDistance);
        }
    }
}
