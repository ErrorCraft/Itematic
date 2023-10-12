package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItematicItemTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityExtender extends VehicleEntityExtender {
    public FurnaceMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "interact",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;test(Lnet/minecraft/item/ItemStack;)Z"
        )
    )
    private boolean interactTestUseItemTagCheck(Ingredient instance, ItemStack itemStack) {
        return itemStack.isIn(ItematicItemTags.FURNACE_MINECART_FUEL);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return ItemKeys.FURNACE_MINECART;
    }
}
