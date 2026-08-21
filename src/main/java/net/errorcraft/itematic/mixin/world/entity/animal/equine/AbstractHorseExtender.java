package net.errorcraft.itematic.mixin.world.entity.animal.equine;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseExtender extends Animal {
    protected AbstractHorseExtender(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        )
    )
    private boolean isWheatCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.WHEAT);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;SUGAR:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isSugarCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.SUGAR);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/level/block/Blocks;HAY_BLOCK:Lnet/minecraft/world/level/block/Block;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isHayBlockCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.HAY_BLOCK);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;APPLE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isAppleCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.APPLE);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;CARROT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isCarrotCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.CARROT);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GOLDEN_CARROT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isGoldenCarrotCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GOLDEN_CARROT);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GOLDEN_APPLE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isGoldenAppleCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.GOLDEN_APPLE);
    }

    @Redirect(
        method = "handleEating",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;ENCHANTED_GOLDEN_APPLE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isEnchantedGoldenAppleCheckId(ItemStack instance, Item item) {
        return instance.itematic$is(ItemIds.ENCHANTED_GOLDEN_APPLE);
    }
}
