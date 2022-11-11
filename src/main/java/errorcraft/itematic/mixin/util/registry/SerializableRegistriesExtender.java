package errorcraft.itematic.mixin.util.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import errorcraft.itematic.item.ItemUtil;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.RegistryKeys;
import net.minecraft.util.registry.SerializableRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(SerializableRegistries.class)
public abstract class SerializableRegistriesExtender {
    @Shadow
    private static <E> void add(ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder, RegistryKey<? extends Registry<E>> key, Codec<E> networkCodec) {
        throw new AssertionError();
    }

    @Inject(
        method = "method_45958",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
            shift = At.Shift.BEFORE
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        remap = false
    )
    private static void addItemRegistryKey(CallbackInfoReturnable<Map<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>>> info, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder) {
        add(builder, RegistryKeys.ITEM, ItemUtil.CODEC);
    }
}
