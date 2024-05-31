package net.errorcraft.itematic.entity.initializer.initializers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.initializer.EntityInitializer;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;

public record TridentEntityInitializer(boolean preventSpawnFromRiptide) implements EntityInitializer<TridentEntity> {
    public static final MapCodec<TridentEntityInitializer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("prevent_spawn_from_riptide", false).forGetter(TridentEntityInitializer::preventSpawnFromRiptide)
    ).apply(instance, TridentEntityInitializer::new));

    @Override
    public EntityType<?> type() {
        return EntityType.TRIDENT;
    }

    @Override
    public TridentEntity create(ActionContext context) {
        ItemStack stack = context.stack();
        if (this.preventSpawnFromRiptide && EnchantmentHelper.getRiptide(stack) > 0) {
            return null;
        }
        TridentEntity entity = this.create(context, stack);
        entity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return entity;
    }

    public static TridentEntityInitializer of(boolean preventSpawnFromRiptide) {
        return new TridentEntityInitializer(preventSpawnFromRiptide);
    }

    private TridentEntity create(ActionContext context, ItemStack stack) {
        stack.itematic$damage(1, context);
        stack.decrementUnlessCreative(1, context.player(ActionContextParameter.THIS).orElse(null));
        ServerWorld world = context.world();
        return context.player(ActionContextParameter.THIS)
            .map(player -> new TridentEntity(world, player, stack))
            .orElseGet(() -> {
                TridentEntity entity = new TridentEntity(EntityType.TRIDENT, world);
                entity.setPosition(context.position(ActionContextParameter.TARGET));
                return entity;
            });
    }
}
