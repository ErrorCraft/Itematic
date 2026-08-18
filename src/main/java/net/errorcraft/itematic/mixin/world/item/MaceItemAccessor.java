package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.world.item.MaceItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MaceItem.class)
public interface MaceItemAccessor {
    @Accessor("SMASH_ATTACK_HEAVY_THRESHOLD")
    static float heavySmashAttackFallDistance() {
        throw new AssertionError();
    }

    @Accessor("SMASH_ATTACK_KNOCKBACK_POWER")
    static float knockbackPower() {
        throw new AssertionError();
    }
}
