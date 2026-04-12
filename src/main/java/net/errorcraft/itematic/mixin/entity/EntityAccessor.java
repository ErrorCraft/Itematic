package net.errorcraft.itematic.mixin.entity;

import net.minecraft.component.ComponentsAccess;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker("copyComponentsFrom")
    void itematic$copyComponentsFrom(ComponentsAccess from);
}
