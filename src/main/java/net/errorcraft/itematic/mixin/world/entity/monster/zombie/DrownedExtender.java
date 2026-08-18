package net.errorcraft.itematic.mixin.world.entity.monster.zombie;

import net.errorcraft.itematic.mixin.world.entity.MobExtender;
import net.errorcraft.itematic.references.ItemIds;
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
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Drowned.class)
public abstract class DrownedExtender extends MobExtender {
    public DrownedExtender(EntityType<? extends Zombie> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "finalizeSpawn",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack newItemStackForNautilusShellUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.NAUTILUS_SHELL);
    }

    @Redirect(
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
    private ItemStack newItemStackForTridentUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.TRIDENT);
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
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;TRIDENT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForFishingRodUseCreateStack(ItemLike item) {
        return this.level().itematic$createStack(ItemIds.FISHING_ROD);
    }

    @Redirect(
        method = "canReplaceCurrentItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;NAUTILUS_SHELL:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isNautilusShellCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.NAUTILUS_SHELL);
    }

    @Override
    protected @Nullable ResourceKey<Item> pickBlockKey() {
        return ItemIds.DROWNED_SPAWN_EGG;
    }

    @Mixin(targets = "net/minecraft/world/entity/monster/zombie/Drowned$DrownedTridentAttackGoal")
    public static class DrownedTridentAttackGoalExtender {
        @Redirect(
            method = "canUse",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
            )
        )
        private boolean isTridentCheckId(ItemStack instance, Item item) {
            return instance.itematic$is(ItemIds.TRIDENT);
        }
    }
}
