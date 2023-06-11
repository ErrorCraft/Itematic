package net.errorcraft.itematic.mixin.util;

import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(Rarity.class)
public class RarityExtender implements StringIdentifiable {
    private String name;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void initSetNameField(String string, int i, Formatting formatting, CallbackInfo info) {
        this.name = string.toLowerCase(Locale.ROOT);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
