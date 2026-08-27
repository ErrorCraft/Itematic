package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.access.world.level.block.state.BlockBehaviourAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockExtender implements BlockBehaviourAccess {
    @WrapOperation(
        method = "playerWillDestroy",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item, Operation<ItemStack> original, Level level) {
        return level.itematic$createStack(this.itematic$asItemId());
    }

    @Override
    public void itematic$addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
    }
}
