package errorcraft.itematic.mixin.block.entity;

import errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityExtender extends LootableContainerBlockEntity {
    protected ChestBlockEntityExtender(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;writeNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound writeNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        if (this.world != null) {
            InventoryUtil.writeToNbt(nbt, this.world.getRegistryManager(), stacks);
        }
        return nbt;
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;readNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)V"
        )
    )
    private void readNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks) {
        if (this.world != null) {
            InventoryUtil.readFromNbt(nbt, this.world.getRegistryManager(), stacks);
        }
    }
}
