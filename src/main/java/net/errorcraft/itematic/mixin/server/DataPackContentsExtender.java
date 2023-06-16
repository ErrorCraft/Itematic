package net.errorcraft.itematic.mixin.server;

import net.errorcraft.itematic.recipe.RecipeSerializerUtil;
import net.minecraft.item.Item;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataPackContents.class)
public class DataPackContentsExtender {
    @Shadow
    @Final
    private ServerAdvancementLoader serverAdvancementLoader;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void constructorStoreDynamicRegistryManager(DynamicRegistryManager.Immutable dynamicRegistryManager, FeatureSet enabledFeatures, CommandManager.RegistrationEnvironment environment, int functionPermissionLevel, CallbackInfo info) {
        Registry<Item> itemRegistry = dynamicRegistryManager.get(RegistryKeys.ITEM);
        this.serverAdvancementLoader.setRegistryManager(dynamicRegistryManager);
        this.recipeManager.setItemRegistry(itemRegistry);
        RecipeSerializerUtil.setItemRegistry(itemRegistry);
    }

    @Inject(
        method = "method_40425",
        at = @At("HEAD"),
        remap = false
    )
    private static void method_40425ResetTemporaryRegistries(DataPackContents dataPackContents, Object void_, CallbackInfoReturnable<DataPackContents> info) {
        RecipeSerializerUtil.setItemRegistry(null);
    }
}
