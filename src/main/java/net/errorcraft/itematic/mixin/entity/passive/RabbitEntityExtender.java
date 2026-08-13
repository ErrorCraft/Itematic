package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Rabbit.class)
public abstract class RabbitEntityExtender extends MobEntityExtender {
    protected RabbitEntityExtender(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.RABBIT_SPAWN_EGG;
    }
}
