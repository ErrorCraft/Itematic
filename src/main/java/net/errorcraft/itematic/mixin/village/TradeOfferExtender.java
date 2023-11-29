package net.errorcraft.itematic.mixin.village;

import net.errorcraft.itematic.access.village.TradeOfferAccess;
import net.minecraft.village.TradeOffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TradeOffer.class)
public class TradeOfferExtender implements TradeOfferAccess {
    @Shadow
    private boolean rewardingPlayerExperience;

    @Override
    public void itematic$rewardsPlayerExperience(boolean rewardsPlayerExperience) {
        this.rewardingPlayerExperience = rewardsPlayerExperience;
    }
}
