package net.errorcraft.itematic.mixin.entity.projectile.thrown;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEgg.class)
public abstract class EggEntityExtender extends ThrownItemEntityExtender {
    @Unique
    private EitherHolder<ChickenVariant> chickenVariant;

    public EggEntityExtender(EntityType<? extends ThrowableItemProjectile> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter from) {
        super.applyImplicitComponents(from);
        this.chickenVariant = from.get(DataComponents.CHICKEN_VARIANT);
    }

    @ModifyExpressionValue(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
        )
    )
    private Object getChickenVariantPossiblyUseDefault(Object original) {
        if (original != null) {
            return original;
        }

        return this.chickenVariant;
    }

    @Override
    protected ResourceKey<Item> getDefaultItemKey() {
        return ItemIds.EGG;
    }
}
