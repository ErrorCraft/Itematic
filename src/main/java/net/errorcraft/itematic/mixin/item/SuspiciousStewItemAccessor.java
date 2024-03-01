package net.errorcraft.itematic.mixin.item;

import net.minecraft.block.SuspiciousStewIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(SuspiciousStewItem.class)
public interface SuspiciousStewItemAccessor {
    @Invoker("forEachEffect")
    static void forEachEffect(ItemStack stew, Consumer<SuspiciousStewIngredient.StewEffect> effectConsumer) {}
}
