package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.stats.Stats;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ItemSteerable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public record EntityController(HolderSet<EntityType<?>> target, int damagePerUse) {
    public static final Codec<EntityController> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryCodecs.holderSet(Registries.ENTITY_TYPE).fieldOf("target").forGetter(EntityController::target),
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("damage_per_use", 1).forGetter(EntityController::damagePerUse)
    ).apply(instance, EntityController::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityController> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.holderSet(Registries.ENTITY_TYPE), EntityController::target,
        ByteBufCodecs.VAR_INT, EntityController::damagePerUse,
        EntityController::new
    );

    public boolean canControlVehicle(Entity user, Level level) {
        if (level.isClientSide()) {
            return false;
        }

        Entity vehicle = user.getControlledVehicle();
        return vehicle != null && vehicle.is(this.target);
    }

    public InteractionResult controlVehicle(Level level, Player user, InteractionHand hand, ItemStack stack) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        ItemStackExchanger stackExchanger = ItemStackExchanger.forEntity(user, stack);
        ActionContext context = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .add(LootContextParams.THIS_ENTITY, user)
            .add(LootContextParams.ORIGIN, user.position())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, hand)
            .build();
        if (tryBoostVehicle(user)) {
            stack.itematic$damage(this.damagePerUse, context);
            user.awardStat(Stats.ITEM_USED.itematic$get(stack.typeHolder()));
            return InteractionResult.SUCCESS_SERVER.heldItemTransformedTo(stackExchanger.result());
        }

        return InteractionResult.PASS;
    }

    private static boolean tryBoostVehicle(Player user) {
        Entity vehicle = user.getControlledVehicle();
        if (!user.isPassenger() || !(vehicle instanceof ItemSteerable itemSteerable)) {
            return false;
        }

        return itemSteerable.boost();
    }
}
