package net.errorcraft.itematic.mixin.client.network;

import com.mojang.authlib.GameProfile;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ZoomItemComponent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityExtender extends PlayerEntity {
    public AbstractClientPlayerEntityExtender(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Redirect(
        method = "getFovMultiplier",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean isOfForBowUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.BOW);
    }

    @ModifyConstant(
        method = "getFovMultiplier",
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
