package net.errorcraft.itematic.mixin.entity.vehicle;

import net.errorcraft.itematic.access.entity.vehicle.BoatEntityTypeAccess;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

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
    private ItemStack newItemStackUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(this.asItemKey());
    }

    @Redirect(
        method = "fall",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;",
            ordinal = 0
        )
    )
    @SuppressWarnings("DataFlowIssue")
    private ItemEntity dropItemUseRegistryKey(BoatEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(((BoatEntityTypeAccess)(Object) this.getVariant()).itematic$materialItemKey());
    }

    @Redirect(
        method = "fall",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/vehicle/BoatEntity;dropItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/entity/ItemEntity;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;STICK:Lnet/minecraft/item/Item;"
            )
        )
    )
    private ItemEntity dropItemForStickUseRegistryKey(BoatEntity instance, ItemConvertible itemConvertible) {
        return this.itematic$dropItem(ItemKeys.STICK);
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

    @Mixin(BoatEntity.Type.class)
    public static class TypeExtender implements BoatEntityTypeAccess {
        @Shadow
        @Final
        public static BoatEntity.Type OAK;

        @Shadow
        @Final
        public static BoatEntity.Type SPRUCE;

        @Shadow
        @Final
        public static BoatEntity.Type BIRCH;

        @Shadow
        @Final
        public static BoatEntity.Type JUNGLE;

        @Shadow
        @Final
        public static BoatEntity.Type ACACIA;

        @Shadow
        @Final
        public static BoatEntity.Type CHERRY;

        @Shadow
        @Final
        public static BoatEntity.Type DARK_OAK;

        @Shadow
        @Final
        public static BoatEntity.Type MANGROVE;

        @Shadow
        @Final
        public static BoatEntity.Type BAMBOO;

        @Unique
        private RegistryKey<Item> materialItemKey;

        static {
            ((BoatEntityExtender.TypeExtender)(Object) OAK).materialItemKey = ItemKeys.OAK_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) SPRUCE).materialItemKey = ItemKeys.SPRUCE_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) BIRCH).materialItemKey = ItemKeys.BIRCH_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) JUNGLE).materialItemKey = ItemKeys.JUNGLE_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) ACACIA).materialItemKey = ItemKeys.ACACIA_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) CHERRY).materialItemKey = ItemKeys.CHERRY_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) DARK_OAK).materialItemKey = ItemKeys.DARK_OAK_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) MANGROVE).materialItemKey = ItemKeys.MANGROVE_PLANKS;
            ((BoatEntityExtender.TypeExtender)(Object) BAMBOO).materialItemKey = ItemKeys.BAMBOO_PLANKS;
        }

        @Override
        public RegistryKey<Item> itematic$materialItemKey() {
            return this.materialItemKey;
        }
    }
}
