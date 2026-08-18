package net.errorcraft.itematic.mixin.world.level.block;

import net.errorcraft.itematic.access.world.level.block.GrowingPlantHeadBlockAccess;
import net.errorcraft.itematic.references.ItemIds;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.WeepingVinesBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WeepingVinesBlock.class)
public class WeepingVinesBlockExtender implements GrowingPlantHeadBlockAccess {
    @Override
    public @Nullable ResourceKey<Item> itematic$stemItemId() {
        return ItemIds.WEEPING_VINES;
    }
}
