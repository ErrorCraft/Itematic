package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public record ConsumableItemComponent(Optional<RegistryEntry<Item>> resultItem) implements ItemComponent<ConsumableItemComponent> {
    public static final Codec<ConsumableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("result_item").forGetter(ConsumableItemComponent::resultItem)
    ).apply(instance, ConsumableItemComponent::new));

    @Override
    public ItemComponentType<ConsumableItemComponent> type() {
        return ItemComponentTypes.CONSUMABLE;
    }

    @Override
    public Codec<ConsumableItemComponent> codec() {
        return CODEC;
    }

    public void consume(LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer, World world, Hand hand) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            stack.itematic$invokeEvent(ItemEvents.CONSUME_ITEM, context);
        }
        this.resultItem.map(ItemStack::new)
            .map(resultStack -> ItemUsage.exchangeStack(stack, player, resultStack))
            .ifPresentOrElse(resultStackConsumer::set, () -> stack.decrementUnlessCreative(1, user));
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
    }

    public static ConsumableItemComponent of(RegistryEntry<Item> resultItem) {
        return new ConsumableItemComponent(Optional.ofNullable(resultItem));
    }
}
