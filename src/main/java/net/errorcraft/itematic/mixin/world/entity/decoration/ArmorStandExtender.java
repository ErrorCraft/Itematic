package net.errorcraft.itematic.mixin.world.entity.decoration;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ArmorStand.class)
public abstract class ArmorStandExtender extends LivingEntity {
    protected ArmorStandExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = {
            "brokenByPlayer",
            "getPickResult"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForArmorStandUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.ARMOR_STAND);
    }

    @Redirect(
        method = "interactAt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isNameTagCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.NAME_TAG);
    }
}
