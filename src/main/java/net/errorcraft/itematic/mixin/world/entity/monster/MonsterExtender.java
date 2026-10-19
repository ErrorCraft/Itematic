package net.errorcraft.itematic.mixin.world.entity.monster;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.mixin.world.entity.LivingEntityExtender;
import net.minecraft.references.ItemIds;
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

    @WrapMethod(
        method = "getProjectile"
    )
    private ItemStack useDataComponent(ItemStack heldWeapon, Operation<ItemStack> original) {
        return this.itematic$getAmmunition(heldWeapon);
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
