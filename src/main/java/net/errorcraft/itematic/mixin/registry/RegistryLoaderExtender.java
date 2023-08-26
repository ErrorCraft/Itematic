package net.errorcraft.itematic.mixin.registry;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProvider;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(RegistryLoader.class)
public class RegistryLoaderExtender {
    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;"
        )
    )
    private static List<RegistryLoader.Entry<?>> addCustomEntries(List<RegistryLoader.Entry<?>> original) {
        return new ImmutableList.Builder<RegistryLoader.Entry<?>>()
            .addAll(original)
            .add(new RegistryLoader.Entry<>(RegistryKeys.ITEM, ItemUtil.CODEC))
            .add(new RegistryLoader.Entry<>(ItematicRegistryKeys.ARMOR_MATERIAL, ArmorMaterial.CODEC))
            .add(new RegistryLoader.Entry<>(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProvider.CODEC))
            .build();
    }
}
