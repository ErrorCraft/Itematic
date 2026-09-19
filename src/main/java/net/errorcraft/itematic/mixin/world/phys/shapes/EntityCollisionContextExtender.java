package net.errorcraft.itematic.mixin.world.phys.shapes;

import net.errorcraft.itematic.access.world.phys.shapes.CollisionContextAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EntityCollisionContext.class)
public class EntityCollisionContextExtender implements CollisionContextAccess {
    @Shadow
    @Final
    private ItemStack heldItem;

    @Override
    public boolean itematic$isHoldingItem(ResourceKey<Item> item) {
        return this.heldItem.is(item);
    }
}
