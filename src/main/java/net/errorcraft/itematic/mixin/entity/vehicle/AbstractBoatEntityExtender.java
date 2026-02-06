package net.errorcraft.itematic.mixin.entity.vehicle;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(AbstractBoatEntity.class)
public abstract class AbstractBoatEntityExtender extends VehicleEntityExtender {
    @Unique
    private RegistryKey<Item> itemKey;

    public AbstractBoatEntityExtender(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setItemKey(EntityType<? extends AbstractBoatEntity> type, World world, Supplier<Item> itemSupplier, CallbackInfo info) {
        this.itemKey = Registries.ITEM.getKey(itemSupplier.get()).orElseThrow();
    }

    @Redirect(
        method = "getPickBlockStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(this.itemKey);
    }

    @Override
    protected RegistryKey<Item> asItemKey() {
        return this.itemKey;
    }
}
