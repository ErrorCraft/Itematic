package net.errorcraft.itematic.mixin.world.entity.projectile.throwableitemprojectile;

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
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ThrownEgg.class)
public abstract class ThrownEggExtender extends ThrowableItemProjectileExtender {
    @Unique
    @Nullable
    private EitherHolder<ChickenVariant> chickenVariant;

    public ThrownEggExtender(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
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
    @Nullable
    private Object getChickenVariantPossiblyUseDefault(@Nullable Object original) {
        if (original != null) {
            return original;
        }

        return this.chickenVariant;
    }

    @Override
    protected ResourceKey<Item> getDefaultItemId() {
        return ItemIds.EGG;
    }
}
