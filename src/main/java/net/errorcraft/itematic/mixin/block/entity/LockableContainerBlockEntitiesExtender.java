package net.errorcraft.itematic.mixin.block.entity;

import net.errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ AbstractFurnaceBlockEntity.class, BarrelBlockEntity.class, BrewingStandBlockEntity.class, ChestBlockEntity.class, DispenserBlockEntity.class, HopperBlockEntity.class, CrafterBlockEntity.class })
public abstract class LockableContainerBlockEntitiesExtender extends LockableContainerBlockEntity {
    protected LockableContainerBlockEntitiesExtender(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;readNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)V"
        )
    )
    private void readNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        InventoryUtil.readFromNbt(nbt, this.getRegistryManager(), stacks);
    }
}
