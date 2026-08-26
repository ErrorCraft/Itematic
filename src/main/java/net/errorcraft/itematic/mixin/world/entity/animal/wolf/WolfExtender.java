package net.errorcraft.itematic.mixin.world.entity.animal.wolf;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Wolf.class)
public abstract class WolfExtender extends MobExtender {
    protected WolfExtender(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "actuallyHurt",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/core/particles/ParticleType;Lnet/minecraft/world/item/Item;)Lnet/minecraft/core/particles/ItemParticleOption;"
        )
    )
    private ItemParticleOption newItemStackTemplateForArmadilloScuteUseCreateStackTemplate(ParticleType<ItemParticleOption> type, Item item, Operation<ItemParticleOption> original) {
        return new ItemParticleOption(
            type,
            this.level().itematic$createStackTemplate(ItemIds.ARMADILLO_SCUTE)
        );
    }

    @WrapOperation(
        method = "canArmorAbsorb",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isWolfArmorCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.WOLF_ARMOR);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"
            )
        )
    )
    private boolean isBoneCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.BONE);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.WOLF_SPAWN_EGG;
    }
}
