package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenExtender extends AbstractContainerScreen<CreativeModeInventoryScreen.ItemPickerMenu> {
    public CreativeInventoryScreenExtender(CreativeModeInventoryScreen.ItemPickerMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Definition(id = "getCreativeHotbarStorage", method = "Lnet/minecraft/client/Minecraft;getHotbarManager()Lnet/minecraft/client/HotbarManager;")
    @Expression("? = ?.getCreativeHotbarStorage()")
    @Inject(
        method = "selectTab",
        at = @At(
            value = "MIXINEXTRAS:EXPRESSION",
            shift = At.Shift.AFTER
        )
    )
    @SuppressWarnings("ConstantConditions")
    private void storePaperRegistryEntry(CreativeModeTab group, CallbackInfo info, @Share("paper") LocalRef<Holder<Item>> paper) {
        paper.set(this.minecraft.level.itematic$getItem(ItemIds.PAPER));
    }

    @Redirect(
        method = "selectTab",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPaperUseRegistryEntry(ItemLike item, @Share("paper") LocalRef<Holder<Item>> paper) {
        return new ItemStack(paper.get());
    }

    @Redirect(
        method = "renderTabButton",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/CreativeModeTab;getIconItem()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private ItemStack getIconUseDynamicRegistry(CreativeModeTab instance, GuiGraphics context) {
        return instance.itematic$icon(this.minecraft.level.itematic$getItemAccess());
    }

    @Redirect(
        method = "updateVisibleTags",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getTags()Ljava/util/stream/Stream;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private Stream<HolderSet.Named<Item>> streamTagsUseDynamicRegistry(DefaultedRegistry<Item> instance) {
        return this.minecraft.level.registryAccess()
            .lookupOrThrow(Registries.ITEM)
            .getTags();
    }
}
