package net.errorcraft.itematic.world.level.storage.loot.functions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class SetItemPointerLocationItemModifier extends LootItemConditionalFunction {
    public static final MapCodec<SetItemPointerLocationItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(
        PositionTarget.CODEC.fieldOf("position").forGetter(split -> split.position)
    ).apply(instance, SetItemPointerLocationItemModifier::new));

    private final PositionTarget position;

    public SetItemPointerLocationItemModifier(PositionTarget position) {
        this(List.of(), position);
    }

    public SetItemPointerLocationItemModifier(List<LootItemCondition> conditions, PositionTarget position) {
        super(conditions);
        this.position = position;
    }

    public static Builder<?> builder(PositionTarget position) {
        return simpleBuilder(conditions -> new SetItemPointerLocationItemModifier(conditions, position));
    }

    @Override
    public MapCodec<? extends LootItemConditionalFunction> codec() {
        return CODEC;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Vec3 pos = context.getOptionalParameter(this.position.contextParam());
        if (pos == null) {
            return stack;
        }

        stack.set(
            DataComponents.LODESTONE_TRACKER,
            new LodestoneTracker(
                Optional.of(GlobalPos.of(
                    context.getLevel().dimension(),
                    BlockPos.containing(pos)
                )),
                true
            )
        );
        return stack;
    }
}
