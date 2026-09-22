package net.errorcraft.itematic.mixin.world.entity.projectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityExtender extends Projectile {
    public FireworkRocketEntityExtender(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getHandHoldingItemAngle(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private Item getFireworkRocketUseDynamicRegistry(Item item) {
        return this.level().itematic$getItem(ItemIds.FIREWORK_ROCKET).value();
    }

    @Redirect(
        method = {
            "defineSynchedData",
            "readAdditionalSaveData"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;getDefaultItem()Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForFireworkRocketUseCreateStack() {
        return this.level().itematic$createStack(ItemIds.FIREWORK_ROCKET);
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
}
