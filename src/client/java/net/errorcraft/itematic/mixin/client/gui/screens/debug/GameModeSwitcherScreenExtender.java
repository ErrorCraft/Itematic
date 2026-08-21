package net.errorcraft.itematic.mixin.client.gui.screens.debug;


import net.errorcraft.itematic.access.client.gui.screens.debug.GameModeSwitcherScreenAccess;
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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

public class GameModeSwitcherScreenExtender {
    @Mixin(GameModeSwitcherScreen.GameModeSlot.class)
    public static class GameModeSlotExtender {
        @Redirect(
            method = "renderWidget",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;drawIcon(Lnet/minecraft/client/gui/GuiGraphics;II)V"
            )
        )
        private void drawIconUseHolder(GameModeSwitcherScreen.GameModeIcon instance, GuiGraphics graphics, int x, int y) {
            Level level = Minecraft.getInstance().level;
            if (level == null) {
                return;
            }

            ItemStack stack = instance.itematic$icon(level.registryAccess().lookupOrThrow(Registries.ITEM));
            graphics.renderItem(stack, x, y);
        }
    }

    @Mixin(GameModeSwitcherScreen.GameModeIcon.class)
    public static class GameModeIconExtender implements GameModeSwitcherScreenAccess.GameModeIconAccess {
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
        @Nullable
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
        private static ItemStack newItemStackUseEmptyStack(ItemLike item) {
            return ItemStack.EMPTY;
        }

        @Override
        public ItemStack itematic$icon(Registry<Item> items) {
            if (this.icon == null) {
                return ItemStack.EMPTY;
            }

            return items.get(this.icon)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
        }

        @Override
        public void itematic$setIcon(ResourceKey<Item> icon) {
            this.icon = icon;
        }
    }
}
