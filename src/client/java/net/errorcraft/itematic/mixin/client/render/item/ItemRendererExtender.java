package net.errorcraft.itematic.mixin.client.render.item;

import net.errorcraft.itematic.access.DynamicRegistryAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.color.ItemColor;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.TintedItemComponent;
import net.minecraft.client.color.item.ItemColors;
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
        method = "usesDynamicDisplay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean usesDynamicDisplayIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.CLOCK);
    }

    @Redirect(
        method = "renderBakedItemQuads",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/color/item/ItemColors;getColor(Lnet/minecraft/item/ItemStack;I)I"
        )
    )
    private int renderBakedItemQuadsGetColorUseItemComponent(ItemColors instance, ItemStack item, int tintIndex) {
        return item.getComponent(ItemComponentTypes.TINTED)
            .map(TintedItemComponent::tint)
            .map(c -> c.getColor(item, tintIndex))
            .orElse(ItemColor.DEFAULT_COLOR);
    }

    public void loadDynamicEntries(DynamicRegistryManager registryManager) {
        Registry<Item> registry = registryManager.get(RegistryKeys.ITEM);
        this.models.reloadModelIds(registry);
    }
}
