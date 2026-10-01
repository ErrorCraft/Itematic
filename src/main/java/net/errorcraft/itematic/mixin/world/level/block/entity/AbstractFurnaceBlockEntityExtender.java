package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityExtender {
    @Unique
    private static final ScopedValue<ServerLevel> LEVEL = ScopedValue.newInstance();

    @WrapOperation(
        method = "serverTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;burn(Lnet/minecraft/core/NonNullList;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"
        )
    )
    private static void passLevel(NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result, Operation<Void> original, ServerLevel level) {
        ScopedValue.where(LEVEL, level)
            .run(() -> original.call(items, inputItemStack, result));
    }

    @WrapOperation(
        method = "burn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;WET_SPONGE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isWetSpongeCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(BlockItemIds.WET_SPONGE.item());
    }

    @WrapOperation(
        method = {
            "burn",
            "canTakeItemThroughFace"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;BUCKET:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isBucketCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.BUCKET);
    }

    @WrapOperation(
        method = "burn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseHolder(ItemLike item, Operation<ItemStack> original) {
        return LEVEL.get().itematic$createStack(ItemIds.WATER_BUCKET);
    }

    @WrapOperation(
        method = "canPlaceItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBucketCheckIdForCanPlaceItem(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.BUCKET);
    }

    @WrapOperation(
        method = "consumeFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getCraftingRemainder()Lnet/minecraft/world/item/ItemStackTemplate;"
        )
    )
    @Nullable
    private static ItemStackTemplate useDataComponent(Item instance, Operation<ItemStackTemplate> original, @Local(name = "fuel", argsOnly = true) ItemStack fuel) {
        return fuel.get(ItematicDataComponents.COOKING_FUEL_REMAINDER);
    }
}
