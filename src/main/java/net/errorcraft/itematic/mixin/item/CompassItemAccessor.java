package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.CompassItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Optional;

@Mixin(CompassItem.class)
public interface CompassItemAccessor {
    @Invoker("getLodestoneDimension")
    static Optional<RegistryKey<World>> getLodestoneDimension(NbtCompound nbt) {
        throw new AssertionError();
    }
}
