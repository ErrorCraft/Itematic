package net.errorcraft.itematic.mixin.block;

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
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private static boolean isOfForEnchantedBookUseItemBehavior(ItemStack instance, Item item) {
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
    private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(argsOnly = true) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getItemHolder());
    }
}
