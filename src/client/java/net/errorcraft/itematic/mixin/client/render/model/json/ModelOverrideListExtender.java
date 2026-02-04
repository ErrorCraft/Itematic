package net.errorcraft.itematic.mixin.client.render.model.json;

import net.errorcraft.itematic.client.item.ModelPredicateProviderWrapper;
import net.errorcraft.itematic.item.model.override.ModelOverride;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModelOverrideList.class)
public class ModelOverrideListExtender {
    @Redirect(
        method = "getModel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/item/ModelPredicateProviderRegistry;get(Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/item/ModelPredicateProvider;"
        )
    )
    @Nullable
    @SuppressWarnings("deprecation")
    private ModelPredicateProvider getModelOverrideUseRegistry(ItemStack stack, Identifier id) {
        ModelOverride override = ItematicRegistries.MODEL_OVERRIDE.get(id);
        if (override == null) {
            return null;
        }

        if (!override.isApplicable(stack)) {
            return null;
        }

        return new ModelPredicateProviderWrapper(override);
    }
}
