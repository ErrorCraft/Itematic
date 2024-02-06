package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.MappableItemComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.ExplorationMapLootFunction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ExplorationMapLootFunction.class)
public class ExplorationMapLootFunctionExtender {
    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForMapUseItemComponentCheck(ItemStack instance, Item item, @Share("mappableItemComponent") LocalRef<MappableItemComponent> mappableItemComponent) {
        Optional<MappableItemComponent> optionalComponent = instance.itematic$getComponent(ItemComponentTypes.MAPPABLE);
        optionalComponent.ifPresent(mappableItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/FilledMapItem;createMap(Lnet/minecraft/world/World;IIBZZ)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack createMapUseItemComponent(World world, int x, int z, byte scale, boolean showIcons, boolean unlimitedTracking, @Share("mappableItemComponent") LocalRef<MappableItemComponent> mappableItemComponent) {
        return mappableItemComponent.get().createStack(world, x, z, scale, showIcons, unlimitedTracking);
    }
}
