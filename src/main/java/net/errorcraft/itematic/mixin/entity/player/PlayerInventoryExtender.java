package net.errorcraft.itematic.mixin.entity.player;

import net.errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryExtender {
    @Shadow
    @Final
    public DefaultedList<ItemStack> main;

    @Shadow
    @Final
    public DefaultedList<ItemStack> armor;

    @Shadow
    @Final
    public DefaultedList<ItemStack> offHand;

    @Shadow
    @Final
    public PlayerEntity player;

    @Inject(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/collection/DefaultedList;clear()V",
            ordinal = 0,
            shift = At.Shift.AFTER
        ),
        slice = @Slice(
            from = @At(
                value = "FIELD",
                target = "Lnet/minecraft/entity/player/PlayerInventory;offHand:Lnet/minecraft/util/collection/DefaultedList;",
                opcode = Opcodes.GETFIELD
            )
        ),
        cancellable = true
    )
    private void readInventoryUseDynamicRegistry(NbtList nbtList, CallbackInfo info) {
        DynamicRegistryManager registryManager = this.player.getWorld().getRegistryManager();
        InventoryUtil.readFromNbt(nbtList, registryManager, this::setItem);
        info.cancel();
    }

    @Unique
    private void setItem(int slot, ItemStack itemStack) {
        if (slot >= 0 && slot < this.main.size()) {
            this.main.set(slot, itemStack);
            return;
        }
        if (slot >= 100 && slot < this.armor.size() + 100) {
            this.armor.set(slot - 100, itemStack);
            return;
        }
        if (slot == 150) {
            this.offHand.set(0, itemStack);
        }
    }
}
