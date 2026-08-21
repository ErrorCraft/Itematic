package net.errorcraft.itematic.mixin.sounds;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundSource.class)
public class SoundSourceExtender implements StringRepresentable {
    @Shadow
    @Final
    private String name;

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
