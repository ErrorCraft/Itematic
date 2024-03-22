package net.errorcraft.itematic.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantmentHolderItemComponent;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChiseledBookshelfBlock.class)
public class ChiseledBookshelfBlockExtender {
    @Redirect(
        method = { "tryAddBook", "tryRemoveBook" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private static boolean isOfForEnchantedBookUseItemComponent(ItemStack instance, Item item) {
        return instance.itematic$getComponent(ItemComponentTypes.ENCHANTMENT_HOLDER)
            .map(EnchantmentHolderItemComponent::grindingTransformsInto)
            .isPresent();
    }

    @Redirect(
        method = "tryAddBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private static <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local(argsOnly = true) ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }
}
