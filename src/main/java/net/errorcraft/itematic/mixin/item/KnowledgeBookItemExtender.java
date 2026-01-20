package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.KnowledgeBookItem;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
import net.minecraft.util.TypedActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KnowledgeBookItem.class)
public class KnowledgeBookItemExtender {
    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/TypedActionResult;fail(Ljava/lang/Object;)Lnet/minecraft/util/TypedActionResult;"
        )
    )
    private <T> TypedActionResult<T> failResultPassInstead(T data) {
        return TypedActionResult.pass(data);
    }

    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stat/StatType;getOrCreateStat(Ljava/lang/Object;)Lnet/minecraft/stat/Stat;"
        )
    )
    private <T> Stat<Item> getOrCreateStatUseRegistryEntry(StatType<Item> instance, T key, @Local ItemStack stack) {
        return instance.itematic$getOrCreateStat(stack.getRegistryEntry());
    }
}
