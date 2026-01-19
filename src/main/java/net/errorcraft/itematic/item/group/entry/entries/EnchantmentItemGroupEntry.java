package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.stream.IntStream;

public record EnchantmentItemGroupEntry(RegistryEntry<Item> item) implements ItemGroupEntry {
    public static final MapCodec<EnchantmentItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(EnchantmentItemGroupEntry::item)
    ).apply(instance, EnchantmentItemGroupEntry::new));

    public static EnchantmentItemGroupEntry of(RegistryEntry<Item> item) {
        return new EnchantmentItemGroupEntry(item);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.ENCHANTMENT;
    }

    @Override
    public void addStacks(ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        context.lookup().getWrapperOrThrow(RegistryKeys.ENCHANTMENT).streamEntries()
            .forEach(enchantment -> IntStream.rangeClosed(enchantment.value().getMinLevel(), enchantment.value().getMaxLevel())
                .forEach(level -> entries.add(
                    this.createStack(enchantment, level),
                    visibility(enchantment, level)
                ))
            );
    }

    @Override
    public Either<RegistryEntry<Item>, ItemGroupEntry> createEither() {
        return Either.right(this);
    }

    private ItemStack createStack(RegistryEntry<Enchantment> enchantment, int level) {
        ItemStack stack = new ItemStack(this.item);
        stack.addEnchantment(enchantment, level);
        return stack;
    }

    private static ItemGroup.StackVisibility visibility(RegistryEntry<Enchantment> enchantment, int level) {
        if (enchantment.value().getMaxLevel() == level) {
            return ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS;
        }

        return ItemGroup.StackVisibility.SEARCH_TAB_ONLY;
    }
}
