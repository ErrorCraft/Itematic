package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.entity.spawn.rule.ConditionedEntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.EntitySpawnRule;
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
import net.minecraft.block.enums.RailShape;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
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
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public record EntityItemComponent(RegistryEntry<EntityType<?>> entity, List<ConditionedEntitySpawnRule> spawnRules, Optional<RegistryEntry<SoundEvent>> spawnSound, boolean allowItemData, Set<Pass> passes) implements ItemComponent<EntityItemComponent> {
    public static final Codec<EntityItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Registries.ENTITY_TYPE.getEntryCodec().fieldOf("entity").forGetter(EntityItemComponent::entity),
        ConditionedEntitySpawnRule.CODEC.listOf().optionalFieldOf("spawn_rules", List.of()).forGetter(EntityItemComponent::spawnRules),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("spawn_sound").forGetter(EntityItemComponent::spawnSound),
        Codec.BOOL.optionalFieldOf("allow_item_data", false).forGetter(EntityItemComponent::allowItemData),
        SetCodec.forEnum(Pass.CODEC).optionalFieldOf("passes", Pass.DEFAULT_PASSES).forGetter(EntityItemComponent::passes)
    ).apply(instance, EntityItemComponent::new));
    private static final Text RANDOM_TEXT = DecorationItemAccessor.randomText();

    public static Builder builder(RegistryEntry<EntityType<?>> type) {
        return new Builder(type, false);
    }

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity) {
        return new EntityItemComponent(entity, List.of(), Optional.empty(), false, Pass.DEFAULT_PASSES);
    }

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity, RegistryEntry<SoundEvent> spawnSound, ConditionedEntitySpawnRule... spawnRules) {
        return new EntityItemComponent(entity, List.of(spawnRules), Optional.of(spawnSound), false, Pass.DEFAULT_PASSES);
    }

    public static EntityItemComponent of(RegistryEntry<EntityType<?>> entity, boolean allowItemData, Pass... passes) {
        return new EntityItemComponent(entity, List.of(), Optional.empty(), allowItemData, Set.of(passes));
    }

    public static ItemComponent<?>[] from(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            of(entity),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
        };
    }

    public static ItemComponent<?>[] minecart(RegistryEntry<EntityType<?>> entity, RegistryEntryLookup<Block> blocks, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return EntityItemComponent.builder(entity)
            .spawnRule(
                DiscardEntitySpawnRule.INSTANCE,
                InvertedLootCondition.builder(
                    LocationCheckLootConditionUtil.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.create()
                            .block(BlockPredicate.Builder.create()
                                .tag(blocks, BlockTags.RAILS))
                    )
                )
            )
            .spawnRule(OffsetSpawnPositionEntitySpawnRule.of(new Vec3d(0.0d, 0.0625d, 0.0d)))
            .spawnRule(
                OffsetSpawnPositionEntitySpawnRule.of(new Vec3d(0.0d, 0.5d, 0.0d)),
                LocationCheckLootConditionUtil.builder(
                    PositionTarget.INTERACTED,
                    LocationPredicate.Builder.create()
                        .block(BlockPredicate.Builder.create()
                            .state(StatePredicate.Builder.create()
                                .itematic$range(Properties.RAIL_SHAPE, RailShape.ASCENDING_EAST, RailShape.ASCENDING_SOUTH)))
                )
            )
            .build(dispenseBehaviors);
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
        this.place(itemUsageContext, stackExchanger);
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

        this.place(context, stackExchanger);
        return ItemResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Text> builder, TooltipType type) {
        if (this.entity.value() != EntityType.PAINTING) {
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

    public EntityType<?> entityType(ItemStack stack, RegistryWrapper.WrapperLookup registries) {
        if (!this.allowItemData) {
            return this.entity.value();
        }

        NbtComponent entityData = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        EntityType<?> entityType = entityData.getRegistryValueOfId(registries, RegistryKeys.ENTITY_TYPE);
        if (entityType == null) {
            return this.entity.value();
        }

        return entityType;
    }

    private boolean isUnuseable(Pass pass) {
        return !this.passes.contains(pass);
    }

    private void place(ItemUsageContext context, ItemStackExchanger stackExchanger) {
        if (!(context.getWorld() instanceof ServerWorld world)) {
            return;
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
        this.place(actionContext);
    }

    public Entity place(ActionContext context) {
        return EntityPlacer.of(
            this.entityType(
                context.getOrDefault(LootContextParameters.TOOL, ItemStack.EMPTY),
                context.world().getRegistryManager()
            ),
            this.spawnRules,
            this.spawnSound,
            context,
            true,
            SpawnReason.SPAWN_ITEM_USE,
            null,
            this.allowItemData,
            PositionTarget.INTERACTED
        ).place();
    }

    public static class Builder {
        private final RegistryEntry<EntityType<?>> entity;
        private final List<ConditionedEntitySpawnRule> spawnRules = new ArrayList<>();
        private RegistryEntry<SoundEvent> spawnSound;
        private final boolean allowItemData;
        private Set<Pass> passes = Pass.DEFAULT_PASSES;

        private Builder(RegistryEntry<EntityType<?>> entity, boolean allowItemData) {
            this.entity = entity;
            this.allowItemData = allowItemData;
        }

        public EntityItemComponent build() {
            return new EntityItemComponent(
                this.entity,
                this.spawnRules,
                Optional.ofNullable(this.spawnSound),
                this.allowItemData,
                this.passes
            );
        }

        public ItemComponent<?>[] build(RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
            return new ItemComponent<?>[] {
                this.build(),
                DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM))
            };
        }

        public Builder spawnRule(EntitySpawnRule<?> rule) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule));
            return this;
        }

        public Builder spawnRule(EntitySpawnRule<?> rule, LootCondition.Builder condition) {
            this.spawnRules.add(ConditionedEntitySpawnRule.of(rule, condition.build()));
            return this;
        }

        public Builder spawnSound(RegistryEntry<SoundEvent> spawnSound) {
            this.spawnSound = spawnSound;
            return this;
        }

        public Builder passes(Pass... passes) {
            this.passes = Set.of(passes);
            return this;
        }
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
