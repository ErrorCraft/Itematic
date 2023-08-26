package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class StackItemGroupEntry extends ItemGroupEntry {
    public static final Codec<StackItemGroupEntry> CODEC = RecordCodecBuilder.create(instance -> createCodec(instance).and(instance.group(
        RegistryKey.createCodec(RegistryKeys.ITEM).fieldOf("item").forGetter(StackItemGroupEntry::item),
        StringNbtReader.STRINGIFIED_CODEC.optionalFieldOf("nbt").forGetter(StackItemGroupEntry::nbt)
    )).apply(instance, StackItemGroupEntry::new));

    private final RegistryKey<Item> item;
    private final Optional<NbtCompound> nbt;

    public StackItemGroupEntry(RegistryKey<Item> item) {
        this(item, null);
    }

    public StackItemGroupEntry(RegistryKey<Item> item, NbtCompound nbt) {
        this.item = item;
        this.nbt = Optional.ofNullable(nbt);
    }

    public StackItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryKey<Item> item, Optional<NbtCompound> nbt) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.nbt = nbt;
    }

    private RegistryKey<Item> item() {
        return this.item;
    }

    private Optional<NbtCompound> nbt() {
        return this.nbt;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        RegistryWrapper.Impl<Item> items = context.lookup().getWrapperOrThrow(RegistryKeys.ITEM);
        Optional<RegistryEntry.Reference<Item>> optionalEntry = items.getOptional(this.item);
        return optionalEntry.map(ItemStack::new).map(stack -> {
            this.nbt.map(NbtCompound::copy).ifPresent(stack::setNbt);
            return stack;
        }).map(List::of).orElseGet(List::of);
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.STACK;
    }

    @Override
    protected Either<RegistryKey<Item>, ItemGroupEntry> createEither() {
        if (this.isSimple()) {
            return Either.left(this.item);
        }
        return super.createEither();
    }

    public static ItemGroupEntry fromStack(ItemStack stack) {
        return new StackItemGroupEntry(stack.getRegistryEntry().getKey().orElse(ItemKeys.AIR), stack.getNbt());
    }

    public static ItemGroupEntry fromStack(ItemStack stack, ItemGroup.StackVisibility visibility) {
        return new StackItemGroupEntry(visibility, false, stack.getRegistryEntry().getKey().orElse(ItemKeys.AIR), Optional.ofNullable(stack.getNbt()));
    }

    private boolean isSimple() {
        return this.visibility() == ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
            && !this.requiresPermissions()
            && this.nbt.isEmpty();
    }
}
