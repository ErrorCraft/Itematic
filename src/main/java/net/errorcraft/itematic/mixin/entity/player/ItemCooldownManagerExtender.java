package net.errorcraft.itematic.mixin.entity.player;

import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemCooldownManager.class)
public class ItemCooldownManagerExtender {
    @Redirect(
        method = "getGroup",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/DefaultedRegistry;getId(Ljava/lang/Object;)Lnet/minecraft/util/Identifier;"
        )
    )
    private <T> Identifier getIdUseRegistryEntry(DefaultedRegistry<Item> instance, T t, ItemStack stack) {
        return stack.itematic$key().getValue();
    }
}
