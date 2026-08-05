package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.loot.condition.LocationCheckPredicates;
import net.errorcraft.itematic.loot.predicate.SideCheckPredicate;
import net.errorcraft.itematic.references.BlockKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

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

    private Actions() {}

    public static void bootstrap(BootstrapContext<ActionEntry> registerable) {
        HolderGetter<ActionEntry> actions = registerable.lookup(ItematicRegistryKeys.ACTION);
        HolderGetter<SoundEvent> soundEvents = registerable.lookup(Registries.SOUND_EVENT);
        HolderGetter<Block> blocks = registerable.lookup(Registries.BLOCK);
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);

        registerable.register(USE_HOE_ON_BLOCK, ActionEntry.of(
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.of(actions.getOrThrow(ActionTags.USE_HOE_ON_BLOCK)))
                .add(DamageItemAction.of(1))
                .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventKeys.HOE_TILL), SoundSource.BLOCKS))
        ));
        registerable.register(TILL_DIRT, ActionEntry.of(
            setBlockConditions(blocks, builder -> builder.of(blocks, ItematicBlockTags.TILLABLE_INTO_FARMLAND)),
            SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockKeys.FARMLAND))
        ));
        registerable.register(TILL_COARSE_DIRT, ActionEntry.of(
            setBlockConditions(blocks, builder -> builder.of(blocks, blocks.getOrThrow(BlockKeys.COARSE_DIRT).value())),
            SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockKeys.DIRT))
        ));
        registerable.register(TILL_ROOTED_DIRT, ActionEntry.of(
            LocationCheckPredicates.builder(
                PositionTarget.INTERACTED,
                LocationPredicate.Builder.location()
                    .setBlock(BlockPredicate.Builder.block()
                        .of(blocks, blocks.getOrThrow(BlockKeys.ROOTED_DIRT).value()))
            ),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockKeys.DIRT)))
                .add(DropItemFromBlockAction.of(PositionTarget.INTERACTED, items.getOrThrow(ItemKeys.HANGING_ROOTS)))
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
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(BlockKeys.DIRT_PATH)))
                .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventKeys.SHOVEL_FLATTEN), SoundSource.BLOCKS))
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
                .add(ModifyBlockStateAction.builder(PositionTarget.INTERACTED)
                    .property(BlockStateProperties.LIT, false)
                    .build())
                .add(PlaySoundAction.builder(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventKeys.FIRE_EXTINGUISH), SoundSource.BLOCKS)
                    .volume(0.5f)
                    .pitch(1.8f, 3.4f)
                    .build())
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
                                            .hasProperty(BlockStateProperties.LIT, false)))),
                            InvertedLootItemCondition.invert(
                                LocationCheckPredicates.builder(
                                    PositionTarget.INTERACTED,
                                    LocationPredicate.Builder.location()
                                        .setBlock(BlockPredicate.Builder.block()
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BlockStateProperties.WATERLOGGED, true)))))
                        ),
                        ModifyBlockStateAction.builder(PositionTarget.INTERACTED)
                            .property(BlockStateProperties.LIT, true)
                            .build()
                    )
                    .add(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .of(blocks, blocks.getOrThrow(BlockKeys.TNT).value()))
                        ),
                        PassingSequenceHandler.builder()
                            .add(PrimeTntAction.of(PositionTarget.INTERACTED))
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, soundEvents.getOrThrow(SoundEventKeys.TNT_PRIMED), SoundSource.BLOCKS))
                    )
                    .add(PlaceBlockAction.of(blocks.getOrThrow(BlockKeys.FIRE), PositionTarget.INTERACTED)))
                .addOptional(SwingHandAction.of(LootContext.EntityTarget.THIS))
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
                        .of(blocks, blocks.getOrThrow(BlockKeys.FLOWER_POT).value()))
            ),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(PositionTarget.INTERACTED, blocks.getOrThrow(pottedBlock)))
                .add(InvokeGameEventAction.of(GameEvent.BLOCK_CHANGE, PositionTarget.INTERACTED, LootContext.EntityTarget.THIS))
                .add(IncrementStatAction.of(LootContext.EntityTarget.THIS, Stats.CUSTOM.get(Stats.POT_FLOWER)))
                .add(DecrementItemAction.of(1))
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
                .offset(Vec3dProvider.of(
                    -1.0d / 3.0d, 1.0d / 3.0d,
                    -1.0d, 1.0d,
                    -1.0d / 3.0d, 1.0d / 3.0d
                ))
                .delta(Vec3dProvider.of(0.0d, 0.07d, 0.0d))
                .force()
                .build())
            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, type)
                .count(20)
                .offset(Vec3dProvider.of(
                    -0.25d, 0.25d,
                    0.4d, 0.4d,
                    -0.25d, 0.25d
                ))
                .delta(Vec3dProvider.of(0.0d, 0.005d, 0.0d))
                .build());
    }

    private static ResourceKey<ActionEntry> of(String name) {
        return ResourceKey.create(ItematicRegistryKeys.ACTION, Identifier.withDefaultNamespace(name));
    }
}
