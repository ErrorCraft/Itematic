package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record ConsumableComponent(int amount) implements UseableListener.Finished {
    public static final Codec<ConsumableComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.optionalFieldOf("amount", 1).forGetter(ConsumableComponent::amount)
    ).apply(instance, ConsumableComponent::new));
    public static final StreamCodec<ByteBuf, ConsumableComponent> STREAM_CODEC = ByteBufCodecs.VAR_INT.map(
        ConsumableComponent::new,
        ConsumableComponent::amount
    );

    @Override
    public void itematic$onFinishedUsing(ItemStack stack, ItemStackExchanger stackExchanger, Level level, LivingEntity user) {
        if (user instanceof Player player) {
            consumeForPlayer(player, stack);
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, user.getUsedItemHand())
                .build();
            stack.itematic$invokeEvent(ItemEvent.CONSUME_ITEM, context);
        }

        user.gameEvent(stack.getUseAnimation() == ItemUseAnimation.DRINK ? GameEvent.DRINK : GameEvent.EAT);
        stack.consume(this.amount, user);
    }

    private static void consumeForPlayer(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }
    }
}
