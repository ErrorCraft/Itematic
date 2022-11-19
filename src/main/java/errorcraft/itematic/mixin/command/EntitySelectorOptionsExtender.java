package errorcraft.itematic.mixin.command;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntitySelectorOptions.class)
public class EntitySelectorOptionsExtender {
    @Redirect(
        method = "method_9957",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        ),
        remap = false
    )
    private static NbtCompound writeNbtUseDynamicRegistry(ItemStack instance, NbtCompound nbt, NbtCompound nbtCompound, boolean bl, Entity entity) {
        return ItemStackUtil.writeToNbt(nbt, entity.world.getRegistryManager(), instance);
    }
}
