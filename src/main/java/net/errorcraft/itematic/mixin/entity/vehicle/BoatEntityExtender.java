package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatEntity.class)
public abstract class BoatEntityExtender extends VehicleEntityExtender {
    @Shadow
    public abstract BoatEntity.Type getVariant();

    public BoatEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getPickBlockStateNewItemStackUseRegistryEntry(ItemConvertible item) {
        return this.getWorld().createStack(this.asItemKey());
    }

    @Redirect(
        method = "fall",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity fallDropItemUseRegistryEntry(BoatEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(this.getWorld().getItem(ItemKeys.STICK));
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return switch (this.getVariant()) {
            default -> ItemKeys.OAK_BOAT;
            case SPRUCE -> ItemKeys.SPRUCE_BOAT;
            case BIRCH -> ItemKeys.BIRCH_BOAT;
            case JUNGLE -> ItemKeys.JUNGLE_BOAT;
            case ACACIA -> ItemKeys.ACACIA_BOAT;
            case CHERRY -> ItemKeys.CHERRY_BOAT;
            case DARK_OAK -> ItemKeys.DARK_OAK_BOAT;
            case MANGROVE -> ItemKeys.MANGROVE_BOAT;
            case BAMBOO -> ItemKeys.BAMBOO_RAFT;
        };
    }
}
