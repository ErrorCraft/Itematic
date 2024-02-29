package net.errorcraft.itematic.item.group.entry;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.entries.StackItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.entries.TagItemGroupEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.dynamic.Codecs;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public abstract class ItemGroupEntry {
    public static final Codec<ItemGroupEntry> ENTRY_CODEC = StringIdentifiable.createCodec(ItemGroupEntryType::values).dispatch(ItemGroupEntry::type, ItemGroupEntryType::codec);
    public static final Codec<ItemGroupEntry> CODEC = Codec.either(RegistryFixedCodec.of(RegistryKeys.ITEM), ENTRY_CODEC).xmap(either -> either.map(StackItemGroupEntry::new, Function.identity()), ItemGroupEntry::createEither);

    private final ItemGroup.StackVisibility visibility;
    private final boolean requiresPermissions;

    protected ItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions) {
        this.visibility = visibility;
        this.requiresPermissions = requiresPermissions;
    }

    protected ItemGroupEntry() {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false);
    }

    protected ItemGroup.StackVisibility visibility() {
        return visibility;
    }

    protected boolean requiresPermissions() {
        return requiresPermissions;
    }

    protected abstract Collection<ItemStack> createStacks(ItemGroup.DisplayContext context);
    protected abstract ItemGroupEntryType type();

    public final void addStacks(ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        if (this.requiresPermissions && !context.hasPermissions()) {
            return;
        }
        entries.addAll(this.createStacks(context), this.visibility);
    }

    protected Either<RegistryEntry<Item>, ItemGroupEntry> createEither() {
        return Either.right(this);
    }

    public static ItemGroupEntry simple(RegistryEntry<Item> item) {
        return new StackItemGroupEntry(item);
    }

    public static ItemGroupEntry requiresPermissions(RegistryEntry<Item> item) {
        return new StackItemGroupEntry(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, true, item, Optional.empty());
    }

    public static ItemGroupEntry tag(TagKey<Item> tag) {
        return new TagItemGroupEntry(tag);
    }

    protected static <T extends ItemGroupEntry> Products.P2<RecordCodecBuilder.Mu<T>, ItemGroup.StackVisibility, Boolean> createCodec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
            StringIdentifiable.createCodec(ItemGroup.StackVisibility::values).optionalFieldOf("visibility", ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS).forGetter(ItemGroupEntry::visibility),
            Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "requires_permissions", false).forGetter(ItemGroupEntry::requiresPermissions)
        );
    }
}
