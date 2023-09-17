package net.errorcraft.itematic.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientExtender {
    @Redirect(
        method = "method_1502",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        ),
        remap = false
    )
    @NotNull
    private static <T> Identifier initializeSearchProvidersUseRegistryEntry(DefaultedRegistry<T> instance, T t, ItemStack stack) {
        return stack.key().getValue();
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    private static Identifier method_53861(RecipeResultCollection resultCollection, RecipeEntry<?> entry) {
        DynamicRegistryManager registryManager = resultCollection.getRegistryManager();
        return entry.value().getResult(registryManager).key().getValue();
    }
}
