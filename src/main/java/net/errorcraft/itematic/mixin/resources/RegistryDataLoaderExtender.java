package net.errorcraft.itematic.mixin.resources;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;",
            ordinal = 0
        )
    )
    private static List<RegistryDataLoader.RegistryData<?>> addCustomWorldRegistries(List<RegistryDataLoader.RegistryData<?>> original) {
        return new ImmutableList.Builder<RegistryDataLoader.RegistryData<?>>()
            .addAll(original)
            .add(createData(Registries.ITEM, Items.CODEC))
            .add(createData(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC))
            .add(createData(ItematicRegistries.ACTION, ActionEntry.CODEC))
            .add(createData(ItematicRegistries.DISPENSE_BEHAVIOR, DispenseBehavior.CODEC))
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
    private static List<RegistryDataLoader.RegistryData<?>> addCustomSynchronizedRegistries(List<RegistryDataLoader.RegistryData<?>> original) {
        return new ImmutableList.Builder<RegistryDataLoader.RegistryData<?>>()
            .addAll(original)
            .add(createData(Registries.ITEM, Items.CODEC))
            .add(createData(ItematicRegistries.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC))
            .add(createData(ItematicRegistries.ACTION, ActionEntry.CODEC))
            .add(createData(ItematicRegistries.DISPENSE_BEHAVIOR, DispenseBehavior.CODEC))
            .build();
    }

    @Unique
    private static <T> RegistryDataLoader.RegistryData<T> createData(ResourceKey<Registry<T>> registry, Codec<T> codec) {
        return RegistryDataLoaderAccessor.RegistryDataAccessor.create(registry, codec);
    }
}
