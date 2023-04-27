package errorcraft.itematic.mixin.entity.vehicle;

import errorcraft.itematic.access.entity.vehicle.BoatEntityAccess;
import errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BoatEntity.class)
public abstract class BoatEntityExtender extends Entity implements BoatEntityAccess {
    @Shadow
    public abstract BoatEntity.Type getVariant();

    public BoatEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(
        method = "dropItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity dropItemsDropItemUseRegistryEntry(BoatEntity instance, ItemConvertible itemConvertible) {
        return this.dropItem(this.asItemRegistryEntry());
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getPickBlockStateNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.asItemRegistryEntry());
    }

    @Redirect(
        method = "fall",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;"
        )
    )
    private ItemEntity fallDropItemUseRegistryEntry(BoatEntity instance, ItemConvertible itemConvertible) {
        RegistryEntry<Item> entry = this.world.getRegistryManager().get(RegistryKeys.ITEM).entryOf(ItemKeys.STICK);
        return this.dropItem(entry);
    }

    @Override
    public RegistryKey<Item> asItemKey() {
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

    private RegistryEntry<Item> asItemRegistryEntry() {
        Registry<Item> registry = this.world.getRegistryManager().get(RegistryKeys.ITEM);
        return registry.entryOf(this.asItemKey());
    }
}
