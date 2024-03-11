package net.errorcraft.itematic.block.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.block.entity.Sherds;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

public class SherdsUtil {
    private SherdsUtil() {}

    public static ItemStack getStackWith(Sherds sherds, RegistryWrapper.WrapperLookup lookup) {
        RegistryEntry.Reference<Item> entry = lookup.getWrapperOrThrow(RegistryKeys.ITEM).getOrThrow(ItemKeys.DECORATED_POT);
        ItemStack stack = new ItemStack(entry);
        stack.set(DataComponentTypes.POT_DECORATIONS, sherds);
        return stack;
    }

    public static Sherds fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup lookup) {
        if (nbt == null || !nbt.contains(DecoratedPotBlockEntity.SHERDS_NBT_KEY)) {
            return Sherds.DEFAULT;
        }
        return Sherds.CODEC.parse(lookup.getOps(NbtOps.INSTANCE), nbt.get(DecoratedPotBlockEntity.SHERDS_NBT_KEY))
            .result()
            .orElse(Sherds.DEFAULT);
    }
}
