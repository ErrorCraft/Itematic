package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.placement.BlockPlacer;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.item.placement.FluidPlacer;
import net.errorcraft.itematic.item.placement.Placer;
import net.errorcraft.itematic.item.placement.block.picker.BlockPicker;
import net.errorcraft.itematic.item.placement.block.picker.pickers.SimpleBlockPicker;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Bucketable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record BucketItemComponent(Optional<RegistryEntry<Fluid>> fluid, Optional<EntityTarget> entity, Optional<BlockPicker<?>> block, Optional<RegistryEntry<SoundEvent>> emptyingSound, Optional<RegistryEntry<Item>> transformsInto) implements ItemComponent<BucketItemComponent> {
    public static final Codec<BucketItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(RegistryKeys.FLUID).optionalFieldOf("fluid").forGetter(BucketItemComponent::fluid),
        EntityTarget.CODEC.optionalFieldOf("entity").forGetter(BucketItemComponent::entity),
        BlockPicker.CODEC.optionalFieldOf("block").forGetter(BucketItemComponent::block),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("emptying_sound_event").forGetter(BucketItemComponent::emptyingSound),
        RegistryFixedCodec.of(RegistryKeys.ITEM).optionalFieldOf("transforms_into").forGetter(BucketItemComponent::transformsInto)
    ).apply(instance, BucketItemComponent::new));

    public static ItemComponent<?>[] fluid(RegistryEntry<Fluid> fluid, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(16),
            new BucketItemComponent(Optional.of(fluid), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] fluid(RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(Optional.of(fluid), Optional.empty(), Optional.empty(), Optional.of(emptyingSound), Optional.of(items.getOrThrow(ItemKeys.BUCKET))),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] entity(RegistryEntry<Fluid> fluid, RegistryEntry<EntityType<?>> entity, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(Optional.of(fluid), Optional.of(EntityTarget.ofRequired(entity)), Optional.empty(), Optional.of(emptyingSound), Optional.of(items.getOrThrow(ItemKeys.BUCKET))),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] block(RegistryEntry<Block> block, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(Optional.empty(), Optional.empty(), Optional.of(new SimpleBlockPicker(block)), Optional.of(emptyingSound), Optional.of(items.getOrThrow(ItemKeys.BUCKET))),
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.getFluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        return this.place(world, user, hand, stack, stackExchanger, blockHitResult);
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        if (this.entity.isPresent()) {
            builder.add(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        }
    }

    public ItemResult place(World world, @Nullable PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger, BlockHitResult blockHitResult) {
        ItemResult result = ItemResult.PASS;
        if (this.fluid.isPresent()) {
            FluidPlacer fluidPlacer = FluidPlacer.of(stack, stackExchanger, world, blockHitResult, user, this.fluid.get(), this.emptyingSound.orElse(null));
            result = place(fluidPlacer, result);
        }

        if (this.block.isPresent()) {
            ItemUsageContext context = new ItemUsageContext(world, user, hand, stack, blockHitResult);
            BlockPlacer blockPlacer = BlockPlacer.of(context, stackExchanger, this.block.get(), false, false);
            result = place(blockPlacer, result);
        }

        result = this.tryPlaceEntity(world, user, hand, stack, blockHitResult, stackExchanger, result);
        if (result.succeeds()) {
            stack.decrement(1);
            this.transformsInto.map(ItemStack::new).ifPresent(stackExchanger::exchange);
        }

        return result;
    }

    private RaycastContext.FluidHandling getFluidHandling() {
        if (this.fluid.isEmpty()) {
            return RaycastContext.FluidHandling.NONE;
        }

        if (this.fluid.get().matchesKey(FluidKeys.EMPTY)) {
            return RaycastContext.FluidHandling.SOURCE_ONLY;
        }

        return RaycastContext.FluidHandling.NONE;
    }

    private ItemResult tryPlaceEntity(World world, @Nullable PlayerEntity user, Hand hand, ItemStack stack, BlockHitResult blockHitResult, ItemStackExchanger stackExchanger, ItemResult currentResult) {
        if (this.entity.isEmpty()) {
            return currentResult;
        }

        EntityTarget target = this.entity.get();
        if (target.requireOtherSuccessfulPlacement && !currentResult.succeeds()) {
            return currentResult;
        }

        if (world.isClient()) {
            return currentResult;
        }

        NewActionContext context = NewActionContext.builder((ServerWorld) world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParameters.THIS_ENTITY, user)
            .addOptional(LootContextParameters.ORIGIN, user, Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, blockHitResult.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, stack)
            .add(ItematicContextParameters.HAND, hand)
            .add(ItematicContextParameters.SIDE, blockHitResult.getSide())
            .build();
        EntityPlacer.of(
            target.entity.value(),
            context,
            false,
            SpawnReason.BUCKET,
            BucketItemComponent::initializeBucketEntity,
            true,
            PositionTarget.INTERACTED_POSITION
        ).place();
        return ItemResult.CONSUME;
    }

    private static ItemResult place(Placer placer, ItemResult currentResult) {
        ItemResult result = placer.place();
        return currentResult.max(result);
    }

    public static void initializeBucketEntity(Entity entity, ItemStack stack) {
        if (entity instanceof Bucketable bucketable) {
            bucketable.copyDataFromNbt(stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt());
            bucketable.setFromBucket(true);
        }
    }

    public record EntityTarget(RegistryEntry<EntityType<?>> entity, boolean requireOtherSuccessfulPlacement) {
        public static final Codec<EntityTarget> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(EntityTarget::entity),
            Codec.BOOL.optionalFieldOf("require_other_successful_placement", false).forGetter(EntityTarget::requireOtherSuccessfulPlacement)
        ).apply(instance, EntityTarget::new));

        public static EntityTarget ofRequired(RegistryEntry<EntityType<?>> entity) {
            return new EntityTarget(entity, true);
        }
    }
}
