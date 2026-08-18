package net.errorcraft.itematic.mixin.world.entity.vehicle;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VehicleEntity.class)
public abstract class VehicleEntityExtender extends Entity {
    public VehicleEntityExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "destroy(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/Item;)V",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseRegistryEntry(ItemLike item) {
        return this.level().itematic$createStack(this.asItemId());
    }

    @Unique
    protected ResourceKey<Item> asItemId() {
        return ItemIds.MINECART;
    }
}
