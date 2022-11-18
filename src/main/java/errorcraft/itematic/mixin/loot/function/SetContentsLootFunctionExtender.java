package errorcraft.itematic.mixin.loot.function;

import errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetContentsLootFunction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SetContentsLootFunction.class)
public class SetContentsLootFunctionExtender {
    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/inventory/Inventories;writeNbt(Lnet/minecraft/nbt/NbtCompound;Lnet/minecraft/util/collection/DefaultedList;)Lnet/minecraft/nbt/NbtCompound;"
        )
    )
    private NbtCompound writeNbtUseDynamicRegistry(NbtCompound nbt, DefaultedList<ItemStack> stacks, ItemStack stack, LootContext context) {
        return InventoryUtil.writeToNbt(nbt, context.getWorld().getRegistryManager(), stacks);
    }
}
