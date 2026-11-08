package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.stream.IntStream;

public record EnchantmentEntry(Holder<Item> item) implements ItemGroupEntry<EnchantmentEntry> {
    public static final MapCodec<EnchantmentEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Item.CODEC.fieldOf("item").forGetter(EnchantmentEntry::item)
    ).apply(instance, EnchantmentEntry::new));

    public static EnchantmentEntry of(Holder<Item> item) {
        return new EnchantmentEntry(item);
    }

    @Override
    public MapCodec<EnchantmentEntry> codec() {
        return CODEC;
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

    private ItemStack createStack(Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment, int level) {
        ItemStack stack = new ItemStack(this.item);
        stack.enchant(enchantment, level);
        return stack;
    }

    private static CreativeModeTab.TabVisibility visibility(Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment, int level) {
        if (enchantment.value().getMaxLevel() == level) {
            return CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
        }

        return CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY;
    }
}
