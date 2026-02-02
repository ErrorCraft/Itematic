package net.errorcraft.itematic.mixin.client.gui.screen;


import net.errorcraft.itematic.access.client.gui.screen.GameModeSelectionScreenGameModeSelectionAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameModeSelectionScreen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class GameModeSelectionScreenExtender {
    @Mixin(GameModeSelectionScreen.ButtonWidget.class)
    public static class ButtonWidgetExtender {
        @Redirect(
            method = "renderWidget",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screen/GameModeSelectionScreen$GameModeSelection;renderIcon(Lnet/minecraft/client/gui/DrawContext;II)V"
            )
        )
        private void renderIconUseRegistryEntry(GameModeSelectionScreen.GameModeSelection instance, DrawContext context, int x, int y) {
            World world = MinecraftClient.getInstance().world;
            if (world == null) {
                return;
            }
            ItemStack stack = instance.itematic$icon(world.getRegistryManager().getOrThrow(RegistryKeys.ITEM));
            context.drawItem(stack, x, y);
        }
    }

    @Mixin(GameModeSelectionScreen.GameModeSelection.class)
    public static class GameModeSelectionExtender implements GameModeSelectionScreenGameModeSelectionAccess {
        @Final
        @Shadow
        public static GameModeSelectionScreen.GameModeSelection CREATIVE;

        @Final
        @Shadow
        public static GameModeSelectionScreen.GameModeSelection SURVIVAL;

        @Final
        @Shadow
        public static GameModeSelectionScreen.GameModeSelection ADVENTURE;

        @Final
        @Shadow
        public static GameModeSelectionScreen.GameModeSelection SPECTATOR;

        @Unique
        private RegistryKey<Item> icon;

        static {
            ((GameModeSelectionExtender)(Object) CREATIVE).icon = ItemKeys.GRASS_BLOCK;
            ((GameModeSelectionExtender)(Object) SURVIVAL).icon = ItemKeys.IRON_SWORD;
            ((GameModeSelectionExtender)(Object) ADVENTURE).icon = ItemKeys.MAP;
            ((GameModeSelectionExtender)(Object) SPECTATOR).icon = ItemKeys.ENDER_EYE;
        }

        @Redirect(
            method = "<clinit>",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemConvertible item) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack itematic$icon(Registry<Item> registry) {
            if (this.icon == null) {
                return ItemStack.EMPTY;
            }
            return registry.getOptional(this.icon)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }
    }
}
