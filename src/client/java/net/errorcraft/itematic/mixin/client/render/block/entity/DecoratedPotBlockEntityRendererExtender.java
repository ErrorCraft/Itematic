package net.errorcraft.itematic.mixin.client.render.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DecoratedPotPatternItemComponent;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotBlockEntityRendererExtender {
    @Redirect(
        method = "getSideMaterial",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/DecoratedPotPatterns;getPatternFromItem(Lnet/minecraft/world/item/Item;)Lnet/minecraft/resources/ResourceKey;",
            ordinal = 0
        )
    )
    private static ResourceKey<DecoratedPotPattern> fromSherdUseItemComponent(Item sherd) {
        return sherd.itematic$getBehavior(ItemComponentTypes.DECORATED_POT_PATTERN)
            .map(DecoratedPotPatternItemComponent::pattern)
            .flatMap(Holder::unwrapKey)
            .orElse(null);
    }
}
