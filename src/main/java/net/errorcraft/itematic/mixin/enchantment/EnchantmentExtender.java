package net.errorcraft.itematic.mixin.enchantment;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Enchantment.class)
public class EnchantmentExtender {
    // TODO: Merge with Mojang's data-driven implementation
//    /**
//     * @author ErrorCraft
//     * @reason Uses the ItemComponent implementation for data-driven items.
//     */
//    @Overwrite
//    public boolean isAcceptableItem(ItemStack stack) {
//        RegistryEntry<Enchantment> enchantment = Registries.ENCHANTMENT.getEntry((Enchantment) (Object)this);
//        return stack.itematic$getComponent(ItemComponentTypes.FORGEABLE)
//            .map(ForgeableItemComponent::enchantments)
//            .map(tag -> tag.map(enchantment::isIn).orElse(true))
//            .orElse(false);
//    }
}
