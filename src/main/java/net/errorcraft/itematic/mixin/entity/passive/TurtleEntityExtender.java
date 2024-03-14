package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TurtleEntity.class)
public abstract class TurtleEntityExtender extends AnimalEntity {
    protected TurtleEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "isBreedingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean testForFoodItemsUseItemTagCheck(ItemStack instance, Item item) {
        return instance.isIn(ItematicItemTags.TURTLE_FOOD);
    }

    @Redirect(
        method = "onGrowUp",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/TurtleEntity;dropItem(Lnet/minecraft/item/ItemConvertible;I)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForTurtleScuteUseRegistryKey(TurtleEntity instance, ItemConvertible itemConvertible, int yOffset) {
        return this.itematic$dropItem(ItemKeys.TURTLE_SCUTE, yOffset);
    }
}
