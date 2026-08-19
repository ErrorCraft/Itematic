package net.errorcraft.itematic.world.level.storage.loot.predicates;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.minecraft.core.Direction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import java.util.Set;

public record SideCheckPredicate(Set<Direction> sides) implements LootItemCondition {
    public static final MapCodec<SideCheckPredicate> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SetCodec.forEnum(Direction.CODEC).fieldOf("sides").forGetter(SideCheckPredicate::sides)
    ).apply(instance, SideCheckPredicate::new));

    public static LootItemCondition.Builder builder(Direction... sides) {
        return () -> new SideCheckPredicate(Set.of(sides));
    }

    @Override
    public LootItemConditionType getType() {
        return ItematicPredicateTypes.SIDE_CHECK;
    }

    @Override
    public boolean test(LootContext context) {
        return this.sides.contains(context.getOptionalParameter(ItematicContextKeys.SIDE));
    }
}
