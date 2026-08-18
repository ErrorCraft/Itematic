package net.errorcraft.itematic.mixin.world.entity.boss.enderdragon;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EndCrystal.class)
public abstract class EndCrystalExtender extends Entity {
    public EndCrystalExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForEndCrystalUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.END_CRYSTAL);
    }
}
