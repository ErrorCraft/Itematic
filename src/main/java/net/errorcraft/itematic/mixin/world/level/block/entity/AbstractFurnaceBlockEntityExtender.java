package net.errorcraft.itematic.mixin.world.level.block.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.FuelItemBehavior;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.FuelValues;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
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
        method = "getBurnDuration",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/FuelValues;burnDuration(Lnet/minecraft/world/item/ItemStack;)I"
        )
    )
    private int burnDurationUseItemBehavior(FuelValues instance, ItemStack itemStack, Operation<Integer> original) {
        return itemStack.itematic$getBehavior(ItemBehaviorType.FUEL)
            .map(FuelItemBehavior::ticks)
            .orElse(0);
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
        return instance.is(ItemIds.WET_SPONGE);
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
        method = "canTakeItemThroughFace",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        )
    )
    private boolean isWaterBucketCheckKey(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.WATER_BUCKET);
    }

    @WrapOperation(
        method = "canPlaceItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/entity/FuelValues;isFuel(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean isFuelCheckFuelItemBehavior(FuelValues instance, ItemStack itemStack, Operation<Boolean> original) {
        return itemStack.itematic$hasBehavior(ItemBehaviorType.FUEL);
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

    @ModifyArg(
        method = "consumeFuel",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/NonNullList;set(ILjava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private static <E> E setRemainderItemStackUseItemBehavior(E element, @Local(name = "fuelItem") Item fuelItem) {
        return (E) fuelItem.itematic$getBehavior(ItemBehaviorType.FUEL)
            .flatMap(FuelItemBehavior::remainder)
            .map(ItemStackTemplate::create)
            .orElse(ItemStack.EMPTY);
    }
}
