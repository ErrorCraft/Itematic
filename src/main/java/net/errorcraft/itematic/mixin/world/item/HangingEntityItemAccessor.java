package net.errorcraft.itematic.mixin.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.HangingEntityItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HangingEntityItem.class)
public interface HangingEntityItemAccessor {
    @Accessor("TOOLTIP_RANDOM_VARIANT")
    static Component randomVariantTooltip() {
        throw new AssertionError();
    }
}
