package net.errorcraft.itematic.mixin.world.entity.monster;

import net.errorcraft.itematic.mixin.world.entity.LivingEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Monster.class)
public abstract class MonsterExtender extends LivingEntityExtender {
    public MonsterExtender(EntityType<?> type, Level level) {
        super(type, level);
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
