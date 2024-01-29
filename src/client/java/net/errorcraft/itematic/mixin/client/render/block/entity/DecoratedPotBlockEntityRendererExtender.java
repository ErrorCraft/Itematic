package net.errorcraft.itematic.mixin.client.render.block.entity;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.block.entity.DecoratedPotBlockEntityAccess;
import net.errorcraft.itematic.block.entity.DecoratedPotBlockEntityUtil;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.DecoratedPotPatternItemComponent;
import net.errorcraft.itematic.mixin.block.DecoratedPotPatternsAccessor;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.DecoratedPotBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(DecoratedPotBlockEntityRenderer.class)
public class DecoratedPotBlockEntityRendererExtender {
    @Inject(
        method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity;getSherds()Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;",
            shift = At.Shift.AFTER
        )
    )
    private void storeRegistryEntrySherds(DecoratedPotBlockEntity decoratedPotBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info, @Share("sherds") LocalRef<Optional<DecoratedPotBlockEntityUtil.Sherds>> sherds) {
        sherds.set(((DecoratedPotBlockEntityAccess) decoratedPotBlockEntity).itematic$sherds());
    }

    @Redirect(
        method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;front()Lnet/minecraft/item/Item;"
        )
    )
    private Item frontUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, @Share("sherds") LocalRef<Optional<DecoratedPotBlockEntityUtil.Sherds>> sherds) {
        return sherds.get()
            .map(DecoratedPotBlockEntityUtil.Sherds::front)
            .map(RegistryEntry::value)
            .orElse(null);
    }

    @Redirect(
        method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;back()Lnet/minecraft/item/Item;"
        )
    )
    private Item backUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, @Share("sherds") LocalRef<Optional<DecoratedPotBlockEntityUtil.Sherds>> sherds) {
        return sherds.get()
            .map(DecoratedPotBlockEntityUtil.Sherds::back)
            .map(RegistryEntry::value)
            .orElse(null);
    }

    @Redirect(
        method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;left()Lnet/minecraft/item/Item;"
        )
    )
    private Item leftUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, @Share("sherds") LocalRef<Optional<DecoratedPotBlockEntityUtil.Sherds>> sherds) {
        return sherds.get()
            .map(DecoratedPotBlockEntityUtil.Sherds::left)
            .map(RegistryEntry::value)
            .orElse(null);
    }

    @Redirect(
        method = "render(Lnet/minecraft/block/entity/DecoratedPotBlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/DecoratedPotBlockEntity$Sherds;right()Lnet/minecraft/item/Item;"
        )
    )
    private Item rightUseRegistryEntry(DecoratedPotBlockEntity.Sherds instance, @Share("sherds") LocalRef<Optional<DecoratedPotBlockEntityUtil.Sherds>> sherds) {
        return sherds.get()
            .map(DecoratedPotBlockEntityUtil.Sherds::right)
            .map(RegistryEntry::value)
            .orElse(null);
    }

    @Redirect(
        method = "getTextureIdFromSherd",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DecoratedPotPatterns;fromSherd(Lnet/minecraft/item/Item;)Lnet/minecraft/registry/RegistryKey;",
            ordinal = 0
        )
    )
    private static RegistryKey<String> fromSherdUseItemComponent(Item sherd) {
        if (sherd == null) {
            return DecoratedPotPatternsAccessor.decoratedPotSide();
        }
        return sherd.itematic$getComponent(ItemComponentTypes.DECORATED_POT_PATTERN)
            .map(DecoratedPotPatternItemComponent::pattern)
            .flatMap(RegistryEntry::getKey)
            .orElse(DecoratedPotPatternsAccessor.decoratedPotSide());
    }

    @Redirect(
        method = "getTextureIdFromSherd",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DecoratedPotPatterns;fromSherd(Lnet/minecraft/item/Item;)Lnet/minecraft/registry/RegistryKey;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BRICK:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static RegistryKey<String> fromSherdUseDefaultRegistryKey(Item sherd) {
        return DecoratedPotPatternsAccessor.decoratedPotSide();
    }
}
