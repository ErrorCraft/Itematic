package net.errorcraft.itematic.mixin.component.type;

import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DyedItemColor.class)
public class DyedColorComponentExtender {
    @Redirect(
        method = "applyDyes",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"
        )
    )
    private static boolean isInForDyeableUseItemComponentCheck(ItemStack instance, TagKey<Item> tag) {
        return instance.itematic$hasBehavior(ItemComponentTypes.DYEABLE);
    }
}
