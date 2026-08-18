package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.GrowingPlantHeadBlockAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GrowingPlantHeadBlock.class)
public class GrowingPlantHeadBlockExtender implements GrowingPlantHeadBlockAccess {
    @Unique
    @Nullable
    private ResourceKey<Item> stemItemId;

    @Override
    public @Nullable ResourceKey<Item> itematic$stemItemId() {
        return this.stemItemId;
    }

    @Override
    public void itematic$setStemItemId(ResourceKey<Item> stemItemId) {
        this.stemItemId = stemItemId;
    }
}
