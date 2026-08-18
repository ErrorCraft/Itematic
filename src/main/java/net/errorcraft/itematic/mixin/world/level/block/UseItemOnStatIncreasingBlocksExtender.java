package net.errorcraft.itematic.mixin.world.level.block;

import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({
    BeehiveBlock.class,
    CakeBlock.class,
    ComposterBlock.class,
    DecoratedPotBlock.class,
    SignBlock.class,
    TntBlock.class
})
public class UseItemOnStatIncreasingBlocksExtender {
    @Redirect(
        method = "useItemOn",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T key, ItemStack stack) {
        return instance.itematic$get(stack.getItemHolder());
    }
}
