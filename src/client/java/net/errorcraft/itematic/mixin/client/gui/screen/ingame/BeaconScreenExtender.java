package net.errorcraft.itematic.mixin.client.gui.screen.ingame;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.client.gui.screen.ingame.BeaconScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.screen.BeaconScreenHandler;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BeaconScreen.class)
public abstract class BeaconScreenExtender extends HandledScreen<BeaconScreenHandler> {
    public BeaconScreenExtender(BeaconScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForNetheriteIngotUseRegistryEntry(ItemConvertible item) {
        return this.createStack(ItemKeys.NETHERITE_INGOT);
    }

    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;NETHERITE_INGOT:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForEmeraldUseRegistryEntry(ItemConvertible item) {
        return this.createStack(ItemKeys.EMERALD);
    }

    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;EMERALD:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForDiamondUseRegistryEntry(ItemConvertible item) {
        return this.createStack(ItemKeys.DIAMOND);
    }

    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;DIAMOND:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForGoldIngotUseRegistryEntry(ItemConvertible item) {
        return this.createStack(ItemKeys.GOLD_INGOT);
    }

    @Redirect(
        method = "drawBackground",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/item/Items;GOLD_INGOT:Lnet/minecraft/item/Item;",
                opcode = Opcodes.GETSTATIC
            )
        )
    )
    private ItemStack newItemStackForIronIngotUseRegistryEntry(ItemConvertible item) {
        return this.createStack(ItemKeys.IRON_INGOT);
    }

    @Unique
    private ItemStack createStack(RegistryKey<Item> item) {
        if (this.client == null || this.client.world == null) {
            return ItemStack.EMPTY;
        }
        return this.client.world.itematic$createStack(item);
    }
}
