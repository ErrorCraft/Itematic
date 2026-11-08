package net.errorcraft.itematic.world.entity.spawn.rule;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.tags.ItematicEntityTypeTags;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.AlignYaw;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.Discard;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.FitsInVolume;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPosition;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionByInteractedSide;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.RestsOnTopOfFace;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.SetSpawnPositionToInteractedPosition;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.errorcraft.itematic.world.level.storage.loot.predicates.SideCheckPredicate;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.advancements.predicates.entity.EntityFlagsPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.phys.Vec3;

public class EntitySpawnRuleSets {
    public static final ResourceKey<EntitySpawnRuleSet> ARMOR_STAND = create("armor_stand");
    public static final ResourceKey<EntitySpawnRuleSet> MINECART = create("minecart");
    public static final ResourceKey<EntitySpawnRuleSet> END_CRYSTAL = create("end_crystal");
    public static final ResourceKey<EntitySpawnRuleSet> PAINTING = create("painting");
    public static final ResourceKey<EntitySpawnRuleSet> CUSHION = create("cushion");
    public static final ResourceKey<EntitySpawnRuleSet> FIREWORK_ROCKET = create("firework_rocket");
    private EntitySpawnRuleSets() {}

    public static void bootstrap(BootstrapContext<EntitySpawnRuleSet> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        HolderGetter<EntityType<?>> entityTypes = context.lookup(Registries.ENTITY_TYPE);

        context.register(
            ARMOR_STAND,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    SideCheckPredicate.builder(Direction.DOWN)
                )
                .add(FitsInVolume.entityDimensions())
                .add(AlignYaw.ofFacingAway(8))
                .build()
        );
        context.register(
            MINECART,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    InvertedLootItemCondition.invert(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(
                                    BlockPredicate.Builder.block()
                                        .of(blocks, BlockTags.RAILS)
                                )
                        )
                    )
                )
                .add(OffsetSpawnPosition.of(new Vec3(0.0d, 0.0625d, 0.0d)))
                .add(
                    OffsetSpawnPosition.of(new Vec3(0.0d, 0.5d, 0.0d)),
                    LocationCheckPredicates.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.location()
                            .setBlock(
                                BlockPredicate.Builder.block()
                                    .setProperties(
                                        StatePropertiesPredicate.Builder.properties()
                                            .itematic$range(
                                                BlockStateProperties.RAIL_SHAPE,
                                                RailShape.ASCENDING_EAST,
                                                RailShape.ASCENDING_SOUTH
                                            )
                                    )
                            )
                    )
                )
                .build()
        );
        context.register(
            END_CRYSTAL,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    InvertedLootItemCondition.invert(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(
                                    BlockPredicate.Builder.block()
                                        .of(blocks, ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)
                                ),
                            new BlockPos(0, -1, 0)
                        )
                    )
                )
                .add(
                    Discard.INSTANCE,
                    InvertedLootItemCondition.invert(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(
                                    BlockPredicate.Builder.block()
                                        .of(blocks, BlockTags.AIR)
                                )
                        )
                    )
                )
                .add(
                    FitsInVolume.of(
                        false,
                        true,
                        new Vec3(1.0d, 2.0d, 1.0d)
                    )
                )
                .build()
        );
        context.register(
            PAINTING,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    SideCheckPredicate.builder(
                        Direction.DOWN,
                        Direction.UP
                    )
                )
                .build()
        );
        context.register(
            CUSHION,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    InvertedLootItemCondition.invert(
                        SideCheckPredicate.builder(
                            Direction.UP
                        )
                    )
                )
                .add(SetSpawnPositionToInteractedPosition.of(false, true, false))
                .add(RestsOnTopOfFace.entityDimensions())
                .add(
                    FitsInVolume.entityDimensionsForEntities(
                        entityTypes.getOrThrow(ItematicEntityTypeTags.CANNOT_INTERSECT_WITH_CUSHION)
                    )
                )
                .add(AlignYaw.ofFacingTowards(4))
                .build()
        );
        context.register(
            FIREWORK_ROCKET,
            EntitySpawnRuleSet.builder()
                .add(
                    Discard.INSTANCE,
                    LootItemEntityPropertyCondition.hasProperties(
                        LootContext.EntityTarget.THIS,
                        EntityPredicate.Builder.entity()
                            .flags(
                                EntityFlagsPredicate.Builder.flags()
                                    .setIsFallFlying(true)
                            )
                    )
                )
                .add(SetSpawnPositionToInteractedPosition.of(true, true, true))
                .add(OffsetSpawnPositionByInteractedSide.of(FireworkRocketItem.ROCKET_PLACEMENT_OFFSET))
                .build()
        );
    }

    private static ResourceKey<EntitySpawnRuleSet> create(String id) {
        return ResourceKey.create(ItematicRegistries.ENTITY_SPAWN_RULE_SET, Identifier.withDefaultNamespace(id));
    }
}
