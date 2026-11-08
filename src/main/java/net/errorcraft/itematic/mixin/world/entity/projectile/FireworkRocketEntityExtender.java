package net.errorcraft.itematic.mixin.world.entity.projectile;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.access.world.entity.projectile.FireworkRocketEntityAccess;
import net.errorcraft.itematic.network.syncher.ItematicEntityDataSerializers;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.component.GliderFuel;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkRocketEntity.class)
public abstract class FireworkRocketEntityExtender extends Projectile implements FireworkRocketEntityAccess {
    @Shadow
    public abstract ItemStack getItem();

    @Unique
    private static final EntityDataAccessor<GliderFuel.Computed> DATA_ID_GLIDER_FUEL = SynchedEntityData.defineId(
        FireworkRocketEntityExtender.class,
        ItematicEntityDataSerializers.GLIDER_FUEL
    );

    public FireworkRocketEntityExtender(EntityType<? extends Projectile> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "defineSynchedData",
        at = @At("TAIL")
    )
    private void addGliderFuelSynchedData(SynchedEntityData.Builder entityData, CallbackInfo info) {
        entityData.define(DATA_ID_GLIDER_FUEL, GliderFuel.Computed.DEFAULT);
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

    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getLookAngle()Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private void storeGliderFuel(CallbackInfo info, @Share("gliderFuel") LocalRef<GliderFuel.Computed> gliderFuel) {
        gliderFuel.set(this.entityData.get(DATA_ID_GLIDER_FUEL));
    }

    @ModifyConstant(
        method = "tick",
        constant = @Constant(
            doubleValue = 0.1d
        )
    )
    private double powerAdd(double constant, @Share("gliderFuel") LocalRef<GliderFuel.Computed> gliderFuel) {
        return gliderFuel.get().constantlyAddedSpeed();
    }

    @ModifyConstant(
        method = "tick",
        constant = @Constant(
            doubleValue = 1.5d
        )
    )
    private double power(double constant, @Share("gliderFuel") LocalRef<GliderFuel.Computed> gliderFuel) {
        return gliderFuel.get().targetSpeed();
    }

    @ModifyConstant(
        method = "tick",
        constant = @Constant(
            doubleValue = 0.5d
        ),
        slice = @Slice(
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/projectile/FireworkRocketEntity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"
            )
        )
    )
    private double powerFactor(double constant, @Share("gliderFuel") LocalRef<GliderFuel.Computed> gliderFuel) {
        return gliderFuel.get().targetSpeedFactor();
    }

    @ModifyArg(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getHandHoldingItemAngle(Lnet/minecraft/world/item/Item;)Lnet/minecraft/world/phys/Vec3;"
        )
    )
    private Item getFireworkRocketUseExistingItemStack(Item item) {
        return this.getItem().getItem();
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

    @Override
    public void itematic$setGliderFuel(GliderFuel.Computed gliderFuel) {
        this.entityData.set(DATA_ID_GLIDER_FUEL, gliderFuel);
    }
}
