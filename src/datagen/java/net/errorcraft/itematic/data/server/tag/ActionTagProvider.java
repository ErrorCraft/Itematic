package net.errorcraft.itematic.data.server.tag;

import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionTags;
import net.errorcraft.itematic.world.action.Actions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ActionTagProvider extends FabricTagProvider<ActionEntry> {
    public ActionTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, ItematicRegistryKeys.ACTION, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        this.getOrCreateTagBuilder(ActionTags.USE_HOE_ON_BLOCK)
            .add(Actions.TILL_DIRT)
            .add(Actions.TILL_COARSE_DIRT)
            .add(Actions.TILL_ROOTED_DIRT);
        this.getOrCreateTagBuilder(ActionTags.USE_SHOVEL_ON_BLOCK)
            .add(Actions.FLATTEN_GROUND)
            .add(Actions.EXTINGUISH_CAMPFIRE);
    }
}
