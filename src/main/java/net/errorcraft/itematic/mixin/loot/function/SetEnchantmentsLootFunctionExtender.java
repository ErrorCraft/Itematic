package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.EnchantableItemComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetEnchantmentsLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SetEnchantmentsLootFunction.class)
public class SetEnchantmentsLootFunctionExtender {
    @Inject(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"
        ),
        cancellable = true
    )
    private void checkForTransformingItem(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> info, @Local Object2IntMap<Enchantment> levelledEnchantments) {
        Optional<RegistryEntry<Item>> transformedEntry = stack.itematic$getComponent(ItemComponentTypes.ENCHANTABLE)
            .flatMap(EnchantableItemComponent::transformsInto);
        if (transformedEntry.isEmpty()) {
            return;
        }
        ItemStack transformedStack = new ItemStack(transformedEntry.get());
        levelledEnchantments.forEach((enchantment, level) -> EnchantedBookItem.addEnchantment(transformedStack, new EnchantmentLevelEntry(enchantment, level)));
        info.setReturnValue(transformedStack);
    }
}
