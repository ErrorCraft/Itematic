package net.errorcraft.itematic.mixin.world.item.trading;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.item.trading.VillagerTrades;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(VillagerTrades.class)
public class VillagerTradesExtender {
    @WrapMethod(
        method = "bootstrap"
    )
    @Nullable
    private static Holder<VillagerTrade> doNotRunVillagerTradeDataGenerationYourLogsWillDie(BootstrapContext<VillagerTrade> context, Operation<Holder<VillagerTrade>> original) {
        return null;
    }
}
