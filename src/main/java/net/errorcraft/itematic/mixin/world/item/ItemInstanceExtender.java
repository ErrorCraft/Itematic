package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.access.world.item.ItemInstanceAccess;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.world.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemInstance.class)
public interface ItemInstanceExtender extends ItemInstanceAccess {
    @WrapMethod(
        method = "getMaxStackSize"
    )
    private int alsoCheckStackableItemBehavior(Operation<Integer> original) {
        if (!this.itematic$hasBehavior(ItemBehaviorType.STACKABLE)) {
            return Items.UNSTACKABLE_MAX_STACK_SIZE;
        }

        return original.call();
    }
}
