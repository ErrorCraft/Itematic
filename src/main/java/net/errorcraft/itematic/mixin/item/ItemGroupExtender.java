package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.item.ItemGroupAccess;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.errorcraft.itematic.world.level.ItemAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(CreativeModeTab.class)
public class ItemGroupExtender implements ItemGroupAccess {
    @Shadow
    @Final
    private CreativeModeTab.Type type;

    @Unique
    private ResourceKey<Item> iconKey;
    @Unique
    private TagKey<ItemGroupEntryProvider> entryProviderTag;

    @WrapWithCondition(
        method = "buildContents",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab$DisplayItemsGenerator;accept(Lnet/minecraft/world/item/CreativeModeTab$ItemDisplayParameters;Lnet/minecraft/world/item/CreativeModeTab$Output;)V"
        )
    )
    private boolean collectEntries(CreativeModeTab.DisplayItemsGenerator instance, CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        if (this.type != CreativeModeTab.Type.CATEGORY) {
            return true;
        }

        context.holders()
            .lookupOrThrow(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER)
            .get(this.entryProviderTag)
            .ifPresent(entryList -> collectEntries(entryList, context, entries));
        return false;
    }

    @Override
    public ItemStack itematic$icon(ItemAccess access) {
        return new ItemStack(access.getOrThrow(this.iconKey));
    }

    @Override
    public void itematic$setIconKey(ResourceKey<Item> iconKey) {
        this.iconKey = iconKey;
    }

    @Override
    public void itematic$setEntryProviderTag(TagKey<ItemGroupEntryProvider> entryProviderTag) {
        this.entryProviderTag = entryProviderTag;
    }

    @Unique
    private static void collectEntries(HolderSet.Named<ItemGroupEntryProvider> entryList, CreativeModeTab.ItemDisplayParameters context, CreativeModeTab.Output entries) {
        for (Holder<ItemGroupEntryProvider> entry : entryList) {
            entry.value().collectEntries(context, entries);
        }
    }

    @Mixin(CreativeModeTab.TabVisibility.class)
    public static class StackVisibilityExtender implements StringRepresentable {
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
        public String getSerializedName() {
            return this.name;
        }
    }

    @Mixin(targets = "net/minecraft/world/item/CreativeModeTab$ItemDisplayBuilder")
    public static class EntriesImplExtender {
        @Shadow
        @Final
        private CreativeModeTab tab;

        @Unique
        private static final Logger LOGGER = LogUtils.getLogger();

        @Inject(
            method = "accept",
            at = @At(
                value = "NEW",
                target = "(Ljava/lang/String;)Ljava/lang/IllegalStateException;",
                remap = false
            ),
            cancellable = true
        )
        private void logDuplicateEntryMessageAndCancelToPreventException(ItemStack stack, CreativeModeTab.TabVisibility visibility, CallbackInfo info) {
            LOGGER.warn("Accidentally adding the same item stack twice {} to a Creative Mode Tab: {}", stack.getDisplayName().getString(), this.tab.getDisplayName().getString());
            info.cancel();
        }
    }
}
