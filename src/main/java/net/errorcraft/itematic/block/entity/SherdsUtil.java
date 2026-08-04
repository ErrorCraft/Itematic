package net.errorcraft.itematic.block.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import net.minecraft.world.level.block.entity.PotDecorations;

public class SherdsUtil {
    private SherdsUtil() {}

    public static ItemStack addSherdsToStack(ItemStack stack, PotDecorations sherds) {
        stack.set(DataComponents.POT_DECORATIONS, sherds);
        return stack;
    }

    public static PotDecorations fromNbt(CompoundTag nbt, HolderLookup.Provider lookup) {
        if (nbt == null || !nbt.contains(DecoratedPotBlockEntity.TAG_SHERDS)) {
            return PotDecorations.EMPTY;
        }

        return PotDecorations.CODEC.parse(lookup.createSerializationContext(NbtOps.INSTANCE), nbt.get(DecoratedPotBlockEntity.TAG_SHERDS))
            .result()
            .orElse(PotDecorations.EMPTY);
    }
}
