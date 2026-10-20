package net.errorcraft.itematic.world.item.enchantment;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantable;

import java.util.Optional;

public class Enchantables {
    private Enchantables() {}

    public static Optional<Holder<Item>> transformsInto(ItemStack stack) {
        Enchantable enchantable = stack.get(DataComponents.ENCHANTABLE);
        if (enchantable == null) {
            return Optional.empty();
        }

        return enchantable.itematic$transformsInto();
    }
}
