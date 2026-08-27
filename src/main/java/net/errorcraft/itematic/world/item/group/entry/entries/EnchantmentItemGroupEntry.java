package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.stream.IntStream;

public record EnchantmentItemGroupEntry(Holder<Item> item) implements ItemGroupEntry<EnchantmentItemGroupEntry> {
    public static final MapCodec<EnchantmentItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Item.CODEC.fieldOf("item").forGetter(EnchantmentItemGroupEntry::item)
    ).apply(instance, EnchantmentItemGroupEntry::new));

    public static EnchantmentItemGroupEntry of(Holder<Item> item) {
        return new EnchantmentItemGroupEntry(item);
    }

    @Override
    public ItemGroupEntryType<EnchantmentItemGroupEntry> type() {
        return ItemGroupEntryType.ENCHANTMENT;
    }

    @Override
    public void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        context.holders().lookupOrThrow(Registries.ENCHANTMENT).listElements()
            .forEach(enchantment -> IntStream.rangeClosed(enchantment.value().getMinLevel(), enchantment.value().getMaxLevel())
                .forEach(level -> entries.accept(
                    this.createStack(enchantment, level),
                    visibility(enchantment, level)
                ))
            );
    }

    @Override
    public Either<ItemGroupEntry<?>, Holder<Item>> createEither() {
        return Either.left(this);
    }

    private ItemStack createStack(Holder<Enchantment> enchantment, int level) {
        ItemStack stack = new ItemStack(this.item);
        stack.enchant(enchantment, level);
        return stack;
    }

    private static CreativeModeTab.TabVisibility visibility(Holder<Enchantment> enchantment, int level) {
        if (enchantment.value().getMaxLevel() == level) {
            return CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
        }

        return CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
    }
}
