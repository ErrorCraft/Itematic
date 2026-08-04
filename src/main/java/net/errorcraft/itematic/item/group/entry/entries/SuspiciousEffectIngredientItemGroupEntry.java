package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackLinkedSet;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public class SuspiciousEffectIngredientItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<SuspiciousEffectIngredientItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, SuspiciousEffectIngredientItemGroupEntry::new));

    private final Holder<Item> item;

    public SuspiciousEffectIngredientItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static SuspiciousEffectIngredientItemGroupEntry of(Holder<Item> item) {
        return new SuspiciousEffectIngredientItemGroupEntry(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.SUSPICIOUS_EFFECT_INGREDIENT;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        Set<ItemStack> set = ItemStackLinkedSet.createTypeAndComponentsSet();
        context.holders().lookupOrThrow(Registries.ITEM)
            .listElements()
            .map(Holder::value)
            .map(item -> item.itematic$getBehavior(ItemComponentTypes.SUSPICIOUS_EFFECT_INGREDIENT))
            .flatMap(Optional::stream)
            .forEach(c -> {
                ItemStack stack = new ItemStack(this.item);
                stack.set(DataComponents.SUSPICIOUS_STEW_EFFECTS, c.getSuspiciousEffects());
                set.add(stack);
            });
        return set;
    }
}
