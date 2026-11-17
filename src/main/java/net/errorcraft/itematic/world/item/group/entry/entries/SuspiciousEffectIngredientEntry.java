package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class SuspiciousEffectIngredientEntry extends PossiblyHiddenItemGroupEntry<SuspiciousEffectIngredientEntry> {
    public static final MapCodec<SuspiciousEffectIngredientEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(
        Item.CODEC.fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, SuspiciousEffectIngredientEntry::new));

    private final Holder<Item> item;

    public SuspiciousEffectIngredientEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static SuspiciousEffectIngredientEntry of(Holder<Item> item) {
        return new SuspiciousEffectIngredientEntry(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    @Override
    public MapCodec<SuspiciousEffectIngredientEntry> codec() {
        return CODEC;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
        context.holders().lookupOrThrow(Registries.ITEM)
            .listElements()
            .map(Holder::value)
            .map(item -> item.components().get(ItematicDataComponents.PROVIDES_SUSPICIOUS_STEW_EFFECTS))
            .filter(Objects::nonNull)
            .forEach(providesSuspiciousStewEffects -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, providesSuspiciousStewEffects.getSuspiciousEffects());
                set.add(stack);
            });
        return set;
    }
}
