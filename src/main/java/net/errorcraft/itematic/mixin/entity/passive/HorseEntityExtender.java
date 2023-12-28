package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.AnimalArmorItemComponent;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(HorseEntity.class)
public class HorseEntityExtender {
    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public boolean isHorseArmor(ItemStack item) {
        return item.itematic$getComponent(ItemComponentTypes.ANIMAL_ARMOR)
            .map(AnimalArmorItemComponent::armorType)
            .map(type -> type == AnimalArmorItem.Type.EQUESTRIAN)
            .orElse(false);
    }

    @Redirect(
        method = "setArmorTypeFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        )
    )
    private Item getItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "setArmorTypeFromStack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/AnimalArmorItem;getBonus()I"
        )
    )
    private int getBonusReturnDefault(AnimalArmorItem instance) {
        return 0;
    }
}
