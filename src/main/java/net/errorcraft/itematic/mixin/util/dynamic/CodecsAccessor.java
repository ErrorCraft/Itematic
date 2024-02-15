package net.errorcraft.itematic.mixin.util.dynamic;

import com.mojang.serialization.Codec;
import net.minecraft.util.dynamic.Codecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(Codecs.class)
public interface CodecsAccessor {
    @Invoker("rangedFloat")
    static Codec<Float> rangedFloat(float min, float max, Function<Float, String> messageFactory) {
        throw new AssertionError();
    }
}
