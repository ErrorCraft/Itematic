package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;

public class TridentEntityInitializer implements EntityInitializer<TridentEntity> {
    public static final TridentEntityInitializer INSTANCE = new TridentEntityInitializer();
    public static final MapCodec<TridentEntityInitializer> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public EntityType<?> type() {
        return EntityType.TRIDENT;
    }

    @Override
    public TridentEntity create(ActionContext context, SpawnReason reason) {
        ItemStack stack = context.stack();
        LivingEntity user = context.livingEntity(ActionContextParameter.THIS).orElse(null);
        float spinAttackStrength = user != null ?
            EnchantmentHelper.getTridentSpinAttackStrength(stack, user) :
            0.0f;
        if (spinAttackStrength > 0.0f) {
            return null;
        }

        stack.itematic$damage(1, context);
        TridentEntity entity = this.create(context, stack);
        stack.decrementUnlessCreative(1, context.player(ActionContextParameter.THIS).orElse(null));
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    private TridentEntity create(ActionContext context, ItemStack stack) {
        return context.livingEntity(ActionContextParameter.THIS)
            .map(user -> new TridentEntity(context.world(), user, stack))
            .orElseGet(() -> {
                TridentEntity entity = new TridentEntity(EntityType.TRIDENT, context.world());
                entity.setPosition(context.position(ActionContextParameter.TARGET));
                return entity;
            });
    }
}
