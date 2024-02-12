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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

import java.util.Optional;

public record ThrowableItemComponent(float speed, float angleOffset, Optional<NumberRange.IntRange> drawDuration, boolean useRiptideCheck) implements ItemComponent<ThrowableItemComponent> {
    public static final Codec<ThrowableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("speed").forGetter(ThrowableItemComponent::speed),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(ThrowableItemComponent::angleOffset),
        Codecs.createStrictOptionalFieldCodec(NumberRange.IntRange.CODEC, "draw_duration").forGetter(ThrowableItemComponent::drawDuration),
        Codecs.createStrictOptionalFieldCodec(Codec.BOOL, "use_riptide_check", false).forGetter(ThrowableItemComponent::useRiptideCheck)
    ).apply(instance, ThrowableItemComponent::new));

    @Override
    public ItemComponentType<ThrowableItemComponent> type() {
        return ItemComponentTypes.THROWABLE;
    }

    @Override
    public Codec<ThrowableItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!this.mayStartUsing(stack, user)) {
            return ActionResult.PASS;
        }
        return this.drawDuration.map(drawDuration -> ItemUsage.consumeHeldItem(world, user, hand).getResult())
            .orElseGet(() -> this.createEntity(world, user, stack, resultStackConsumer));
    }

    @Override
    public void stopUsing(ItemStack stack, World world, LivingEntity user, int usedTicks, int remainingUseTicks, ItemStackConsumer resultStackConsumer) {
        this.drawDuration.ifPresent(drawDuration -> {
            if (drawDuration.test(usedTicks)) {
                this.createEntity(world, user, stack, resultStackConsumer);
            }
        });
    }

    private boolean mayStartUsing(ItemStack stack, Entity user) {
        if (this.useRiptideCheck && EnchantmentHelper.getRiptide(stack) > 0 && !user.isTouchingWaterOrRain()) {
            return false;
        }
        return stack.itematic$getComponent(ItemComponentTypes.DAMAGEABLE)
            .map(c -> c.isUsable(stack))
            .orElse(true);
    }

    private ActionResult createEntity(World world, LivingEntity user, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            createEntity(context, this.angleOffset, this.speed, 1.0f);
        }
        return ActionResult.success(world.isClient());
    }

    public static boolean createEntity(ActionContext context, float angleOffset, float speed, float variation) {
        return context.stack().itematic$getComponent(ItemComponentTypes.PROJECTILE)
            .map(c -> c.createEntity(context, angleOffset, speed, variation))
            .map(projectile -> {
                context.world().spawnEntity(projectile);
                ActionContext projectileContext = context.builderForCopy()
                    .entityPosition(ActionContextParameter.TARGET, projectile)
                    .build();
                context.stack().itematic$invokeEvent(ItemEvents.THROW_PROJECTILE, projectileContext);
                return true;
            })
            .orElse(false);
    }

    public static ThrowableItemComponent of() {
        return new ThrowableItemComponent(0.0f, 0.0f, Optional.empty(), false);
    }

    public static ThrowableItemComponent of(float speed) {
        return new ThrowableItemComponent(speed, 0.0f, Optional.empty(), false);
    }

    public static ThrowableItemComponent of(float speed, float angleOffset) {
        return new ThrowableItemComponent(speed, angleOffset, Optional.empty(), false);
    }

    public static ThrowableItemComponent trident(float speed, float angleOffset, int minDrawDuration) {
        return new ThrowableItemComponent(speed, angleOffset, Optional.of(NumberRange.IntRange.atLeast(minDrawDuration)), true);
    }
}
