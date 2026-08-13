package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import java.util.ArrayList;
import java.util.List;

public class DyeItemModifier extends LootItemConditionalFunction {
    public static final MapCodec<DyeItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(
        Codec.floatRange(0.0f, 1.0f).listOf().fieldOf("chances").forGetter(DyeItemModifier::chances)
    ).apply(instance, DyeItemModifier::new));

    private final List<Float> chances;

    public DyeItemModifier(List<LootItemCondition> conditions, List<Float> chances) {
        super(conditions);
        this.chances = chances;
    }

    public List<Float> chances() {
        return this.chances;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (!stack.itematic$hasBehavior(ItemBehaviorType.DYEABLE)) {
            return stack;
        }
        List<DyeItem> dyes = new ArrayList<>();
        RandomSource random = context.getRandom();
        for (float chance : this.chances) {
            if (random.nextFloat() < chance) {
                dyes.add(dye(random));
            }
        }
        if (dyes.isEmpty()) {
            return stack;
        }
        return DyedItemColor.applyDyes(stack, dyes);
    }

    @Override
    public LootItemFunctionType<DyeItemModifier> getType() {
        return ItematicItemModifierTypes.DYE;
    }

    public static DyeItemModifier of(Float... chances) {
        return new DyeItemModifier(List.of(), List.of(chances));
    }

    private DyeItem dye(RandomSource random) {
        // Using DyeItem is intended, so we don't have to copy the entire DyedColorComponent::setColor method
        DyeColor dye = DyeColor.values()[random.nextInt(DyeColor.values().length)];
        return DyeItem.byColor(dye);
    }
}
