package net.errorcraft.itematic.mixin.enchantment;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ForgeableItemComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public class EnchantmentExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isAcceptableItem(ItemStack stack) {
        RegistryEntry<Enchantment> enchantment = Registries.ENCHANTMENT.getEntry((Enchantment) (Object)this);
        return stack.itematic$getComponent(ItemComponentTypes.FORGEABLE)
            .map(ForgeableItemComponent::enchantments)
            .map(tag -> tag.map(enchantment::isIn).orElse(true))
            .orElse(false);
    }
}
