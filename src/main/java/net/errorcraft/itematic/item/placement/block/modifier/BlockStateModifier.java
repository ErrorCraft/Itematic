package net.errorcraft.itematic.item.placement.block.modifier;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.placement.block.modifier.modifiers.SimpleBlockStateModifier;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;

public interface BlockStateModifier<T extends BlockStateModifier<T>> {
    Codec<BlockStateModifier<?>> ELEMENT_CODEC = ItematicRegistries.BLOCK_STATE_MODIFIER_TYPE.getCodec().dispatch("type", BlockStateModifier::type, BlockStateModifierType::codec);
    Codec<BlockStateModifier<?>> CODEC = Codecs.createLazy(() -> Codecs.either(ELEMENT_CODEC, RegistryFixedCodec.of(RegistryKeys.BLOCK)).xmap(either -> either.map(modifier -> modifier, SimpleBlockStateModifier::new), modifier -> {
        if (modifier instanceof SimpleBlockStateModifier simpleModifier) {
            return Either.right(simpleModifier.block());
        }
        return Either.left(modifier);
    }));

    BlockStateModifierType<T> type();
    RegistryEntry<Block> defaultBlock();
    @Nullable
    BlockState placementState(ItemPlacementContext context);
    default ItemPlacementContext placementContext(ItemUsageContext context) {
        return this.defaultBlock().value()
            .itematic$placementContext(new ItemPlacementContext(context));
    }
}
