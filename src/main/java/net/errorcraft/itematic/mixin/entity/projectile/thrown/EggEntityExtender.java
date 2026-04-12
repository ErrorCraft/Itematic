package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EggEntity.class)
public abstract class EggEntityExtender extends ThrownItemEntityExtender {
    @Unique
    private LazyRegistryEntryReference<ChickenVariant> chickenVariant;

    public EggEntityExtender(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void copyComponentsFrom(ComponentsAccess from) {
        super.copyComponentsFrom(from);
        this.chickenVariant = from.get(DataComponentTypes.CHICKEN_VARIANT);
    }

    @ModifyExpressionValue(
        method = "onCollision",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;get(Lnet/minecraft/component/ComponentType;)Ljava/lang/Object;"
        )
    )
    private Object getChickenVariantPossiblyUseDefault(Object original) {
        if (original != null) {
            return original;
        }

        return this.chickenVariant;
    }

    @Override
    protected RegistryKey<Item> getDefaultItemKey() {
        return ItemKeys.EGG;
    }
}
