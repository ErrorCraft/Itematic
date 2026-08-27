package net.errorcraft.itematic.mixin.world.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.KnowledgeBookItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KnowledgeBookItem.class)
public class KnowledgeBookItemExtender {
    @Redirect(
        method = "use",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/stats/StatType;get(Ljava/lang/Object;)Lnet/minecraft/stats/Stat;"
        )
    )
    private <T> Stat<Item> getStatUseHolder(StatType<Item> instance, T argument, @Local(name = "itemStack") ItemStack itemStack) {
        return instance.itematic$get(itemStack.typeHolder());
    }
}
