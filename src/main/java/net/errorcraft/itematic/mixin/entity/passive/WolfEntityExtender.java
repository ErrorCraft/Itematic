package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Wolf.class)
public abstract class WolfEntityExtender extends MobEntityExtender {
    protected WolfEntityExtender(EntityType<? extends LivingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "actuallyHurt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/Item;getDefaultInstance()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack getDefaultStackForArmadilloScuteUseCreateStack(Item instance) {
        return this.level().itematic$createStack(ItemIds.ARMADILLO_SCUTE);
    }

    @Redirect(
        method = "canArmorAbsorb",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForWolfArmorUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.WOLF_ARMOR);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/animal/wolf/Wolf;setTarget(Lnet/minecraft/world/entity/LivingEntity;)V"
            )
        )
    )
    private boolean isOfForBoneNotTamedUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.BONE);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.WOLF_SPAWN_EGG;
    }
}
