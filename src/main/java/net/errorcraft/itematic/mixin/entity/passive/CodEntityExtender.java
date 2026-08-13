package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.fish.Cod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Cod.class)
public abstract class CodEntityExtender extends MobEntityExtender {
    public CodEntityExtender(EntityType<? extends AbstractSchoolingFish> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getBucketItemStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForCodBucketUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.COD_BUCKET);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.COD_SPAWN_EGG;
    }
}
