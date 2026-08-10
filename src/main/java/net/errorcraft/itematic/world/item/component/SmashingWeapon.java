package net.errorcraft.itematic.world.item.component;

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
import org.jspecify.annotations.Nullable;

public record SmashingWeapon(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
    public static final Codec<SmashingWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        HitSounds.CODEC.fieldOf("hit_sounds").forGetter(SmashingWeapon::hitSounds),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("smash_attack_fall_distance").forGetter(SmashingWeapon::smashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("heavy_smash_attack_fall_distance").forGetter(SmashingWeapon::heavySmashAttackFallDistance),
        ItematicCodecs.POSITIVE_DOUBLE.fieldOf("knockback_power").forGetter(SmashingWeapon::knockbackPower)
    ).apply(instance, SmashingWeapon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SmashingWeapon> PACKET_CODEC = StreamCodec.composite(
        HitSounds.PACKET_CODEC, SmashingWeapon::hitSounds,
        ByteBufCodecs.DOUBLE, SmashingWeapon::smashAttackFallDistance,
        ByteBufCodecs.DOUBLE, SmashingWeapon::heavySmashAttackFallDistance,
        ByteBufCodecs.DOUBLE, SmashingWeapon::knockbackPower,
        SmashingWeapon::new
    );

    public static SmashingWeapon of(HitSounds hitSounds, double smashAttackFallDistance, double heavySmashAttackFallDistance, double knockbackPower) {
        return new SmashingWeapon(hitSounds, smashAttackFallDistance, heavySmashAttackFallDistance, knockbackPower);
    }

    public boolean canSmash(LivingEntity attacker) {
        return attacker.fallDistance > this.smashAttackFallDistance && !attacker.isFallFlying();
    }

    @Nullable
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
