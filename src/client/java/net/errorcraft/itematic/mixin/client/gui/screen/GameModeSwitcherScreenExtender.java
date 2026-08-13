package net.errorcraft.itematic.mixin.client.gui.screen;


import net.errorcraft.itematic.access.client.gui.screen.GameModeSwitcherScreenAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class GameModeSwitcherScreenExtender {
    @Mixin(GameModeSwitcherScreen.GameModeSlot.class)
    public static class ButtonWidgetExtender {
        @Redirect(
            method = "renderWidget",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;drawIcon(Lnet/minecraft/client/gui/GuiGraphics;II)V"
            )
        )
        private void renderIconUseRegistryEntry(GameModeSwitcherScreen.GameModeIcon instance, GuiGraphics context, int x, int y) {
            Level world = Minecraft.getInstance().level;
            if (world == null) {
                return;
            }

            ItemStack stack = instance.itematic$icon(world.registryAccess().lookupOrThrow(Registries.ITEM));
            context.renderItem(stack, x, y);
        }
    }

    @Mixin(GameModeSwitcherScreen.GameModeIcon.class)
    public static class GameModeSelectionExtender implements GameModeSwitcherScreenAccess.GameModeSelectionAccess {
        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeIcon CREATIVE;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeIcon SURVIVAL;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeIcon ADVENTURE;

        @Shadow
        @Final
        public static GameModeSwitcherScreen.GameModeIcon SPECTATOR;

        @Unique
        private ResourceKey<Item> icon;

        static {
            CREATIVE.itematic$setIcon(ItemIds.GRASS_BLOCK);
            SURVIVAL.itematic$setIcon(ItemIds.IRON_SWORD);
            ADVENTURE.itematic$setIcon(ItemIds.MAP);
            SPECTATOR.itematic$setIcon(ItemIds.ENDER_EYE);
        }

        @Redirect(
            method = "<clinit>",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private static ItemStack newItemStackReturnEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack itematic$icon(Registry<Item> registry) {
            if (this.icon == null) {
                return ItemStack.EMPTY;
            }

            return registry.get(this.icon)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }

        @Override
        public void itematic$setIcon(ResourceKey<Item> icon) {
            this.icon = icon;
        }
    }
}
