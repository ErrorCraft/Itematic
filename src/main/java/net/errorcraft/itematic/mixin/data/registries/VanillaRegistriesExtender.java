package net.errorcraft.itematic.mixin.data.registries;

import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.village.trade.Trades;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.item.Items;
import net.errorcraft.itematic.world.item.group.entry.ItemGroupEntryProviders;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VanillaRegistries.class)
public class VanillaRegistriesExtender {
    @Shadow
    @Final
    private static RegistrySetBuilder BUILDER;

    static {
        BUILDER.add(Registries.ITEM, Items::bootstrap)
            .add(ItematicRegistryKeys.ITEM_GROUP_ENTRY_PROVIDER, ItemGroupEntryProviders::bootstrap)
            .add(ItematicRegistryKeys.TRADE, Trades::bootstrap)
            .add(ItematicRegistryKeys.ACTION, Actions::bootstrap)
            .add(ItematicRegistryKeys.DISPENSE_BEHAVIOR, DispenseBehaviors::bootstrap);
    }
}
