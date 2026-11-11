package net.errorcraft.itematic.client.resources.item.smithing;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class SmithingTemplateDisplayManager extends SimpleJsonResourceReloadListener<SmithingTemplateDisplay> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("smithing_template_display");
    private Map<Identifier, SmithingTemplateDisplay> smithingTemplateDisplays = Map.of();

    public SmithingTemplateDisplayManager() {
        super(SmithingTemplateDisplay.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<Identifier, SmithingTemplateDisplay> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.smithingTemplateDisplays = preparations;
    }

    @Nullable
    public SmithingTemplateDisplay get(Identifier id) {
        return this.smithingTemplateDisplays.get(id);
    }
}
