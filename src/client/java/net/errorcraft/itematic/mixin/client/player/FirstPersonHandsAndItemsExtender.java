package net.errorcraft.itematic.mixin.client.player;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Chargeable;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.Direct;
import net.minecraft.client.player.FirstPersonHandsAndItems;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItems.class)
public class FirstPersonHandsAndItemsExtender {
    @Inject(
        method = "extractRenderState",
        at = @At("TAIL")
    )
    private void setUsedItemTicks(LocalPlayer player, float partialTicks, FirstPersonHandsAndItemsRenderState state, CallbackInfo info) {
        state.itematic$setUsedItemTicks(player.itematic$usedItemTicks());
    }

    @WrapOperation(
        method = "evaluateWhichHandsToRender",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private static boolean isBowOrCrossbowUseItemBehavior(ItemStack instance, Object o, Operation<Boolean> original) {
        ShooterMethod<?> method = instance.get(ItematicDataComponents.SHOOTER_METHOD);
        if (o == Items.BOW) {
            return method == Direct.INSTANCE;
        }

        return method == Chargeable.INSTANCE;
    }

    @WrapOperation(
        method = "isChargedCrossbow",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private static boolean isCrossbowUseDataComponent(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.get(ItematicDataComponents.SHOOTER_METHOD) == Chargeable.INSTANCE;
    }
}
