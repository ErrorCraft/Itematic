package net.errorcraft.itematic.client.resources.item.bar;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class ItemBarStyleManager extends SimpleJsonResourceReloadListener<ItemBarStyle> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("item_bar_style");
    private Map<Identifier, ItemBarStyle> styles = Map.of();

    public ItemBarStyleManager() {
        super(ItemBarStyle.CODEC, ASSET_LISTER);
    }

    @Override
    protected void apply(Map<Identifier, ItemBarStyle> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.styles = preparations;
    }

    @Nullable
    public ItemBarStyle get(Identifier id) {
        return this.styles.get(id);
    }
}
