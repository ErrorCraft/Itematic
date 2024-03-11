package net.errorcraft.itematic.access.client.render.entity.feature;

import net.minecraft.client.render.model.BakedModelManager;

public interface WolfArmorFeatureRendererAccess {
    default void itematic$setArmorMaterialsAtlas(BakedModelManager bakery) {}
}
