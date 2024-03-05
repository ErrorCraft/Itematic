package net.errorcraft.itematic.mixin.client.gui.screen;

import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenAccess;
import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.world.CustomizeFlatLevelScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CustomizeFlatLevelScreen.class)
public class CustomizeFlatLevelScreenExtender implements CustomizeFlatLevelScreenAccess {
    @Unique
    private RegistryWrapper.Impl<Item> itemLookup;

    @Override
    public RegistryWrapper.Impl<Item> itematic$itemLookup() {
        return this.itemLookup;
    }

    @Override
    public void itematic$setItemLookup(RegistryWrapper.Impl<Item> itemLookup) {
        this.itemLookup = itemLookup;
    }

    @Mixin(targets = "net.minecraft.client.gui.screen.world.CustomizeFlatLevelScreen$SuperflatLayersListWidget")
    public static class SuperflatLayersListWidgetExtender implements CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess {
        @Shadow
        @Final
        CustomizeFlatLevelScreen field_18738;

        @Override
        public RegistryWrapper.Impl<Item> itematic$itemLookup() {
            return ((CustomizeFlatLevelScreenAccess) this.field_18738).itematic$itemLookup();
        }

        @Mixin(targets = "net/minecraft/client/gui/screen/world/CustomizeFlatLevelScreen$SuperflatLayersListWidget$SuperflatLayerEntry")
        public static class SuperflatLayerEntryExtender {
            @Shadow
            @Final
            CustomizeFlatLevelScreen.SuperflatLayersListWidget field_18739;

            @Redirect(
                method = "render",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CustomizeFlatLevelScreen$SuperflatLayersListWidget$SuperflatLayerEntry;createItemStackFor(Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"
                )
            )
            private ItemStack createItemStackUseRegistryEntry(@Coerce Object instance, BlockState state) {
                RegistryWrapper.Impl<Item> itemLookup = ((CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess) this.field_18739).itematic$itemLookup();
                return itemLookup.getOptional(state.getBlock().itematic$asItemKey())
                    .map(ItemStack::new)
                    .orElse(ItemStack.EMPTY);
            }
        }
    }
}
