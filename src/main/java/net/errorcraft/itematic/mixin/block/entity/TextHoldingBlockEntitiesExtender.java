package net.errorcraft.itematic.mixin.block.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ BannerBlockEntity.class, BeaconBlockEntity.class, EnchantingTableBlockEntity.class, LockableContainerBlockEntity.class })
public class TextHoldingBlockEntitiesExtender {
    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private String toJsonStringUseDynamicRegistry(Text text, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return TextUtil.toJsonString(text, lookup);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private MutableText fromJsonUseDynamicRegistry(String json, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return TextUtil.fromJsonString(json, lookup);
    }
}
