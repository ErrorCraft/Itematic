package net.errorcraft.itematic.item.bucket.modification;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.world.RaycastContext;

import java.util.Optional;

public interface WorldModification {
    Codec<WorldModification> CODEC = ItematicRegistries.WORLD_MODIFICATION_TYPE.getCodec().dispatch(
        WorldModification::type,
        WorldModificationType::codec
    );

    WorldModificationType<?> type();
    Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset);
    RaycastContext.FluidHandling fluidHandling();
}
