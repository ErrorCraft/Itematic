package net.errorcraft.itematic.mixin.entity.player;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.FoodItemComponent;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(HungerManager.class)
public abstract class HungerManagerExtender {
    @Shadow
    public abstract void add(int food, float saturationModifier);

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public void eat(Item item, ItemStack stack) {
        if (item == null) {
            return;
        }
        Optional<FoodItemComponent> foodItemComponent = item.itematic$getComponent(ItemComponentTypes.FOOD);
        foodItemComponent.ifPresent(food -> add(food.nutrition(), food.saturationModifier()));
    }
}
