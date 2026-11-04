package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.Optional;

public record Throwable(float speed, float angleOffset, Optional<MinMaxBounds.Ints> drawDuration) {
    public static final Codec<Throwable> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_FLOAT.fieldOf("speed").forGetter(Throwable::speed),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(Throwable::angleOffset),
        MinMaxBounds.Ints.CODEC.optionalFieldOf("draw_duration").forGetter(Throwable::drawDuration)
    ).apply(instance, Throwable::new));
    public static final StreamCodec<ByteBuf, Throwable> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, Throwable::speed,
        ByteBufCodecs.FLOAT, Throwable::angleOffset,
        MinMaxBounds.Ints.STREAM_CODEC.apply(ByteBufCodecs::optional), Throwable::drawDuration,
        Throwable::new
    );

    public boolean tryUseInstantly(Level level, Player user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.drawDuration.isPresent()) {
            return false;
        }

        this.createEntity(level, user, stack, stackExchanger);
        return true;
    }

    public boolean stopUsing(ItemStack stack, Level level, LivingEntity user, int usedTicks, ItemStackExchanger stackExchanger) {
        if (!this.usedForCorrectTime(usedTicks)) {
            return false;
        }

        if (user instanceof Player player) {
            player.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
        }

        this.createEntity(level, user, stack, stackExchanger);
        return true;
    }

    private boolean usedForCorrectTime(int usedTicks) {
        return this.drawDuration.map(drawDuration -> drawDuration.matches(usedTicks))
            .orElse(false);
    }

    private void createEntity(Level level, LivingEntity user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.TOOL, stack)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(ItematicContextKeys.INTERACTED_POSITION, user.getEyePosition().add(0.0d, -0.1d, 0.0d))
                .build();
            this.createEntity(context, stack);
        }
    }

    private void createEntity(ActionContext context, ItemStack stack) {
        Entity projectile = ProjectileComponent.spawn(context, PositionTarget.INTERACTED, this.angleOffset, this.speed, 1.0f);
        if (projectile == null) {
            return;
        }

        ActionContext spawnedContext = context.extend()
            .add(ItematicContextKeys.SPAWNED_ENTITY, projectile)
            .add(ItematicContextKeys.SPAWNED_POSITION, projectile.position())
            .build();
        stack.itematic$invokeEvent(ItemEvent.THROW_PROJECTILE, spawnedContext);
    }
}
