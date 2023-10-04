package net.errorcraft.itematic.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.atlas.AtlasSource;
import net.minecraft.client.texture.atlas.DirectoryAtlasSource;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.BiConsumer;

public class ItematicTexturedRenderLayers {
    public static final Identifier ARMOR_MATERIALS_ID = new Identifier("armor_materials");
    public static final Identifier ARMOR_MATERIALS_ATLAS_TEXTURE = new Identifier("textures/atlas/armor_materials.png");
    public static final RenderLayer ARMOR_MATERIAL_RENDER_LAYER = RenderLayer.getArmorCutoutNoCull(ARMOR_MATERIALS_ATLAS_TEXTURE);

    private ItematicTexturedRenderLayers() {}

    public static void bootstrap(BiConsumer<Identifier, List<AtlasSource>> provider) {
        provider.accept(ARMOR_MATERIALS_ID, List.of(
            new DirectoryAtlasSource("models/armor", "models/armor/"),
            new DirectoryAtlasSource("entity/horse/armor", "entity/horse/armor/")
        ));
    }
}
