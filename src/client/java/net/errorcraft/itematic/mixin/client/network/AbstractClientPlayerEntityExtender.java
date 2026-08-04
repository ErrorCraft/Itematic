package net.errorcraft.itematic.mixin.client.network;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ZoomItemComponent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerEntityExtender extends Player {
    public AbstractClientPlayerEntityExtender(Level world, GameProfile profile) {
        super(world, profile);
    }

    @Redirect(
        method = "getFieldOfViewModifier",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }

    @ModifyConstant(
        method = "getFieldOfViewModifier",
        constant = @Constant(
            floatValue = 0.1f
        )
    )
    private float fovMultiplierForSpyglassUseItemComponent(float original) {
        return this.getActiveItem().itematic$getBehavior(ItemComponentTypes.ZOOM)
            .map(ZoomItemComponent::fieldOfViewMultiplier)
            .orElse(original);
    }
}
