package net.errorcraft.itematic.mixin.entity.mob;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityExtender extends MobEntityExtender {
    public DrownedEntityExtender(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Redirect(
        method = "initialize",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForNautilusShellUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.NAUTILUS_SHELL);
    }

    @Redirect(
        method = {
            "initEquipment",
            "shootAt"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForTridentUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.TRIDENT);
    }

    @Redirect(
        method = "initEquipment",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;TRIDENT:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForFishingRodUseCreateStack(ItemConvertible item) {
        return this.getWorld().itematic$createStack(ItemKeys.FISHING_ROD);
    }

    @Redirect(
        method = "prefersNewEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;NAUTILUS_SHELL:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForNautilusShellUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemKeys.NAUTILUS_SHELL);
    }

    @Override
    protected @Nullable RegistryKey<Item> pickBlockKey() {
        return ItemKeys.DROWNED_SPAWN_EGG;
    }

    @Mixin(targets = "net/minecraft/entity/mob/DrownedEntity$TridentAttackGoal")
    public static class TridentAttackGoalExtender {
        @Redirect(
            method = "canStart",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"
            )
        )
        private boolean isOfForTridentUseRegistryKeyCheck(ItemStack instance, Item item) {
            return instance.itematic$isOf(ItemKeys.TRIDENT);
        }
    }
}
