package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
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
public class SetEnchantmentsLootFunctionExtender {
    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z"
        )
    )
    private boolean isBookUseItemBehaviorCheck(ItemStack instance, Item item, @Share("transformedItem") LocalRef<Holder<Item>> transformedItem) {
        Optional<Holder<Item>> optionalItem = instance.itematic$getBehavior(ItemBehaviorType.ENCHANTABLE)
            .flatMap(EnchantableItemBehavior::transformsInto);
        optionalItem.ifPresent(transformedItem::set);
        return optionalItem.isPresent();
    }

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;transmuteCopy(Lnet/minecraft/world/level/ItemLike;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack withItemForEnchantedBookUseItemBehavior(ItemStack instance, ItemLike item, @Share("transformedItem") LocalRef<Holder<Item>> transformedItem) {
        return instance.itematic$copyWithItem(transformedItem.get());
    }
}
