package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(MooshroomEntity.class)
public abstract class MooshroomEntityExtender extends AnimalEntity {
    public MooshroomEntityExtender(EntityType<? extends MooshroomEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean interactMobIsOfUseRegistryEntryCheck(ItemStack instance, Item item) {
        return instance.isOf(ItemKeys.BOWL);
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack interactMobNewItemStackForSuspiciousStewUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.SUSPICIOUS_STEW));
    }

    @Redirect(
        method = "interactMob",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;SUSPICIOUS_STEW:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack interactMobNewItemStackForMushroomStewUseRegistryEntry(ItemConvertible item) {
        return new ItemStack(this.getWorld().getItem(ItemKeys.MUSHROOM_STEW));
    }
}
