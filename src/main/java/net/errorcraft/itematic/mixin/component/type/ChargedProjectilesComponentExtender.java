package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.access.component.type.ChargedProjectilesComponentAccess;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChargedProjectilesComponent.class)
public class ChargedProjectilesComponentExtender implements ChargedProjectilesComponentAccess {
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
}
