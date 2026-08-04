package net.errorcraft.itematic.mixin.entity.passive;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.mixin.entity.mob.MobEntityExtender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.fox.Fox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Fox.class)
public abstract class FoxEntityExtender extends MobEntityExtender {
    protected FoxEntityExtender(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForEmeraldUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.EMERALD);
    }

    @Redirect(
        method = "populateDefaultEquipmentSlots",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 1
        )
    )
    private ItemStack newItemStackForEggUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.EGG);
    }

    @Redirect(
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
    private ItemStack newItemStackForRabbitFootUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.RABBIT_FOOT);
    }

    @Redirect(
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
    private ItemStack newItemStackForRabbitHideUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.RABBIT_HIDE);
    }

    @Redirect(
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
    private ItemStack newItemStackForWheatUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.WHEAT);
    }

    @Redirect(
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
    private ItemStack newItemStackForLeatherUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.LEATHER);
    }

    @Redirect(
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
    private ItemStack newItemStackForFeatherUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemKeys.FEATHER);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemKeys.FOX_SPAWN_EGG;
    }

    @Mixin(Fox.FoxEatBerriesGoal.class)
    public static class EatBerriesGoalExtender {
        @Shadow
        @Final
        Fox field_17975;

        @Redirect(
            method = "pickSweetBerries",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
                ordinal = 0
            )
        )
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemLike item) {
            return this.field_17975.level().itematic$createStack(ItemKeys.SWEET_BERRIES);
        }

        @Redirect(
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
        private ItemStack newItemStackForSweetBerriesUseCreateStack(ItemLike item, int count) {
            return this.field_17975.level().itematic$createStack(ItemKeys.SWEET_BERRIES, count);
        }
    }
}
