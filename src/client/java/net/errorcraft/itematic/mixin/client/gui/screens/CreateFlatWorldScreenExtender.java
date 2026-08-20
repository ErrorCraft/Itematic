package net.errorcraft.itematic.mixin.client.gui.screens;

import net.errorcraft.itematic.access.client.gui.screens.CreateFlatWorldScreenAccess;
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
public class CreateFlatWorldScreenExtender implements CreateFlatWorldScreenAccess {
    @Unique
    private HolderLookup.RegistryLookup<Item> items;

    @Override
    public HolderLookup.RegistryLookup<Item> itematic$items() {
        return this.items;
    }

    @Override
    public void itematic$setItems(HolderLookup.RegistryLookup<Item> items) {
        this.items = items;
    }

    @Mixin(CreateFlatWorldScreen.DetailsList.class)
    public static class DetailsListExtender implements DetailsListAccess {
        @Shadow
        @Final
        CreateFlatWorldScreen field_18738;

        @Override
        public HolderLookup.RegistryLookup<Item> itematic$items() {
            return this.field_18738.itematic$items();
        }

        @Mixin(targets = "net/minecraft/client/gui/screens/CreateFlatWorldScreen$DetailsList$LayerEntry")
        public static class LayerEntryExtender {
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
                HolderLookup.RegistryLookup<Item> itemLookup = this.field_18739.itematic$items();
                return itemLookup.get(state.getBlock().itematic$asItemId())
                    .map(ItemStack::new)
                    .orElse(ItemStack.EMPTY);
            }
        }
    }
}
