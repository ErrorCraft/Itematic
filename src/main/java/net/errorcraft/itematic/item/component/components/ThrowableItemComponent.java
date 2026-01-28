package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.use.provider.providers.TridentIntegerProvider;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import java.util.Optional;

public record ThrowableItemComponent(float speed, float angleOffset, Optional<NumberRange.IntRange> drawDuration) implements ItemComponent<ThrowableItemComponent> {
    public static final Codec<ThrowableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("speed").forGetter(ThrowableItemComponent::speed),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(ThrowableItemComponent::angleOffset),
        NumberRange.IntRange.CODEC.optionalFieldOf("draw_duration").forGetter(ThrowableItemComponent::drawDuration)
    ).apply(instance, ThrowableItemComponent::new));

    public static ThrowableItemComponent of() {
        return new ThrowableItemComponent(0.0f, 0.0f, Optional.empty());
    }

    public static ThrowableItemComponent of(float speed) {
        return new ThrowableItemComponent(speed, 0.0f, Optional.empty());
    }

    public static ThrowableItemComponent of(float speed, float angleOffset) {
        return new ThrowableItemComponent(speed, angleOffset, Optional.empty());
    }

    public static ItemComponent<?>[] trident(float speed, float angleOffset, int minDrawDuration) {
        return new ItemComponent<?>[] {
            UseableItemComponent.builder()
                .ticks(TridentIntegerProvider.INSTANCE)
                .animation(UseAction.SPEAR)
                .build(),
            new ThrowableItemComponent(speed, angleOffset, Optional.of(NumberRange.IntRange.atLeast(minDrawDuration)))
        };
    }

    @Override
    public ItemComponentType<ThrowableItemComponent> type() {
        return ItemComponentTypes.THROWABLE;
    }

    @Override
    public Codec<ThrowableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (this.drawDuration.isPresent()) {
            return ItemResult.PASS;
        }
        return this.createEntity(world, user, stack, resultStackConsumer);
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        if (this.drawDuration.filter(drawDuration -> drawDuration.test(usedTicks)).isPresent()) {
            this.createEntity(world, user, stack, resultStackConsumer);
            if (user instanceof PlayerEntity player) {
                player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
            }
        }
    }

    private ItemResult createEntity(World world, LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer)
                .entityPosition(ActionContextParameter.THIS, user)
                .position(ActionContextParameter.TARGET, user.getEyePos().add(0.0d, -0.1d, 0.0d))
                .build();
            this.createEntity(context);
        }

        return ItemResult.SUCCEED;
    }

    private void createEntity(ActionContext context) {
        context.stack().itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(context, ActionContextParameter.TARGET, this.angleOffset, this.speed, 1.0f))
            .ifPresent(projectile -> {
                context.world().spawnEntity(projectile);
                ActionContext projectileContext = context.builderForCopy()
                    .entityPosition(ActionContextParameter.TARGET, projectile)
                    .build();
                context.stack().itematic$invokeEvent(ItemEvents.THROW_PROJECTILE, projectileContext);
            });
    }
}
