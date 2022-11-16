package errorcraft.itematic.mixin.entity.player;

import errorcraft.itematic.inventory.InventoryUtil;
import errorcraft.itematic.inventory.SlotUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        method = "writeNbt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void writeNbtUseDynamicRegistry(NbtList nbtList, CallbackInfoReturnable<NbtList> info) {
        DynamicRegistryManager registryManager = this.player.world.getRegistryManager();
        for (int i = 0; i < this.main.size(); i++) {
            SlotUtil.writeToNbt(nbtList, registryManager, i, this.main.get(i));
        }
        for (int i = 0; i < this.armor.size(); i++) {
            SlotUtil.writeToNbt(nbtList, registryManager, i + 100, this.armor.get(i));
        }
        for (int i = 0; i < this.offHand.size(); i++) {
            SlotUtil.writeToNbt(nbtList, registryManager, i + 150, this.offHand.get(i));
        }
        info.setReturnValue(nbtList);
    }

    @Inject(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/collection/DefaultedList;clear()V",
            ordinal = 2,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void readNbtUseDynamicRegistry(NbtList nbtList, CallbackInfo info) {
        DynamicRegistryManager registryManager = this.player.world.getRegistryManager();
        InventoryUtil.readFromNbt(nbtList, registryManager, this::setItem);
        info.cancel();
    }

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
