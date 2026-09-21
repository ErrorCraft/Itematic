package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.errorcraft.itematic.world.level.modification.WorldModificationType;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public record NoneWorldModification(Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<NoneWorldModification> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Item.CODEC.fieldOf("transforms_into").forGetter(NoneWorldModification::transformsInto)
    ).apply(instance, NoneWorldModification::new));

    @Override
    public WorldModificationType<?> type() {
        return WorldModificationType.NONE;
    }

    @Override
    public Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset) {
        return Optional.of(new ItemStack(this.transformsInto));
    }

    @Override
    public ClipContext.Fluid fluidHandling() {
        return ClipContext.Fluid.SOURCE_ONLY;
    }
}
