package net.errorcraft.itematic.mixin.world.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.world.entity.EntityAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.advancements.criterion.PlayerInteractTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Entity.class)
public abstract class EntityExtender implements EntityAccess {
    @Shadow
    @Nullable
    public abstract ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack);

    @WrapOperation(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD:FIRST",
                target = "Lnet/minecraft/world/item/Items;SHEARS:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/entity/Mob;canShearEquipment(Lnet/minecraft/world/entity/player/Player;)Z"
            )
        )
    )
    private boolean isShearsCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.SHEARS);
    }

    @WrapOperation(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;LEAD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isLeadCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.LEAD);
    }

    @Definition(id = "ServerPlayer", type = ServerPlayer.class)
    @Definition(id = "player", local = @Local(type = Player.class))
    @Expression("(ServerPlayer) player")
    @WrapOperation(
        method = "attemptToShearEquipment",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    @Nullable
    private ServerPlayer checkForServerPlayer(Object obj, Operation<ServerPlayer> original) {
        return obj instanceof ServerPlayer serverPlayer ? serverPlayer : null;
    }

    @WrapWithCondition(
        method = "attemptToShearEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancements/criterion/PlayerInteractTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/Entity;)V"
        )
    )
    private boolean checkForServerPlayer(PlayerInteractTrigger instance, @Nullable ServerPlayer player, ItemStack itemStack, Entity interactedWith) {
        return player != null;
    }

    @Override
    public @Nullable ItemEntity itematic$spawnAtLocation(ServerLevel level, ResourceKey<Item> item) {
        return this.spawnAtLocation(level, level.itematic$createStack(item));
    }
}
