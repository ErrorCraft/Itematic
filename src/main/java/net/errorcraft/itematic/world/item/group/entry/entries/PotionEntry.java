package net.errorcraft.itematic.world.item.group.entry.entries;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.item.alchemy.PotionContentsUtil;
import net.errorcraft.itematic.world.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class PotionEntry extends PossiblyHiddenItemGroupEntry<PotionEntry> {
    public static final MapCodec<PotionEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> codec(instance).and(
        Item.CODEC.fieldOf("item").forGetter(entry -> entry.item)
    ).apply(instance, PotionEntry::new));

    private final Holder<Item> item;

    public PotionEntry(Holder<Item> item) {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false, item);
    }

    public PotionEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item) {
        super(visibility, requiresPermissions);
        this.item = item;
    }

    public static PotionEntry of(Holder<Item> item) {
        return new PotionEntry(item);
    }

    @Override
    public MapCodec<PotionEntry> codec() {
        return CODEC;
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return context.holders()
            .lookupOrThrow(Registries.POTION)
            .listElements()
            .map(entry -> PotionContentsUtil.setPotion(new ItemStack(this.item), entry))
            .toList();
    }
}
