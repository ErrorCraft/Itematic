package net.errorcraft.itematic.mixin.client.gui.screens.inventory;

import com.mojang.datafixers.DataFixUtils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeaconScreen.class)
public abstract class BeaconScreenExtender extends AbstractContainerScreen<BeaconMenu> {
    @Unique
    private static final int TICKS_PER_PAYMENT_ITEM = 30;
    @Unique
    private static final int PAYMENT_ITEM_COUNT = 5;
    @Unique
    private HolderSet<Item> paymentItems = HolderSet.empty();
    @Unique
    private int waitedTicks;
    @Unique
    private boolean goingForwards = true;
    @Unique
    private int currentPaymentItemIndex = 0;

    public BeaconScreenExtender(BeaconMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    @SuppressWarnings("DataFlowIssue")
    private void getPaymentItems(BeaconMenu menu, Inventory inventory, Component title, CallbackInfo info) {
        this.paymentItems = DataFixUtils.orElseGet(
            this.minecraft.level.registryAccess()
                .lookupOrThrow(Registries.ITEM)
                .get(ItemTags.BEACON_PAYMENT_ITEMS),
            HolderSet::empty
        );
    }

    @Inject(
        method = "containerTick",
        at = @At("TAIL")
    )
    private void increaseProgress(CallbackInfo info) {
        if (this.paymentItems.size() <= PAYMENT_ITEM_COUNT) {
            this.currentPaymentItemIndex = 0;
            return;
        }

        this.waitedTicks++;
        if (this.waitedTicks <= TICKS_PER_PAYMENT_ITEM) {
            return;
        }

        this.waitedTicks = 0;
        this.currentPaymentItemIndex += this.goingForwards ? 1 : -1;
        int maxIndex = this.paymentItems.size() - PAYMENT_ITEM_COUNT;
        if (this.currentPaymentItemIndex >= maxIndex) {
            this.goingForwards = false;
        } else if (this.currentPaymentItemIndex <= 0) {
            this.goingForwards = true;
        }
    }

    @Redirect(
        method = "extractBackground",
        at = @At(
            value = "NEW",
            target = "(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;",
            ordinal = 0
        )
    )
    private ItemStack newItemStackForFirstItemUseHolder(ItemLike item) {
        return this.createPaymentStack(0);
    }

    @Redirect(
        method = "extractBackground",
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
    private ItemStack newItemStackForSecondItemUseHolder(ItemLike item) {
        return this.createPaymentStack(1);
    }

    @Redirect(
        method = "extractBackground",
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
    private ItemStack newItemStackForThirdItemUseHolder(ItemLike item) {
        return this.createPaymentStack(2);
    }

    @Redirect(
        method = "extractBackground",
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
    private ItemStack newItemStackForFourthItemUseHolder(ItemLike item) {
        return this.createPaymentStack(3);
    }

    @Redirect(
        method = "extractBackground",
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
    private ItemStack newItemStackForFifthItemUseHolder(ItemLike item) {
        return this.createPaymentStack(4);
    }

    @Unique
    private ItemStack createPaymentStack(int offset) {
        if (this.paymentItems.size() == 0) {
            return ItemStack.EMPTY;
        }

        int index = this.currentPaymentItemIndex + offset;
        if (index >= this.paymentItems.size()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(this.paymentItems.get(index));
    }
}
