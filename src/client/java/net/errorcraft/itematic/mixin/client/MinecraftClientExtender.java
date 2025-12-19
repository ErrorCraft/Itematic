package net.errorcraft.itematic.mixin.client;

import net.errorcraft.itematic.access.client.MinecraftClientAccess;
import net.errorcraft.itematic.client.item.bar.ItemBarStyleLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientExtender implements MinecraftClientAccess {
    @Shadow
    @Final
    private ReloadableResourceManagerImpl resourceManager;

    @Unique
    private final ItemBarStyleLoader itemBarStyles = new ItemBarStyleLoader();

    @Inject(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/ReloadableResourceManagerImpl;reload(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Ljava/util/concurrent/CompletableFuture;Ljava/util/List;)Lnet/minecraft/resource/ResourceReload;"
        )
    )
    private void addCustomLoaders(RunArgs args, CallbackInfo info) {
        this.resourceManager.registerReloader(this.itemBarStyles);
    }

    @Redirect(
        method = "method_1502",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    @NotNull
    private static <T> Identifier initializeItemTooltipSearchProviderUseRegistryEntry(DefaultedRegistry<T> instance, T t, ItemStack stack) {
        return stack.itematic$key().getValue();
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    private static Identifier method_53861(RecipeResultCollection resultCollection, RecipeEntry<?> entry) {
        DynamicRegistryManager registryManager = resultCollection.getRegistryManager();
        return entry.value().getResult(registryManager).itematic$key().getValue();
    }

    @Override
    public ItemBarStyleLoader itematic$itemBarStyles() {
        return this.itemBarStyles;
    }
}
