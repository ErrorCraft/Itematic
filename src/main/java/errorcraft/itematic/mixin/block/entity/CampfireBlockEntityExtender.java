package errorcraft.itematic.mixin.block.entity;

import errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CampfireBlockEntity.class)
public abstract class CampfireBlockEntityExtender extends BlockEntity {
    public CampfireBlockEntityExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = { "writeNbt", "toInitialChunkDataNbt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;writeNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;Z)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound writeNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks, boolean setIfEmpty) {
        if (this.world != null) {
            return InventoryUtil.writeToNbt(nbt, this.world.getRegistryManager(), stacks, setIfEmpty);
        }
        return nbt;
    }
}
