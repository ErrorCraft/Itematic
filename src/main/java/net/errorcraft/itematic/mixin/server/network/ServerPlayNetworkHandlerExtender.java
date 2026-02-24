package net.errorcraft.itematic.mixin.server.network;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.recipe.RecipeAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.WritableItemComponent;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerExtender {
    @Shadow
    public ServerPlayerEntity player;

    @ModifyExpressionValue(
        method = "updateBookContent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private boolean containsWritableBookContentDataComponentAlsoCheckItemBehaviorComponent(boolean original, @Local ItemStack stack) {
        return original && stack.itematic$hasBehavior(ItemComponentTypes.WRITABLE);
    }

    @ModifyExpressionValue(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;contains(Lnet/minecraft/component/ComponentType;)Z"
        )
    )
    private boolean containsWritableBookContentDataComponentAlsoCheckAndStoreItemBehaviorComponent(boolean original, @Local ItemStack stack, @Share("writable") LocalRef<WritableItemComponent> writable) {
        Optional<WritableItemComponent> optionalWritable = stack.itematic$getBehavior(ItemComponentTypes.WRITABLE);
        optionalWritable.ifPresent(writable::set);
        return original && optionalWritable.isPresent();
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;withItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack withItemForWrittenBookUseItemComponent(ItemStack instance, ItemConvertible itemConvertible, @Share("writable") LocalRef<WritableItemComponent> writable) {
        return instance.itematic$copyWithItem(writable.get().transformsInto());
    }

    @Redirect(
        method = "onCraftRequest",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Recipe;getIngredientPlacement()Lnet/minecraft/recipe/IngredientPlacement;"
        )
    )
    private IngredientPlacement getIngredientPlacementUseDynamicRegistry(Recipe<?> instance) {
        return ((RecipeAccess) instance).itematic$ingredientPlacement(this.player.getWorld().getRegistryManager().getOrThrow(RegistryKeys.ITEM));
    }
}
