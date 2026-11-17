package net.errorcraft.itematic.world.level.storage.loot.providers.number.ints;

import net.errorcraft.itematic.core.component.provider.number.ints.providers.OminousBottleAmplifierProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;

public class ItematicContextIntProviders {
    public static final ResourceKey<ContextIntProvider> CONSUMABLE_OMINOUS_BOTTLE_AMPLIFIER = createKey("consumable/ominous_bottle_amplifier");
    public static final ResourceKey<ContextIntProvider> FISHING_EXPERIENCE = createKey("fishing/experience");

    private ItematicContextIntProviders() {}

    public static void bootstrap(BootstrapContext<ContextIntProvider> context) {
        context.register(
            CONSUMABLE_OMINOUS_BOTTLE_AMPLIFIER,
            DataComponentValue.of(
                OminousBottleAmplifierProvider.INSTANCE,
                LootContextParams.TOOL
            )
        );
        context.register(
            FISHING_EXPERIENCE,
            new UniformGenerator(
                ContextIntProviders.exactly(1),
                ContextIntProviders.exactly(6)
            )
        );
    }

    private static ResourceKey<ContextIntProvider> createKey(String name) {
        return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, Identifier.withDefaultNamespace(name));
    }
}
