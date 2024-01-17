package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VindicatorEntity.class)
public abstract class VindicatorEntityExtender extends IllagerEntity {
    protected VindicatorEntityExtender(EntityType<? extends IllagerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = { "initEquipment", "addBonusForWave" },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForIronAxeUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.IRON_AXE);
    }
}
