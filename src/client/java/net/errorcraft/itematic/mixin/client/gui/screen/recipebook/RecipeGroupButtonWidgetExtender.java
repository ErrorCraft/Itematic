package net.errorcraft.itematic.mixin.client.gui.screen.recipebook;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeGroupButtonWidget;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(RecipeGroupButtonWidget.class)
public class RecipeGroupButtonWidgetExtender {
    @Redirect(
        method = "renderIcons",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/recipebook/RecipeBookGroup;getIcons()Ljava/util/List;"
        )
    )
    private List<ItemStack> stacks(RecipeBookGroup instance) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null) {
            return List.of();
        }
        return instance.itematic$icons(world.getRegistryManager().getOrThrow(RegistryKeys.ITEM));
    }
}
