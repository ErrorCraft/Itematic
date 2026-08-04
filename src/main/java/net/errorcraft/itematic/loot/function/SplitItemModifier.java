package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;

import java.util.List;

public class SplitItemModifier extends LootItemConditionalFunction {
    public static final MapCodec<SplitItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(
        NumberProviders.CODEC.fieldOf("count").forGetter(split -> split.count)
    ).apply(instance, SplitItemModifier::new));

    private final NumberProvider count;

    public SplitItemModifier(NumberProvider count) {
        this(List.of(), count);
    }

    public SplitItemModifier(List<LootItemCondition> conditions, NumberProvider count) {
        super(conditions);
        this.count = count;
    }

    public static net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction.Builder<?> builder(int count) {
        return simpleBuilder(conditions -> new SplitItemModifier(conditions, ConstantValue.exactly(count)));
    }

    @Override
    public LootItemFunctionType<SplitItemModifier> getType() {
        return ItematicItemModifierTypes.SPLIT;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        LivingEntity holder = context.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof LivingEntity target ? target : null;
        return stack.itematic$copyOrSplit(holder, this.count.getInt(context));
    }
}
