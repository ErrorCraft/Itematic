package net.errorcraft.itematic.mixin.client.gui.screen;

import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenAccess;
import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreateFlatWorldScreen.class)
public class CustomizeFlatLevelScreenExtender implements CustomizeFlatLevelScreenAccess {
    @Unique
    private HolderLookup.RegistryLookup<Item> itemLookup;

    @Override
    public HolderLookup.RegistryLookup<Item> itematic$itemLookup() {
        return this.itemLookup;
    }

    @Override
    public void itematic$setItemLookup(HolderLookup.RegistryLookup<Item> itemLookup) {
        this.itemLookup = itemLookup;
    }

    @Mixin(CreateFlatWorldScreen.DetailsList.class)
    public static class SuperflatLayersListWidgetExtender implements CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess {
        @Shadow
        @Final
        CreateFlatWorldScreen field_18738;

        @Override
        public HolderLookup.RegistryLookup<Item> itematic$itemLookup() {
            return ((CustomizeFlatLevelScreenAccess) this.field_18738).itematic$itemLookup();
        }

        @Mixin(targets = "net/minecraft/client/gui/screens/CreateFlatWorldScreen$DetailsList$LayerEntry")
        public static class SuperflatLayerEntryExtender {
            @Shadow
            @Final
            CreateFlatWorldScreen.DetailsList field_18739;

            @Redirect(
                method = "renderContent",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/CreateFlatWorldScreen$DetailsList$LayerEntry;getDisplayItem(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;"
                )
            )
            private ItemStack createItemStackUseRegistryEntry(@Coerce Object instance, BlockState state) {
                HolderLookup.RegistryLookup<Item> itemLookup = ((CustomizeFlatLevelScreenSuperflatLayersListWidgetAccess) this.field_18739).itematic$itemLookup();
                return itemLookup.get(state.getBlock().itematic$asItemKey())
                    .map(ItemStack::new)
                    .orElse(ItemStack.EMPTY);
            }
        }
    }
}
