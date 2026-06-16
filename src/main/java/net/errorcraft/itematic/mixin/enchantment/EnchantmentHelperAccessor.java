package net.errorcraft.itematic.mixin.enchantment;

import net.minecraft.component.ComponentType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnchantmentHelper.class)
public interface EnchantmentHelperAccessor {
    @Invoker("getEnchantmentsComponentType")
    static ComponentType<ItemEnchantmentsComponent> getComponentType(ItemStack stack) {
        throw new AssertionError();
    }

    @Invoker("forEachEnchantment")
    static void forEachEnchantment(ItemStack stack, EnchantmentHelper.Consumer consumer) {
        throw new AssertionError();
    }
}
