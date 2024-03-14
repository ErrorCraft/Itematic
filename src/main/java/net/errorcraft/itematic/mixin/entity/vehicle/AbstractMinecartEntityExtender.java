package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityExtender extends Entity {
    @Shadow
    public abstract AbstractMinecartEntity.Type getMinecartType();

    public AbstractMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    public ItemStack newItemStackUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(this.asPickBlockItemKey());
    }

    @Unique
    private RegistryKey<Item> asPickBlockItemKey() {
        return switch (this.getMinecartType()) {
            case FURNACE -> ItemKeys.FURNACE_MINECART;
            case CHEST -> ItemKeys.CHEST_MINECART;
            case TNT -> ItemKeys.TNT_MINECART;
            case HOPPER -> ItemKeys.HOPPER_MINECART;
            case COMMAND_BLOCK -> ItemKeys.COMMAND_BLOCK_MINECART;
            default -> ItemKeys.MINECART;
        };
    }
}
