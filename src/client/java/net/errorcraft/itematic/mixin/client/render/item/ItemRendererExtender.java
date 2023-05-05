package net.errorcraft.itematic.mixin.client.render.item;

import net.errorcraft.itematic.access.DynamicRegistryAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public class ItemRendererExtender implements DynamicRegistryAccess {
    @Shadow
    @Final
    private ItemModels models;

    @Redirect(
        method = "method_51795",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean method_51795IsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.CLOCK);
    }

    public void loadDynamicEntries(DynamicRegistryManager registryManager) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        this.models.reloadModelIds(registry);
    }
}
