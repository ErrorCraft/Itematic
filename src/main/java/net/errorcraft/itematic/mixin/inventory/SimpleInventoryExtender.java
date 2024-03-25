package net.errorcraft.itematic.mixin.inventory;

import net.errorcraft.itematic.access.inventory.SimpleInventoryAccess;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleInventory.class)
public abstract class SimpleInventoryExtender implements SimpleInventoryAccess {
    @Shadow
    @Final
    private int size;

    @Shadow
    public abstract ItemStack getStack(int slot);

    @Shadow
    public abstract void markDirty();

    @Override
    public void itematic$removeItem(RegistryKey<Item> item, int count) {
        int countLeft = count;
        for (int i = this.size - 1; i >= 0; i--) {
            ItemStack heldStack = this.getStack(i);
            if (!heldStack.itematic$isOf(item)) {
                continue;
            }
            countLeft -= heldStack.itematic$tryDecrement(countLeft);
            if (countLeft <= 0) {
                break;
            }
        }
        if (countLeft < count) {
            this.markDirty();
        }
    }
}
