package net.errorcraft.itematic.mixin.text;

import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.NbtTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(NbtTextContent.class)
public class NbtTextContentExtender {
    @Redirect(
        method = "method_10917",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private static MutableText fromJsonUseDynamicRegistry(String json, ServerCommandSource source) {
        return TextUtil.fromJsonString(json, source.getRegistryManager());
    }
}
