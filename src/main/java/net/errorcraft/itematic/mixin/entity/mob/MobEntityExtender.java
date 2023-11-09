package net.errorcraft.itematic.mixin.entity.mob;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.access.entity.mob.MobEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.SpawnEggItemComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import java.util.Optional;

@Mixin(MobEntity.class)
public abstract class MobEntityExtender extends LivingEntity implements MobEntityAccess {
    protected MobEntityExtender(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    public abstract void setBaby(boolean baby);

    @ModifyConstant(
        method = "interactWithItem",
        constant = @Constant(
            classValue = SpawnEggItem.class,
            ordinal = 0
        )
    )
    private boolean interactWithItemInstanceOfSpawnEggItemUseItemComponentCheck(Object reference, Class<SpawnEggItem> clazz, @Local ItemStack itemStack, @Share("spawnEggItemComponent") LocalRef<SpawnEggItemComponent> spawnEggItemComponent) {
        Optional<SpawnEggItemComponent> optionalSpawnEggItemComponent = itemStack.itematic$getComponent(ItemComponentTypes.SPAWN_EGG);
        optionalSpawnEggItemComponent.ifPresent(spawnEggItemComponent::set);
        return optionalSpawnEggItemComponent.isPresent();
    }

    @Redirect(
        method = "interactWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;",
            ordinal = 1
        )
    )
    private Item interactWithItemGetItemReturnNull(ItemStack instance) {
        return null;
    }

    @Redirect(
        method = "interactWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/SpawnEggItem;spawnBaby(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/mob/MobEntity;Lnet/minecraft/entity/EntityType;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/item/ItemStack;)Ljava/util/Optional;"
        )
    )
    private Optional<MobEntity> interactWithItemSpawnBabyUseItemComponent(SpawnEggItem instance, PlayerEntity user, MobEntity entity, EntityType<? extends MobEntity> entityType, ServerWorld world, Vec3d pos, ItemStack stack, @Share("spawnEggItemComponent") LocalRef<SpawnEggItemComponent> spawnEggItemComponent) {
        return spawnEggItemComponent.get().spawnBaby(user, entity, entityType, world, pos, stack);
    }

    @ModifyArg(
        method = "disablePlayerShield",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"
        )
    )
    private Item setCooldownForShieldUseDynamicRegistry(Item item) {
        return this.getWorld().itematic$getItem(ItemKeys.SHIELD).value();
    }

    @Override
    public boolean trySetBaby(boolean baby) {
        this.setBaby(baby);
        return this.isBaby() == baby;
    }
}
