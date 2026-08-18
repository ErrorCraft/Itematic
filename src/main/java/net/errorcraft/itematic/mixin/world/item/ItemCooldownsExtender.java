package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemCooldowns.class)
public class ItemCooldownsExtender {
    @Redirect(
        method = "getCooldownGroup",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/Identifier;"
        )
    )
    private <T> Identifier getIdUseHolder(DefaultedRegistry<Item> instance, T t, ItemStack stack) {
        return stack.itematic$key().identifier();
    }
}
