package net.errorcraft.itematic.mixin.world.entity.monster.cubemob;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Slime.class)
public abstract class SlimeExtender extends MobExtender {
    protected SlimeExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.SLIME_SPAWN_EGG;
    }
}
