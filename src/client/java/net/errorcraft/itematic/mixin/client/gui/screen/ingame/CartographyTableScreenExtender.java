package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(CartographyTableScreen.class)
public class CartographyTableScreenExtender {
    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;PAPER:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForPaperUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.PAPER);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;MAP:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForMapUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.MAP);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GLASS_PANE:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private boolean isOfForGlassPaneUseRegistryKeyCheck(ItemStack instance, Item item) {
        return instance.itematic$isOf(ItemIds.GLASS_PANE);
    }
}
