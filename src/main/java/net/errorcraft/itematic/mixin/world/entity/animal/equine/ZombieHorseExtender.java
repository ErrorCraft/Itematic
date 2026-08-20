package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.ZombieHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombieHorse.class)
public abstract class ZombieHorseExtender extends MobExtender {
    protected ZombieHorseExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronSpearUseCreateStack(ItemLike item, ServerLevelAccessor level) {
        return level.itematic$createStack(ItemIds.IRON_SPEAR);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.ZOMBIE_HORSE_SPAWN_EGG;
    }
}
