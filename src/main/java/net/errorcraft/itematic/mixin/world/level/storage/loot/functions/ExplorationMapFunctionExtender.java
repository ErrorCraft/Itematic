package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.MappableItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.functions.ExplorationMapFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(ExplorationMapFunction.class)
public class ExplorationMapFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isMapUseItemBehavior(ItemStack instance, Item item, @Share("mappable") LocalRef<MappableItemBehavior> mappable) {
        Optional<MappableItemBehavior> optionalMappable = instance.itematic$getBehavior(ItemBehaviorType.MAPPABLE);
        optionalMappable.ifPresent(mappable::set);
        return optionalMappable.isPresent();
    }

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/MapItem;create(Lnet/minecraft/server/level/ServerLevel;IIBZZ)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack createUseItemBehavior(ServerLevel world, int x, int z, byte scale, boolean showIcons, boolean unlimitedTracking, @Share("mappable") LocalRef<MappableItemBehavior> mappable) {
        return mappable.get().createStack(world, x, z, scale, showIcons, unlimitedTracking);
    }
}
