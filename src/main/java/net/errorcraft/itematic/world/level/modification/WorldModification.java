package net.errorcraft.itematic.world.level.modification;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import java.util.Optional;

public interface WorldModification {
    Codec<WorldModification> CODEC = ItematicBuiltInRegistries.WORLD_MODIFICATION_TYPE.byNameCodec().dispatch(
        WorldModification::type,
        WorldModificationType::codec
    );

    WorldModificationType<?> type();
    Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset);
    ClipContext.Fluid fluidHandling();
}
