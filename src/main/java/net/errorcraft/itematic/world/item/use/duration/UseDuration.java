package net.errorcraft.itematic.world.item.use.duration;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.core.component.ItematicDataComponents;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.world.item.use.duration.provider.UseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.Constant;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.IntFunction;

public record UseDuration(UseDurationProvider provider) {
    private static final Codec<UseDuration> FULL_CODEC = UseDurationProvider.CODEC.xmap(
        UseDuration::new,
        UseDuration::provider
    );
    public static final Codec<UseDuration> CODEC = Codec.withAlternative(
        FULL_CODEC,
        ExtraCodecs.POSITIVE_INT,
        UseDuration::new
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, UseDuration> STREAM_CODEC = UseDurationProvider.STREAM_CODEC.map(
        UseDuration::new,
        UseDuration::provider
    );
    public static final int NONE = 0;
    public static final int INSTANT = 0;
    public static final int INDEFINITE = -1;

    public UseDuration(int ticks) {
        this(new Constant(ticks));
    }

    public int ticks(ItemStack stack, LivingEntity user) {
        return this.provider.get(stack, user).orElse(INSTANT);
    }

    public boolean canUse(ItemStack stack, Pass pass) {
        return stack.getOrDefault(ItematicDataComponents.USE_PASSES, Pass.EMPTY)
            .contains(pass);
    }

    public InteractionResult tryStartUsing(Level level, ItemStack stack, LivingEntity user, InteractionHand hand) {
        if (!Consumables.DEFAULT_FOOD.canConsume(user, stack)) {
            return InteractionResult.FAIL;
        }

        OptionalInt possibleTicks = this.provider.get(stack, user);
        if (possibleTicks.isEmpty()) {
            return InteractionResult.FAIL;
        }

        int ticks = possibleTicks.getAsInt();
        switch (ticks) {
            case INSTANT -> {
                ItemStack result = stack.finishUsingItem(level, user);
                return InteractionResult.CONSUME.heldItemTransformedTo(result);
            }
            case INDEFINITE -> user.startUsingItem(hand);
            default -> user.itematic$startUsingItem(hand, ticks);
        }
        return InteractionResult.CONSUME;
    }

    public enum Pass implements StringRepresentable {
        NORMAL(0, "normal"),
        BLOCK(1, "block"),
        ENTITY(2, "entity");

        public static final Set<Pass> EMPTY = Set.of();
        public static final Set<Pass> DEFAULT = Set.of(NORMAL);
        public static final Codec<Pass> CODEC = StringRepresentable.fromEnum(Pass::values);
        public static final Codec<Set<Pass>> SET_CODEC = SetCodec.forEnum(UseDuration.Pass.CODEC);
        private static final IntFunction<Pass> BY_ID = ByIdMap.continuous(
            Pass::id,
            values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
        );
        public static final StreamCodec<ByteBuf, Pass> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Pass::id);
        public static final StreamCodec<ByteBuf, Set<Pass>> SET_STREAM_CODEC = ByteBufCodecs.collection(HashSet::new, STREAM_CODEC);

        private final int id;
        private final String name;

        Pass(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public int id() {
            return this.id;
        }
    }
}
