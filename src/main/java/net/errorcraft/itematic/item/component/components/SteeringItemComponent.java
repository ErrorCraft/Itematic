package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemSteerable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.world.World;

public record SteeringItemComponent(RegistryEntry<EntityType<?>> target, int damagePerUse) implements ItemComponent<SteeringItemComponent> {
    public static final Codec<SteeringItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.ENTITY_TYPE).fieldOf("target").forGetter(SteeringItemComponent::target),
        Codecs.NONNEGATIVE_INT.optionalFieldOf("damage_per_use", 1).forGetter(SteeringItemComponent::damagePerUse)
    ).apply(instance, SteeringItemComponent::new));

    @Override
    public ItemComponentType<SteeringItemComponent> type() {
        return ItemComponentTypes.STEERING;
    }

    @Override
    public Codec<SteeringItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (world.isClient()) {
            return ActionResult.PASS;
        }
        ActionContext context = ActionContext.builder((ServerWorld) world, stack, resultStackConsumer, hand)
            .entityPosition(ActionContextParameter.THIS, user)
            .build();
        if (this.apply(user, stack, context)) {
            return ActionResult.SUCCESS;
        }
        user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        return ActionResult.PASS;
    }

    public static SteeringItemComponent of(RegistryEntry<EntityType<?>> target, int damage) {
        return new SteeringItemComponent(target, damage);
    }

    private boolean apply(PlayerEntity user, ItemStack stack, ActionContext context) {
        Entity vehicle = user.getControllingVehicle();
        if (!user.hasVehicle() || !(vehicle instanceof ItemSteerable itemSteerable)) {
            return false;
        }
        if (!this.matchesEntityType(vehicle)) {
            return false;
        }
        if (!itemSteerable.consumeOnAStickItem()) {
            return false;
        }
        stack.itematic$damage(this.damagePerUse, context);
        return true;
    }

    private boolean matchesEntityType(Entity vehicle) {
        return Registries.ENTITY_TYPE.getKey(vehicle.getType())
            .map(this.target::matchesKey)
            .orElse(false);
    }
}
