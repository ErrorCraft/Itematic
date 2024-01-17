package net.errorcraft.itematic.mixin.client.gui.screen;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

public class CustomizeFlatLevelScreenExtender {
    public static class SuperflatLayersListWidgetExtender {
        @Mixin(targets = "net/minecraft/client/gui/screen/world/CustomizeFlatLevelScreen$SuperflatLayersListWidget$SuperflatLayerEntry")
        public static class SuperflatLayerEntryExtender {
            @Redirect(
                method = "render",
                at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/world/CustomizeFlatLevelScreen$SuperflatLayersListWidget$SuperflatLayerEntry;createItemStackFor(Lnet/minecraft/block/BlockState;)Lnet/minecraft/item/ItemStack;"
                )
            )
            private ItemStack createItemStackUseRegistryEntry(@Coerce Object instance, BlockState state) {
                World world = MinecraftClient.getInstance().world;
                if (world == null) {
                    return ItemStack.EMPTY;
                }
                return world.itematic$createStack(this.itemKey(state));
            }

            @Unique
            private RegistryKey<Item> itemKey(BlockState state) {
                if (state.isOf(Blocks.WATER)) {
                    return ItemKeys.WATER_BUCKET;
                }
                if (state.isOf(Blocks.LAVA)) {
                    return ItemKeys.LAVA_BUCKET;
                }
                return ItemUtil.keyFromBlock(state.getBlock());
            }
        }
    }
}
