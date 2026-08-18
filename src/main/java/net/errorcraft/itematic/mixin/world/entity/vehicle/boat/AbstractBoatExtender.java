package net.errorcraft.itematic.mixin.world.entity.vehicle.boat;

import net.errorcraft.itematic.mixin.world.entity.vehicle.VehicleEntityExtender;
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
public abstract class AbstractBoatExtender extends VehicleEntityExtender {
    @Unique
    private ResourceKey<Item> itemId;

    public AbstractBoatExtender(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void setItemId(EntityType<? extends AbstractBoat> type, Level level, Supplier<Item> itemSupplier, CallbackInfo info) {
        this.itemId = BuiltInRegistries.ITEM.getResourceKey(itemSupplier.get()).orElseThrow();
    }

    @Redirect(
        method = "getPickResult",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(this.itemId);
    }

    @Override
    protected ResourceKey<Item> asItemId() {
        return this.itemId;
    }
}
