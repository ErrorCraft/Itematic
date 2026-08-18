package net.errorcraft.itematic.mixin.world.entity.vehicle.minecart;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecartCommandBlock.class)
public abstract class MinecartCommandBlockExtender extends AbstractMinecart {
    protected MinecartCommandBlockExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForCommandBlockMinecartUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.COMMAND_BLOCK_MINECART);
    }
}
