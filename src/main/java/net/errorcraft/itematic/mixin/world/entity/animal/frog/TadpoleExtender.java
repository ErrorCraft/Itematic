package net.errorcraft.itematic.mixin.world.entity.animal.frog;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Tadpole.class)
public abstract class TadpoleExtender extends MobExtender {
    public TadpoleExtender(EntityType<? extends AbstractFish> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getBucketItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForTadpoleBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.TADPOLE_BUCKET);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.TADPOLE_SPAWN_EGG;
    }
}
