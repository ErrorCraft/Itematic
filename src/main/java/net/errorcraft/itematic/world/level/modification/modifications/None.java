package net.errorcraft.itematic.world.level.modification.modifications;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public record None(Holder<Item> transformsInto) implements WorldModification {
    public static final MapCodec<None> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Item.CODEC.fieldOf("transforms_into").forGetter(None::transformsInto)
    ).apply(instance, None::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, None> STREAM_CODEC = Item.STREAM_CODEC.map(
        None::new,
        None::transformsInto
    );
    public static final Type<None> TYPE = new Type<>(CODEC, STREAM_CODEC);

    @Override
    public Type<?> type() {
        return TYPE;
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
