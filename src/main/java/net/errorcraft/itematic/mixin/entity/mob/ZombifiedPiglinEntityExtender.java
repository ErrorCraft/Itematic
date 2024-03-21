package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ZombifiedPiglinEntity.class)
public abstract class ZombifiedPiglinEntityExtender extends MobEntityExtender {
    public ZombifiedPiglinEntityExtender(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackForGoldenSwordUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.GOLDEN_SWORD);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG;
    }
}
