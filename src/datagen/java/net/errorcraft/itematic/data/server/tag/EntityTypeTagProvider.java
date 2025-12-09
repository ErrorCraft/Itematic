package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.entity.ItematicEntityTypeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends FabricTagProvider<EntityType<?>> {
    public EntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, RegistryKeys.ENTITY_TYPE, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        this.getOrCreateTagBuilder(ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS)
            .add(EntityTypeKeys.PIGLIN)
            .add(EntityTypeKeys.PIGLIN_BRUTE)
            .add(EntityTypeKeys.ZOMBIFIED_PIGLIN);
    }
}
