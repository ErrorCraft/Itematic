package net.errorcraft.itematic.mixin.world.entity.monster.creaking;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.creaking.Creaking;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Creaking.class)
public abstract class CreakingExtender extends MobExtender {
    protected CreakingExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.CREAKING_SPAWN_EGG;
    }
}
