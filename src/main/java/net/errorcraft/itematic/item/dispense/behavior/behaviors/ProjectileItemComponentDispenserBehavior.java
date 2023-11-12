package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ProjectileItemComponentDispenserBehavior extends ProjectileDispenserBehavior {
    @Override
    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
        Entity entity = stack.itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(world, position, stack, 1.1f, 6.0f))
            .orElse(null);
        if (entity instanceof ProjectileEntity projectileEntity) {
            return projectileEntity;
        }
        return null;
    }
}
