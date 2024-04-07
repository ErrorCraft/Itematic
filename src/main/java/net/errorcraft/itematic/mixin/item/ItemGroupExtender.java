package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.item.ItemGroupAccess;
import net.errorcraft.itematic.item.ItemAccess;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.StringIdentifiable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(ItemGroup.class)
public class ItemGroupExtender implements ItemGroupAccess {
    @Shadow
    @Final
    private ItemGroup.Type type;

    @Unique
    private RegistryKey<Item> iconKey;
    @Unique
    private TagKey<ItemGroupEntryProvider> entryProviderTag;

    @WrapWithCondition(
        method = "updateEntries",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup$EntryCollector;accept(Lnet/minecraft/item/ItemGroup$DisplayContext;Lnet/minecraft/item/ItemGroup$Entries;)V"
        )
    )
    private boolean collectEntries(ItemGroup.EntryCollector instance, ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        if (this.type != ItemGroup.Type.CATEGORY) {
            return true;
        }

        context.lookup()
            .getWrapperOrThrow(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER)
            .getOptional(this.entryProviderTag)
            .ifPresent(entryList -> collectEntries(entryList, context, entries));
        return false;
    }

    @Override
    public ItemStack itematic$icon(ItemAccess access) {
        return new ItemStack(access.getEntry(this.iconKey));
    }

    @Override
    public void itematic$setIconKey(RegistryKey<Item> iconKey) {
        this.iconKey = iconKey;
    }

    @Override
    public void itematic$setEntryProviderTag(TagKey<ItemGroupEntryProvider> entryProviderTag) {
        this.entryProviderTag = entryProviderTag;
    }

    @Unique
    private static void collectEntries(RegistryEntryList.Named<ItemGroupEntryProvider> entryList, ItemGroup.DisplayContext context, ItemGroup.Entries entries) {
        for (RegistryEntry<ItemGroupEntryProvider> entry : entryList) {
            entry.value().collectEntries(context, entries);
        }
    }

    @Mixin(ItemGroup.StackVisibility.class)
    public static class StackVisibilityExtender implements StringIdentifiable {
        @Unique
        private String name;

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void initSetNameField(String string, int i, CallbackInfo info) {
            this.name = string.toLowerCase(Locale.ROOT);
        }

        @Override
        public String asString() {
            return this.name;
        }
    }

    @Mixin(targets = "net/minecraft/item/ItemGroup$EntriesImpl")
    public static class EntriesImplExtender {
        @Shadow
        @Final
        private ItemGroup group;

        @Unique
        private static final Logger LOGGER = LogUtils.getLogger();

        @Inject(
            method = "add",
            at = @At(
                value = "NEW",
                target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;",
                remap = false
            ),
            cancellable = true
        )
        private void logDuplicateEntryMessageAndCancelToPreventException(ItemStack stack, ItemGroup.StackVisibility visibility, CallbackInfo info) {
            LOGGER.warn("Accidentally adding the same item stack twice {} to a Creative Mode Tab: {}", stack.toHoverableText().getString(), this.group.getDisplayName().getString());
            info.cancel();
        }
    }
}
