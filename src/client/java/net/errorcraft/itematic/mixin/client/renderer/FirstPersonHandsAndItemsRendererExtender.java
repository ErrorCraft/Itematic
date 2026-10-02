package net.errorcraft.itematic.mixin.client.renderer;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.weapon.shooter.method.ShooterMethodType;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererExtender {
    @WrapOperation(
        method = "submitArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CROSSBOW:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isCrossbowUseItemBehavior(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.itematic$getBehavior(ItemBehaviorType.SHOOTER)
            .filter(shooterItemBehavior -> shooterItemBehavior.method().type() == ShooterMethodType.CHARGEABLE)
            .isPresent();
    }

    @Definition(id = "timeHeld", local = @Local(type = float.class, name = "timeHeld"))
    @Expression("timeHeld")
    @ModifyExpressionValue(
        method = "submitArmWithItem",
        at = @At("MIXINEXTRAS:EXPRESSION")
    )
    private float timeHeldUseDirectField(float original, @Local(name = "state", argsOnly = true) FirstPersonHandsAndItemsRenderState state, @Local(name = "partialTicks", argsOnly = true) float partialTicks) {
        return state.itematic$usedItemTicks() + partialTicks - 1.0f;
    }
}
