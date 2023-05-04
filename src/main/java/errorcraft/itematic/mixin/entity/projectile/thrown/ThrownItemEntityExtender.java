package errorcraft.itematic.mixin.entity.projectile.thrown;

import errorcraft.itematic.access.entity.projectile.thrown.ThrownItemEntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ThrownItemEntity.class)
public abstract class ThrownItemEntityExtender extends ThrownEntity implements ThrownItemEntityAccess {
    protected ThrownItemEntityExtender(EntityType<? extends ThrownEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "setItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
        )
    )
    private boolean setItemIsOfUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.isOf(this.getDefaultItemKey());
    }

    @Redirect(
        method = "getStack",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack getStackNewItemStackUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getDefaultItemRegistryEntry());
    }

    private RegistryEntry<Item> getDefaultItemRegistryEntry() {
        return this.getWorld().getRegistryManager().get(RegistryKeys.ITEM).entryOf(this.getDefaultItemKey());
    }
}
