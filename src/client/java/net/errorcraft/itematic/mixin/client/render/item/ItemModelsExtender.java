package net.errorcraft.itematic.mixin.client.render.item;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemModels.class)
public abstract class ItemModelsExtender {
    @Shadow
    @Final
    private BakedModelManager modelManager;

    @Redirect(
        method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/item/ItemModels;getModel(Lnet/minecraft/item/Item;)Lnet/minecraft/client/render/model/BakedModel;"
        )
    )
    private BakedModel getModelUseModelManager(ItemModels instance, Item item, ItemStack stack) {
        return this.modelManager.getModel(new ModelIdentifier(stack.itematic$key().getValue(), "inventory"));
    }
}
