package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Chicken.class)
public abstract class ChickenEntityExtender extends MobEntityExtender {
    protected ChickenEntityExtender(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.CHICKEN_SPAWN_EGG;
    }
}
