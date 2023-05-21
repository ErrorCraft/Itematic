package net.errorcraft.itematic.item.dispense.behavior.behaviors;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ProjectileItemComponent;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;

public class ProjectileItemComponentDispenserBehavior extends ProjectileDispenserBehavior {
    @Override
    protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
        Entity entity = stack.getComponent(ItemComponentTypes.PROJECTILE)
            .map(ProjectileItemComponent::entity)
            .map(RegistryEntry::value)
            .map(e -> e.create(world))
            .orElse(null);
        if (entity instanceof ThrownItemEntity thrownItemEntity) {
            thrownItemEntity.setItem(stack);
        }
        if (entity instanceof ProjectileEntity projectileEntity) {
            projectileEntity.setPos(position.getX(), position.getY(), position.getZ());
            return projectileEntity;
        }
        return null;
    }
}
