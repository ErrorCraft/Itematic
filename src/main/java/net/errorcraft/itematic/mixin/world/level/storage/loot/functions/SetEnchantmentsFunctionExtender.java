package net.errorcraft.itematic.mixin.world.level.storage.loot.functions;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.enchantment.Enchantables;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

@Mixin(SetEnchantmentsFunction.class)
public class SetEnchantmentsFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
        )
    )
    private boolean isBookUseDataComponent(ItemStack instance, Object o, @Share("transformsInto") LocalRef<Holder<Item>> transformsIntoReference) {
        Optional<Holder<Item>> transformsInto = Enchantables.transformsInto(instance);
        transformsInto.ifPresent(transformsIntoReference::set);
        return transformsInto.isPresent();
    }

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack transmuteCopyForEnchantedBookUseDataComponent(ItemStack instance, ItemLike newItem, @Share("transformsInto") LocalRef<Holder<Item>> transformsIntoReference) {
        return instance.itematic$transmuteCopy(transformsIntoReference.get());
    }
}
