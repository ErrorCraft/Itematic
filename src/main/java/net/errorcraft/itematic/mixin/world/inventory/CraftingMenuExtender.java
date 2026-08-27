package net.errorcraft.itematic.mixin.world.inventory;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CraftingMenu.class)
public class CraftingMenuExtender {
    @Definition(id = "ServerPlayer", type = ServerPlayer.class)
    @Definition(id = "player", local = @Local(type = Player.class))
    @Expression("(ServerPlayer) player")
    @WrapOperation(
        method = "slotChangedCraftingGrid",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    @Nullable
    private static ServerPlayer checkForServerPlayer(Object obj, Operation<ServerPlayer> original) {
        return obj instanceof ServerPlayer serverPlayer ? serverPlayer : null;
    }

    @WrapOperation(
        method = "slotChangedCraftingGrid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/inventory/ResultContainer;setRecipeUsed(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/item/crafting/RecipeHolder;)Z"
        )
    )
    private static boolean checkForServerPlayer(ResultContainer instance, @Nullable ServerPlayer serverPlayer, RecipeHolder<CraftingRecipe> recipeHolder, Operation<Boolean> original) {
        return serverPlayer != null && original.call(instance, serverPlayer, recipeHolder);
    }

    @WrapOperation(
        method = "slotChangedCraftingGrid",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/server/level/ServerPlayer;connection:Lnet/minecraft/server/network/ServerGamePacketListenerImpl;",
            opcode = Opcodes.GETFIELD
        )
    )
    @Nullable
    private static ServerGamePacketListenerImpl checkForServerPlayer(@Nullable ServerPlayer instance, Operation<ServerGamePacketListenerImpl> original) {
        if (instance == null) {
            return null;
        }

        return original.call(instance);
    }

    @WrapWithCondition(
        method = "slotChangedCraftingGrid",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V"
        )
    )
    private static boolean checkForServerGamePacketListenerImpl(@Nullable ServerGamePacketListenerImpl instance, Packet<?> packet) {
        return instance != null;
    }
}
