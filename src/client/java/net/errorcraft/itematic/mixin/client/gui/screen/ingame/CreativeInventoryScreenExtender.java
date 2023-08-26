package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.fabricmc.fabric.impl.client.itemgroup.FabricCreativeGuiComponents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenExtender extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler> {
    public CreativeInventoryScreenExtender(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(
        method = "setSelectedTab",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/client/MinecraftClient;getCreativeHotbarStorage()Lnet/minecraft/client/option/HotbarStorage;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private void setSelectedTabStorePaperRegistryEntry(ItemGroup group, CallbackInfo info, @Share("paper") LocalRef<RegistryEntry<Item>> paper) {
        paper.set(this.client.world.getItem(ItemKeys.PAPER));
    }

    @Redirect(
        method = "setSelectedTab",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack setSelectedTabNewItemStackUseRegistryEntry(ItemConvertible item, @Share("paper") LocalRef<RegistryEntry<Item>> paper) {
        return new ItemStack(paper.get());
    }

    @Redirect(
        method = "renderTabIcon",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroup;getIcon()Lnet/minecraft/item/ItemStack;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private ItemStack renderTabIconUseDynamicRegistry(ItemGroup instance, DrawContext context) {
        return instance.icon(this.client.world.getItemAccess());
    }

    @Inject(
        method = "fabric_isButtonVisible",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    @SuppressWarnings("UnstableApiUsage")
    private void fabricApiCompatibility_checkDisplayContextNull(FabricCreativeGuiComponents.Type type, CallbackInfoReturnable<Boolean> info) {
        if (ItemGroupsAccessor.displayContext() == null) {
            info.setReturnValue(false);
        }
    }
}
