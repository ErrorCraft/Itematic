package errorcraft.itematic.mixin.entity;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

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

    @Redirect(
        method = "applyFoodEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/FoodComponent;getStatusEffects()Ljava/util/List;"
        )
    )
    private List<Pair<StatusEffectInstance, Float>> useEmptyCollection(FoodComponent instance) {
        return List.of();
    }
}
