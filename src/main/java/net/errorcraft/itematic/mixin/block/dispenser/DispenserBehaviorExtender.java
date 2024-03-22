package net.errorcraft.itematic.mixin.block.dispenser;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.block.dispenser.DispenserBehaviorUtil;
import net.errorcraft.itematic.component.PotionContentsComponentUtil;
import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
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
                target = "Lnet/minecraft/item/Items;BRUSH:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeBrushDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.BRUSH_DISPENSER_BEHAVIOR = behavior;
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
                target = "Lnet/minecraft/item/Items;CHEST:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeChestDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.CHEST_EQUIPMENT_DISPENSER_BEHAVIOR = behavior;
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
                target = "Lnet/minecraft/block/Blocks;TNT:Lnet/minecraft/block/Block;"
            )
        )
    )
    private static DispenserBehavior storeTntDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.TNT_DISPENSER_BEHAVIOR = behavior;
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
                target = "Lnet/minecraft/block/Blocks;CARVED_PUMPKIN:Lnet/minecraft/block/Block;"
            )
        )
    )
    private static DispenserBehavior storeCarvedPumpkinDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.CARVED_PUMPKIN_DISPENSER_BEHAVIOR = behavior;
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
                target = "Lnet/minecraft/item/Items;GLOWSTONE:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeRespawnAnchorDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.CHARGE_RESPAWN_ANCHOR_DISPENSER_BEHAVIOR = behavior;
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
                target = "Lnet/minecraft/item/Items;SHEARS:Lnet/minecraft/item/Item;"
            )
        )
    )
    private static DispenserBehavior storeShearsDispenserBehavior(DispenserBehavior behavior) {
        return DispenserBehaviorUtil.SHEAR_DISPENSER_BEHAVIOR = behavior;
    }

    @Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$18")
    class GlassBottleExtender {
        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;",
                ordinal = 0
            )
        )
        private ItemStack newItemStackForHoneyBottleUseCreateStack(ItemConvertible item, @Local ServerWorld serverWorld) {
            return serverWorld.itematic$createStack(ItemKeys.HONEY_BOTTLE);
        }

        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/component/type/PotionContentsComponent;createStack(Lnet/minecraft/item/Item;Lnet/minecraft/registry/entry/RegistryEntry;)Lnet/minecraft/item/ItemStack;"
            ),
            slice = @Slice(
                from = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/item/Items;POTION:Lnet/minecraft/item/Item;",
                    opcode = Opcodes.GETSTATIC
                )
            )
        )
        private ItemStack newItemStackForPotionUseCreateStack(Item item, RegistryEntry<Potion> potion, @Local ServerWorld serverWorld) {
            return PotionContentsComponentUtil.setPotion(serverWorld.itematic$createStack(ItemKeys.POTION), potion);
        }
    }

    @Mixin(targets = "net/minecraft/block/dispenser/DispenserBehavior$22")
    class PotionExtender {
        @Redirect(
            method = "dispenseSilently",
            at = @At(
                value = "NEW",
                target = "(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
            )
        )
        private ItemStack newItemStackForGlassBottleUseCreateStack(ItemConvertible item, @Local ServerWorld serverWorld) {
            return serverWorld.itematic$createStack(ItemKeys.GLASS_BOTTLE);
        }
    }
}
