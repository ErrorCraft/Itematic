package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashKnotEntityExtender extends BlockAttachedEntity {
    protected LeashKnotEntityExtender(EntityType<? extends BlockAttachedEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackForLeadUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.LEAD);
    }
}
