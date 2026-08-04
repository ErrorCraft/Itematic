package net.errorcraft.itematic.mixin.village;

import net.errorcraft.itematic.access.village.TradeOfferAccess;
import net.minecraft.world.item.trading.MerchantOffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MerchantOffer.class)
public class TradeOfferExtender implements TradeOfferAccess {
    @Shadow
    @Final
    @Mutable
    private boolean rewardExp;

    @Override
    public void itematic$rewardsPlayerExperience(boolean rewardsPlayerExperience) {
        this.rewardExp = rewardsPlayerExperience;
    }
}
