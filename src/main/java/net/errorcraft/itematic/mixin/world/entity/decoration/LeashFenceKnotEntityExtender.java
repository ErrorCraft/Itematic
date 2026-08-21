package net.errorcraft.itematic.mixin.world.entity.decoration;

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
public abstract class LeashFenceKnotEntityExtender extends BlockAttachedEntity {
    protected LeashFenceKnotEntityExtender(EntityType<? extends BlockAttachedEntity> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForLeadUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.LEAD);
    }
}
