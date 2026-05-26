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
import net.errorcraft.itematic.world.modification.WorldModification;
import net.errorcraft.itematic.world.modification.type.DrainFluidWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceBlockWorldModification;
import net.errorcraft.itematic.world.modification.type.PlaceFluidWorldModification;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.Optional;

public record BucketItemComponent(WorldModification modification, Optional<RegistryEntry<EntityType<?>>> entity) implements ItemComponent<BucketItemComponent> {
    public static final Codec<BucketItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        WorldModification.CODEC.fieldOf("modification").forGetter(BucketItemComponent::modification),
        Registries.ENTITY_TYPE.getEntryCodec().optionalFieldOf("entity").forGetter(BucketItemComponent::entity)
    ).apply(instance, BucketItemComponent::new));

    public static ItemComponent<?>[] drainFluid(RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(16),
            new BucketItemComponent(
                DrainFluidWorldModification.INSTANCE,
                Optional.empty()
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeFluid(RegistryEntry<Fluid> fluid, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemKeys.BUCKET)),
                Optional.empty()
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeFluidWithEntity(RegistryEntry<Fluid> fluid, RegistryEntry<EntityType<?>> entity, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent[] {
            StackableItemComponent.of(1),
            new BucketItemComponent(
                new PlaceFluidWorldModification(fluid, emptyingSound, items.getOrThrow(ItemKeys.BUCKET)),
                Optional.of(entity)
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET))
        };
    }

    public static ItemComponent<?>[] placeBlock(RegistryEntry<Block> block, RegistryEntry<SoundEvent> emptyingSound, RegistryEntryLookup<Item> items, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, this.modification().fluidHandling());
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ActionContext context = ActionContext.builder(world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParameters.THIS_ENTITY, user)
            .addOptional(LootContextParameters.ORIGIN, user, Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, blockHitResult.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, stack)
            .add(ItematicContextParameters.HAND, hand)
            .add(ItematicContextParameters.SIDE, blockHitResult.getSide())
            .build();
        if (this.use(context, PositionTarget.INTERACTED_POSITION, !blockHitResult.isInsideBlock())) {
            return ItemResult.CONSUME;
        }

        return ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        if (this.entity.isPresent()) {
            builder.add(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
        }
    }

    public boolean use(ActionContext context, PositionTarget position, boolean mayOffset) {
        Optional<ItemStack> result = this.modification.modify(context, position, mayOffset);
        if (result.isEmpty()) {
            return false;
        }

        this.entity.ifPresent(entity -> EntityPlacer.of(
            entity.value(),
            context,
            false,
            SpawnReason.BUCKET,
            BucketItemComponent::initializeBucketEntity,
            true,
            PositionTarget.INTERACTED_POSITION
        ).place());
        ItemStack stack = context.get(LootContextParameters.TOOL);
        if (!ItemStackUtil.isNullOrEmpty(stack)) {
            stack.decrementUnlessCreative(
                1,
                context.get(LootContextParameters.THIS_ENTITY, LivingEntity.class)
            );
        }

        context.exchangeStack(result.get());
        return true;
    }

    private static void initializeBucketEntity(Entity entity, ItemStack stack) {
        if (entity instanceof Bucketable bucketable) {
            bucketable.copyDataFromNbt(stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT).copyNbt());
            bucketable.setFromBucket(true);
        }
    }
}
