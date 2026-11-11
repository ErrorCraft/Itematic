package net.errorcraft.itematic.mixin.client.renderer.entity;

import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FishingHookRenderer.class)
public class FishingHookRendererExtender {
    @ModifyConstant(
        method = "getHoldingArm",
        constant = @Constant(
            classValue = FishingRodItem.class
        )
    )
    private static boolean instanceOfFishingRodItemCheckDataComponent(Object reference, Class<ProjectileWeaponItem> clazz, Player owner) {
        return owner.getMainHandItem().has(ItematicDataComponents.CASTABLE);
    }
}
