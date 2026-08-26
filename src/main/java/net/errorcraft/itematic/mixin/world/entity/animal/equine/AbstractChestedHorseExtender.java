package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractChestedHorseExtender extends AbstractHorse {
    protected AbstractChestedHorseExtender(EntityType<? extends AbstractHorse> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/equine/AbstractChestedHorse;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/entity/item/ItemEntity;"
        )
    )
    @Nullable
    private ItemEntity spawnChestUseId(AbstractChestedHorse instance, ServerLevel level, ItemLike resource, Operation<ItemEntity> original) {
        return this.itematic$spawnAtLocation(level, ItemIds.CHEST);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Player;isHolding(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isHoldingGoldenDandelionCheckId(Player instance, Item item, Operation<Boolean> original) {
        return instance.itematic$isHolding(ItemIds.GOLDEN_DANDELION);
    }

    @WrapOperation(
        method = "mobInteract",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isChestCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.CHEST);
    }

    @Mixin(targets = "net/minecraft/world/entity/animal/equine/AbstractChestedHorse$1")
    public static class ChestSlotAccessExtender {
        @Shadow
        @Final
        AbstractChestedHorse this$0;

        @WrapOperation(
            method = "get",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForChestUseCreateStack(ItemLike item, Operation<ItemStack> original) {
            return this.this$0.level().itematic$createStack(ItemIds.CHEST);
        }

        @WrapOperation(
            method = "set",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isChestCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
            return instance.is(ItemIds.CHEST);
        }
    }
}
