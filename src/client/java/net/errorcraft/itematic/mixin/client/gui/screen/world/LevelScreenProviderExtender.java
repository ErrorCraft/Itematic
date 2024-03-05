package net.errorcraft.itematic.mixin.client.gui.screen.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenAccess;
import net.minecraft.client.gui.screen.world.CustomizeFlatLevelScreen;
import net.minecraft.client.gui.screen.world.LevelScreenProvider;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelScreenProvider.class)
public interface LevelScreenProviderExtender {
    @ModifyExpressionValue(
        method = "method_41863",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;Ljava/util/function/Consumer;Lnet/minecraft/world/gen/chunk/FlatChunkGeneratorConfig;)Lnet/minecraft/client/gui/screen/world/CustomizeFlatLevelScreen;"
        )
    )
    private static CustomizeFlatLevelScreen newCustomizeFlatLevelScreenSetItemLookup(CustomizeFlatLevelScreen original, @Local DynamicRegistryManager registryManager) {
        ((CustomizeFlatLevelScreenAccess) original).itematic$setItemLookup(registryManager.getWrapperOrThrow(RegistryKeys.ITEM));
        return original;
    }
}
