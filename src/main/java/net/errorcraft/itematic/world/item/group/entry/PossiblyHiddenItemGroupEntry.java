package net.errorcraft.itematic.world.item.group.entry;

import com.mojang.datafixers.Products;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public abstract class PossiblyHiddenItemGroupEntry<T extends PossiblyHiddenItemGroupEntry<T>> implements ItemGroupEntry<T> {
    private static final Codec<CreativeModeTab.TabVisibility> TAB_VISIBILITY_CODEC = StringRepresentable.fromEnum(CreativeModeTab.TabVisibility::values);

    private final CreativeModeTab.TabVisibility visibility;
    private final boolean requiresPermissions;

    protected PossiblyHiddenItemGroupEntry(CreativeModeTab.TabVisibility visibility, boolean requiresPermissions) {
        this.visibility = visibility;
        this.requiresPermissions = requiresPermissions;
    }

    protected PossiblyHiddenItemGroupEntry() {
        this(CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, false);
    }

    @Override
    public final void addStacks(CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        if (this.requiresPermissions && !context.hasPermissions()) {
            return;
        }

        entries.acceptAll(this.createStacks(context), this.visibility);
    }

    @Override
    public Either<ItemGroupEntry<?>, Holder<Item>> createEither() {
        return Either.left(this);
    }

    protected abstract Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context);

    protected CreativeModeTab.TabVisibility visibility() {
        return this.visibility;
    }

    protected boolean requiresPermissions() {
        return this.requiresPermissions;
    }

    protected static <T extends PossiblyHiddenItemGroupEntry<T>> Products.P2<RecordCodecBuilder.Mu<T>, CreativeModeTab.TabVisibility, Boolean> codec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
            TAB_VISIBILITY_CODEC.optionalFieldOf("visibility", CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS).forGetter(PossiblyHiddenItemGroupEntry::visibility),
            Codec.BOOL.optionalFieldOf("requires_permissions", false).forGetter(PossiblyHiddenItemGroupEntry::requiresPermissions)
        );
    }
}
