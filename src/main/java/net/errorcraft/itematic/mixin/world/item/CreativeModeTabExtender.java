package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.logging.LogUtils;
import net.errorcraft.itematic.access.world.item.CreativeModeTabAccess;
import net.errorcraft.itematic.world.item.group.ItemGroup;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.fabricmc.fabric.impl.creativetab.FabricCreativeModeTabImpl;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = CreativeModeTab.class, priority = 1100)
public class CreativeModeTabExtender implements FabricCreativeModeTabImpl {
    @Unique
    private int page;

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
    public void fabric_setPage(int page) {
        this.page = page;
    }

    @Mixin(CreativeModeTab.Builder.class)
    public static abstract class BuilderExtender implements CreativeModeTabAccess.BuilderAccess {
        @Shadow
        public abstract CreativeModeTab.Builder displayItems(CreativeModeTab.DisplayItemsGenerator displayItemsGenerator);

        @Unique
        private int page = -1;

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        @SuppressWarnings("UnstableApiUsage")
        private CreativeModeTab setPage(CreativeModeTab original) {
            ((FabricCreativeModeTabImpl) original).fabric_setPage(this.page);
            return original;
        }

        @Override
        public CreativeModeTab.Builder itematic$page(int page) {
            this.page = page;
            return (CreativeModeTab.Builder)(Object) this;
        }

        @Override
        public CreativeModeTab.Builder itematic$displayItems(Holder<ItemGroup> itemGroup) {
            return this.displayItems((parameters, output) -> {
                for (Holder<ItemGroupEntryProvider> entry : itemGroup.value().entries()) {
                    entry.value().collectEntries(parameters, output);
                }
            });
        }

        @Override
        public CreativeModeTab.Builder itematic$searchDisplayItems(HolderSet<ItemGroup> itemGroups) {
            return this.displayItems((_, _) -> {});
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
