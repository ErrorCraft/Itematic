package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TurtleEntity.class)
public abstract class TurtleEntityExtender extends MobEntityExtender {
    protected TurtleEntityExtender(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
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

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.TURTLE_SPAWN_EGG;
    }
}
