package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackUtil;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.type.DrainFluidWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceBlockWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceFluidWorldModification;
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

public record BucketItemComponent(WorldModification modification, Optional<EntitySpawner> entity) implements ItemComponent<BucketItemComponent> {
    public static final Codec<BucketItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        WorldModification.CODEC.fieldOf("modification").forGetter(BucketItemComponent::modification),
        EntitySpawner.CODEC.optionalFieldOf("entity").forGetter(BucketItemComponent::entity)
    ).apply(instance, BucketItemComponent::new));

    public static ItemComponent<?>[] drainFluid(HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(16),
            new BucketItemComponent(
                DrainFluidWorldModification.INSTANCE,
                Optional.empty()
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeFluid(Holder<Fluid> fluid, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemKeys.BUCKET)),
                Optional.empty()
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeFluidWithEntity(Holder<Fluid> fluid, Holder<EntityType<?>> entity, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemKeys.BUCKET)),
                Optional.of(EntitySpawner.of(entity))
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeBlock(Holder<Block> block, Holder<SoundEvent> emptyingSound, HolderGetter<Item> items, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(
                new PlaceBlockWorldModification(new SimpleBlockPicker(block), emptyingSound, items.getOrThrow(ItemKeys.BUCKET)),
                Optional.empty()
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    @Override
    public ItemComponentType<BucketItemComponent> type() {
        return ItemComponentTypes.BUCKET;
    }

    @Override
    public Codec<BucketItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(Level world, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.modification().fluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ActionContext context = ActionContext.builder(world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParams.THIS_ENTITY, user)
            .addOptional(LootContextParams.ORIGIN, user, Entity::position)
            .add(ItematicContextParameters.INTERACTED_POSITION, blockHitResult.getBlockPos().getCenter())
            .add(LootContextParams.TOOL, stack)
            .add(ItematicContextParameters.HAND, hand)
            .add(ItematicContextParameters.SIDE, blockHitResult.getDirection())
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

        this.entity.ifPresent(entity -> EntityPlacer.of(entity, BucketItemComponent::initializeBucketEntity)
            .place(context, PositionTarget.INTERACTED, EntitySpawnReason.BUCKET));
        ItemStack stack = context.get(LootContextParams.TOOL);
        if (!ItemStackUtil.isNullOrEmpty(stack)) {
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
