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
    public abstract ItemEntity dropStack(ItemStack stack);

    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ItemStack stack, float yOffset);

    @Override
    public ItemEntity itematic$dropItem(RegistryKey<Item> key) {
        return this.dropStack(this.world.itematic$createStack(key));
    }

    @Override
    public ItemEntity itematic$dropItem(RegistryKey<Item> key, float yOffset) {
        return this.dropStack(this.world.itematic$createStack(key), yOffset);
    }

    @Override
    public ItemEntity itematic$dropItem(RegistryEntry<Item> entry) {
        return this.dropStack(new ItemStack(entry));
    }

    @Override
    public ItemEntity itematic$dropItem(RegistryEntry<Item> entry, float yOffset) {
        return this.dropStack(new ItemStack(entry), yOffset);
    }
}
