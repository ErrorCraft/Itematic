package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.entity.EntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.advancement.criterion.PlayerInteractedWithEntityCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Entity.class)
public abstract class EntityExtender implements EntityAccess {
    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ServerWorld world, ItemStack stack);

    @Shadow
    @Nullable
    public abstract ItemEntity dropStack(ServerWorld world, ItemStack stack, float yOffset);

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD:FIRST",
                target = "Lnet/minecraft/item/Items;SHEARS:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/mob/MobEntity;canRemoveSaddle(Lnet/minecraft/entity/player/PlayerEntity;)Z"
            )
        )
    )
    private boolean isOfForShearsUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.SHEARS);
    }

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEAD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForLeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.LEAD);
    }

    @Definition(id = "ServerPlayerEntity", type = ServerPlayerEntity.class)
    @Definition(id = "player", local = @Local(type = PlayerEntity.class))
    @Expression("(ServerPlayerEntity) player")
    @WrapOperation(
        method = "shearEquipment",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private ServerPlayerEntity checkForServerPlayer(Object obj, Operation<ServerPlayerEntity> original) {
        return obj instanceof ServerPlayerEntity serverPlayer ? serverPlayer : null;
    }

    @WrapWithCondition(
        method = "shearEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/criterion/PlayerInteractedWithEntityCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/Entity;)V"
        )
    )
    private boolean checkForServerPlayer(PlayerInteractedWithEntityCriterion instance, ServerPlayerEntity player, ItemStack stack, Entity entity) {
        return player != null;
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key) {
        return this.dropStack(world, world.itematic$createStack(key));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryKey<Item> key, float yOffset) {
        return this.dropStack(world, world.itematic$createStack(key), yOffset);
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry) {
        return this.dropStack(world, new ItemStack(entry));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerWorld world, RegistryEntry<Item> entry, float yOffset) {
        return this.dropStack(world, new ItemStack(entry), yOffset);
    }
}
