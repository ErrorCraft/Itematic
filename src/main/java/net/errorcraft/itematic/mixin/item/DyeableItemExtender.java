package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyeableItem.class)
public interface DyeableItemExtender {
    @Redirect(
        method = "blendAndSetColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private static boolean isInForDyeableUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasComponent(ItemComponentTypes.DYEABLE);
    }
}
