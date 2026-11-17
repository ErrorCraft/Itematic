package net.errorcraft.itematic.data.client;

import net.errorcraft.itematic.client.resources.item.smithing.SmithingTemplateDisplay;
import net.errorcraft.itematic.client.resources.item.smithing.SmithingTemplateDisplays;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class SmithingTemplateDisplaysProvider extends FabricCodecDataProvider<SmithingTemplateDisplay> {
    public SmithingTemplateDisplaysProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture, PackOutput.Target.RESOURCE_PACK, "smithing_template_display", SmithingTemplateDisplay.CODEC);
    }

    @Override
    protected void configure(BiConsumer<Identifier, SmithingTemplateDisplay> provider, HolderLookup.Provider registryLookup) {
        SmithingTemplateDisplays.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Smithing Template Displays";
    }
}
