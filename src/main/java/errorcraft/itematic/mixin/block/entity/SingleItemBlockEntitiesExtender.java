package errorcraft.itematic.mixin.block.entity;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ JukeboxBlockEntity.class, LecternBlockEntity.class })
public abstract class SingleItemBlockEntitiesExtender extends BlockEntity {
    public SingleItemBlockEntitiesExtender(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack readNbtUseDynamicRegistry(NbtCompound nbt) {
        if (this.world != null) {
            return ItemStackUtil.readFromNbt(nbt, this.world.getRegistryManager());
        }
        return ItemStack.EMPTY;
    }
}
