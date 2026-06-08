package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.entity.spawn.rule.type.DiscardEntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.type.OffsetSpawnPositionEntitySpawnRule;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.placement.EntityPlacer;
import net.errorcraft.itematic.loot.condition.LocationCheckLootConditionUtil;
import net.errorcraft.itematic.mixin.item.DecorationItemAccessor;
import net.errorcraft.itematic.mixin.item.ItemAccessor;
import net.errorcraft.itematic.serialization.SetCodec;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.RailShape;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public record EntityItemComponent(EntitySpawner entity, boolean allowSpawnerModification, Set<Pass> passes) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EntitySpawner.CODEC.fieldOf("entity").forGetter(EntityItemComponent::entity),
        Codec.BOOL.optionalFieldOf("allow_spawner_modification", false).forGetter(EntityItemComponent::allowSpawnerModification),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityItemComponent::passes)
    ).apply(instance, EntityItemComponent::new));
    private static final Text RANDOM_TEXT = DecorationItemAccessor.randomText();

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return new EntityItemComponent(
            EntitySpawner.of(entity),
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemComponent of(EntitySpawner entity) {
        return new EntityItemComponent(
            entity,
            false,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemComponent of(EntitySpawner entity, boolean allowSpawnerModification) {
        return new EntityItemComponent(
            entity,
            allowSpawnerModification,
            Pass.DEFAULT_PASSES
        );
    }

    public static EntityItemComponent of(EntitySpawner entity, boolean allowSpawnerModification, Pass... passes) {
        return new EntityItemComponent(
            entity,
            allowSpawnerModification,
            Set.of(passes)
        );
    }

    public static ItemComponent<?>[] ofDispensing(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return ofDispensing(EntitySpawner.of(entity), dispenseBehaviors);
    }

    public static ItemComponent<?>[] ofDispensing(EntitySpawner entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            of(entity),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
        };
    }

    public static ItemComponent<?>[] minecart(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<Block> blocks, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return ofDispensing(
            EntitySpawner.builder(entity)
                .spawnRule(
                    DiscardEntitySpawnRule.INSTANCE,
                    InvertedLootCondition.builder(
                        LocationCheckLootConditionUtil.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.create()
                                .block(BlockPredicate.Builder.create()
                                    .tag(blocks, BlockTags.RAILS))
                        )
                    ))
                .spawnRule(OffsetSpawnPositionEntitySpawnRule.of(new Vec3d(0.0d, 0.0625d, 0.0d)))
                .spawnRule(
                    OffsetSpawnPositionEntitySpawnRule.of(new Vec3d(0.0d, 0.5d, 0.0d)),
                    LocationCheckLootConditionUtil.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.create()
                            .block(BlockPredicate.Builder.create()
                                .state(StatePredicate.Builder.create()
                                    .itematic$range(Properties.RAIL_SHAPE, RailShape.ASCENDING_EAST, RailShape.ASCENDING_SOUTH)))
                    ))
                .build(),
            dispenseBehaviors
        );
    }

    public static ItemComponent<?>[] spawnEgg(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            of(
                EntitySpawner.builder(entity)
                    .allowItemData(true)
                    .build(),
                true,
                EntityItemComponent.Pass.BLOCK,
                EntityItemComponent.Pass.FLUID
            ),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)),
            SpawnEggItemComponent.INSTANCE
        };
    }

    @Override
    public ItemComponentType<EntityItemComponent> type() {
        return ItemComponentTypes.ENTITY;
    }

    @Override
    public Codec<EntityItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.FLUID)) {
            return ItemResult.PASS;
        }

        if (world.isClient()) {
            return ItemResult.SUCCEED;
        }

        BlockHitResult blockHitResult = ItemAccessor.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return ItemResult.PASS;
        }

        ItemUsageContext itemUsageContext = new ItemUsageContext(world, user, hand, stack, blockHitResult);
        this.modifyOrPlace(itemUsageContext, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public ItemResult useOnBlock(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (this.isUnuseable(Pass.BLOCK)) {
            return ItemResult.PASS;
        }

        if (context.getWorld().isClient()) {
            return ItemResult.SUCCEED;
        }

        this.modifyOrPlace(context, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Text> builder, TooltipType type) {
        if (this.entity.entity().value() != EntityType.PAINTING) {
            return;
        }

        RegistryEntry<PaintingVariant> paintingVariant = stack.get(DataComponentTypes.PAINTING_VARIANT);
        if (paintingVariant != null) {
            paintingVariant.value().title().ifPresent(builder);
            paintingVariant.value().author().ifPresent(builder);
            builder.accept(Text.translatable("painting.dimensions", paintingVariant.value().width(), paintingVariant.value().height()));
        } else if (type.isCreative()) {
            builder.accept(RANDOM_TEXT);
        }
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private void modifyOrPlace(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (!this.tryModifyOrPlace(context, stackExchanger)) {
            return;
        }

        context.getStack().decrementUnlessCreative(
            1,
            context.getPlayer()
        );
    }

    private boolean tryModifyOrPlace(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        World world = context.getWorld();
        if (world.isClient()) {
            return false;
        }

        if (this.modifySpawner(context)) {
            return true;
        }

        ActionContext actionContext = ActionContext.builder(world)
            .stackExchanger(stackExchanger)
            .addOptional(LootContextParameters.THIS_ENTITY, context.getPlayer())
            .addOptional(LootContextParameters.ORIGIN, context.getPlayer(), Entity::getPos)
            .add(ItematicContextParameters.INTERACTED_POSITION, context.getBlockPos().toCenterPos())
            .add(LootContextParameters.TOOL, context.getStack())
            .add(ItematicContextParameters.HAND, context.getHand())
            .add(ItematicContextParameters.SIDE, context.getSide())
            .build();
        return this.place(actionContext, PositionTarget.INTERACTED) != null;
    }

    private boolean modifySpawner(ItemUsageContext context) {
        if (!this.allowSpawnerModification) {
            return false;
        }

        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        if (!state.isOf(Blocks.SPAWNER)) {
            return false;
        }

        Optional<MobSpawnerBlockEntity> blockEntity = world.getBlockEntity(pos, BlockEntityType.MOB_SPAWNER);
        if (blockEntity.isEmpty()) {
            return false;
        }

        this.modifySpawner(context, world, blockEntity.get(), pos, state);
        return true;
    }

    private void modifySpawner(ItemUsageContext context, World world, MobSpawnerBlockEntity blockEntity, BlockPos pos, BlockState state) {
        EntityType<?> type = this.entity.entityType(
            context.getStack(),
            context.getWorld().getRegistryManager()
        );
        blockEntity.setEntityType(type, world.getRandom());
        blockEntity.markDirty();
        world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
        world.emitGameEvent(
            context.getPlayer(),
            GameEvent.BLOCK_CHANGE,
            pos
        );
    }

    public Entity place(ActionContext context, PositionTarget position) {
        return EntityPlacer.of(this.entity, null)
            .place(context, position, SpawnReason.SPAWN_ITEM_USE);
    }

    public enum Pass implements StringIdentifiable {
        BLOCK("block"),
        FLUID("fluid");

        public static final Set<Pass> DEFAULT_PASSES = Set.of(BLOCK);
        public static final Codec<Pass> CODEC = StringIdentifiable.createCodec(Pass::values);

        private final String name;

        Pass(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
