package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.BlockAttachedEntity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LeashKnotEntity.class)
public abstract class LeashKnotEntityExtender extends BlockAttachedEntity {
    protected LeashKnotEntityExtender(EntityType<? extends BlockAttachedEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForLeadUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.LEAD);
    }
}
