package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntity.class)
public abstract class ItemEntityExtender extends Entity {

    private ItemEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "writeCustomDataToNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound writeCustomDataToNbtUseDynamicRegistry(ItemStack instance, NbtCompound nbt) {
        return ItemStackUtil.writeToNbt(nbt, this.world.getRegistryManager(), instance);
    }

    @Redirect(
        method = "readCustomDataFromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack readCustomDataFromNbtUseDynamicRegistry(NbtCompound nbt) {
        return ItemStackUtil.readFromNbt(nbt, this.world.getRegistryManager());
    }
}
