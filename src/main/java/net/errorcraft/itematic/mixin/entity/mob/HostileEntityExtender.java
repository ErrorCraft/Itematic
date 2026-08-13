package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.mixin.entity.LivingEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
public abstract class HostileEntityExtender extends LivingEntityExtender {
    public HostileEntityExtender(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    public ItemStack itematic$getAmmunition(ItemStack stack) {
        ItemStack ammunition = super.itematic$getAmmunition(stack);
        if (!ammunition.isEmpty()) {
            return ammunition;
        }

        return this.level().itematic$createStack(ItemIds.ARROW);
    }
}
