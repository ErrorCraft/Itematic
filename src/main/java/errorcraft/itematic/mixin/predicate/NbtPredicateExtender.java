package errorcraft.itematic.mixin.predicate;

import errorcraft.itematic.item.ItemStackUtil;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NbtPredicate.class)
public class NbtPredicateExtender {
    @Redirect(
        method = "entityToNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;writeNbt(Lnet/minecraft/nbt/NbtCompound;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private static NbtCompound entityToNbtUseDynamicRegistry(ItemStack instance, NbtCompound nbt, Entity entity) {
        return ItemStackUtil.writeToNbt(nbt, entity.world.getRegistryManager(), instance);
    }
}
