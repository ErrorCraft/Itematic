package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantmentHolderItemBehavior;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChiseledBookShelfBlock.class)
public class ChiseledBookshelfBlockExtender {
    @Redirect(
        method = {
            "addBook",
            "removeBook"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isEnchantedBookUseItemBehavior(ItemStack instance, Object o) {
        return instance.itematic$getBehavior(ItemBehaviorType.ENCHANTMENT_HOLDER)
            .map(EnchantmentHolderItemBehavior::grindingTransformsInto)
            .isPresent();
    }

    @Redirect(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private static <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return instance.itematic$get(itemStack.typeHolder());
    }
}
