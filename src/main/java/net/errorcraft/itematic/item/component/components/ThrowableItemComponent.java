package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.use.provider.providers.TridentIntegerProvider;
import net.errorcraft.itematic.serialization.ItematicCodecs;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import java.util.Optional;

public record ThrowableItemComponent(float speed, float angleOffset, Optional<MinMaxBounds.Ints> drawDuration) implements ItemComponent<ThrowableItemComponent> {
    public static final Codec<ThrowableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItematicCodecs.NON_NEGATIVE_FLOAT.fieldOf("speed").forGetter(ThrowableItemComponent::speed),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(ThrowableItemComponent::angleOffset),
        MinMaxBounds.Ints.CODEC.optionalFieldOf("draw_duration").forGetter(ThrowableItemComponent::drawDuration)
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
                .useFor(TridentIntegerProvider.INSTANCE)
                .animation(ItemUseAnimation.TRIDENT)
                .build(),
            new ThrowableItemComponent(speed, angleOffset, Optional.of(MinMaxBounds.Ints.atLeast(minDrawDuration)))
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
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.drawDuration.isPresent()) {
            return ItemResult.PASS;
        }

        this.createEntity(world, user, stack, stackExchanger);
        return ItemResult.SUCCEED;
    }

    @Override
    public boolean stopUsing(ItemStack stack, Level world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        if (this.drawDuration.filter(drawDuration -> drawDuration.matches(usedTicks)).isPresent()) {
            this.createEntity(world, user, stack, stackExchanger);
            if (user instanceof Player player) {
                player.awardStat(Stats.ITEM_USED.itematic$getOrCreateStat(stack.getItemHolder()));
            }

            return true;
        }

        return false;
    }

    private void createEntity(Level world, LivingEntity user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (world instanceof ServerLevel serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.TOOL, stack)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(ItematicContextParameters.INTERACTED_POSITION, user.getEyePosition().add(0.0d, -0.1d, 0.0d))
                .build();
            this.createEntity(context, serverWorld, stack);
        }
    }

    private void createEntity(ActionContext context, ServerLevel world, ItemStack stack) {
        ProjectileItemComponent projectile = stack.itematic$getBehavior(ItemComponentTypes.PROJECTILE).orElse(null);
        if (projectile == null) {
            return;
        }

        Entity projectileEntity = projectile.spawnEntity(
            context,
            PositionTarget.INTERACTED,
            this.angleOffset,
            this.speed,
            1.0f
        );
        if (projectileEntity == null) {
            return;
        }

        ActionContext spawnedContext = context.extend()
            .add(ItematicContextParameters.SPAWNED_ENTITY, projectileEntity)
            .add(ItematicContextParameters.SPAWNED_POSITION, projectileEntity.position())
            .build();
        stack.itematic$invokeEvent(ItemEvents.THROW_PROJECTILE, spawnedContext);
    }
}
