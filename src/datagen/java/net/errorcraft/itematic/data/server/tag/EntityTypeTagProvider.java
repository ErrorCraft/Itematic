package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.tags.ItematicEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends FabricTagsProvider<EntityType<?>> {
    public EntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.ENTITY_TYPE, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.builder(ItematicEntityTypeTags.CANNOT_INTERSECT_WITH_CUSHION)
            .add(EntityTypeIds.CUSHION);
    }
}
