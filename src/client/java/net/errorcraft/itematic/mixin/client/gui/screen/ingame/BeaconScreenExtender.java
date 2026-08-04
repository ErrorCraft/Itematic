package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BeaconScreen.class)
public abstract class BeaconScreenExtender extends AbstractContainerScreen<BeaconMenu> {
    public BeaconScreenExtender(BeaconMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForNetheriteIngotUseCreateStack(ItemLike item) {
        return this.createStack(ItemKeys.NETHERITE_INGOT);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;NETHERITE_INGOT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForEmeraldUseCreateStack(ItemLike item) {
        return this.createStack(ItemKeys.EMERALD);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;EMERALD:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForDiamondUseCreateStack(ItemLike item) {
        return this.createStack(ItemKeys.DIAMOND);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;DIAMOND:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldIngotUseCreateStack(ItemLike item) {
        return this.createStack(ItemKeys.GOLD_INGOT);
    }

    @Redirect(
        method = "renderBg",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/world/item/Items;GOLD_INGOT:Lnet/minecraft/world/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForIronIngotUseCreateStack(ItemLike item) {
        return this.createStack(ItemKeys.IRON_INGOT);
    }

    @Unique
    private ItemStack createStack(ResourceKey<Item> item) {
        if (this.minecraft == null || this.minecraft.level == null) {
            return ItemStack.EMPTY;
        }
        return this.minecraft.level.itematic$createStack(item);
    }
}
