package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractChestedHorseExtender extends AbstractHorse {
    protected AbstractChestedHorseExtender(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/equine/AbstractChestedHorse;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    @Nullable
    private ItemEntity spawnChestUseId(AbstractChestedHorse instance, ServerLevel level, ItemLike item) {
        return this.itematic$spawnAtLocation(level, ItemIds.CHEST);
    }

    @Redirect(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isChestCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.CHEST);
    }

    @Mixin(targets = "net/minecraft/world/entity/animal/equine/AbstractChestedHorse$1")
    public static class ChestSlotAccessExtender {
        @Shadow
        @Final
        AbstractChestedHorse field_27867;

        @Redirect(
            method = "get",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForChestUseCreateStack(ItemLike item) {
            return this.field_27867.level().itematic$createStack(ItemIds.CHEST);
        }

        @Redirect(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
        )
        private boolean isChestCheckId(ItemStack instance, Item item) {
            return instance.itematic$is(ItemIds.CHEST);
        }
    }
}
