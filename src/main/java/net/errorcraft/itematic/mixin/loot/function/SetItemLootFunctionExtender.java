package net.errorcraft.itematic.mixin.loot.function;

import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.SetItemLootFunction;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SetItemLootFunction.class)
public class SetItemLootFunctionExtender {
    @Shadow
    @Final
    private RegistryEntry<Item> item;

    @Redirect(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;withItem(Lnet/minecraft/item/ItemConvertible;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack withItemUseRegistryEntry(ItemStack instance, ItemConvertible item) {
        return instance.itematic$copyWithItem(this.item);
    }
}
