package net.errorcraft.itematic.item.pointer;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public interface Pointer {
    Codec<RegistryEntry<Pointer>> ENTRY_CODEC = RegistryFixedCodec.of(ItematicRegistryKeys.POINTER);

    @Nullable
    GlobalPos createPos(ItemStack stack, World world, Entity target);
}
