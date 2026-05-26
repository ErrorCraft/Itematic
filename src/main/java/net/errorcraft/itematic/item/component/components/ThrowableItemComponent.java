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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.loot.context.LootContextParameters;
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
                .useFor(TridentIntegerProvider.INSTANCE)
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.drawDuration.isPresent()) {
            return ItemResult.PASS;
        }

        this.createEntity(world, user, stack, stackExchanger);
        return ItemResult.SUCCEED;
    }

    @Override
    public boolean stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackExchanger stackExchanger) {
        if (this.drawDuration.filter(drawDuration -> drawDuration.test(usedTicks)).isPresent()) {
            this.createEntity(world, user, stack, stackExchanger);
            if (user instanceof PlayerEntity player) {
                player.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
            }

            return true;
        }

        return false;
    }

    private void createEntity(World world, LivingEntity user, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParameters.TOOL, stack)
                .add(LootContextParameters.THIS_ENTITY, user)
                .add(LootContextParameters.ORIGIN, user.getPos())
                .add(ItematicContextParameters.INTERACTED_POSITION, user.getEyePos().add(0.0d, -0.1d, 0.0d))
                .build();
            this.createEntity(context, serverWorld, stack);
        }
    }

    private void createEntity(ActionContext context, ServerWorld world, ItemStack stack) {
        ProjectileItemComponent projectile = stack.itematic$getBehavior(ItemComponentTypes.PROJECTILE).orElse(null);
        if (projectile == null) {
            return;
        }

        Entity projectileEntity = projectile.createEntity(
            context,
            PositionTarget.INTERACTED_POSITION,
            this.angleOffset,
            this.speed,
            1.0f
        );
        if (projectileEntity == null) {
            return;
        }

        world.spawnEntity(projectileEntity);
        ActionContext projectileContext = context.extend()
            .add(ItematicContextParameters.TARGET_ENTITY, projectileEntity)
            .build();
        stack.itematic$invokeEvent(ItemEvents.THROW_PROJECTILE, projectileContext);
    }
}
