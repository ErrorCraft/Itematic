package net.errorcraft.itematic.mixin.entity.decoration;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystalEntity.class)
public abstract class EndCrystalEntityExtender extends Entity {
    public EndCrystalEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForEndCrystalUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.END_CRYSTAL);
    }
}
