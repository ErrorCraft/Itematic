package net.errorcraft.itematic.mixin.registry;

import net.errorcraft.itematic.item.ItemUtil;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.group.entry.provider.ItemGroupEntryProviders;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trades;
import net.errorcraft.itematic.world.action.Actions;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VanillaRegistries.class)
public class BuiltinRegistriesExtender {
    @Shadow
    @Final
    private static RegistrySetBuilder BUILDER;

    static {
        BUILDER.add(Registries.ITEM, ItemUtil::bootstrap)
            .add(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProviders::bootstrap)
            .add(ItematicRegistryKeys.TRADE, Trades::bootstrap)
            .add(ItematicRegistryKeys.ACTION, Actions::bootstrap)
            .add(ItematicRegistryKeys.DISPENSE_BEHAVIOR, DispenseBehaviors::bootstrap);
    }
}
