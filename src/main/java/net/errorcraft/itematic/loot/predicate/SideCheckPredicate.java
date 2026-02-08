package net.errorcraft.itematic.loot.predicate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.serialization.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.util.math.Direction;

import java.util.Set;

public record SideCheckPredicate(Set<Direction> sides) implements LootCondition {
    public static final MapCodec<SideCheckPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SetCodec.forEnum(Direction.CODEC).fieldOf("sides").forGetter(SideCheckPredicate::sides)
    ).apply(instance, SideCheckPredicate::new));

    @Override
    public LootConditionType getType() {
        return ItematicPredicateTypes.SIDE_CHECK;
    }

    @Override
    public boolean test(LootContext context) {
        return this.sides.contains(context.get(ItematicContextParameters.SIDE));
    }

    public static LootCondition.Builder builder(Direction... sides) {
        return () -> new SideCheckPredicate(Set.of(sides));
    }
}
