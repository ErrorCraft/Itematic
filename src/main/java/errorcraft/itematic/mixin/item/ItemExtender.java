package errorcraft.itematic.mixin.item;

import net.minecraft.item.Item;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Item.class)
public class ItemExtender {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/registry/DefaultedRegistry;createEntry(Ljava/lang/Object;)Lnet/minecraft/util/registry/RegistryEntry$Reference;"
        )
    )
    private RegistryEntry.Reference<Item> doNotCreateIntrusiveHolder(DefaultedRegistry<Item> instance, Object o) {
        return null;
    }
}
