package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.access.entity.EntityAccess;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityExtender implements EntityAccess {
    @Shadow
    private World world;

    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ItemStack stack, float yOffset);

    @Override
    public ItemEntity dropItem(RegistryKey<Item> key) {
        return this.dropItem(this.world.getItem(key), 0);
    }

    @Override
    public ItemEntity dropItem(RegistryKey<Item> key, int yOffset) {
        return this.dropItem(this.world.getItem(key), yOffset);
    }

    @Override
    public ItemEntity dropItem(RegistryEntry<Item> entry, int yOffset) {
        return this.dropStack(new ItemStack(entry), yOffset);
    }
}
