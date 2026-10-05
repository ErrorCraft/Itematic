package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.world.item.CreativeModeTabAccess;
import net.errorcraft.itematic.world.item.group.CreativeModeTabsCache;
import net.fabricmc.fabric.impl.creativetab.FabricCreativeModeTabImpl;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(value = CreativeModeTab.class, priority = 1100)
@SuppressWarnings("UnstableApiUsage")
public abstract class CreativeModeTabExtender implements CreativeModeTabAccess, FabricCreativeModeTabImpl {
    @Shadow
    @Final
    @Mutable
    private CreativeModeTab.@Nullable Row row;

    @Shadow
    @Final
    @Mutable
    private int column;

    @Shadow
    @Dynamic("Provided by Fabric API")
    private int page;

    @Shadow
    public abstract boolean hasAnyItems();

    @Unique
    private static final int TABS_PER_ROW = 5;

    @Inject(
        method = "buildContents",
        at = @At("TAIL"),
        cancellable = true,
        order = 900
    )
    private void cancelBeforeFabricApiChecksStaticRegistry(CreativeModeTab.ItemDisplayParameters parameters, CallbackInfo info) {
        info.cancel();
    }

    @Override
    public int fabric_getPage() {
        return this.page;
    }

    @Override
    public boolean itematic$place(int index) {
        if (this.row != null) {
            return false;
        }

        if (!this.hasAnyItems()) {
            return false;
        }

        this.row = (index / TABS_PER_ROW) % 2 == 0
            ? CreativeModeTab.Row.TOP
            : CreativeModeTab.Row.BOTTOM;
        this.column = index % TABS_PER_ROW;
        this.page = index / TABS_PER_PAGE;
        return true;
    }

    @Mixin(CreativeModeTab.Builder.class)
    public static abstract class BuilderExtender {
        @Shadow
        @Final
        private CreativeModeTab.@Nullable Row row;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        @SuppressWarnings("UnstableApiUsage")
        private CreativeModeTab setPage(CreativeModeTab original) {
            if (this.row != null) {
                ((FabricCreativeModeTabImpl) original).fabric_setPage(CreativeModeTabsCache.DISPLAY_TAB_ON_EVERY_PAGE);
            }

            return original;
        }
    }

    @Mixin(CreativeModeTab.TabVisibility.class)
    public static class TabVisibilityExtender implements StringRepresentable {
        @Unique
        private String name;

        @Inject(
            method = "<init>",
            at = @At("TAIL")
        )
        private void setName(String string, int i, CallbackInfo info) {
            this.name = string.toLowerCase(Locale.ROOT);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

    @Mixin(targets = "net/minecraft/world/item/CreativeModeTab$ItemDisplayBuilder")
    public static class ItemDisplayBuilderExtender {
        @Unique
        private static final Logger LOGGER = LogUtils.getLogger();

        @WrapMethod(
            method = "accept"
        )
        private void preventDuplicateEntryExceptionAndLogMessageInstead(ItemStack stack, CreativeModeTab.TabVisibility tabVisibility, Operation<Void> original) {
            try {
                original.call(stack, tabVisibility);
            } catch (IllegalStateException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }
}
