package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FuelItemComponent;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityExtender {
    @Inject(
        method = "getFuelTime",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        ),
        cancellable = true
    )
    public void getFuelTimeUseItemComponent(ItemStack fuel, CallbackInfoReturnable<Integer> info) {
        info.setReturnValue(fuel.itematic$getComponent(ItemComponentTypes.FUEL).map(FuelItemComponent::ticks).orElse(0));
    }

    @Inject(
        method = "canUseAsFuel",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void canUseAsFuelUseItemComponentCheck(ItemStack stack, CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(stack.itematic$hasComponent(ItemComponentTypes.FUEL));
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
            target = "net/minecraft/item/ItemStack"
        )
    )
    private static ItemStack newItemStackForWaterBucketUseRegistryEntry(ItemConvertible item, DynamicRegistryManager registryManager) {
        return new ItemStack(registryManager.get(RegistryKeys.ITEM).entryOf(ItemKeys.WATER_BUCKET));
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
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;BUCKET:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        ),
        allow = 2
    )
    private boolean isValidIsOfForBucketUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BUCKET);
    }
}
