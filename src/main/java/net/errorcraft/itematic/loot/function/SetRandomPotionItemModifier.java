package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.potion.Potion;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;

import java.util.List;
import java.util.Optional;

public class SetRandomPotionItemModifier extends ConditionalLootFunction {
    public static final MapCodec<SetRandomPotionItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> addConditionsField(instance).and(
        RegistryCodecs.entryList(RegistryKeys.POTION).optionalFieldOf("options").forGetter(modifier -> modifier.options)
    ).apply(instance, SetRandomPotionItemModifier::new));

    private final Optional<RegistryEntryList<Potion>> options;

    public static SetRandomPotionItemModifier of(RegistryEntryList<Potion> options) {
        return new SetRandomPotionItemModifier(List.of(), Optional.of(options));
    }

    public SetRandomPotionItemModifier(List<LootCondition> conditions, Optional<RegistryEntryList<Potion>> options) {
        super(conditions);
        this.options = options;
    }

    @Override
    public LootFunctionType<? extends ConditionalLootFunction> getType() {
        return ItematicItemModifierTypes.SET_RANDOM_POTION;
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        Optional<? extends RegistryEntry<Potion>> optionalPotion = this.options.flatMap(potions -> potions.getRandom(context.getRandom()));
        if (optionalPotion.isEmpty()) {
            optionalPotion = context.getWorld().getRegistryManager().get(RegistryKeys.POTION).getRandom(context.getRandom());
        }

        optionalPotion.ifPresent(potion -> stack.apply(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT, potion, PotionContentsComponent::with));
        return stack;
    }
}
