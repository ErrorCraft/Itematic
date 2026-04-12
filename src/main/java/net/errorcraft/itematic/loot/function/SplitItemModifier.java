package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;

import java.util.List;

public class SplitItemModifier extends ConditionalLootFunction {
    public static final MapCodec<SplitItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> addConditionsField(instance).and(
        LootNumberProviderTypes.CODEC.fieldOf("count").forGetter(split -> split.count)
    ).apply(instance, SplitItemModifier::new));

    private final LootNumberProvider count;

    public SplitItemModifier(LootNumberProvider count) {
        this(List.of(), count);
    }

    public SplitItemModifier(List<LootCondition> conditions, LootNumberProvider count) {
        super(conditions);
        this.count = count;
    }

    public static Builder<?> builder(int count) {
        return builder(conditions -> new SplitItemModifier(conditions, ConstantLootNumberProvider.create(count)));
    }

    @Override
    public LootFunctionType<SplitItemModifier> getType() {
        return ItematicItemModifierTypes.SPLIT;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        LivingEntity holder = context.get(LootContextParameters.THIS_ENTITY) instanceof LivingEntity target ? target : null;
        return stack.itematic$copyOrSplit(holder, this.count.nextInt(context));
    }
}
