package net.errorcraft.itematic.mixin.registry;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.SerializableRegistries;
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
            target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD,
        remap = false
    )
    private static void addItemRegistryKey(CallbackInfoReturnable<Map<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>>> info, ImmutableMap.Builder<RegistryKey<? extends Registry<?>>, SerializableRegistries.Info<?>> builder) {
        add(builder, RegistryKeys.ITEM, ItemUtil.CODEC);
        add(builder, ItematicRegistryKeys.ARMOR_MATERIAL, ArmorMaterial.CODEC);
        add(builder, ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC);
        add(builder, ItematicRegistryKeys.TRADE, Trade.CODEC);
        add(builder, ItematicRegistryKeys.ACTION, ActionEntry.CODEC);
    }
}
