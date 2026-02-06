package net.errorcraft.itematic.mixin.entity;

import net.errorcraft.itematic.access.entity.EntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class EntityExtender implements EntityAccess {
    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ServerWorld world, ItemStack stack);

    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ServerWorld world, ItemStack stack, float yOffset);

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForLeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.LEAD);
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key) {
        return this.dropStack(world, world.itematic$createStack(key));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key, float yOffset) {
        return this.dropStack(world, world.itematic$createStack(key), yOffset);
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry) {
        return this.dropStack(world, new ItemStack(entry));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry, float yOffset) {
        return this.dropStack(world, new ItemStack(entry), yOffset);
    }
}
