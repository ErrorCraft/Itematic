package net.errorcraft.itematic.mixin.registry;

import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.armor.ArmorMaterials;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviders;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trades;
import net.errorcraft.itematic.world.action.Actions;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinRegistries.class)
public class BuiltinRegistriesExtender {
    @Shadow
    @Final
    private static RegistryBuilder REGISTRY_BUILDER;

    @Inject(
        method = "<clinit>",
        at = @At("TAIL")
    )
    private static void initialiseCustomRegistries(CallbackInfo info) {
        REGISTRY_BUILDER.addRegistry(RegistryKeys.ITEM, ItemUtil::bootstrap)
            .addRegistry(ItematicRegistryKeys.ARMOR_MATERIAL, ArmorMaterials::bootstrap)
            .addRegistry(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProviders::bootstrap)
            .addRegistry(ItematicRegistryKeys.TRADE, Trades::bootstrap)
            .addRegistry(ItematicRegistryKeys.ACTION, Actions::bootstrap);
    }
}
