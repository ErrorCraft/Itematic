package net.errorcraft.itematic.mixin.world.entity.monster.zombie;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.zombie.Drowned;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Drowned.class)
public abstract class DrownedExtender extends MobExtender {
    public DrownedExtender(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @WrapOperation(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForNautilusShellUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.NAUTILUS_SHELL);
    }

    @WrapOperation(
        method = {
            "finalizeSpawn",
            "performRangedAttack"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isTridentCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.TRIDENT);
    }

    @WrapOperation(
        method = {
            "populateDefaultEquipmentSlots",
            "performRangedAttack"
        },
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForTridentUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.TRIDENT);
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
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;TRIDENT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForFishingRodUseCreateStack(ItemLike item, Operation<ItemStack> original) {
        return this.level().itematic$createStack(ItemIds.FISHING_ROD);
    }

    @WrapOperation(
        method = "canReplaceCurrentItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isNautilusShellCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
        return instance.is(ItemIds.NAUTILUS_SHELL);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickResultItem() {
        return ItemIds.DROWNED_SPAWN_EGG;
    }

    @Mixin(targets = "net/minecraft/world/entity/monster/zombie/Drowned$DrownedTridentAttackGoal")
    public static class DrownedTridentAttackGoalExtender {
        @WrapOperation(
            method = "canUse",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
        )
        private boolean isTridentCheckId(ItemStack instance, Object o, Operation<Boolean> original) {
            return instance.is(ItemIds.TRIDENT);
        }
    }
}
