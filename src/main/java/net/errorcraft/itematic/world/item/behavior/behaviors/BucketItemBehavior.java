package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.mixin.world.item.ItemAccessor;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.item.ItemStacks;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.errorcraft.itematic.world.item.placement.EntityPlacer;
import net.errorcraft.itematic.world.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.world.level.modification.WorldModification;
import net.errorcraft.itematic.world.level.modification.modifications.DrainFluidWorldModification;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceBlockWorldModification;
import net.errorcraft.itematic.world.level.modification.modifications.PlaceFluidWorldModification;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public record BucketItemBehavior(WorldModification modification, Optional<EntitySpawner> entity) implements ItemBehavior<BucketItemBehavior> {
    public static final Codec<BucketItemBehavior> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        WorldModification.CODEC.fieldOf("modification").forGetter(BucketItemBehavior::modification),
        EntitySpawner.CODEC.optionalFieldOf("entity").forGetter(BucketItemBehavior::entity)
    ).apply(instance, BucketItemBehavior::new));

    public static ItemBehavior<?>[] drainFluid(HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(16),
            new BucketItemBehavior(
                DrainFluidWorldModification.INSTANCE,
                Optional.empty()
            ),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemBehavior<?>[] placeFluid(Holder<Fluid> fluid, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            new BucketItemBehavior(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.empty()
            ),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemBehavior<?>[] placeFluidWithEntity(Holder<Fluid> fluid, Holder<EntityType<?>> entity, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior[] {
            StackableItemBehavior.of(1),
            new BucketItemBehavior(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.of(EntitySpawner.of(entity))
            ),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemBehavior<?>[] placeBlock(Holder<Block> block, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior[] {
            StackableItemBehavior.of(1),
            new BucketItemBehavior(
                new PlaceBlockWorldModification(new SimpleBlockPicker(block), emptyingSound, items.getOrThrow(ItemIds.BUCKET)),
                Optional.empty()
            ),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    @Override
    public ItemBehaviorType<BucketItemBehavior> type() {
        return ItemBehaviorType.BUCKET;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.getPlayerPOVHitResult(level, user, this.modification().fluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ActionContext context = ActionContext.builder(level)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, user)
            .addOptional(LootContextParams.ORIGIN, user, Entity::position)
            .add(ItematicContextKeys.INTERACTED_POSITION, blockHitResult.getBlockPos().getCenter())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextKeys.HAND, hand)
            .add(ItematicContextKeys.SIDE, blockHitResult.getDirection())
            .build();
        if (this.use(context, PositionTarget.INTERACTED, !blockHitResult.isInside())) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        if (this.entity.isPresent()) {
            builder.set(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        }
    }

    public boolean use(ActionContext context, PositionTarget position, boolean mayOffset) {
        Optional<ItemStack> result = this.modification.modify(context, position, mayOffset);
        if (result.isEmpty()) {
            return false;
        }

        this.entity.ifPresent(entity -> EntityPlacer.of(entity, BucketItemBehavior::initializeBucketEntity)
            .place(context, PositionTarget.INTERACTED, EntitySpawnReason.BUCKET));
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
