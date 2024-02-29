package net.errorcraft.itematic.mixin.text;

import com.google.gson.Gson;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

public interface TextAccessor {
    @Mixin(Text.Serialization.class)
    interface SerializationAccessor {
        @Accessor("GSON")
        static Gson gson() {
            throw new AssertionError();
        }
    }
}
