package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.util.context.ItematicContextKeySets;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;

import java.util.Optional;

public record GliderFuel(ResolvableFloat constantlyAddedSpeed, ResolvableFloat targetSpeed, ResolvableFloat targetSpeedFactor) {
    public static final Codec<GliderFuel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResolvableFloat.CODEC.fieldOf("constantly_added_speed").forGetter(GliderFuel::constantlyAddedSpeed),
        ResolvableFloat.CODEC.fieldOf("target_speed").forGetter(GliderFuel::targetSpeed),
        ResolvableFloat.CODEC.fieldOf("target_speed_factor").forGetter(GliderFuel::targetSpeedFactor)
    ).apply(instance, GliderFuel::new));
    public static final StreamCodec<ByteBuf, GliderFuel> STREAM_CODEC = StreamCodec.composite(
        ResolvableFloat.STREAM_CODEC, GliderFuel::constantlyAddedSpeed,
        ResolvableFloat.STREAM_CODEC, GliderFuel::targetSpeed,
        ResolvableFloat.STREAM_CODEC, GliderFuel::targetSpeedFactor,
        GliderFuel::new
    );

    public boolean canBoost(Player user) {
        return user.isFallFlying();
    }

    public InteractionResult boost(Player user, Level level, ItemStack stack) {
        if (!this.canBoost(user)) {
            return InteractionResult.PASS;
        }

        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        LootContext context = new LootContext.Builder(
            new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.THIS_ENTITY, user)
                .withParameter(LootContextParams.ORIGIN, user.position())
                .withParameter(LootContextParams.TOOL, stack)
                .create(ItematicContextKeySets.GLIDER_BOOST)
        ).create(Optional.empty());
        FireworkRocketEntity fireworkRocket = new FireworkRocketEntity(level, stack, user);
        fireworkRocket.itematic$setGliderFuel(this.compute(context));
        Projectile.spawnProjectile(fireworkRocket, serverLevel, stack);
        stack.consume(1, user);
        user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        return InteractionResult.SUCCESS;
    }

    private Computed compute(LootContext context) {
        return new Computed(
            this.constantlyAddedSpeed.get(context, Computed.DEFAULT.constantlyAddedSpeed),
            this.targetSpeed.get(context, Computed.DEFAULT.targetSpeed),
            this.targetSpeedFactor.get(context, Computed.DEFAULT.targetSpeedFactor)
        );
    }

    public record Computed(float constantlyAddedSpeed, float targetSpeed, float targetSpeedFactor) {
        public static final Computed DEFAULT = new Computed(0.1f, 1.5f, 0.5f);
        public static final StreamCodec<ByteBuf, Computed> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, Computed::constantlyAddedSpeed,
            ByteBufCodecs.FLOAT, Computed::targetSpeed,
            ByteBufCodecs.FLOAT, Computed::targetSpeedFactor,
            Computed::new
        );

        public Computed(float constantlyAddedSpeed, float targetSpeed, float targetSpeedFactor) {
            this.constantlyAddedSpeed = Math.max(constantlyAddedSpeed, 0.0f);
            this.targetSpeed = Math.max(targetSpeed, 0.0f);
            this.targetSpeedFactor = Math.clamp(targetSpeedFactor, 0.0f, 1.0f);
        }
    }
}
