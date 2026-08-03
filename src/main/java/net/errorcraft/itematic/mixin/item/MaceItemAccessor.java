package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MaceItem.class)
public interface MaceItemAccessor {
    @Accessor("HEAVY_SMASH_SOUND_FALL_DISTANCE_THRESHOLD")
    static float heavySmashAttackFallDistance() {
        throw new AssertionError();
    }

    @Accessor("KNOCKBACK_POWER")
    static float knockbackPower() {
        throw new AssertionError();
    }
}
