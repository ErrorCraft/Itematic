package net.errorcraft.itematic.client.resources.model;

import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.cuboid.CuboidModel;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Optional;

public class FailedToLoadModels {
    public static final Identifier ITEM = Identifier.withDefaultNamespace("builtin/failed_to_load_item");
    private static final Identifier ITEM_PARENT = Identifier.withDefaultNamespace("item/generated");
    private static final Identifier ITEM_TEXTURE = Identifier.withDefaultNamespace("item/failed_to_load");
    public static final UnbakedModel ITEM_MODEL = new CuboidModel(
        null,
        null,
        null,
        null,
        new TextureSlots.Data.Builder()
            .addTexture(
                TextureSlot.LAYER0.getId(),
                new Material(ITEM_TEXTURE)
            )
            .build(),
        ITEM_PARENT
    );
    public static final ItemModel.Unbaked ITEM_MODEL_REFERENCE = new CuboidItemModelWrapper.Unbaked(
        ITEM,
        Optional.empty(),
        List.of()
    );

    private FailedToLoadModels() {}
}
