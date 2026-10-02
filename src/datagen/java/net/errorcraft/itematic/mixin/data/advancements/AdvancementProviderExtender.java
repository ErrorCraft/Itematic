package net.errorcraft.itematic.mixin.data.advancements;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AdvancementProvider.class)
public class AdvancementProviderExtender {
    @WrapMethod(
        method = "run"
    )
    private void doNotRunAdvancementDataGenerationYourLogsWillDie(BootstrapContext<Advancement> output, Operation<Void> original) {}
}
