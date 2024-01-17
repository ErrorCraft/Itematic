package net.errorcraft.itematic.mixin.entity.ai.brain.sensor;

import net.errorcraft.itematic.access.entity.ai.brain.sensor.TemptationsSensorAccess;
import net.minecraft.entity.ai.brain.sensor.TemptationsSensor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TemptationsSensor.class)
public class TemptationsSensorExtender implements TemptationsSensorAccess {
    @Unique
    private TagKey<Item> temptations;

    @Override
    public void itematic$setTemptations(TagKey<Item> temptations) {
        this.temptations = temptations;
    }

    @Redirect(
        method = "test(Lnet/minecraft/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        if (this.temptations == null) {
            return false;
        }
        return itemStack.isIn(this.temptations);
    }
}
