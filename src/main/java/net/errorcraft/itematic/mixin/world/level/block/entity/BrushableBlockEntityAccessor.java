package net.errorcraft.itematic.mixin.world.level.block.entity;

import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BrushableBlockEntity.class)
public interface BrushableBlockEntityAccessor {
    @Accessor("REQUIRED_BRUSHES_TO_BREAK")
    static int requiredBrushesToBreak() {
        throw new AssertionError();
    }
}
