package errorcraft.itematic.mixin.util.registry;

import com.google.common.collect.ImmutableMap;
import errorcraft.itematic.item.ItemUtil;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SerializableRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(SerializableRegistries.class)
public class SerializableRegistriesExtender {
    @Inject(
        method = "method_45958",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;",
            shift = At.Shift.BY,
            by = 2
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void addItemRegistryKey(CallbackInfoReturnable<Map<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>>> info, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder) {
        SerializableRegistriesAccessor.add(builder, Registry.ITEM_KEY, ItemUtil.CODEC);
    }
}
