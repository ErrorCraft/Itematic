package net.errorcraft.itematic.mixin.client.render.block.entity;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DecoratedPotPatternItemComponent;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DecoratedPotBlockEntityRenderer.class)
public class DecoratedPotBlockEntityRendererExtender {
    @Redirect(
        method = "getTextureIdFromSherd",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DecoratedPotPatterns;fromSherd(Lnet/minecraft/item/Item;)Lnet/minecraft/registry/RegistryKey;",
            ordinal = 0
        )
    )
    private static RegistryKey<DecoratedPotPattern> fromSherdUseItemComponent(Item sherd) {
        return sherd.itematic$getComponent(ItemComponentTypes.DECORATED_POT_PATTERN)
            .map(DecoratedPotPatternItemComponent::pattern)
            .flatMap(RegistryEntry::getKey)
            .orElse(null);
    }
}
