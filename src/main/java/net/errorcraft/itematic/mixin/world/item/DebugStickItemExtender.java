package net.errorcraft.itematic.mixin.world.item;

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
    private static final String BASE_INTERACTION_TRANSLATION_KEY = Util.makeDescriptionId("item", ItemIds.DEBUG_STICK.identifier());

    @Redirect(
        method = "handleInteraction",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/item/DebugStickItem;descriptionId:Ljava/lang/String;",
            opcode = Opcodes.GETFIELD
        )
    )
    private String descriptionIdUseStaticKey(DebugStickItem instance) {
        return BASE_INTERACTION_TRANSLATION_KEY;
    }
}
