package net.errorcraft.itematic.mixin.item;

import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.util.Util;
import net.minecraft.world.item.DebugStickItem;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugStickItem.class)
public class DebugStickItemExtender {
    @Unique
    private static final String KEY = Util.makeDescriptionId("item", ItemIds.DEBUG_STICK.identifier());

    @Redirect(
        method = "handleInteraction",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/DebugStickItem;descriptionId:Ljava/lang/String;",
            opcode = Opcodes.GETFIELD
        )
    )
    private String translationKeyUseStaticKey(DebugStickItem instance) {
        return KEY;
    }
}
