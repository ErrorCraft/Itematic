package net.errorcraft.itematic.world.action;

import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.loot.predicate.SideCheckPredicate;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.loot.condition.AllOfLootCondition;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.LocationCheckLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.function.UnaryOperator;

public class Actions {
    public static final RegistryKey<ActionEntry> USE_HOE_ON_BLOCK = of("use_hoe_on_block");
    public static final RegistryKey<ActionEntry> TILL_DIRT = of("till_dirt");
    public static final RegistryKey<ActionEntry> TILL_COARSE_DIRT = of("till_coarse_dirt");
    public static final RegistryKey<ActionEntry> TILL_ROOTED_DIRT = of("till_rooted_dirt");
    public static final RegistryKey<ActionEntry> USE_SHOVEL_ON_BLOCK = of("use_shovel_on_block");
    public static final RegistryKey<ActionEntry> FLATTEN_GROUND = of("flatten_ground");
    public static final RegistryKey<ActionEntry> EXTINGUISH_CAMPFIRE = of("extinguish_campfire");
    public static final RegistryKey<ActionEntry> LIGHT_BLOCK = of("light_block");

    private Actions() {}

    public static void bootstrap(Registerable<ActionEntry> registerable) {
        RegistryEntryLookup<ActionEntry> actions = registerable.getRegistryLookup(ItematicRegistryKeys.ACTION);
        RegistryEntryLookup<SoundEvent> soundEvents = registerable.getRegistryLookup(RegistryKeys.SOUND_EVENT);
        RegistryEntryLookup<Block> blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        registerable.register(USE_HOE_ON_BLOCK, ActionEntry.of(
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.tag(actions.getOrThrow(ActionTags.USE_HOE_ON_BLOCK)))
                .add(DamageItemAction.of(1))
                .add(SwingHandAction.INSTANCE)
                .add(PlaySoundAction.of(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.HOE_TILL), SoundCategory.BLOCKS))
        ));
        registerable.register(TILL_DIRT, ActionEntry.of(
            setBlockRequirements(builder -> builder.tag(ItematicBlockTags.TILLABLE_INTO_FARMLAND), true),
            SetBlockStateAction.of(ActionContextParameter.TARGET, blocks.getOrThrow(BlockKeys.FARMLAND))
        ));
        registerable.register(TILL_COARSE_DIRT, ActionEntry.of(
            setBlockRequirements(builder -> builder.blocks(Blocks.COARSE_DIRT), true),
            SetBlockStateAction.of(ActionContextParameter.TARGET, blocks.getOrThrow(BlockKeys.DIRT))
        ));
        registerable.register(TILL_ROOTED_DIRT, ActionEntry.of(
            setBlockRequirements(builder -> builder.blocks(Blocks.ROOTED_DIRT), false),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(ActionContextParameter.TARGET, blocks.getOrThrow(BlockKeys.DIRT)))
                .add(DropItemFromBlockAction.of(ActionContextParameter.TARGET, items.getOrThrow(ItemKeys.HANGING_ROOTS)))
        ));
        registerable.register(USE_SHOVEL_ON_BLOCK, ActionEntry.of(
            ActionRequirements.of(
                ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                InvertedLootCondition.builder(
                    SideCheckPredicate.builder(Direction.DOWN)
                ).build()
            ),
            PassingSequenceHandler.builder()
                .add(FirstToPassRequirementsSequenceHandler.tag(actions.getOrThrow(ActionTags.USE_SHOVEL_ON_BLOCK)))
                .add(DamageItemAction.of(1))
                .add(SwingHandAction.INSTANCE)
        ));
        registerable.register(FLATTEN_GROUND, ActionEntry.of(
            setBlockRequirements(builder -> builder.tag(ItematicBlockTags.FLATTENABLE_INTO_DIRT_PATH), true),
            PassingSequenceHandler.builder()
                .add(SetBlockStateAction.of(ActionContextParameter.TARGET, blocks.getOrThrow(BlockKeys.DIRT_PATH)))
                .add(PlaySoundAction.of(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.SHOVEL_FLATTEN), SoundCategory.BLOCKS))
        ));
        registerable.register(EXTINGUISH_CAMPFIRE, ActionEntry.of(
            ActionRequirements.of(
                ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                LocationCheckLootCondition.builder(
                    LocationPredicate.Builder.create()
                        .block(BlockPredicate.Builder.create()
                            .tag(BlockTags.CAMPFIRES)
                            .state(StatePredicate.Builder.create()
                                .exactMatch(Properties.LIT, true))))
                .build()
            ),
            PassingSequenceHandler.builder()
                .add(ModifyBlockStateAction.builder(ActionContextParameter.TARGET)
                    .property(Properties.LIT, false)
                    .build())
                .add(PlaySoundAction.builder(ActionContextParameter.TARGET, soundEvents.getOrThrow(SoundEventKeys.FIRE_EXTINGUISH), SoundCategory.BLOCKS)
                    .volume(0.5f)
                    .pitch(1.8f, 3.4f)
                    .build())
                .add(FirstToPassRequirementsSequenceHandler.builder()
                    .add(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            LocationCheckLootCondition.builder(
                                LocationPredicate.Builder.create()
                                    .block(BlockPredicate.Builder.create()
                                        .state(StatePredicate.Builder.create()
                                            .exactMatch(Properties.SIGNAL_FIRE, true))))
                                .build()
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
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            AllOfLootCondition.builder(
                                LocationCheckLootCondition.builder(
                                    LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                            .state(StatePredicate.Builder.create()
                                                .exactMatch(Properties.LIT, false)))),
                                InvertedLootCondition.builder(
                                    LocationCheckLootCondition.builder(
                                        LocationPredicate.Builder.create()
                                            .block(BlockPredicate.Builder.create()
                                                .state(StatePredicate.Builder.create()
                                                    .exactMatch(Properties.WATERLOGGED, true))))))
                                .build()
                        ),
                        ModifyBlockStateAction.builder(ActionContextParameter.TARGET)
                            .property(Properties.LIT, true)
                            .build()
                    )
                    .add(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            LocationCheckLootCondition.builder(
                                LocationPredicate.Builder.create()
                                    .block(BlockPredicate.Builder.create()
                                        .blocks(Blocks.TNT)))
                                .build()
                        ),
                        new PrimeTntAction(ActionContextParameter.TARGET)
                    )
                    .add(PlaceBlockAction.of(blocks.getOrThrow(BlockKeys.FIRE), ActionContextParameter.TARGET, false)))
                .add(SwingHandAction.INSTANCE)
        ));
    }

    public static ActionEntry waxSign(boolean wax) {
        return modifySign(ModifySignAction.wax(ActionContextParameter.TARGET, wax));
    }

    public static ActionEntry glowSign(boolean glow) {
        return modifySign(ModifySignAction.glow(ActionContextParameter.TARGET, glow));
    }

    private static ActionEntry modifySign(ModifySignAction action) {
        return ActionEntry.of(
            ActionRequirements.of(
                ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                LocationCheckLootCondition.builder(
                    LocationPredicate.Builder.create()
                        .block(BlockPredicate.Builder.create()
                            .tag(BlockTags.SIGNS)))
                    .build()
            ),
            PassingSequenceHandler.builder()
                .add(action)
                .add(DecrementItemAction.of(1))
                .add(SwingHandAction.INSTANCE)
        );
    }

    private static ActionRequirements setBlockRequirements(UnaryOperator<BlockPredicate.Builder> blockPredicateBuilder, boolean checkEmptySpace) {
        return ActionRequirements.of(
            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
            setBlockConditions(blockPredicateBuilder.apply(BlockPredicate.Builder.create()), checkEmptySpace).build()
        );
    }

    private static LootCondition.Builder setBlockConditions(BlockPredicate.Builder blockPredicate, boolean checkEmptySpace) {
        LootCondition.Builder locationCheckPredicate = LocationCheckLootCondition.builder(
            LocationPredicate.Builder.create()
                .block(blockPredicate));
        if (!checkEmptySpace) {
            return locationCheckPredicate;
        }
        return AllOfLootCondition.builder(
            locationCheckPredicate,
            InvertedLootCondition.builder(
                SideCheckPredicate.builder(Direction.DOWN)
            ),
            LocationCheckLootCondition.builder(
                LocationPredicate.Builder.create()
                    .block(BlockPredicate.Builder.create()
                        .tag(ItematicBlockTags.AIR)),
                new BlockPos(0, 1, 0)));
    }

    private static UncheckedSequenceHandler.Builder campfireParticles(boolean signal) {
        DefaultParticleType type = signal ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        return UncheckedSequenceHandler.builder()
            .add(DisplayParticleAction.builder(ActionContextParameter.TARGET, type)
                .count(20)
                .offset(Vec3dProvider.of(
                    -1.0d / 3.0d, 1.0d / 3.0d,
                    -1.0d, 1.0d,
                    -1.0d / 3.0d, 1.0d / 3.0d
                ))
                .delta(Vec3dProvider.of(0.0d, 0.07d, 0.0d))
                .force()
                .build())
            .add(DisplayParticleAction.builder(ActionContextParameter.TARGET, type)
                .count(20)
                .offset(Vec3dProvider.of(
                    -0.25d, 0.25d,
                    0.4d, 0.4d,
                    -0.25d, 0.25d
                ))
                .delta(Vec3dProvider.of(0.0d, 0.005d, 0.0d))
                .build());
    }

    private static RegistryKey<ActionEntry> of(String name) {
        return RegistryKey.of(ItematicRegistryKeys.ACTION, new Identifier(name));
    }
}
