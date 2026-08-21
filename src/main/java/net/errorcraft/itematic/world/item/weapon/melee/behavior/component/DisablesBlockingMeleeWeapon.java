package net.errorcraft.itematic.world.item.weapon.melee.behavior.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

public record DisablesBlockingMeleeWeapon(float seconds) {
    public static final Codec<DisablesBlockingMeleeWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("seconds").forGetter(DisablesBlockingMeleeWeapon::seconds)
    ).apply(instance, DisablesBlockingMeleeWeapon::new));
}
