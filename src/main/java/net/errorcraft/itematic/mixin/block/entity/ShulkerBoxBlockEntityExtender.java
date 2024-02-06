package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityExtender extends LootableContainerBlockEntity {
    @Unique
    private RegistryWrapper.WrapperLookup wrapperLookup;

    protected ShulkerBoxBlockEntityExtender(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;readInventoryNbt(Lnet/minecraft/nbt/NbtCompound;)V"
        )
    )
    private void storeWrapperLookup(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        this.wrapperLookup = registryLookup;
    }

    @Inject(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/ShulkerBoxBlockEntity;readInventoryNbt(Lnet/minecraft/nbt/NbtCompound;)V",
            shift = At.Shift.AFTER
        )
    )
    private void resetWrapperLookup(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup, CallbackInfo info) {
        this.wrapperLookup = null;
    }

    @Redirect(
        method = "readInventoryNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;readNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)V"
        )
    )
    private void readInventoryUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        InventoryUtil.readFromNbt(nbt, stacks, this.wrapperLookup);
    }
}
