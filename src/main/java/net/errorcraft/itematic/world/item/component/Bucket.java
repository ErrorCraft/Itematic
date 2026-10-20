package net.errorcraft.itematic.world.item.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Bucketable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public record Bucket(WorldModification modification, Optional<EntitySpawner> entity) {
    public static final Codec<Bucket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        WorldModification.CODEC.fieldOf("modification").forGetter(Bucket::modification),
        EntitySpawner.CODEC.optionalFieldOf("entity").forGetter(Bucket::entity)
    ).apply(instance, Bucket::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Bucket> STREAM_CODEC = StreamCodec.composite(
        WorldModification.STREAM_CODEC, Bucket::modification,
        EntitySpawner.STREAM_CODEC.apply(ByteBufCodecs::optional), Bucket::entity,
        Bucket::new
    );

    public InteractionResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(level, user, this.modification().fluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, user)
            .addOptional(LootContextParams.ORIGIN, user, Entity::position)
            .add(ItematicContextKeys.INTERACTED_POSITION, Vec3.atCenterOf(blockHitResult.getBlockPos()))
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, hand)
            .add(ItematicContextKeys.SIDE, blockHitResult.getDirection())
            .build();
        if (this.use(context, PositionTarget.INTERACTED, !blockHitResult.isInside(), blockHitResult.getLocation().y())) {
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    public void addComponents(DataComponentMap.Builder builder) {
        if (this.entity.isPresent()) {
            builder.set(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        }
    }

    public boolean use(ActionContext context, PositionTarget position, boolean mayOffset, @Nullable Double exactY) {
        Optional<ItemStack> result = this.modification.modify(context, position, mayOffset);
        if (result.isEmpty()) {
            return false;
        }

        this.entity.map(entity -> EntityPlacer.of(entity, Bucket::initializeBucketEntity))
            .ifPresent(placer -> placer.place(context, PositionTarget.INTERACTED, exactY, EntitySpawnReason.BUCKET));
        ItemStack stack = context.get(LootContextParams.TOOL, ItemStacks::fromItemInstance);
        if (!ItemStacks.isNullOrEmpty(stack)) {
            stack.consume(
                1,
                context.get(LootContextParams.THIS_ENTITY, LivingEntity.class)
            );
        }

        context.exchangeStack(result.get());
        return true;
    }

    private static void initializeBucketEntity(Entity entity, ItemStack stack) {
        if (entity instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY).copyTag());
            bucketable.setFromBucket(true);
        }
    }
}
