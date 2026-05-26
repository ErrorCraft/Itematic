package net.errorcraft.itematic.entity.initializer.initializers;

import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.world.World;

public class TridentEntityInitializer implements EntityInitializer<TridentEntity> {
    public static final TridentEntityInitializer INSTANCE = new TridentEntityInitializer();

    @Override
    public TridentEntity create(ActionContext context, SpawnReason reason) {
        ItemStack stack = context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY);
        LivingEntity user = context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class);
        float spinAttackStrength = user != null ?
            EnchantmentHelper.getTridentSpinAttackStrength(stack, user) :
            0.0f;
        if (spinAttackStrength > 0.0f) {
            return null;
        }

        stack.itematic$damage(1, context);
        TridentEntity entity = this.create(context.world(), user, stack);
        stack.decrementUnlessCreative(1, context.get(LootContextParameters.THIS_ENTITY, PlayerEntity.class));
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    private TridentEntity create(World world, LivingEntity possibleUser, ItemStack stack) {
        if (possibleUser != null) {
            return new TridentEntity(world, possibleUser, stack);
        }

        return new TridentEntity(EntityType.TRIDENT, world);
    }
}
