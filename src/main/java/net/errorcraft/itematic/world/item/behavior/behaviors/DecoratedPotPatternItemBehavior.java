package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;

public record DecoratedPotPatternItemBehavior(Holder<DecoratedPotPattern> pattern) implements ItemBehavior<DecoratedPotPatternItemBehavior> {
    public static final Codec<DecoratedPotPatternItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.DECORATED_POT_PATTERN).fieldOf("pattern").forGetter(DecoratedPotPatternItemBehavior::pattern)
    ).apply(instance, DecoratedPotPatternItemBehavior::new));

    public static ItemBehavior<?>[] of(Holder<DecoratedPotPattern> pattern) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(64),
            new DecoratedPotPatternItemBehavior(pattern)
        };
    }

    @Override
    public ItemBehaviorType<DecoratedPotPatternItemBehavior> type() {
        return ItemBehaviorType.DECORATED_POT_PATTERN;
    }

}
