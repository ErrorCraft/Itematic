package net.errorcraft.itematic.mixin.enchantment;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EnchantmentHelper.class)
public interface EnchantmentHelperAccessor {
    @Invoker("getComponentType")
    static DataComponentType<ItemEnchantments> getComponentType(ItemStack stack) {
        throw new AssertionError();
    }
}
