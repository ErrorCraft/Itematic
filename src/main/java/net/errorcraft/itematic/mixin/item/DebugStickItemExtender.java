package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.item.ItemKeys;
import net.minecraft.item.DebugStickItem;
import net.minecraft.util.Util;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugStickItem.class)
public class DebugStickItemExtender {
    @Unique
    private static final String KEY = Util.createTranslationKey("item", ItemKeys.DEBUG_STICK.getValue());

    @Redirect(
        method = "use",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/item/DebugStickItem;translationKey:Ljava/lang/String;",
            opcode = Opcodes.GETFIELD
        )
    )
    private String translationKeyUseStaticKey(DebugStickItem instance) {
        return KEY;
    }
}
