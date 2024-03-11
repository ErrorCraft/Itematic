package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.DecorationItem;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DecorationItem.class)
public interface DecorationItemAccessor {
    @Accessor("RANDOM_TEXT")
    static Text randomText() {
        throw new AssertionError();
    }
}
