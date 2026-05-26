package net.errorcraft.itematic.mixin.client.gui.screen;


import net.errorcraft.itematic.access.client.gui.screen.GameModeSwitcherScreenAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameModeSwitcherScreen;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class GameModeSwitcherScreenExtender {
    @Mixin(GameModeSwitcherScreen.ButtonWidget.class)
    public static class ButtonWidgetExtender {
        @Redirect(
            method = "renderWidget",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screen/GameModeSwitcherScreen$GameModeSelection;renderIcon(Lnet/minecraft/client/gui/DrawContext;II)V"
            )
        )
        private void renderIconUseRegistryEntry(GameModeSwitcherScreen.GameModeSelection instance, DrawContext context, int x, int y) {
            World world = MinecraftClient.getInstance().world;
            if (world == null) {
                return;
            }

            ItemStack stack = instance.itematic$icon(world.getRegistryManager().getOrThrow(RegistryKeys.ITEM));
            context.drawItem(stack, x, y);
        }
    }

    @Mixin(GameModeSwitcherScreen.GameModeSelection.class)
    public static class GameModeSelectionExtender implements GameModeSwitcherScreenAccess.GameModeSelectionAccess {
        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeSelection CREATIVE;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeSelection SURVIVAL;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeSelection ADVENTURE;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeSelection SPECTATOR;

        @Unique
        private RegistryKey<Item> icon;

        @Inject(
            method = "<clinit>",
            at = @At("TAIL")
        )
        private static void setIcons(CallbackInfo info) {
            CREATIVE.itematic$setIcon(ItemKeys.GRASS_BLOCK);
            SURVIVAL.itematic$setIcon(ItemKeys.IRON_SWORD);
            ADVENTURE.itematic$setIcon(ItemKeys.MAP);
            SPECTATOR.itematic$setIcon(ItemKeys.ENDER_EYE);
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

        @Override
        public void itematic$setIcon(RegistryKey<Item> icon) {
            this.icon = icon;
        }
    }
}
