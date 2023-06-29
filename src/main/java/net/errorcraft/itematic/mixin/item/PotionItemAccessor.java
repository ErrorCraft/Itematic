package net.errorcraft.itematic.mixin.item;

import net.minecraft.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PotionItem.class)
public interface PotionItemAccessor {
    @Accessor("MAX_USE_TIME")
    static int getMaxUseTime() {
        throw new AssertionError();
    }
}
