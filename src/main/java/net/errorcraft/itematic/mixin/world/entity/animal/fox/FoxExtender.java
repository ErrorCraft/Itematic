package net.errorcraft.itematic.mixin.world.entity.animal.fox;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Fox.class)
public abstract class FoxExtender extends MobExtender {
    protected FoxExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForEmeraldUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.EMERALD);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 1
        )
    )
    private ItemStack newItemStackForEggUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.EGG);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.4f"
            )
        )
    )
    private ItemStack newItemStackForRabbitFootUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.RABBIT_FOOT);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.4f"
            )
        )
    )
    private ItemStack newItemStackForRabbitHideUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.RABBIT_HIDE);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.6f"
            )
        )
    )
    private ItemStack newItemStackForWheatUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.WHEAT);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack newItemStackForLeatherUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.LEATHER);
    }

    @WrapOperation(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 1
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "floatValue=0.8f"
            )
        )
    )
    private ItemStack newItemStackForFeatherUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.FEATHER);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.FOX_SPAWN_EGG;
    }

    @Mixin(Fox.FoxEatBerriesGoal.class)
    public static class FoxEatBerriesGoalExtender {
        @Shadow
        @Final
        Fox this$0;

        @WrapOperation(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
                ordinal = 0
            )
        )
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemLike item, Operation<ItemStack> original) {
            return this.this$0.level().itematic$createStack(BlockItemIds.SWEET_BERRY_CROP.item());
        }

        @WrapOperation(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;I)Lnet/minecraft/world/item/ItemStack;",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/fox/Fox;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V"
                )
            )
        )
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemLike item, int count, Operation<ItemStack> original) {
            return this.this$0.level().itematic$createStack(BlockItemIds.SWEET_BERRY_CROP.item(), count);
        }
    }
}
