package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.Stream;

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
    private void storePaperRegistryEntry(ItemGroup group, CallbackInfo info, @Share("paper") LocalRef<RegistryEntry<Item>> paper) {
        paper.set(this.client.world.itematic$getItem(ItemKeys.PAPER));
    }

    @Redirect(
        method = "setSelectedTab",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForPaperUseRegistryEntry(ItemConvertible item, @Share("paper") LocalRef<RegistryEntry<Item>> paper) {
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
    private ItemStack getIconUseDynamicRegistry(ItemGroup instance, DrawContext context) {
        return instance.itematic$icon(this.client.world.itematic$getItemAccess());
    }

    @Redirect(
        method = "searchForTags",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;streamTags()Ljava/util/stream/Stream;"
        )
    )
    @SuppressWarnings("ConstantConditions")
    private Stream<TagKey<Item>> streamTagsUseDynamicRegistry(DefaultedRegistry<Item> instance) {
        return this.client.world.getRegistryManager()
            .get(RegistryKeys.ITEM)
            .streamTags();
    }
}
