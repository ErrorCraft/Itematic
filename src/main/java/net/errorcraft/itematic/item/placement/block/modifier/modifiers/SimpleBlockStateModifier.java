package net.errorcraft.itematic.item.placement.block.modifier.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifier;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifierType;
import net.errorcraft.itematic.item.placement.block.modifier.BlockStateModifierTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.jetbrains.annotations.Nullable;

public record SimpleBlockStateModifier(RegistryEntry<Block> block) implements BlockStateModifier<SimpleBlockStateModifier> {
    public static final Codec<SimpleBlockStateModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.BLOCK).fieldOf("block").forGetter(SimpleBlockStateModifier::block)
    ).apply(instance, SimpleBlockStateModifier::new));

    @Override
    public BlockStateModifierType<SimpleBlockStateModifier> type() {
        return BlockStateModifierTypes.SIMPLE;
    }

    @Override
    public RegistryEntry<Block> defaultBlock() {
        return this.block;
    }

    @Override
    public @Nullable BlockState placementState(ItemPlacementContext context) {
        return this.block.value().getPlacementState(context);
    }
}
