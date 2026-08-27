package net.errorcraft.itematic.mixin.core.cauldron;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

public interface CauldronInteractionExtender {
    @Mixin(CauldronInteraction.Dispatcher.class)
    class DispatcherExtender {
        @ModifyArg(
            method = "put(Lnet/minecraft/world/item/Item;Lnet/minecraft/core/cauldron/CauldronInteraction;)V",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            index = 0
        )
        @SuppressWarnings("unchecked")
        private <K> K putItemIdInstead(K key) {
            return (K) BuiltInRegistries.ITEM.getResourceKey((Item) key).orElseThrow();
        }

        @ModifyArg(
            method = "get",
            at = @At(
                value = "INVOKE",
                target = "Ljava/util/Map;getOrDefault(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"
            ),
            index = 0
        )
        private Object useItemIdInstead(Object key, @Local(name = "itemStack", argsOnly = true) ItemStack itemStack) {
            return itemStack.itematic$key();
        }
    }
}
