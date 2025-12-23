package net.errorcraft.itematic.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.component.type.BundleContentsComponentUtil;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(BundleItem.class)
public class BundleItemExtender {
    @ModifyArg(
        method = "dropAllBundledItems",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"
        )
    )
    private static Object copyWithExtraFields(Object type, @Local BundleContentsComponent contents) {
        return BundleContentsComponentUtil.create(contents.itematic$extraFields().rules());
    }
}
