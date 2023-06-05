package net.errorcraft.itematic.mixin.util;

import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.UseAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(UseAction.class)
public class UseActionExtender implements StringIdentifiable {
    private String name;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void initSetNameField(String string, int i, CallbackInfo ci) {
        this.name = string.toLowerCase(Locale.ROOT);
    }

    @Override
    public String asString() {
        return this.name;
    }
}
