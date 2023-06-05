package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class FireworkRocketDispenserBehavior extends ProjectileDispenserBehavior {
    @Override
    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
        return new FireworkRocketEntity(world, stack, position.getX(), position.getY(), position.getZ(), true);
    }

    @Override
    protected float getForce() {
        return 0.5f;
    }

    @Override
    protected float getVariation() {
        return 1.0f;
    }
}
