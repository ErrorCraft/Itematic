package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public record ThrowableItemComponent(float speed, float angleOffset) implements ItemComponent {
    public static final Codec<ThrowableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.FLOAT.fieldOf("speed").forGetter(ThrowableItemComponent::speed),
        Codec.FLOAT.fieldOf("angle_offset").forGetter(ThrowableItemComponent::angleOffset)
    ).apply(instance, ThrowableItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.THROWABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (world instanceof ServerWorld serverWorld) {
            stack.itematic$getComponent(ItemComponentTypes.PROJECTILE)
                .map(c -> c.createEntity(world, user, stack, this.angleOffset, this.speed))
                .ifPresent(projectile -> {
                    world.spawnEntity(projectile);
                    ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, resultStackConsumer)
                        .entityPosition(ActionContextParameter.THIS, user)
                        .entityPosition(ActionContextParameter.TARGET, projectile);
                    stack.itematic$invokeEvent(ItemEvents.THROW_PROJECTILE, context);
                });
        }
        return ActionResult.success(world.isClient());
    }
}
