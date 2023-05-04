package errorcraft.itematic.mixin.entity;

import errorcraft.itematic.access.entity.LivingEntityAccess;
import errorcraft.itematic.item.component.ItemComponentTypes;
import errorcraft.itematic.item.component.components.EquipmentItemComponent;
import errorcraft.itematic.item.component.components.FoodItemComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityExtender implements LivingEntityAccess {
    @Shadow
    public abstract boolean isHolding(Predicate<ItemStack> predicate);

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

    /**
     * @author ErrorCraft
     * @reason Uses the ItemComponent implementation for data-driven items.
     */
    @Overwrite
    public static EquipmentSlot getPreferredEquipmentSlot(ItemStack stack) {
        return stack.getComponent(ItemComponentTypes.EQUIPMENT).map(EquipmentItemComponent::slot).orElse(EquipmentSlot.MAINHAND);
    }

    @Override
    public boolean isHolding(RegistryKey<Item> key) {
        return this.isHolding(stack -> stack.isOf(key));
    }
}
