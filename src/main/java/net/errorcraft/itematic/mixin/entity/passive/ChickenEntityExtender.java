package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityExtender extends MobEntityExtender {
    protected ChickenEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "tickMovement",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/passive/ChickenEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemForEggUseRegistryKey(ChickenEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(ItemKeys.EGG);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.CHICKEN_SPAWN_EGG;
    }
}
