package net.errorcraft.itematic.mixin.server;

import net.minecraft.loot.LootManager;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.server.DataPackContents;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DataPackContents.class)
public class DataPackContentsExtender {
    @Shadow
    @Final
    private ServerAdvancementLoader serverAdvancementLoader;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Shadow
    @Final
    private LootManager lootManager;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setDynamicRegistryManagers(DynamicRegistryManager.Immutable dynamicRegistryManager, FeatureSet enabledFeatures, CommandManager.RegistrationEnvironment environment, int functionPermissionLevel, CallbackInfo info) {
        this.lootManager.setRegistryManager(dynamicRegistryManager);
        this.serverAdvancementLoader.setRegistryManager(dynamicRegistryManager);
        this.recipeManager.setRegistryManager(dynamicRegistryManager);
    }
}
