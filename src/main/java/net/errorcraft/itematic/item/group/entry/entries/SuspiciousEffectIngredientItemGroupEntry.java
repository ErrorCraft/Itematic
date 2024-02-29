package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class SuspiciousEffectIngredientItemGroupEntry extends ItemGroupEntry {
    public static final Codec<SuspiciousEffectIngredientItemGroupEntry> CODEC = RecordCodecBuilder.create(instance -> createCodec(instance).and(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(SuspiciousEffectIngredientItemGroupEntry::item)
    ).apply(instance, SuspiciousEffectIngredientItemGroupEntry::new));

    private final RegistryEntry<Item> item;

    public SuspiciousEffectIngredientItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public RegistryEntry<Item> item() {
        return this.item;
    }

    public static SuspiciousEffectIngredientItemGroupEntry of(RegistryEntry<Item> item) {
        return new SuspiciousEffectIngredientItemGroupEntry(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        Set<ItemStack> set = ItemStackSet.create();
        context.lookup().getWrapperOrThrow(RegistryKeys.ITEM)
            .streamEntries()
            .map(RegistryEntry::value)
            .map(item -> item.itematic$getComponent(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .forEach(c -> {
                ItemStack stack = new ItemStack(this.item);
                SuspiciousStewItem.writeEffectsToStew(stack, c.effects());
                set.add(stack);
            });
        return set;
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.SUSPICIOUS_EFFECT_INGREDIENT;
    }
}
