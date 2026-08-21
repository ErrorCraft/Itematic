package net.errorcraft.itematic.mixin.world.entity.animal.fish;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.Salmon;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Salmon.class)
public abstract class SalmonExtender extends MobExtender {
    public SalmonExtender(EntityType<? extends AbstractSchoolingFish> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getBucketItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForSalmonBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.SALMON_BUCKET);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.SALMON_SPAWN_EGG;
    }
}
