package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.MappableItemComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ExplorationMapFunction.class)
public class ExplorationMapLootFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForMapUseItemComponentCheck(ItemStack instance, Item item, @Share("mappableItemComponent") LocalRef<MappableItemComponent> mappableItemComponent) {
        Optional<MappableItemComponent> optionalComponent = instance.itematic$getBehavior(ItemComponentTypes.MAPPABLE);
        optionalComponent.ifPresent(mappableItemComponent::set);
        return optionalComponent.isPresent();
    }

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MapItem;create(Lnet/minecraft/server/level/ServerLevel;IIBZZ)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack createMapUseItemComponent(ServerLevel world, int x, int z, byte scale, boolean showIcons, boolean unlimitedTracking, @Share("mappableItemComponent") LocalRef<MappableItemComponent> mappableItemComponent) {
        return mappableItemComponent.get().createStack(world, x, z, scale, showIcons, unlimitedTracking);
    }
}
