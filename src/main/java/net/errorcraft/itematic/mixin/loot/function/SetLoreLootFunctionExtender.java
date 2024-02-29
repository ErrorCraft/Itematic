package net.errorcraft.itematic.mixin.loot.function;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetLoreLootFunction;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Function;

@Mixin(SetLoreLootFunction.class)
public class SetLoreLootFunctionExtender {
    @ModifyArg(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;map(Ljava/util/function/Function;)Ljava/util/stream/Stream;",
            ordinal = 1
        )
    )
    private <T, R> Function<Text, String> mapToJsonStringUseDynamicRegistry(Function<? super T, ? extends R> mapper, @Local(argsOnly = true) LootContext context) {
        return text -> TextUtil.toJsonString(text, context.getWorld().getRegistryManager());
    }
}
