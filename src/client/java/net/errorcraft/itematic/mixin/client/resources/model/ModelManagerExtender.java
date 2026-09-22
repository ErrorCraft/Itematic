package net.errorcraft.itematic.mixin.client.resources.model;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.client.resources.model.ModelManagerAccess;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelManager.class)
public class ModelManagerExtender implements ModelManagerAccess {
    @Unique
    private ItemModel failedToLoadItemModel;

    @Inject(
        method = "apply",
        at = @At("TAIL")
    )
    private void setFailedToLoadItemModel(CallbackInfo info, @Local(name = "bakedModels") ModelBakery.BakingResult bakedModels) {
        this.failedToLoadItemModel = bakedModels.itematic$failedToLoadItemModel();
    }

    @Override
    public ItemModel itematic$failedToLoadItemModel() {
        return this.failedToLoadItemModel;
    }
}
