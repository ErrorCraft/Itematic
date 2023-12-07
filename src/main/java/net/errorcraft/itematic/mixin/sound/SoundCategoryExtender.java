package net.errorcraft.itematic.mixin.sound;

import net.minecraft.sound.SoundCategory;
import net.minecraft.util.StringIdentifiable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(SoundCategory.class)
public class SoundCategoryExtender implements StringIdentifiable {
    @Shadow
    @Final
    private String name;

    @Override
    public String asString() {
        return this.name;
    }
}
