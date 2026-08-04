package net.errorcraft.itematic.item.group.entry;

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

public abstract class PossiblyHiddenItemGroupEntry implements ItemGroupEntry {
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
    public Either<Holder<Item>, ItemGroupEntry> createEither() {
        return Either.right(this);
    }

    protected abstract Collection<ItemStack> createStacks(CreativeModeTab.ItemDisplayParameters context);

    protected CreativeModeTab.TabVisibility visibility() {
        return this.visibility;
    }

    protected boolean requiresPermissions() {
        return this.requiresPermissions;
    }

    protected static <T extends PossiblyHiddenItemGroupEntry> Products.P2<RecordCodecBuilder.Mu<T>, CreativeModeTab.TabVisibility, Boolean> createCodec(RecordCodecBuilder.Instance<T> instance) {
        return instance.group(
            StringRepresentable.fromEnum(CreativeModeTab.TabVisibility::values).optionalFieldOf("visibility", CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS).forGetter(PossiblyHiddenItemGroupEntry::visibility),
            Codec.BOOL.optionalFieldOf("requires_permissions", false).forGetter(PossiblyHiddenItemGroupEntry::requiresPermissions)
        );
    }
}
