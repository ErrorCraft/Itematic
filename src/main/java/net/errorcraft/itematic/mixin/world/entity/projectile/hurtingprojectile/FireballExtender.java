package net.errorcraft.itematic.mixin.world.entity.projectile.hurtingprojectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Fireball.class)
public class FireballExtender extends AbstractHurtingProjectile {
    protected FireballExtender(EntityType<? extends AbstractHurtingProjectile> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = {
            "addAdditionalSaveData",
            "readAdditionalSaveData"
        },
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/ItemStack;CODEC:Lcom/mojang/serialization/Codec;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private Codec<ItemStack> useFailableItemStackCodec(Operation<Codec<ItemStack>> original) {
        return ItemStacks.POSSIBLY_FAILED_CODEC;
    }

    @Redirect(
        method = "getDefaultItem",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireChargeUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.FIRE_CHARGE);
    }
}
