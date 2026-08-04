package net.errorcraft.itematic.loot.function;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;
import java.util.Optional;

public class SetRandomPotionItemModifier extends LootItemConditionalFunction {
    public static final MapCodec<SetRandomPotionItemModifier> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).and(
        RegistryCodecs.homogeneousList(Registries.POTION).optionalFieldOf("options").forGetter(modifier -> modifier.options)
    ).apply(instance, SetRandomPotionItemModifier::new));

    private final Optional<HolderSet<Potion>> options;

    public static SetRandomPotionItemModifier of(HolderSet<Potion> options) {
        return new SetRandomPotionItemModifier(List.of(), Optional.of(options));
    }

    public SetRandomPotionItemModifier(List<LootItemCondition> conditions, Optional<HolderSet<Potion>> options) {
        super(conditions);
        this.options = options;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ItematicItemModifierTypes.SET_RANDOM_POTION;
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        Optional<? extends Holder<Potion>> optionalPotion = this.options.flatMap(potions -> potions.getRandomElement(context.getRandom()));
        if (optionalPotion.isEmpty()) {
            optionalPotion = context.getLevel()
                .registryAccess()
                .lookupOrThrow(Registries.POTION)
                .getRandom(context.getRandom());
        }

        optionalPotion.ifPresent(potion -> stack.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, potion, PotionContents::withPotion));
        return stack;
    }
}
