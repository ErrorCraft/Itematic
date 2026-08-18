package net.errorcraft.itematic.mixin.world.entity.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.entity.player.StackedItemContentsAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(StackedItemContents.class)
public class StackedItemContentsExtender implements StackedItemContentsAccess {
    @Unique
    @Nullable
    private Level level;

    @WrapOperation(
        method = {
            "canCraft(Lnet/minecraft/world/item/crafting/Recipe;ILnet/minecraft/world/entity/player/StackedContents$Output;)Z",
            "getBiggestCraftableStack(Lnet/minecraft/world/item/crafting/Recipe;ILnet/minecraft/world/entity/player/StackedContents$Output;)I"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/crafting/Recipe;placementInfo()Lnet/minecraft/world/item/crafting/PlacementInfo;"
        )
    )
    private PlacementInfo placementInfoUseDynamicRegistry(Recipe<?> instance, Operation<PlacementInfo> original) {
        if (this.level == null) {
            return original.call(instance);
        }

        return instance.itematic$placementInfo(this.level.registryAccess().lookupOrThrow(Registries.ITEM));
    }

    @Override
    public void itematic$setLevel(Level level) {
        this.level = level;
    }
}
