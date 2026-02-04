package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemStackSet;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class SuspiciousEffectIngredientItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<SuspiciousEffectIngredientItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, SuspiciousEffectIngredientItemGroupEntry::new));

    private final RegistryEntry<Item> item;

    public SuspiciousEffectIngredientItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static SuspiciousEffectIngredientItemGroupEntry of(RegistryEntry<Item> item) {
        return new SuspiciousEffectIngredientItemGroupEntry(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.SUSPICIOUS_EFFECT_INGREDIENT;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        Set<ItemStack> set = ItemStackSet.create();
        context.lookup().getOrThrow(RegistryKeys.ITEM)
            .streamEntries()
            .map(RegistryEntry::value)
            .map(item -> item.itematic$getBehavior(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT))
            .flatMap(Optional::stream)
            .forEach(c -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS, c.getStewEffects());
                set.add(stack);
            });
        return set;
    }
}
