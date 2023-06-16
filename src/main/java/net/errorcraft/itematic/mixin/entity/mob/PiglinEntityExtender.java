package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.PiglinEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PiglinEntity.class)
public abstract class PiglinEntityExtender extends AbstractPiglinEntity {
    public PiglinEntityExtender(EntityType<? extends AbstractPiglinEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack dropEquipmentNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.PIGLIN_HEAD));
    }
}
