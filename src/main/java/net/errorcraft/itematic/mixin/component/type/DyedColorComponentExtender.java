package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyedColorComponent.class)
public class DyedColorComponentExtender {
    @Redirect(
        method = "setColor",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"
        )
    )
    private static boolean isInForDyeableUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasComponent(ItemComponentTypes.DYEABLE);
    }
}
