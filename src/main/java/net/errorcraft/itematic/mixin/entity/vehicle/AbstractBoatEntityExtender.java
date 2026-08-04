package net.errorcraft.itematic.mixin.entity.vehicle;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(AbstractBoat.class)
public abstract class AbstractBoatEntityExtender extends VehicleEntityExtender {
    @Unique
    private ResourceKey<Item> itemKey;

    public AbstractBoatEntityExtender(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setItemKey(EntityType<? extends AbstractBoat> type, Level world, Supplier<Item> itemSupplier, CallbackInfo info) {
        this.itemKey = BuiltInRegistries.ITEM.getResourceKey(itemSupplier.get()).orElseThrow();
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "net/minecraft/world/item/ItemStack"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(this.itemKey);
    }

    @Override
    protected ResourceKey<Item> asItemKey() {
        return this.itemKey;
    }
}
