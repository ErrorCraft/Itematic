package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Donkey.class)
public abstract class DonkeyExtender extends MobExtender {
    protected DonkeyExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.DONKEY_SPAWN_EGG;
    }
}
