package errorcraft.itematic.mixin.inventory;

import errorcraft.itematic.access.PlayerAccess;
import errorcraft.itematic.inventory.InventoryUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderChestInventory.class)
public class EnderChestInventoryExtender extends SimpleInventory implements PlayerAccess {
    @Unique
    private PlayerEntity player;

    @Inject(
        method = "readNbtList",
        at = @At("HEAD"),
        cancellable = true
    )
    private void readNbtListUseDynamicRegistry(NbtList nbtList, CallbackInfo info) {
        InventoryUtil.readFromNbt(nbtList, this.player.getWorld().getRegistryManager(), this.stacks);
        info.cancel();
    }

    @Override
    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }
}
