package net.errorcraft.itematic.world.entity.initializer.initializers;

import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.entity.initializer.EntityInitializer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ThrownTridentEntityInitializer implements EntityInitializer<ThrownTrident> {
    public static final ThrownTridentEntityInitializer INSTANCE = new ThrownTridentEntityInitializer();

    @Override
    public ThrownTrident create(ActionContext context, EntitySpawnReason reason) {
        ItemStack stack = context.getOrDefault(LootContextParams.TOOL, ItemStack.EMPTY);
        LivingEntity user = context.get(LootContextParams.THIS_ENTITY, LivingEntity.class);
        float spinAttackStrength = user != null ?
            EnchantmentHelper.getTridentSpinAttackStrength(stack, user) :
            0.0f;
        if (spinAttackStrength > 0.0f) {
            return null;
        }

        stack.itematic$damage(1, context);
        ThrownTrident entity = this.create(context.world(), user, stack);
        stack.consume(1, context.get(LootContextParams.THIS_ENTITY, Player.class));
        entity.pickup = AbstractArrow.Pickup.ALLOWED;
        return entity;
    }

    private ThrownTrident create(Level level, LivingEntity possibleUser, ItemStack stack) {
        if (possibleUser != null) {
            return new ThrownTrident(level, possibleUser, stack);
        }

        return new ThrownTrident(EntityType.TRIDENT, level);
    }
}
