package net.errorcraft.itematic.mixin.world.level.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ChiseledBookShelfBlock.class)
public class ChiseledBookshelfBlockExtender {
    @WrapOperation(
        method = {
            "addBook",
            "removeBook"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isEnchantedBookCheckStoredEnchantmentsDataComponent(ItemStack instance, Object object, Operation<Boolean> original) {
        return instance.has(DataComponents.STORED_ENCHANTMENTS);
    }

    @WrapOperation(
        method = "addBook",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private static <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, Operation<Stat<T>> original, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
        return instance.itematic$get(itemStack.typeHolder());
    }
}
