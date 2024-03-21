package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FrogEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FrogEntity.class)
public abstract class FrogEntityExtender extends MobEntityExtender {
    protected FrogEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.FROG_FOOD);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.FROG_SPAWN_EGG;
    }
}
