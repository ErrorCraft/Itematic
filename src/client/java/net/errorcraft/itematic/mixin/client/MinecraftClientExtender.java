package net.errorcraft.itematic.mixin.client;

import net.errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.stream.Stream;

@Mixin(MinecraftClient.class)
public class MinecraftClientExtender {
    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    private static Stream<Identifier> method_1485(ItemStack stack) {
        return Stream.of(ItemStackUtil.getId(stack.getRegistryEntry()));
    }

    @Redirect(
        method = "method_1556",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    private static <T> Identifier initializeSearchProvidersUseDynamicRegistry(DefaultedRegistry<Item> instance, T t, RecipeResultCollection resultCollection) {
        return resultCollection.getRegistryManager().get(RegistryKeys.ITEM).getId((Item) t);
    }
}
