package net.errorcraft.itematic.mixin.village;

import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TradeOffers.class)
public interface TradeOffersAccessor {
    @Accessor("DEFAULT_MAX_USES")
    static int getDefaultMaxUses() {
        throw new AssertionError();
    }
}
