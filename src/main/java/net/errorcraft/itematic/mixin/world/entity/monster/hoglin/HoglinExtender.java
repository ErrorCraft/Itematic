package net.errorcraft.itematic.mixin.world.entity.monster.hoglin;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Hoglin.class)
public abstract class HoglinExtender extends MobExtender {
    protected HoglinExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.HOGLIN_SPAWN_EGG;
    }
}
