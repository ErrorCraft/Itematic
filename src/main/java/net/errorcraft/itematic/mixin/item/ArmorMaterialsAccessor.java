package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.ArmorMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsAccessor {
    @Accessor("LEATHER_ENCHANTABILITY")
    static int leatherEnchantability() {
        throw new AssertionError();
    }

    @Accessor("CHAIN_ENCHANTABILITY")
    static int chainEnchantability() {
        throw new AssertionError();
    }

    @Accessor("IRON_ENCHANTABILITY")
    static int ironEnchantability() {
        throw new AssertionError();
    }

    @Accessor("GOLD_ENCHANTABILITY")
    static int goldEnchantability() {
        throw new AssertionError();
    }

    @Accessor("DIAMOND_ENCHANTABILITY")
    static int diamondEnchantability() {
        throw new AssertionError();
    }

    @Accessor("TURTLE_ENCHANTABILITY")
    static int turtleEnchantability() {
        throw new AssertionError();
    }

    @Accessor("NETHERITE_ENCHANTABILITY")
    static int netheriteEnchantability() {
        throw new AssertionError();
    }
}
