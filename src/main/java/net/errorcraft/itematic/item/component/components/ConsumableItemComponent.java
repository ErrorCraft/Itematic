package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
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
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Optional;

public record ConsumableItemComponent(Optional<RegistryEntry<Item>> resultItem) implements ItemComponent {
    public static final Codec<ConsumableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("result_item").forGetter(ConsumableItemComponent::resultItem)
    ).apply(instance, ConsumableItemComponent::new));

    @Override
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.CONSUMABLE;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (stack.mayStartUsing(world, user, hand, stack)) {
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
        return TypedActionResult.pass(stack);
    }

    public ItemStack consume(LivingEntity user, ItemStack stack) {
        if (!(user instanceof PlayerEntity player)) {
            return stack;
        }

        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.CONSUME_ITEM.trigger(serverPlayer, stack);
            ActionContext context = MutableActionContext.stackUsage(serverPlayer.getServerWorld(), stack, user.getActiveHand())
                .entityPosition(ActionContextParameter.THIS, serverPlayer);
            stack.invokeEvent(ItemEvents.CONSUME_ITEM, context);
        }

        player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        ItemStack resultItemStack = this.getResultItemStack();
        return ItemUsage.exchangeStack(stack, player, resultItemStack);
    }

    public static ConsumableItemComponent of(RegistryEntry<Item> resultItem) {
        return new ConsumableItemComponent(Optional.ofNullable(resultItem));
    }

    private ItemStack getResultItemStack() {
        if (this.resultItem.isEmpty()) {
            return ItemStack.EMPTY;
        }
        return new ItemStack(this.resultItem.get());
    }
}
