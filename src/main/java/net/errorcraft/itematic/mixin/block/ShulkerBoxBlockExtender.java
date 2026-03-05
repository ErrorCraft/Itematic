package net.errorcraft.itematic.mixin.block;

import net.errorcraft.itematic.access.block.AbstractBlockAccess;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockExtender implements AbstractBlockAccess {
    @Redirect(
        method = "onBreak",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/ShulkerBoxBlock;getItemStack(Lnet/minecraft/util/DyeColor;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(DyeColor color, World world) {
        return world.itematic$createStack(this.itematic$asItemKey());
    }

    @Override
    public void itematic$addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.CONTAINER, ContainerComponent.DEFAULT);
    }
}
