package net.errorcraft.itematic.mixin.client.player;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.world.item.component.ZoomFieldOfViewMultiplier;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.references.ItemIds;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerExtender extends Player {
    public AbstractClientPlayerExtender(Level level, GameProfile profile) {
        super(level, profile);
    }

    @Redirect(
        method = "getFieldOfViewModifier",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBowCheckId(ItemStack instance, Object o) {
        return instance.is(ItemIds.BOW);
    }

    @ModifyConstant(
        method = "getFieldOfViewModifier",
        constant = @Constant(
            floatValue = 0.1f
        )
    )
    private float fieldOfViewModifierUseDataComponent(float original) {
        ZoomFieldOfViewMultiplier zoomFieldOfViewMultiplier = this.getActiveItem().get(ItematicDataComponents.ZOOM_FIELD_OF_VIEW_MULTIPLIER);
        if (zoomFieldOfViewMultiplier != null) {
            return zoomFieldOfViewMultiplier.multiplier();
        }

        return original;
    }
}
