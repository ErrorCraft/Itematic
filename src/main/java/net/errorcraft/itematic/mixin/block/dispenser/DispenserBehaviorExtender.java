package net.errorcraft.itematic.mixin.block.dispenser;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.block.dispenser.DispenserBehaviorUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorExtender {
    @ModifyArg(
        method = "registerDefaults",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DispenserBlock;registerBehavior(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/block/dispenser/DispenserBehavior;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GLASS_BOTTLE:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeGlassBottleDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.BOTTLE_DISPENSER_BEHAVIOR = behavior;
    }

    @ModifyArg(
        method = "registerDefaults",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DispenserBlock;registerBehavior(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/block/dispenser/DispenserBehavior;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storePotionDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.POTION_DISPENSER_BEHAVIOR = behavior;
    }

    @ModifyArg(
        method = "registerDefaults",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/DispenserBlock;registerBehavior(Lnet/minecraft/item/ItemConvertible;Lnet/minecraft/block/dispenser/DispenserBehavior;)V",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;LEATHER_HORSE_ARMOR:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeHorseArmorDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.HORSE_ARMOR_DISPENSER_BEHAVIOR = behavior;
    }

    @Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$17")
    class GlassBottleExtender {
        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack",
                ordinal = 0
            )
        )
        private ItemStack newItemStackForHoneyBottleUseDynamicRegistry(ItemConvertible item, @Local ServerWorld serverWorld) {
            return new ItemStack(serverWorld.getItem(ItemKeys.HONEY_BOTTLE));
        }

        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack",
                ordinal = 0
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;HONEY_BOTTLE:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private ItemStack newItemStackForPotionUseDynamicRegistry(ItemConvertible item, @Local ServerWorld serverWorld) {
            return new ItemStack(serverWorld.getItem(ItemKeys.POTION));
        }
    }

    @Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$20")
    class PotionExtender {
        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "NEW",
                target = "net/minecraft/item/ItemStack",
                ordinal = 0
            )
        )
        private ItemStack newItemStackUseDynamicRegistry(ItemConvertible item, @Local ServerWorld serverWorld) {
            return new ItemStack(serverWorld.getItem(ItemKeys.GLASS_BOTTLE));
        }
    }
}
