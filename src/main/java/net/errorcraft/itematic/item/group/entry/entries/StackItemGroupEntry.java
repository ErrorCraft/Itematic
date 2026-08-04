package net.errorcraft.itematic.item.group.entry.entries;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntry;
import net.errorcraft.itematic.item.group.entry.ItemGroupEntryType;
import net.errorcraft.itematic.item.group.entry.PossiblyHiddenItemGroupEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Collection;
import java.util.List;

public final class StackItemGroupEntry extends PossiblyHiddenItemGroupEntry {
    public static final MapCodec<StackItemGroupEntry> CODEC = RecordCodecBuilder.mapCodec(instance -> createCodec(instance).and(instance.group(
        RegistryFixedCodec.create(Registries.ITEM).fieldOf("item").forGetter(entry -> entry.item),
        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(entry -> entry.components)
    )).apply(instance, StackItemGroupEntry::new));

    private final Holder<Item> item;
    private final DataComponentPatch components;

    public StackItemGroupEntry(Holder<Item> item) {
        this(item, DataComponentPatch.EMPTY);
    }

    public StackItemGroupEntry(Holder<Item> item, DataComponentPatch components) {
        this.item = item;
        this.components = components;
    }

    public StackItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions, Holder<Item> item, DataComponentPatch components) {
        super(visibility, requiresPermissions);
        this.item = item;
        this.components = components;
    }

    public static StackItemGroupEntry fromStack(ItemStack stack) {
        return new StackItemGroupEntry(stack.getItemHolder(), stack.getComponentsPatch());
    }

    public static StackItemGroupEntry fromStack(ItemStack stack, boolean requiresPermissions) {
        return new StackItemGroupEntry(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, requiresPermissions, stack.getItemHolder(), stack.getComponentsPatch());
    }

    public static Builder builder(Holder<Item> item) {
        return new Builder(item);
    }

    @Override
    public ItemGroupEntryType type() {
        return ItemGroupEntryType.STACK;
    }

    @Override
    public Either<Holder<Item>, ItemGroupEntry> createEither() {
        if (this.isSimple()) {
            return Either.left(this.item);
        }

        return super.createEither();
    }

    @Override
    protected Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context) {
        return List.of(new ItemStack(this.item, 1, this.components));
    }

    private boolean isSimple() {
        return this.visibility() == CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS
            && !this.requiresPermissions()
            && this.components.isEmpty();
    }

    public static class Builder {
        private final Holder<Item> item;
        private DataComponentPatch components;
        private CreativeModeTab.TabVisibility visibility = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

        public Builder(Holder<Item> item) {
            this.item = item;
        }

        public StackItemGroupEntry build() {
            return new StackItemGroupEntry(this.visibility, false, this.item, this.components == null ? DataComponentPatch.EMPTY : this.components);
        }

        public Builder components(DataComponentPatch.Builder builder) {
            this.components = builder.build();
            return this;
        }

        public Builder visibility(CreativeModeTab.TabVisibility visibility) {
            this.visibility = visibility;
            return this;
        }
    }
}
