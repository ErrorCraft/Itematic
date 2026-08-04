package net.errorcraft.itematic.mixin.client.gui.screen.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.gui.screen.CustomizeFlatLevelScreenAccess;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PresetEditor.class)
public interface LevelScreenProviderExtender {
    @ModifyExpressionValue(
        method = "method_41863",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;Ljava/util/function/Consumer;Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorSettings;)Lnet/minecraft/client/gui/screens/CreateFlatWorldScreen;"
        )
    )
    private static CreateFlatWorldScreen newCustomizeFlatLevelScreenSetItemLookup(CreateFlatWorldScreen original, @Local RegistryAccess registryManager) {
        ((CustomizeFlatLevelScreenAccess) original).itematic$setItemLookup(registryManager.lookupOrThrow(Registries.ITEM));
        return original;
    }
}
