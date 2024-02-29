package net.errorcraft.itematic.mixin.item;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WrittenBookItem.class)
public class WrittenBookItemExtender {
    @Unique
    private static final JsonElement EMPTY = new JsonPrimitive("");

    @Redirect(
        method = "textToJson",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromLenientJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private static MutableText fromLenientJsonUseDynamicRegistry(String json, @Nullable ServerCommandSource commandSource) {
        if (commandSource == null) {
            return Text.empty();
        }
        return TextUtil.fromJsonStringLenient(json, commandSource.getRegistryManager());
    }

    @Redirect(
        method = "textToJson",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private static String toJsonStringUseDynamicRegistry(Text text, @Nullable ServerCommandSource commandSource) {
        if (commandSource == null) {
            return EMPTY.toString();
        }
        return TextUtil.toJsonString(text, commandSource.getRegistryManager());
    }
}
