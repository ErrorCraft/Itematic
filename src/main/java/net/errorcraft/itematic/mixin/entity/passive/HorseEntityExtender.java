package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ArmorItemComponent;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HorseEntity.class)
public class HorseEntityExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isHorseArmor(ItemStack item) {
        return item.itematic$getComponent(ItemComponentTypes.ARMOR)
            .flatMap(ArmorItemComponent::armorType)
            .map(type -> type == AnimalArmorItem.Type.EQUESTRIAN)
            .orElse(false);
    }
}
