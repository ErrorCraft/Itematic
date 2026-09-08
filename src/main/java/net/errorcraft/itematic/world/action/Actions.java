package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.references.BlockIds;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.references.PotionIds;
import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.tags.ActionTags;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.world.action.actions.DamageItemAction;
import net.errorcraft.itematic.world.action.actions.DecrementItemAction;
import net.errorcraft.itematic.world.action.actions.DisplayParticleAction;
import net.errorcraft.itematic.world.action.actions.DropItemFromBlockAction;
import net.errorcraft.itematic.world.action.actions.ExchangeItemAction;
import net.errorcraft.itematic.world.action.actions.IncrementStatAction;
import net.errorcraft.itematic.world.action.actions.InvokeGameEventAction;
import net.errorcraft.itematic.world.action.actions.ModifySignAction;
import net.errorcraft.itematic.world.action.actions.PlaceBlockAction;
import net.errorcraft.itematic.world.action.actions.PlaySoundAction;
import net.errorcraft.itematic.world.action.actions.PrimeTntAction;
import net.errorcraft.itematic.world.action.actions.SequenceAction;
import net.errorcraft.itematic.world.action.actions.SetBlockStateAction;
import net.errorcraft.itematic.world.action.actions.SwingHandAction;
import net.errorcraft.itematic.world.action.actions.TransformBlockStateAction;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.errorcraft.itematic.world.item.component.BlockItemStatePropertiesBuilder;
import net.errorcraft.itematic.world.level.levelgen.blockpredicates.MatchingPropertiesBlockPredicate;
import net.errorcraft.itematic.world.level.levelgen.feature.stateproviders.ApplyPropertiesProvider;
import net.errorcraft.itematic.world.level.levelgen.feature.stateproviders.MapPropertiesProvider;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.errorcraft.itematic.world.level.storage.loot.predicates.SideCheckPredicate;
import net.errorcraft.itematic.world.phys.Vec3Provider;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.FluidPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.function.UnaryOperator;

public class Actions {
    public static final ResourceKey<ActionEntry> USE_HOE_ON_BLOCK = of("use_hoe_on_block");
    public static final ResourceKey<ActionEntry> TILL_DIRT = of("till_dirt");
    public static final ResourceKey<ActionEntry> TILL_COARSE_DIRT = of("till_coarse_dirt");
    public static final ResourceKey<ActionEntry> TILL_ROOTED_DIRT = of("till_rooted_dirt");
    public static final ResourceKey<ActionEntry> USE_SHOVEL_ON_BLOCK = of("use_shovel_on_block");
    public static final ResourceKey<ActionEntry> FLATTEN_GROUND = of("flatten_ground");
    public static final ResourceKey<ActionEntry> EXTINGUISH_CAMPFIRE = of("extinguish_campfire");
    public static final ResourceKey<ActionEntry> LIGHT_BLOCK = of("light_block");
    public static final ResourceKey<ActionEntry> DECREASE_WATER_CAULDRON_LAYER = of("decrease_water_cauldron_layer");
    public static final ResourceKey<ActionEntry> INCREASE_WATER_CAULDRON_LAYER = of("increase_water_cauldron_layer");
    public static final ResourceKey<ActionEntry> USE_POTION_ON_BLOCK = of("use_potion_on_block");
    public static final ResourceKey<ActionEntry> CONVERT_BLOCK_TO_MUD = of("convert_block_to_mud");
    public static final ResourceKey<ActionEntry> ADD_WATER_FROM_BOTTLE_TO_CAULDRON = of("add_water_from_bottle_to_cauldron");

    private Actions() {}

    public static void bootstrap(BootstrapContext<ActionEntry> registerable) {
        HolderGetter<ActionEntry> actions = registerable.lookup(ItematicRegistries.ACTION);
        HolderGetter<SoundEvent> soundEvents = registerable.lookup(Registries.SOUND_EVENT);
        HolderGetter<Block> blocks = registerable.lookup(Registries.BLOCK);
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);
        HolderGetter<Potion> potions = registerable.lookup(Registries.POTION);

        registerable.register(USE_HOE_ON_BLOCK, ActionEntry.of(
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.of(actions.getOrThrow(ActionTags.USE_HOE_ON_BLOCK)))
                .add(DamageItemAction.of(1))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.HOE_TILL), SoundSource.BLOCKS))
        ));
        registerable.register(TILL_DIRT, ActionEntry.of(
            setBlockConditions(blocks, builder -> builder.of(blocks, ItematicBlockTags.TILLABLE_INTO_FARMLAND)),
            SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockIds.FARMLAND))
        ));
        registerable.register(TILL_COARSE_DIRT, ActionEntry.of(
            setBlockConditions(blocks, builder -> builder.of(blocks, blocks.getOrThrow(BlockIds.COARSE_DIRT).value())),
            SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockIds.DIRT))
        ));
        registerable.register(TILL_ROOTED_DIRT, ActionEntry.of(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, blocks.getOrThrow(BlockIds.ROOTED_DIRT).value()))
            ),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockIds.DIRT)))
                .add(DropItemFromBlockAction.of(PositionTarget.INTERACTED, items.getOrThrow(ItemIds.HANGING_ROOTS)))
        ));
        registerable.register(USE_SHOVEL_ON_BLOCK, ActionEntry.of(
            InvertedLootItemCondition.invert(
                SideCheckPredicate.builder(Direction.DOWN)
            ),
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.of(actions.getOrThrow(ActionTags.USE_SHOVEL_ON_BLOCK)))
                .add(DamageItemAction.of(1))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
        ));
        registerable.register(FLATTEN_GROUND, ActionEntry.of(
            setBlockConditions(blocks, builder -> builder.of(blocks, ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH)),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockIds.DIRT_PATH)))
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.SHOVEL_FLATTEN), SoundSource.BLOCKS))
        ));
        registerable.register(EXTINGUISH_CAMPFIRE, ActionEntry.of(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, BlockTags.CAMPFIRES)
                        .setProperties(StatePropertiesPredicate.Builder.properties()
                            .hasProperty(BlockStateProperties.LIT, true)))
            ),
            PassingSequenceHandler.builder()
                .add(
                    TransformBlockStateAction.of(
                        PositionTarget.INTERACTED,
                        ApplyPropertiesProvider.of(
                            BlockItemStatePropertiesBuilder.create()
                                .property(BlockStateProperties.LIT, false)
                                .build()
                        )
                    )
                )
                .add(PlaySoundAction.builder(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.FIRE_EXTINGUISH), SoundSource.BLOCKS)
                    .volume(0.5f)
                    .pitch(1.8f, 3.4f)
                    .build()
                )
                .add(FirstToPassRequirementsSequenceHandler.builder()
                    .add(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BlockStateProperties.SIGNAL_FIRE, true)))
                        ),
                        campfireParticles(true)
                    )
                    .add(campfireParticles(false))
                )
        ));
        registerable.register(LIGHT_BLOCK, ActionEntry.of(
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.builder()
                    .add(
                        AllOfCondition.allOf(
                            LocationCheckPredicates.builder(
                                PositionTarget.INTERACTED,
                                LocationPredicate.Builder.location()
                                    .setBlock(BlockPredicate.Builder.block()
                                        .setProperties(StatePropertiesPredicate.Builder.properties()
                                            .hasProperty(BlockStateProperties.LIT, false)))
                            ),
                            InvertedLootItemCondition.invert(
                                LocationCheckPredicates.builder(
                                    PositionTarget.INTERACTED,
                                    LocationPredicate.Builder.location()
                                        .setBlock(BlockPredicate.Builder.block()
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BlockStateProperties.WATERLOGGED, true)))
                                )
                            )
                        ),
                        TransformBlockStateAction.of(
                            PositionTarget.INTERACTED,
                            ApplyPropertiesProvider.of(
                                BlockItemStatePropertiesBuilder.create()
                                    .property(BlockStateProperties.LIT, true)
                                    .build()
                            )
                        )
                    )
                    .add(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .of(blocks, blocks.getOrThrow(BlockIds.TNT).value()))
                        ),
                        PassingSequenceHandler.builder()
                            .add(PrimeTntAction.of(PositionTarget.INTERACTED))
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.TNT_PRIMED), SoundSource.BLOCKS))
                    )
                    .add(PlaceBlockAction.of(blocks.getOrThrow(BlockIds.FIRE), PositionTarget.INTERACTED)))
                .addOptional(SwingHandAction.of(LootContext.EntityTarget.THIS))
        ));
        registerable.register(DECREASE_WATER_CAULDRON_LAYER, ActionEntry.of(
            TransformBlockStateAction.of(
                PositionTarget.INTERACTED,
                RuleBasedStateProvider.builder()
                    .ifTrueThenProvide(
                        MatchingPropertiesBlockPredicate.of(
                            StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LayeredCauldronBlock.LEVEL, 3)
                        ),
                        ApplyPropertiesProvider.of(
                            BlockItemStatePropertiesBuilder.create()
                                .property(LayeredCauldronBlock.LEVEL, 2)
                                .build()
                        )
                    )
                    .ifTrueThenProvide(
                        MatchingPropertiesBlockPredicate.of(
                            StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LayeredCauldronBlock.LEVEL, 2)
                        ),
                        ApplyPropertiesProvider.of(
                            BlockItemStatePropertiesBuilder.create()
                                .property(LayeredCauldronBlock.LEVEL, 1)
                                .build()
                        )
                    )
                    .ifTrueThenProvide(
                        MatchingPropertiesBlockPredicate.of(
                            StatePropertiesPredicate.Builder.properties()
                                .hasProperty(LayeredCauldronBlock.LEVEL, 1)
                        ),
                        BlockStateProvider.simple(blocks.getOrThrow(BlockIds.CAULDRON).value())
                    )
                    .build()
            )
        ));
        registerable.register(INCREASE_WATER_CAULDRON_LAYER, ActionEntry.of(
            TransformBlockStateAction.of(
                PositionTarget.INTERACTED,
                RuleBasedStateProvider.builder()
                    .ifTrueThenProvide(
                        net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(
                            blocks.getOrThrow(BlockIds.CAULDRON).value()
                        ),
                        BlockStateProvider.simple(
                            blocks.getOrThrow(BlockIds.WATER_CAULDRON).value()
                                .defaultBlockState()
                                .setValue(LayeredCauldronBlock.LEVEL, LayeredCauldronBlock.MIN_FILL_LEVEL)
                        )
                    )
                    .ifTrueThenProvide(
                        net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.matchesBlocks(
                            blocks.getOrThrow(BlockIds.WATER_CAULDRON).value()
                        ),
                        MapPropertiesProvider.builder()
                            .add(
                                LayeredCauldronBlock.LEVEL,
                                level -> level.map(1, 2).map(2, 3)
                            )
                            .build()
                    )
                    .build()
            )
        ));
        registerable.register(USE_POTION_ON_BLOCK, ActionEntry.of(
            SequenceAction.of(
                FirstToPassRequirementsSequenceHandler.of(
                    actions.getOrThrow(ActionTags.USE_POTION_ON_BLOCK)
                )
            )
        ));
        registerable.register(CONVERT_BLOCK_TO_MUD, ActionEntry.of(
            AllOfCondition.allOf(
                InvertedLootItemCondition.invert(
                    SideCheckPredicate.builder(Direction.DOWN)
                ),
                LocationCheckPredicates.builder(
                    PositionTarget.INTERACTED,
                    LocationPredicate.Builder.location()
                        .setBlock(BlockPredicate.Builder.block()
                            .of(blocks, BlockTags.CONVERTABLE_TO_MUD))
                ),
                MatchTool.toolMatches(ItemPredicate.Builder.item()
                    .withComponents(DataComponentMatchers.Builder.components()
                        .partial(
                            DataComponentPredicates.POTIONS,
                            new PotionsPredicate(
                                HolderSet.direct(
                                    potions.getOrThrow(PotionIds.WATER)
                                )
                            )
                        )
                        .build()
                    )
                )
            ),
            UncheckedSequenceHandler.builder()
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.GENERIC_SPLASH), SoundSource.BLOCKS))
                .add(ExchangeItemAction.of(items.getOrThrow(ItemIds.GLASS_BOTTLE)))
                .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, ParticleTypes.SPLASH)
                    .count(5)
                    .offset(Vec3Provider.of(
                        -0.5d, 0.5d,
                        1.0d, 1.0d,
                        -0.5d, 0.5d
                    ))
                    .speed(1.0d)
                    .build()
                )
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventIds.BOTTLE_EMPTY), SoundSource.BLOCKS))
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockIds.MUD)))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
        ));
        registerable.register(ADD_WATER_FROM_BOTTLE_TO_CAULDRON, ActionEntry.of(
            MatchTool.toolMatches(ItemPredicate.Builder.item()
                .withComponents(DataComponentMatchers.Builder.components()
                    .partial(
                        DataComponentPredicates.POTIONS,
                        new PotionsPredicate(
                            HolderSet.direct(
                                potions.getOrThrow(PotionIds.WATER)
                            )
                        )
                    )
                    .build()
                )
            ),
            PassingSequenceHandler.builder()
                .add(actions.getOrThrow(INCREASE_WATER_CAULDRON_LAYER))
                .add(ExchangeItemAction.of(items.getOrThrow(ItemIds.GLASS_BOTTLE)))
                .add(
                    IncrementStatAction.of(
                        LootContext.EntityTarget.THIS,
                        Stats.CUSTOM.get(Stats.USE_CAULDRON)
                    )
                )
                .add(
                    IncrementStatAction.of(
                        LootContext.EntityTarget.THIS,
                        Stats.ITEM_USED.itematic$get(items.getOrThrow(ItemIds.POTION))
                    )
                )
                .add(
                    PlaySoundAction.of(
                        PositionTarget.INTERACTED,
                        soundEvents.getOrThrow(SoundEventIds.BOTTLE_EMPTY),
                        SoundSource.BLOCKS
                    )
                )
                .add(
                    InvokeGameEventAction.of(
                        GameEvent.FLUID_PLACE,
                        PositionTarget.INTERACTED,
                        LootContext.EntityTarget.THIS
                    )
                )
        ));
    }

    public static ActionEntry waxSign(HolderGetter<Block> blocks, boolean wax) {
        return modifySign(blocks, ModifySignAction.wax(PositionTarget.INTERACTED, wax));
    }

    public static ActionEntry glowSign(HolderGetter<Block> blocks, boolean glow) {
        return modifySign(blocks, ModifySignAction.glow(PositionTarget.INTERACTED, glow));
    }

    public static ActionEntry potBlock(HolderGetter<Block> blocks, ResourceKey<Block> pottedBlock) {
        return ActionEntry.of(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, blocks.getOrThrow(BlockIds.FLOWER_POT).value())
                    )
            ),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(pottedBlock)))
                .add(InvokeGameEventAction.of(GameEvent.BLOCK_CHANGE, PositionTarget.INTERACTED, LootContext.EntityTarget.THIS))
                .add(IncrementStatAction.of(LootContext.EntityTarget.THIS, Stats.CUSTOM.get(Stats.POT_FLOWER)))
                .add(DecrementItemAction.of(1))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
        );
    }

    public static ActionEntry fillCauldron(HolderGetter<Item> items, ResourceKey<Item> usedItem, HolderGetter<Block> blocks, ResourceKey<Block> placedBlock, UnaryOperator<BlockState> state, Holder<SoundEvent> emptySound, HolderGetter<Fluid> fluids, boolean checkUnderWater) {
        LootItemCondition.Builder predicate = LocationCheckPredicates.builder(
            PositionTarget.INTERACTED,
            LocationPredicate.Builder.location()
                .setBlock(BlockPredicate.Builder.block()
                    .of(blocks, BlockTags.CAULDRONS)
                )
        );

        if (checkUnderWater) {
            predicate = AllOfCondition.allOf(
                predicate,
                InvertedLootItemCondition.invert(
                    LocationCheckPredicates.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.location()
                            .setFluid(FluidPredicate.Builder.fluid()
                                .of(fluids.getOrThrow(FluidTags.WATER))
                            ),
                        new BlockPos(0, 1, 0)
                    )
                )
            );
        }

        return ActionEntry.of(
            predicate,
            PassingSequenceHandler.builder()
                .add(ExchangeItemAction.of(items.getOrThrow(ItemIds.BUCKET)))
                .add(
                    IncrementStatAction.of(
                        LootContext.EntityTarget.THIS,
                        Stats.CUSTOM.get(Stats.FILL_CAULDRON)
                    )
                )
                .add(
                    IncrementStatAction.of(
                        LootContext.EntityTarget.THIS,
                        Stats.ITEM_USED.itematic$get(items.getOrThrow(usedItem))
                    )
                )
                .add(
                    SetBlockStateAction.of(
                        PositionTarget.INTERACTED,
                        state.apply(
                            blocks.getOrThrow(placedBlock)
                                .value()
                                .defaultBlockState()
                        )
                    )
                )
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, emptySound, SoundSource.BLOCKS))
                .add(InvokeGameEventAction.of(GameEvent.FLUID_PLACE, PositionTarget.INTERACTED))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
        );
    }

    private static ActionEntry modifySign(HolderGetter<Block> blocks, ModifySignAction action) {
        return ActionEntry.of(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, BlockTags.SIGNS))
            ),
            PassingSequenceHandler.builder()
                .add(action)
                .add(DecrementItemAction.of(1))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
        );
    }

    private static LootItemCondition.Builder setBlockConditions(HolderGetter<Block> blocks, UnaryOperator<BlockPredicate.Builder> blockPredicateBuilder) {
        return AllOfCondition.allOf(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(blockPredicateBuilder.apply(BlockPredicate.Builder.block()))
            ),
            InvertedLootItemCondition.invert(
                SideCheckPredicate.builder(Direction.DOWN)
            ),
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, BlockTags.AIR)),
                new BlockPos(0, 1, 0)
            )
        );
    }

    private static UncheckedSequenceHandler.Builder campfireParticles(boolean signal) {
        SimpleParticleType type = signal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        return UncheckedSequenceHandler.builder()
            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, type)
                .count(20)
                .offset(Vec3Provider.of(
                    -1.0d / 3.0d, 1.0d / 3.0d,
                    -1.0d, 1.0d,
                    -1.0d / 3.0d, 1.0d / 3.0d
                ))
                .delta(Vec3Provider.exactly(0.0d, 0.07d, 0.0d))
                .force()
                .build())
            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, type)
                .count(20)
                .offset(Vec3Provider.of(
                    -0.25d, 0.25d,
                    0.4d, 0.4d,
                    -0.25d, 0.25d
                ))
                .delta(Vec3Provider.exactly(0.0d, 0.005d, 0.0d))
                .build());
    }

    private static ResourceKey<ActionEntry> of(String name) {
        return ResourceKey.create(ItematicRegistries.ACTION, Identifier.withDefaultNamespace(name));
    }
}
