package net.errorcraft.itematic.mixin.client.gui.screens.worldselection;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.screens.CreateFlatWorldScreen;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PresetEditor.class)
public interface PresetEditorExtender {
    @ModifyExpressionValue(
        method = "lambda$static$0",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/client/gui/screens/worldselection/CreateWorldScreen;Ljava/util/function/Consumer;Lnet/minecraft/world/level/levelgen/flat/FlatLevelGeneratorSettings;)Lnet/minecraft/client/gui/screens/CreateFlatWorldScreen;"
        )
    )
    private static CreateFlatWorldScreen setItems(CreateFlatWorldScreen original, @Local(name = "registryAccess") RegistryAccess registryAccess) {
        original.itematic$setItems(registryAccess.lookupOrThrow(Registries.ITEM));
        return original;
    }
}
