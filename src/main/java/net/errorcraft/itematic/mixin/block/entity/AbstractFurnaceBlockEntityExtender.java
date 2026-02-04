package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FuelItemComponent;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.FuelRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityExtender {
    @Redirect(
        method = "getFuelTime",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/FuelRegistry;getFuelTicks(Lnet/minecraft/item/ItemStack;)I"
        )
    )
    private int getFuelTicksUseDataComponent(FuelRegistry instance, ItemStack item) {
        return item.itematic$getBehavior(ItemComponentTypes.FUEL)
            .map(FuelItemComponent::ticks)
            .orElse(0);
    }

    @Redirect(
        method = "craftRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/block/Blocks;WET_SPONGE:Lnet/minecraft/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isOfForWetSpongeUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WET_SPONGE);
    }

    @Redirect(
        method = "craftRecipe",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BUCKET:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isOfForBucketUseRegistryKeyCheckStatic(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUCKET);
    }

    @Redirect(
        method = "craftRecipe",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseRegistryEntry(ItemConvertible item, DynamicRegistryManager registryManager) {
        return new ItemStack(registryManager.getOrThrow(RegistryKeys.ITEM).getOrThrow(ItemKeys.WATER_BUCKET));
    }

    @Redirect(
        method = "canExtract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isOfForWaterBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.WATER_BUCKET);
    }
    @Redirect(
        method = "canExtract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BUCKET:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUCKET);
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/FuelRegistry;isFuel(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean isFuelUseItemComponentCheck(FuelRegistry instance, ItemStack item) {
        return item.itematic$hasBehavior(ItemComponentTypes.FUEL);
    }

    @Redirect(
        method = "isValid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isValidIsOfForBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUCKET);
    }

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/collection/DefaultedList;set(ILjava/lang/Object;)Ljava/lang/Object;"
        )
    )
    @SuppressWarnings("unchecked")
    private static <E> E setRemainderItemStackUseItemComponent(E element, @Local(ordinal = 0) Item item) {
        return (E) item.itematic$getBehavior(ItemComponentTypes.FUEL)
            .flatMap(FuelItemComponent::remainder)
            .orElse(ItemStack.EMPTY);
    }
}
