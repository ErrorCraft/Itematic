package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

import java.util.Collection;
import java.util.List;

public final class StackItemGroupEntry extends ItemGroupEntry {
    public static final MapCodec<StackItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).fieldOf("item").forGetter(StackItemGroupEntry::item),
        ComponentChanges.CODEC.optionalFieldOf("components", ComponentChanges.EMPTY).forGetter(StackItemGroupEntry::components)
    )).apply(instance, StackItemGroupEntry::new));

    private final RegistryEntry<Item> item;
    private final ComponentChanges components;

    public StackItemGroupEntry(RegistryEntry<Item> item) {
        this(item, ComponentChanges.EMPTY);
    }

    public StackItemGroupEntry(RegistryEntry<Item> item, ComponentChanges components) {
        this.item = item;
        this.components = components;
    }

    public StackItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions, RegistryEntry<Item> item, ComponentChanges components) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.components = components;
    }

    public static Builder builder(RegistryEntry<Item> item) {
        return new Builder(item);
    }

    private RegistryEntry<Item> item() {
        return this.item;
    }

    private ComponentChanges components() {
        return this.components;
    }

    @Override
    protected Collection<ItemStack> createStacks(ItemGroup.DisplayContext context) {
        return List.of(new ItemStack(this.item, 1, this.components));
    }

    @Override
    protected ItemGroupEntryType type() {
        return ItemGroupEntryType.STACK;
    }

    @Override
    protected Either<RegistryEntry<Item>, ItemGroupEntry> createEither() {
        if (this.isSimple()) {
            return Either.left(this.item);
        }
        return super.createEither();
    }

    public static ItemGroupEntry fromStack(ItemStack stack) {
        return new StackItemGroupEntry(stack.getRegistryEntry(), stack.getComponentChanges());
    }

    public static ItemGroupEntry fromStack(ItemStack stack, boolean requiresPermissions) {
        return new StackItemGroupEntry(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, requiresPermissions, stack.getRegistryEntry(), stack.getComponentChanges());
    }

    private boolean isSimple() {
        return this.visibility() == ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
            && !this.requiresPermissions()
            && this.components.isEmpty();
    }

    public static class Builder {
        private final RegistryEntry<Item> item;
        private ComponentChanges components;
        private ItemGroup.StackVisibility visibility = ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS;
        private boolean requiresPermissions = false;

        public Builder(RegistryEntry<Item> item) {
            this.item = item;
        }

        public StackItemGroupEntry build() {
            return new StackItemGroupEntry(this.visibility, this.requiresPermissions, this.item, this.components == null ? ComponentChanges.EMPTY : this.components);
        }

        public Builder components(ComponentChanges.Builder builder) {
            this.components = builder.build();
            return this;
        }

        public Builder visibility(ItemGroup.StackVisibility visibility) {
            this.visibility = visibility;
            return this;
        }

        public Builder requiresPermissions() {
            this.requiresPermissions = true;
            return this;
        }
    }
}
