package net.errorcraft.itematic.mixin.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HangingEntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HangingEntityItem.class)
public interface DecorationItemAccessor {
    @Accessor("TOOLTIP_RANDOM_VARIANT")
    static Component randomText() {
        throw new AssertionError();
    }
}
