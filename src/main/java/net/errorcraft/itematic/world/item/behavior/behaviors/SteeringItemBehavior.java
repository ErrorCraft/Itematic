package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record SteeringItemBehavior(Holder<EntityType<?>> target, int damagePerUse) implements ItemBehavior<SteeringItemBehavior> {
    public static final Codec<SteeringItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.create(Registries.ENTITY_TYPE).fieldOf("target").forGetter(SteeringItemBehavior::target),
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_use", 1).forGetter(SteeringItemBehavior::damagePerUse)
    ).apply(instance, SteeringItemBehavior::new));

    public static SteeringItemBehavior of(Holder<EntityType<?>> target, int damage) {
        return new SteeringItemBehavior(target, damage);
    }

    @Override
    public ItemBehaviorType<SteeringItemBehavior> type() {
        return ItemBehaviorType.STEERING;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (level.isClientSide()) {
            return ItemResult.PASS;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, hand)
            .build();
        if (this.apply(user, stack, context)) {
            return ItemResult.SUCCEED;
        }

        user.awardStat(Stats.ITEM_USED.itematic$get(stack.getItemHolder()));
        return ItemResult.PASS;
    }

    private boolean apply(Player user, ItemStack stack, ActionContext context) {
        Entity vehicle = user.getControlledVehicle();
        if (!user.isPassenger() || !(vehicle instanceof ItemSteerable itemSteerable)) {
            return false;
        }

        if (!this.matchesEntityType(vehicle)) {
            return false;
        }

        if (!itemSteerable.boost()) {
            return false;
        }

        stack.itematic$damage(this.damagePerUse, context);
        return true;
    }

    private boolean matchesEntityType(Entity vehicle) {
        return BuiltInRegistries.ENTITY_TYPE.getResourceKey(vehicle.getType())
            .map(this.target::is)
            .orElse(false);
    }
}
