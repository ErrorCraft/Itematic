package net.errorcraft.itematic.mixin.world.entity.animal.axolotl;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Axolotl.class)
public abstract class AxolotlExtender extends MobExtender {
    protected AxolotlExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "usePlayerItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isTropicalFishBucketCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.TROPICAL_FISH_BUCKET);
    }

    @Redirect(
        method = "usePlayerItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForWaterBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.WATER_BUCKET);
    }

    @Redirect(
        method = "getBucketItemStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForAxolotlBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.AXOLOTL_BUCKET);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.AXOLOTL_SPAWN_EGG;
    }
}
