package net.errorcraft.itematic.item.group.entry;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringIdentifiable;

import java.util.Collection;

public abstract class PossiblyHiddenItemGroupEntry implements ItemGroupEntry {
    private final ItemGroup.StackVisibility visibility;
    private final boolean requiresPermissions;

    protected PossiblyHiddenItemGroupEntry(ItemGroup.StackVisibility visibility, boolean requiresPermissions) {
        this.visibility = visibility;
        this.requiresPermissions = requiresPermissions;
    }

    protected PossiblyHiddenItemGroupEntry() {
        this(ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS, false);
    }

    @Override
    public final void addStacks(ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        if (this.requiresPermissions && !context.hasPermissions()) {
            return;
        }

        entries.addAll(this.createStacks(context), this.visibility);
    }

    @Override
    public Either<RegistryEntry<Item>, ItemGroupEntry> createEither() {
        return Either.right(this);
    }

    protected abstract Collection<ItemStack> createStacks(ItemGroup.DisplayContext context);

    protected ItemGroup.StackVisibility visibility() {
        return this.visibility;
    }

    protected boolean requiresPermissions() {
        return this.requiresPermissions;
    }

    protected static <T extends PossiblyHiddenItemGroupEntry> Products.P2<RecordCodecBuilder.Mu<T>, ItemGroup.StackVisibility, Boolean> createCodec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
            StringIdentifiable.createCodec(ItemGroup.StackVisibility::values).optionalFieldOf("visibility", ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS).forGetter(PossiblyHiddenItemGroupEntry::visibility),
            Codec.BOOL.optionalFieldOf("requires_permissions", false).forGetter(PossiblyHiddenItemGroupEntry::requiresPermissions)
        );
    }
}
