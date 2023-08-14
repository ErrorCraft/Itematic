package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.access.entity.vehicle.AbstractMinecartEntityAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartEntityExtender extends Entity implements AbstractMinecartEntityAccess {
    @Shadow
    public abstract AbstractMinecartEntity.Type getMinecartType();

    public AbstractMinecartEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * @author ErrorCraft
     * @reason Uses a registry entry for data-driven items.
     */
    @Overwrite
    public ItemStack getPickBlockStack() {
        RegistryEntry<Item> entry = this.getWorld().getItem(this.asPickBlockItemKey());
        return new ItemStack(entry);
    }

    @Redirect(
        method = "dropItems",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack dropItemsNewItemStackUseRegistryEntry(ItemConvertible item) {
        RegistryEntry<Item> entry = this.getWorld().getItem(this.asItemKey());
        return new ItemStack(entry);
    }

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
