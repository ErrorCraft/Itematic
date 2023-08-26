package net.errorcraft.itematic.mixin.client.network;

import net.errorcraft.itematic.mixin.item.ItemGroupsAccessor;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerExtender {
    @Shadow
    @Final
    private DynamicRegistryManager.Immutable combinedDynamicRegistries;

    @Shadow
    @Final
    private RecipeManager recipeManager;

    @Inject(
        method = "onGameJoin",
        at = @At("HEAD")
    )
    private void setRecipeManagerItemRegistry(GameJoinS2CPacket packet, CallbackInfo info) {
        Registry<Item> itemRegistry = this.combinedDynamicRegistries.get(RegistryKeys.ITEM);
        this.recipeManager.setItemRegistry(itemRegistry);
    }

    @Inject(
        method = "refreshTagBasedData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemGroups;getSearchGroup()Lnet/minecraft/item/ItemGroup;"
        )
    )
    private void resetDisplayContext(CallbackInfo info) {
        ItemGroupsAccessor.setDisplayContext(null);
    }
}
