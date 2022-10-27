package errorcraft.itematic.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public class ItemsExtender {
    @Inject(method = "register(Lnet/minecraft/util/Identifier;Lnet/minecraft/item/Item;)Lnet/minecraft/item/Item;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/Registry;register(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/util/Identifier;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.BEFORE), cancellable = true)
    private static void doNotAddItemToRegistry(Identifier id, Item item, CallbackInfoReturnable<Item> info) {
        info.setReturnValue(item);
    }
}
