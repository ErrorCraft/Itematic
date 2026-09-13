package net.errorcraft.itematic.mixin.client.resources.model;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.client.resources.model.FailedToLoadModels;
import net.minecraft.client.resources.model.ModelDiscovery;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(ModelDiscovery.class)
public abstract class ModelDiscoveryExtender {
    @Shadow
    public abstract void addRoot(ResolvableModel model);

    @Inject(
        method = "<init>",
        at = @At("HEAD")
    )
    private static void addFailedToLoadModels(CallbackInfo info, @Local(name = "unbakedModels", argsOnly = true) LocalRef<Map<Identifier, UnbakedModel>> unbakedModels) {
        Map<Identifier, UnbakedModel> mutableUnbakedModels = new HashMap<>(unbakedModels.get());
        mutableUnbakedModels.put(
            FailedToLoadModels.ITEM,
            FailedToLoadModels.ITEM_MODEL
        );
        unbakedModels.set(mutableUnbakedModels);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void addFailedToLoadModelRoots(CallbackInfo info) {
        this.addRoot(FailedToLoadModels.ITEM_MODEL_REFERENCE);
    }
}
