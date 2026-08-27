package net.errorcraft.itematic.mixin.client.renderer.blockentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.DecoratedPotPatternItemBehavior;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotRendererExtender {
    @WrapOperation(
        method = "getSideSprite",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/DecoratedPotPatterns;getPatternFromItem(Lnet/minecraft/world/item/Item;)Lnet/minecraft/resources/ResourceKey;"
        )
    )
    @Nullable
    private static ResourceKey<DecoratedPotPattern> getPatternFromItemUseItemBehavior(Item item, Operation<ResourceKey<DecoratedPotPattern>> original) {
        return item.itematic$getBehavior(ItemBehaviorType.DECORATED_POT_PATTERN)
            .map(DecoratedPotPatternItemBehavior::pattern)
            .flatMap(Holder::unwrapKey)
            .orElse(null);
    }
}
