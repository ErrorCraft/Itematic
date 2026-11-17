package net.errorcraft.itematic.world.level.modification;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.Optional;

public interface WorldModification {
    Codec<WorldModification> CODEC = ItematicBuiltInRegistries.WORLD_MODIFICATION_TYPE.byNameCodec()
        .dispatch(WorldModification::type, Type::codec);
    StreamCodec<RegistryFriendlyByteBuf, WorldModification> STREAM_CODEC = ByteBufCodecs.registry(ItematicRegistries.WORLD_MODIFICATION_TYPE)
        .dispatch(WorldModification::type, Type::streamCodec);

    Type<?> type();
    Optional<ItemStack> modify(ActionContext context, PositionTarget position, boolean mayOffset);
    ClipContext.Fluid fluidHandling();

    record Type<T extends WorldModification>(MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {}
}
