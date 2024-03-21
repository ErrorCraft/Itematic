package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class DyeItemModifier extends ConditionalLootFunction {
    public static final Codec<DyeItemModifier> CODEC = RecordCodecBuilder.create(instance -> addConditionsField(instance).and(
        Codec.floatRange(0.0f, 1.0f).listOf().fieldOf("chances").forGetter(DyeItemModifier::chances)
    ).apply(instance, DyeItemModifier::new));

    private final List<Float> chances;

    public DyeItemModifier(List<LootCondition> conditions, List<Float> chances) {
        super(conditions);
        this.chances = chances;
    }

    public List<Float> chances() {
        return this.chances;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        if (!stack.itematic$hasComponent(ItemComponentTypes.DYEABLE)) {
            return stack;
        }
        List<DyeItem> dyes = new ArrayList<>();
        Random random = context.getRandom();
        for (float chance : this.chances) {
            if (random.nextFloat() < chance) {
                dyes.add(dye(random));
            }
        }
        if (dyes.isEmpty()) {
            return stack;
        }
        return DyedColorComponent.setColor(stack, dyes);
    }

    @Override
    public LootFunctionType getType() {
        return ItematicItemModifierTypes.DYE;
    }

    public static DyeItemModifier of(Float... chances) {
        return new DyeItemModifier(List.of(), List.of(chances));
    }

    private DyeItem dye(Random random) {
        // Using DyeItem is intended, so we don't have to copy the entire DyedColorComponent::setColor method
        DyeColor dye = DyeColor.values()[random.nextInt(DyeColor.values().length)];
        return DyeItem.byColor(dye);
    }
}
