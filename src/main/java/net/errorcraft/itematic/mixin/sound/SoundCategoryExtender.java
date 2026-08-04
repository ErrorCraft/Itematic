package net.errorcraft.itematic.mixin.sound;

import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundSource.class)
public class SoundCategoryExtender implements StringRepresentable {
    @Shadow
    @Final
    private String name;

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
