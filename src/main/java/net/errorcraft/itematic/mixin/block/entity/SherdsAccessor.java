package net.errorcraft.itematic.mixin.block.entity;

import net.minecraft.block.entity.Sherds;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Optional;

@Mixin(Sherds.class)
public interface SherdsAccessor {
    @Invoker("<init>")
    static Sherds create(List<Optional<RegistryEntry<Item>>> sherds) {
        throw new AssertionError();
    }
    @Invoker("<init>")
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static Sherds create(Optional<RegistryEntry<Item>> back, Optional<RegistryEntry<Item>> left, Optional<RegistryEntry<Item>> right, Optional<RegistryEntry<Item>> front) {
        throw new AssertionError();
    }
}
