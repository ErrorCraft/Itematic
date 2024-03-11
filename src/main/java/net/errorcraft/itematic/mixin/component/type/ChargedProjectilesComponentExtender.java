package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.access.component.type.ChargedProjectilesComponentAccess;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.ProjectileItemComponent;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ChargedProjectilesComponent.class)
public class ChargedProjectilesComponentExtender implements ChargedProjectilesComponentAccess {
    @Unique
    private static final float DEFAULT_CHARGED_SPEED = CrossbowItemAccessor.defaultSpeed();

    @Shadow
    @Final
    private List<ItemStack> projectiles;

    @Override
    public boolean itematic$contains(RegistryKey<Item> item) {
        for (ItemStack projectile : this.projectiles) {
            if (projectile.itematic$isOf(item)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public float itematic$getChargedSpeed() {
        float actualChargedSpeed = DEFAULT_CHARGED_SPEED;
        for (ItemStack projectile : this.projectiles) {
            float chargedSpeed = projectile.itematic$getComponent(ItemComponentTypes.PROJECTILE)
                .map(ProjectileItemComponent::chargedSpeed)
                .orElse(DEFAULT_CHARGED_SPEED);
            actualChargedSpeed = Math.min(actualChargedSpeed, chargedSpeed);
        }
        return actualChargedSpeed;
    }
}
