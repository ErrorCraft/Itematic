package errorcraft.itematic.mixin.entity.player;

import errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
            ordinal = 2,
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void readNbtUseDynamicRegistry(NbtList nbtList, CallbackInfo info) {
        DynamicRegistryManager registryManager = this.player.getWorld().getRegistryManager();
        InventoryUtil.readFromNbt(nbtList, registryManager, this::setItem);
        info.cancel();
    }

    @Redirect(
        method = "addStack(ILnet/minecraft/item/ItemStack;)I",
        at = @At(
            value = "NEW",
            target = "net/minecraft/item/ItemStack"
        )
    )
    private ItemStack addStackCreateItemStackWithRegistryEntry(ItemConvertible item, int count, int slot, ItemStack stack) {
        return new ItemStack(stack.getRegistryEntry(), count);
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
