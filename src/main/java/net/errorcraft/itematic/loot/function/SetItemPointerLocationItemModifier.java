package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;

public class SetItemPointerLocationItemModifier extends ConditionalLootFunction {
    public static final MapCodec<SetItemPointerLocationItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> addConditionsField(instance).and(
        PositionTarget.CODEC.fieldOf("position").forGetter(split -> split.position)
    ).apply(instance, SetItemPointerLocationItemModifier::new));

    private final PositionTarget position;

    public SetItemPointerLocationItemModifier(PositionTarget position) {
        this(List.of(), position);
    }

    public SetItemPointerLocationItemModifier(List<LootCondition> conditions, PositionTarget position) {
        super(conditions);
        this.position = position;
    }

    public static Builder<?> builder(PositionTarget position) {
        return builder(conditions -> new SetItemPointerLocationItemModifier(conditions, position));
    }

    @Override
    public LootFunctionType<SetItemPointerLocationItemModifier> getType() {
        return ItematicItemModifierTypes.SET_ITEM_POINTER_LOCATION;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        Vec3d pos = context.get(this.position.parameter());
        if (pos == null) {
            return stack;
        }

        stack.set(
            DataComponentTypes.LODESTONE_TRACKER,
            new LodestoneTrackerComponent(
                Optional.of(GlobalPos.create(
                    context.getWorld().getRegistryKey(),
                    BlockPos.ofFloored(pos)
                )),
                true
            )
        );
        return stack;
    }
}
