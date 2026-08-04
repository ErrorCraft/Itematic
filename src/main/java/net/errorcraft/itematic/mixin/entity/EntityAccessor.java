package net.errorcraft.itematic.mixin.entity;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("applyImplicitComponents")
    void itematic$copyComponentsFrom(DataComponentGetter from);
}
