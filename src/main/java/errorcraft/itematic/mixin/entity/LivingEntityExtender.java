package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.FoodItemComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(LivingEntity.class)
public class LivingEntityExtender {
    @Redirect(
        method = "eatFood",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isFood()Z"
        )
    )
    private boolean assumeExistingItemComponent(ItemStack instance) {
        return true;
    }

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    private void applyFoodEffects(ItemStack stack, World world, LivingEntity targetEntity) {
        if (world.isClient()) {
            return;
        }
        Optional<FoodItemComponent> component = stack.getComponent(ItemComponentTypes.FOOD);
        if (component.isEmpty()) {
            return;
        }
        for (FoodItemComponent.Effect effect : component.get().effects()) {
            effect.tryApply(targetEntity, world.random);
        }
    }
}
