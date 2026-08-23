package net.errorcraft.itematic.mixin.world.item.component;

import net.errorcraft.itematic.access.world.item.component.ChargedProjectilesAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ChargedProjectiles.class)
public class ChargedProjectilesExtender implements ChargedProjectilesAccess {
    @Shadow
    @Final
    private List<ItemStack> items;

    @Override
    public boolean itematic$contains(ResourceKey<Item> item) {
        for (ItemStack projectile : this.items) {
            if (projectile.is(item)) {
                return true;
            }
        }

        return false;
    }
}
