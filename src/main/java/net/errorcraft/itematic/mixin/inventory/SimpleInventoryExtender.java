package net.errorcraft.itematic.mixin.inventory;

import net.errorcraft.itematic.access.inventory.SimpleInventoryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SimpleContainer.class)
public abstract class SimpleInventoryExtender implements SimpleInventoryAccess {
    @Shadow
    @Final
    private int size;

    @Shadow
    public abstract ItemStack getItem(int slot);

    @Shadow
    public abstract void setChanged();

    @Override
    public void itematic$removeItem(ResourceKey<Item> item, int count) {
        int countLeft = count;
        for (int i = this.size - 1; i >= 0; i--) {
            ItemStack heldStack = this.getItem(i);
            if (!heldStack.itematic$isOf(item)) {
                continue;
            }
            countLeft -= heldStack.itematic$tryDecrement(countLeft);
            if (countLeft <= 0) {
                break;
            }
        }
        if (countLeft < count) {
            this.setChanged();
        }
    }
}
