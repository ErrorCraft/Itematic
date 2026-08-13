package net.errorcraft.itematic.mixin.entity;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.entity.EntityAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.advancements.criterion.PlayerInteractTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
    public abstract ItemEntity spawnAtLocation(ServerLevel world, ItemStack stack);

    @Shadow
    @Nullable
    public abstract ItemEntity spawnAtLocation(ServerLevel world, ItemStack stack, float yOffset);

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
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
    private boolean isOfForShearsUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.SHEARS);
    }

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
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
    private boolean isOfForLeadUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.LEAD);
    }

    @Definition(id = "ServerPlayerEntity", type = ServerPlayer.class)
    @Definition(id = "player", local = @Local(type = Player.class))
    @Expression("(ServerPlayerEntity) player")
    @WrapOperation(
        method = "attemptToShearEquipment",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
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
    private boolean checkForServerPlayer(PlayerInteractTrigger instance, ServerPlayer player, ItemStack stack, Entity entity) {
        return player != null;
    }

    @Override
    public ItemEntity itematic$dropItem(ServerLevel world, ResourceKey<Item> key) {
        return this.spawnAtLocation(world, world.itematic$createStack(key));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerLevel world, ResourceKey<Item> key, float yOffset) {
        return this.spawnAtLocation(world, world.itematic$createStack(key), yOffset);
    }

    @Override
    public ItemEntity itematic$dropItem(ServerLevel world, Holder<Item> entry) {
        return this.spawnAtLocation(world, new ItemStack(entry));
    }

    @Override
    public ItemEntity itematic$dropItem(ServerLevel world, Holder<Item> entry, float yOffset) {
        return this.spawnAtLocation(world, new ItemStack(entry), yOffset);
    }
}
