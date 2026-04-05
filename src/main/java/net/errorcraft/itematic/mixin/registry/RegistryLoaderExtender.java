package net.errorcraft.itematic.mixin.registry;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.registry.ActionValidator;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trade;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(RegistryLoader.class)
public class RegistryLoaderExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;",
            ordinal = 0
        )
    )
    private static List<RegistryLoader.Entry<?>> addCustomEntries(List<RegistryLoader.Entry<?>> original) {
        return new ImmutableList.Builder<RegistryLoader.Entry<?>>()
            .addAll(original)
            .add(createEntry(RegistryKeys.ITEM, ItemUtil.CODEC))
            .add(createEntry(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC))
            .add(createEntry(ItematicRegistryKeys.TRADE, Trade.CODEC))
            .add(createEntry(ItematicRegistryKeys.ACTION, ActionEntry.CODEC))
            .add(createEntry(ItematicRegistryKeys.DISPENSE_BEHAVIOR, DispenseBehavior.CODEC))
            .build();
    }

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;",
            ordinal = 1
        )
    )
    private static List<RegistryLoader.Entry<?>> addCustomNetworkEntries(List<RegistryLoader.Entry<?>> original) {
        return new ImmutableList.Builder<RegistryLoader.Entry<?>>()
            .addAll(original)
            .add(createEntry(RegistryKeys.ITEM, ItemUtil.CODEC))
            .add(createEntry(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC))
            .add(createEntry(ItematicRegistryKeys.TRADE, Trade.CODEC))
            .add(createEntry(ItematicRegistryKeys.ACTION, ActionEntry.CODEC))
            .add(createEntry(ItematicRegistryKeys.DISPENSE_BEHAVIOR, DispenseBehavior.CODEC))
            .build();
    }

    @Inject(
        method = "method_45128",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/registry/Registry;freeze()Lnet/minecraft/registry/Registry;",
            shift = At.Shift.AFTER
        )
    )
    @SuppressWarnings("unchecked")
    private static void postValidateRegistry(Map<RegistryKey<?>, Exception> exceptions, @Coerce Object loader, CallbackInfo info, @Local Registry<?> registry) {
        if (ItematicRegistryKeys.ACTION.equals(registry.getKey())) {
            ActionValidator validator = new ActionValidator((Registry<ActionEntry>) registry);
            validator.validate(exceptions);
        }
    }

    @Unique
    private static <T> RegistryLoader.Entry<T> createEntry(RegistryKey<Registry<T>> registry, Codec<T> codec) {
        return RegistryLoaderAccessor.EntryAccessor.create(registry, codec);
    }
}
