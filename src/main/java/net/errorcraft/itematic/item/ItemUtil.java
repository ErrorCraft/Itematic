package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ItematicBlockTags;
import net.errorcraft.itematic.component.type.ItemDamageRulesDataComponent;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.entity.effect.StatusEffectKeys;
import net.errorcraft.itematic.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.entity.spawn.rule.type.AlignYawEntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.type.DiscardEntitySpawnRule;
import net.errorcraft.itematic.entity.spawn.rule.type.FitsInVolumeEntitySpawnRule;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.item.composting.CompostChances;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.fuel.FuelTimes;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.item.shooter.method.methods.DirectShooterMethod;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.loot.condition.LocationCheckLootConditionUtil;
import net.errorcraft.itematic.loot.function.SetItemPointerLocationItemModifier;
import net.errorcraft.itematic.loot.function.SplitItemModifier;
import net.errorcraft.itematic.loot.predicate.SideCheckPredicate;
import net.errorcraft.itematic.mixin.item.BrushItemAccessor;
import net.errorcraft.itematic.mixin.item.CrossbowItemAccessor;
import net.errorcraft.itematic.potion.PotionKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.ItemStackTarget;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.block.Block;
import net.minecraft.block.DecoratedPotPattern;
import net.minecraft.block.DecoratedPotPatterns;
import net.minecraft.block.jukebox.JukeboxSong;
import net.minecraft.block.jukebox.JukeboxSongs;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.passive.ChickenVariant;
import net.minecraft.entity.passive.ChickenVariants;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.equipment.ArmorMaterials;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.item.equipment.trim.ArmorTrimMaterial;
import net.minecraft.item.equipment.trim.ArmorTrimMaterials;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.function.SetNameLootFunction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.FluidPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.component.ComponentPredicateTypes;
import net.minecraft.predicate.component.ComponentsPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.LazyRegistryEntryReference;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.event.GameEvent;

import java.util.List;
import java.util.Optional;

public class ItemUtil {
    public static final int UNSTACKABLE_MAX_STACK_SIZE = 1;
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemDisplay.CODEC.fieldOf("display").forGetter(Item::itematic$display),
        AttributeModifiersComponent.CODEC.optionalFieldOf("attribute_modifiers", AttributeModifiersComponent.DEFAULT).forGetter(Item::itematic$attributeModifiers),
        ItemComponentSet.CODEC.optionalFieldOf("behavior", ItemComponentSet.EMPTY).forGetter(Item::itematic$behavior),
        ItemEventMap.CODEC.optionalFieldOf("events", ItemEventMap.EMPTY).forGetter(Item::itematic$events)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        new Bootstrapper(registerable).bootstrap();
    }

    public static RegistryKey<Item> keyFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    private static Item create(ItemDisplay display) {
        return create(display, ItemComponentSet.EMPTY);
    }

    private static Item create(ItemDisplay display, ItemComponentSet components) {
        return create(display, components, ItemEventMap.EMPTY);
    }

    private static Item create(ItemDisplay display, ItemComponentSet components, ItemEventMap events) {
        return create(display, AttributeModifiersComponent.DEFAULT, components, events);
    }

    private static Item create(ItemDisplay display, AttributeModifiersComponent attributeModifiers, ItemComponentSet components) {
        return create(display, attributeModifiers, components, ItemEventMap.EMPTY);
    }

    private static Item create(ItemDisplay display, AttributeModifiersComponent attributeModifiers, ItemComponentSet components, ItemEventMap events) {
        Item item = new Item(new Item.Settings());
        item.itematic$setDisplay(display);
        item.itematic$setAttributeModifiers(attributeModifiers);
        item.itematic$setBehavior(components);
        item.itematic$setEvents(events);
        return item;
    }

    private static class Bootstrapper {
        private final Registerable<Item> registerable;
        private final RegistryEntryLookup<Item> items;
        private final RegistryEntryLookup<EntityType<?>> entityTypes;
        private final RegistryEntryLookup<Block> blocks;
        private final RegistryEntryLookup<DispenseBehavior> dispenseBehaviors;
        private final RegistryEntryLookup<SoundEvent> soundEvents;
        private final RegistryEntryLookup<Fluid> fluids;
        private final RegistryEntryLookup<ActionEntry> actions;
        private final RegistryEntryLookup<DecoratedPotPattern> decoratedPotPatterns;
        private final RegistryEntryLookup<StatusEffect> statusEffects;
        private final RegistryEntryLookup<Potion> potions;
        private final RegistryEntryLookup<Enchantment> enchantments;
        private final RegistryEntryLookup<JukeboxSong> jukeboxSongs;
        private final RegistryEntryLookup<Instrument> instruments;
        private final RegistryEntryLookup<ArmorTrimMaterial> trimMaterials;
        private final RegistryEntryLookup<ChickenVariant> chickenVariants;

        private Bootstrapper(Registerable<Item> registerable) {
            this.registerable = registerable;
            this.items = registerable.getRegistryLookup(RegistryKeys.ITEM);
            this.entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
            this.blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
            this.dispenseBehaviors = registerable.getRegistryLookup(ItematicRegistryKeys.DISPENSE_BEHAVIOR);
            this.soundEvents = registerable.getRegistryLookup(RegistryKeys.SOUND_EVENT);
            this.fluids = registerable.getRegistryLookup(RegistryKeys.FLUID);
            this.actions = registerable.getRegistryLookup(ItematicRegistryKeys.ACTION);
            this.decoratedPotPatterns = registerable.getRegistryLookup(RegistryKeys.DECORATED_POT_PATTERN);
            this.statusEffects = registerable.getRegistryLookup(RegistryKeys.STATUS_EFFECT);
            this.potions = registerable.getRegistryLookup(RegistryKeys.POTION);
            this.enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
            this.jukeboxSongs = registerable.getRegistryLookup(RegistryKeys.JUKEBOX_SONG);
            this.instruments = registerable.getRegistryLookup(RegistryKeys.INSTRUMENT);
            this.trimMaterials = registerable.getRegistryLookup(RegistryKeys.TRIM_MATERIAL);
            this.chickenVariants = registerable.getRegistryLookup(RegistryKeys.CHICKEN_VARIANT);
        }

        private void bootstrap() {
            this.bootstrapConsumables();
            this.bootstrapBlocks();
            this.bootstrapToolsAndWeapons();
            this.bootstrapEntities();
            this.bootstrapCompostables();
            this.bootstrapEquipment();
            this.bootstrapFuel();
            this.bootstrapProjectiles();
            this.bootstrapDyes();
            this.bootstrapRecords();
            this.bootstrapBuckets();
            this.bootstrapSmithingTemplates();
            this.bootstrapBanners();
            this.bootstrapDecoratedPotPatterns();
            this.bootstrapImmuneToDamage();
            this.bootstrapTrimMaterialProviders();
            this.bootstrapMiscellaneous();
        }

        private void bootstrapConsumables() {
            this.bootstrapFood();
            this.registerable.register(ItemKeys.MILK_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.MILK_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.MILK_BUCKET)
                        .remainder(this.items.getOrThrow(ItemKeys.BUCKET))
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(ClearStatusEffectsAction.of(LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POTION, create(
                ItemDisplay.Builder.forItem(ItemKeys.POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.DRINK)
                        .remainder(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                        .build())
                    .with(PotionHolderItemComponent.of(1.0f))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        AllOfLootCondition.builder(
                            InvertedLootCondition.builder(
                                SideCheckPredicate.builder(Direction.DOWN)
                            ),
                            LocationCheckLootConditionUtil.builder(
                                PositionTarget.INTERACTED,
                                LocationPredicate.Builder.create()
                                    .block(BlockPredicate.Builder.create()
                                        .tag(this.blocks, BlockTags.CONVERTABLE_TO_MUD))
                            ),
                            MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                                .components(ComponentsPredicate.Builder.create()
                                    .partial(
                                        ComponentPredicateTypes.POTION_CONTENTS,
                                        new PotionContentsPredicate(RegistryEntryList.of(
                                            this.potions.getOrThrow(PotionKeys.WATER)
                                        ))
                                    ).build()
                                )
                            )
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventKeys.GENERIC_SPLASH), SoundCategory.BLOCKS))
                            .add(ExchangeItemAction.of(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, ParticleTypes.SPLASH)
                                .count(5)
                                .offset(Vec3dProvider.of(
                                    -0.5d, 0.5d,
                                    1.0d, 1.0d,
                                    -0.5d, 0.5d
                                ))
                                .speed(1.0d)
                                .build())
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventKeys.BOTTLE_EMPTY), SoundCategory.BLOCKS))
                            .add(SetBlockStateAction.of(PositionTarget.INTERACTED, this.blocks.getOrThrow(BlockKeys.MUD)))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.OMINOUS_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.OMINOUS_BOTTLE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.OMINOUS_BOTTLE).build())
                    .with(OminousEffectProviderItemComponent.INSTANCE)
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(PlaySoundAction.of(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventKeys.OMINOUS_BOTTLE_DISPOSE))))
                    .build()
            ));
        }

        private void bootstrapFood() {
            this.registerable.register(ItemKeys.APPLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.APPLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.APPLE)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON_SLICE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MELON_SLICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.MELON_SLICE)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIED_KELP, create(
                ItemDisplay.Builder.forItem(ItemKeys.DRIED_KELP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.DRIED_KELP)
                        .food(FoodComponents.DRIED_KELP)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARROT, create(
                ItemDisplay.Builder.forItem(ItemKeys.CARROT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.CARROT)
                        .build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARROTS)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.POTATO, create(
                ItemDisplay.Builder.forItem(ItemKeys.POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.POTATO)
                        .build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POTATOES)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAKED_POTATO, create(
                ItemDisplay.Builder.forItem(ItemKeys.BAKED_POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.BAKED_POTATO)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_FRUIT, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHORUS_FRUIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.CHORUS_FRUIT)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(TeleportAction.of(16, LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.BEETROOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.BEETROOT)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.SWEET_BERRIES, create(
                ItemDisplay.Builder.forItem(ItemKeys.SWEET_BERRIES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.SWEET_BERRIES)
                        .build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SWEET_BERRY_BUSH)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_BERRIES, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOW_BERRIES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.GLOW_BERRIES)
                        .build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAVE_VINES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREAD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BREAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.BREAD)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKIE, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKIE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKIE)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.PORKCHOP, create(
                ItemDisplay.Builder.forItem(ItemKeys.PORKCHOP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.PORKCHOP)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_PORKCHOP, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_PORKCHOP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_PORKCHOP)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.BEEF, create(
                ItemDisplay.Builder.forItem(ItemKeys.BEEF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.BEEF)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_BEEF, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_BEEF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_BEEF)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.CHICKEN, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHICKEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.CHICKEN)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        RandomChanceLootCondition.builder(0.3f),
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 600)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_CHICKEN, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_CHICKEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_CHICKEN)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT, create(
                ItemDisplay.Builder.forItem(ItemKeys.RABBIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.RABBIT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_RABBIT, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_RABBIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_RABBIT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.MUTTON, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.MUTTON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_MUTTON, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_MUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_MUTTON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COD, create(
                ItemDisplay.Builder.forItem(ItemKeys.COD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COD)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON, create(
                ItemDisplay.Builder.forItem(ItemKeys.SALMON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.SALMON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH, create(
                ItemDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.TROPICAL_FISH)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH, create(
                ItemDisplay.Builder.forItem(ItemKeys.PUFFERFISH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.PUFFERFISH)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 1200, 1),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.HUNGER), 300, 2),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.NAUSEA), 300)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_COD, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_COD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_COD)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_SALMON, create(
                ItemDisplay.Builder.forItem(ItemKeys.COOKED_SALMON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.COOKED_SALMON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSHROOM_STEW, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSHROOM_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.MUSHROOM_STEW)
                        .remainder(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_STEW, create(
                ItemDisplay.Builder.forItem(ItemKeys.RABBIT_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.RABBIT_STEW)
                        .remainder(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT_SOUP, create(
                ItemDisplay.Builder.forItem(ItemKeys.BEETROOT_SOUP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.BEETROOT_SOUP)
                        .remainder(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_STEW, create(
                ItemDisplay.Builder.forItem(ItemKeys.SUSPICIOUS_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.SUSPICIOUS_STEW)
                        .remainder(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(ApplySuspiciousStewEffectsFromItemAction.of(LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROTTEN_FLESH, create(
                ItemDisplay.Builder.forItem(ItemKeys.ROTTEN_FLESH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.ROTTEN_FLESH)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        RandomChanceLootCondition.builder(0.8f),
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.HUNGER), 600)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPIDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.SPIDER_EYE)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 100)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.POISONOUS_POTATO, create(
                ItemDisplay.Builder.forItem(ItemKeys.POISONOUS_POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.POISONOUS_POTATO)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        RandomChanceLootCondition.builder(0.6f),
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 100)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_APPLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_APPLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.GOLDEN_APPLE)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 100, 1),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.ABSORPTION), 2400)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTED_GOLDEN_APPLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENCHANTED_GOLDEN_APPLE)
                    .rarity(Rarity.RARE)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.ENCHANTED_GOLDEN_APPLE)
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 400, 1),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.RESISTANCE), 6000),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.FIRE_RESISTANCE), 6000),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.ABSORPTION), 2400)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_CARROT, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_CARROT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.GOLDEN_CARROT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN_PIE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PUMPKIN_PIE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.FOOD)
                        .food(FoodComponents.PUMPKIN_PIE)
                        .build())
                    .with(CompostableItemComponent.of(CompostChances.GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEY_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.HONEY_BOTTLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ConsumableItemComponent.builder(ConsumableComponents.HONEY_BOTTLE)
                        .food(FoodComponents.HONEY_BOTTLE)
                        .useAnimation(UseAction.DRINK)
                        .remainder(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventKeys.HONEY_BOTTLE_DRINK))
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        RemoveStatusEffectsAction.of(
                            LootContext.EntityTarget.THIS,
                            this.statusEffects.getOrThrow(StatusEffectKeys.POISON)
                        )
                    ))
                    .build()
            ));
        }

        private void bootstrapBlocks() {
            this.bootstrapAttachedToSideBlocks();
            this.bootstrapColoredBlocks();
            this.bootstrapItemNameBlocks();
            this.bootstrapOperatorOnlyBlocks();
            this.registerable.register(ItemKeys.STONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRANITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_GRANITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIORITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DIORITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANDESITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_ANDESITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CALCITE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CALCITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CALCITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_TUFF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_TUFF_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_TUFF_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_TUFF_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUFF_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_TUFF_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_TUFF_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIPSTONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DRIPSTONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRIPSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRASS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRASS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRASS_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIRT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COARSE_DIRT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COARSE_DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COARSE_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PODZOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PODZOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PODZOL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROOTED_DIRT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ROOTED_DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ROOTED_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_NYLIUM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_NYLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_NYLIUM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_NYLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEDROCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BEDROCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEDROCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_SAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SUSPICIOUS_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUSPICIOUS_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_GRAVEL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SUSPICIOUS_GRAVEL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUSPICIOUS_GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAVEL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAVEL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAL_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COAL_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_COAL_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_COAL_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.IRON_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_IRON_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_IRON_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_COPPER_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_COPPER_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REDSTONE_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_REDSTONE_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_REDSTONE_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EMERALD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_EMERALD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_EMERALD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LAPIS_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_LAPIS_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_LAPIS_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIAMOND_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_DIAMOND_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_DIAMOND_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_QUARTZ_ORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_QUARTZ_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_QUARTZ_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_IRON_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RAW_IRON_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RAW_COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_GOLD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RAW_GOLD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEAVY_CORE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HEAVY_CORE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HEAVY_CORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AMETHYST_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AMETHYST_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUDDING_AMETHYST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BUDDING_AMETHYST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUDDING_AMETHYST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.IRON_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GOLD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIAMOND_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIAMOND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUDDY_MANGROVE_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUDDY_MANGROVE_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUDDY_MANGROVE_ROOTS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_STEM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_STEM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CRIMSON_STEM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_WARPED_STEM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_WARPED_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CRIMSON_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_WARPED_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_WARPED_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPONGE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPONGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WET_SPONGE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WET_SPONGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WET_SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TINTED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TINTED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TINTED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LAPIS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LAPIS_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBWEB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBWEB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBWEB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_STONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_STONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PETRIFIED_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PETRIFIED_OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PETRIFIED_OAK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLESTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUD_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.QUARTZ_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPUR_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_PRISMARINE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_STONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DECORATED_POT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DECORATED_POT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DECORATED_POT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OBSIDIAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OBSIDIAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_ROD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_ROD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_PLANT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHORUS_PLANT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHORUS_PLANT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_FLOWER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHORUS_FLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHORUS_FLOWER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPUR_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_PILLAR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPUR_PILLAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPUR_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPAWNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPAWNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREAKING_HEART, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CREAKING_HEART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CREAKING_HEART)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FARMLAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FARMLAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FARMLAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FURNACE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FURNACE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLESTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SNOW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNOW)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ICE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SNOW_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNOW_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLAY, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CLAY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CLAY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.JACK_O_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JACK_O_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JACK_O_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERRACK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHERRACK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHERRACK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_SAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SOUL_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_SOIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SOUL_SOIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_SOIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BASALT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BASALT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_BASALT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOWSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GLOWSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLOWSTONE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.CHARGE_RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_STONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_MOSSY_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_MOSSY_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_CRACKED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_CRACKED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_CHISELED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_CHISELED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.INFESTED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRACKED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PACKED_MUD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PACKED_MUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PACKED_MUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUD_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_DEEPSLATE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRACKED_DEEPSLATE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_TILES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_DEEPSLATE_TILES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRACKED_DEEPSLATE_TILES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REINFORCED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REINFORCED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REINFORCED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BARS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.IRON_BARS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_BARS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHAIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUD_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MYCELIUM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MYCELIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MYCELIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRACKED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_BRICK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCULK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_VEIN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCULK_VEIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_VEIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_CATALYST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCULK_CATALYST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_CATALYST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_SHRIEKER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCULK_SHRIEKER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_SHRIEKER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ENCHANTING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ENCHANTING_TABLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_PORTAL_FRAME, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_PORTAL_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_PORTAL_FRAME)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_EGG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DRAGON_EGG)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRAGON_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ENDER_CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ENDER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EMERALD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EMERALD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEACON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BEACON)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEACON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLESTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SANDSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRANITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUD_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANDESITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SANDSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIORITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACKSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHIPPED_ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHIPPED_ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHIPPED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DAMAGED_ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DAMAGED_ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DAMAGED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_QUARTZ_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_QUARTZ_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.QUARTZ_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.QUARTZ_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_PILLAR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.QUARTZ_PILLAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.QUARTZ_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BARRIER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BARRIER)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BARRIER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PACKED_ICE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PACKED_ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PACKED_ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIRT_PATH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIRT_PATH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIRT_PATH)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_PRISMARINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PRISMARINE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_PRISMARINE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEA_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SEA_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEA_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CUT_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGMA_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGMA_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRUCTURE_VOID, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRUCTURE_VOID)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRUCTURE_VOID)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_EGG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TURTLE_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TURTLE_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNIFFER_EGG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SNIFFER_EGG)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNIFFER_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUBE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRAIN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BUBBLE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FIRE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HORN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUBE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRAIN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BUBBLE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FIRE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HORN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_ICE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CONDUIT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CONDUIT)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CONDUIT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_GRANITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DIORITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRANITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANDESITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_ANDESITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIORITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_GRANITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DIORITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.END_STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRANITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANDESITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_ANDESITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DIORITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REDSTONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REPEATER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REPEATER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REPEATER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPARATOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COMPARATOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COMPARATOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PISTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PISTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PISTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STICKY_PISTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STICKY_PISTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STICKY_PISTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SLIME_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SLIME_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEY_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HONEY_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HONEY_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OBSERVER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OBSERVER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OBSERVER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HOPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HOPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DISPENSER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DISPENSER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DISPENSER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DROPPER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DROPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DROPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TARGET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TARGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TARGET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEVER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LEVER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LEVER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHTNING_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_SENSOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCULK_SENSOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CALIBRATED_SCULK_SENSOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CALIBRATED_SCULK_SENSOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CALIBRATED_SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIPWIRE_HOOK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TRIPWIRE_HOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIPWIRE_HOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TNT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TNT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TNT)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_TNT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_LAMP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REDSTONE_LAMP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_LAMP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_WEIGHTED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_WEIGHTED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEAVY_WEIGHTED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HEAVY_WEIGHTED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HEAVY_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.IRON_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.IRON_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POWERED_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POWERED_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POWERED_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DETECTOR_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DETECTOR_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DETECTOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACTIVATOR_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACTIVATOR_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACTIVATOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREWING_STAND, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BREWING_STAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BREWING_STAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAULDRON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CAULDRON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAULDRON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWER_POT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FLOWER_POT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWER_POT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOKER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMOKER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOKER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAST_FURNACE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLAST_FURNACE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLAST_FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRINDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRINDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRINDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONECUTTER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STONECUTTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONECUTTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BELL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BELL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BELL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SOUL_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAMPFIRE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CAMPFIRE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_CAMPFIRE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SOUL_CAMPFIRE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEE_NEST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BEE_NEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEE_NEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEEHIVE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BEEHIVE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEEHIVE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEYCOMB_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HONEYCOMB_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HONEYCOMB_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LODESTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LODESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LODESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRYING_OBSIDIAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRYING_OBSIDIAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRYING_OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACKSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACKSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GILDED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GILDED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GILDED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_POLISHED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_POLISHED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESPAWN_ANCHOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESPAWN_ANCHOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMALL_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMALL_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMALL_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MEDIUM_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MEDIUM_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MEDIUM_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LARGE_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LARGE_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LARGE_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_CLUSTER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AMETHYST_CLUSTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AMETHYST_CLUSTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POINTED_DRIPSTONE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POINTED_DRIPSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POINTED_DRIPSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OCHRE_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OCHRE_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OCHRE_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.VERDANT_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.VERDANT_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VERDANT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PEARLESCENT_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PEARLESCENT_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PEARLESCENT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FROGSPAWN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FROGSPAWN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FROGSPAWN), BlockItemComponent.Pass.FLUID))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.EXPOSED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEATHERED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIAL_SPAWNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TRIAL_SPAWNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIAL_SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.VAULT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.VAULT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VAULT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRAFTER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRAFTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRAFTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESIN_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESIN_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESIN_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESIN_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RESIN_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_RESIN_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_RESIN_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_RESIN_BRICKS)))
                    .build()
            ));
        }

        private void bootstrapAttachedToSideBlocks() {
            this.registerable.register(ItemKeys.TORCH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.TORCH), this.blocks.getOrThrow(BlockKeys.WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_TORCH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SOUL_TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SOUL_TORCH), this.blocks.getOrThrow(BlockKeys.SOUL_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TUBE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BRAIN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BUBBLE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FIRE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HORN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.HORN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_TORCH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REDSTONE_TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.REDSTONE_TORCH), this.blocks.getOrThrow(BlockKeys.REDSTONE_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CRIMSON_SIGN), this.blocks.getOrThrow(BlockKeys.CRIMSON_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WARPED_SIGN), this.blocks.getOrThrow(BlockKeys.WARPED_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CRIMSON_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.CRIMSON_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WARPED_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.WARPED_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
        }

        private void bootstrapColoredBlocks() {
            this.bootstrapShulkerBoxes();
            this.registerable.register(ItemKeys.WHITE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_BED, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CANDLE)))
                    .build()
            ));
        }

        private void bootstrapShulkerBoxes() {
            this.registerable.register(ItemKeys.SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
        }

        private void bootstrapItemNameBlocks() {
            this.registerable.register(ItemKeys.STRING, create(
                ItemDisplay.Builder.forItem(ItemKeys.STRING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIPWIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_CLUMP, create(
                ItemDisplay.Builder.forItem(ItemKeys.RESIN_CLUMP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESIN_CLUMP)))
                    .build()
            ));
        }

        private void bootstrapOperatorOnlyBlocks() {
            this.registerable.register(ItemKeys.COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REPEATING_COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.REPEATING_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.REPEATING_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAIN_COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHAIN_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.CHAIN_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRUCTURE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRUCTURE_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.STRUCTURE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.JIGSAW, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JIGSAW)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.JIGSAW)))
                    .build()
            ));
        }

        private void bootstrapToolsAndWeapons() {
            this.registerable.register(ItemKeys.WOODEN_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOODEN_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemComponent.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOODEN_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemComponent.of(FuelTimes.TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOODEN_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemComponent.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOODEN_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.WOOD, 7.0d, 0.2d, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemComponent.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOODEN_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.WOOD, 1.0d, 0.25d, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemComponent.of(FuelTimes.TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.STONE_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.STONE_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.STONE_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.STONE_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.STONE, 8.0d, 0.2d, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.STONE_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.STONE, 2.0d, 0.5d, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.GOLD, 7.0d, 0.25d, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.GOLD, 1.0d, 0.25d, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.IRON, 7.0d, 0.225d, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.IRON, 3.0d, 0.75d, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.DIAMOND, 6.0d, 0.25d, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.DIAMOND, 4.0d, 1.0d, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SWORD, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_AXE, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(this.blocks, ToolMaterial.NETHERITE, 6.0d, 0.25d, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_HOE, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(this.blocks, ToolMaterial.NETHERITE, 5.0d, 1.0d, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.FISHING_ROD, create(
                ItemDisplay.Builder.forItem(ItemKeys.FISHING_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(CastableItemComponent.INSTANCE)
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEARS, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHEARS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(238))
                    .with(ToolItemComponent.builder(1)
                        .rule(ToolComponent.Rule.ofAlwaysDropping(RegistryEntryList.of(this.blocks.getOrThrow(BlockKeys.COBWEB)), 15.0f))
                        .rule(ToolComponent.Rule.of(this.blocks.getOrThrow(BlockTags.LEAVES), 15.0f))
                        .rule(ToolComponent.Rule.of(this.blocks.getOrThrow(BlockTags.WOOL), 5.0f))
                        .rule(ToolComponent.Rule.of(RegistryEntryList.of(this.blocks.getOrThrow(BlockKeys.VINE), this.blocks.getOrThrow(BlockKeys.GLOW_LICHEN)), 2.0f))
                        .build())
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHEAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOW, create(
                ItemDisplay.Builder.forItem(ItemKeys.BOW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(384))
                    .with(ShooterItemComponent.of(
                        UseAction.BOW,
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        BowItem.RANGE,
                        DirectShooterMethod.of()
                    ))
                    .with(EnchantableItemComponent.of(1))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CROSSBOW, create(
                ItemDisplay.Builder.forItem(ItemKeys.CROSSBOW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(465))
                    .with(ShooterItemComponent.of(
                        UseAction.CROSSBOW,
                        this.items.getOrThrow(ItematicItemTags.CROSSBOW_AMMUNITION),
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        CrossbowItem.RANGE,
                        ChargeableShooterMethod.of(
                            CrossbowItemAccessor.defaultChargingSounds(),
                            ChargeableShooterMethod.ChargedPowerRules.Rule.of(
                                RegistryEntryList.of(this.items.getOrThrow(ItemKeys.FIREWORK_ROCKET)),
                                CrossbowItemAccessor.fireworkRocketPower()
                            )
                        ),
                        ItemDamageRulesDataComponent.Rule.of(
                            RegistryEntryList.of(this.items.getOrThrow(ItemKeys.FIREWORK_ROCKET)),
                            3
                        )
                    ))
                    .with(EnchantableItemComponent.of(1))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIDENT, create(
                ItemDisplay.Builder.forItem(ItemKeys.TRIDENT)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.ofPreserved(250))
                    .with(ToolItemComponent.builder(2)
                        .preventCreativeDestruction()
                        .build())
                    .with(WeaponItemComponent.of(1, 0.0f, TridentItem.ATTACK_DAMAGE, 0.275d))
                    .with(ThrowableItemComponent.trident(TridentItem.THROW_SPEED, 0.0f, TridentItem.MIN_DRAW_DURATION))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.TRIDENT)))
                    .with(EnchantableItemComponent.of(1))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.STOPPED_USING, ActionEntry.of(
                        AllOfLootCondition.builder(
                            EntityPropertiesLootCondition.builder(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.create()
                                    .itematic$usedItemAtLeast(TridentItem.MIN_DRAW_DURATION)
                                    .itematic$inWaterOrRain(true)
                            ),
                            MatchToolLootCondition.builder(ItemPredicate.Builder.create()
                                .components(ComponentsPredicate.Builder.create()
                                    .partial(
                                        ComponentPredicateTypes.ENCHANTMENTS,
                                        EnchantmentsPredicate.enchantments(List.of(
                                            new EnchantmentPredicate(this.enchantments.getOrThrow(Enchantments.RIPTIDE), NumberRange.IntRange.ANY)
                                        ))
                                    ).build()
                                )
                            )
                        ),
                        PassingSequenceHandler.builder()
                            .add(TwirlPlayerAction.INSTANCE)
                            .add(DamageItemAction.of(1))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARROT_ON_A_STICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.CARROT_ON_A_STICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(25))
                    .with(SteeringItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.PIG), 7))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.BREAK_ITEM, ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemKeys.FISHING_ROD))))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FUNGUS_ON_A_STICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.WARPED_FUNGUS_ON_A_STICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(100))
                    .with(SteeringItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.STRIDER), 1))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.BREAK_ITEM, ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemKeys.FISHING_ROD))))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLINT_AND_STEEL, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLINT_AND_STEEL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DamageItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventKeys.FLINT_AND_STEEL_USE), SoundCategory.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRUSH, create(
                ItemDisplay.Builder.forItem(ItemKeys.BRUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BrushItemComponent.of(BrushItemAccessor.maxBrushTime()))
                    .with(DamageableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.BRUSH)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MACE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MACE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(500))
                    .with(ToolItemComponent.builder(2).build())
                    .with(WeaponItemComponent.ofSmashing(1, 5.0d, 0.15d))
                    .with(EnchantableItemComponent.of(15))
                    .with(RepairableItemComponent.of(RegistryEntryList.of(
                        this.items.getOrThrow(ItemKeys.BREEZE_ROD)
                    )))
                    .build()
            ));
        }

        private void bootstrapEntities() {
            this.bootstrapSpawnEggs();
            this.registerable.register(ItemKeys.MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHEST_MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHEST_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.CHEST_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.FURNACE_MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.FURNACE_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.FURNACE_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.TNT_MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.TNT_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.TNT_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOPPER_MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.HOPPER_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.HOPPER_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMMAND_BLOCK_MINECART, create(
                ItemDisplay.Builder.forItem(ItemKeys.COMMAND_BLOCK_MINECART)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.minecart(
                        this.entityTypes.getOrThrow(EntityTypeKeys.COMMAND_BLOCK_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.OAK_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.OAK_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPRUCE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.SPRUCE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPRUCE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.SPRUCE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.BIRCH_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.BIRCH_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.BIRCH_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.BIRCH_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.JUNGLE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.JUNGLE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.JUNGLE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.JUNGLE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.ACACIA_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.ACACIA_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.ACACIA_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.ACACIA_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHERRY_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.CHERRY_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHERRY_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.CHERRY_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.DARK_OAK_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.DARK_OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.DARK_OAK_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.DARK_OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.PALE_OAK_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.PALE_OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.PALE_OAK_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.PALE_OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.MANGROVE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.MANGROVE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.MANGROVE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.MANGROVE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_RAFT, create(
                ItemDisplay.Builder.forItem(ItemKeys.BAMBOO_RAFT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.BAMBOO_RAFT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_CHEST_RAFT, create(
                ItemDisplay.Builder.forItem(ItemKeys.BAMBOO_CHEST_RAFT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.ofDispensing(this.entityTypes.getOrThrow(EntityTypeKeys.BAMBOO_CHEST_RAFT), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemKeys.PAINTING, create(
                ItemDisplay.Builder.forItem(ItemKeys.PAINTING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeKeys.PAINTING))
                            .allowItemData()
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                SideCheckPredicate.builder(
                                    Direction.DOWN,
                                    Direction.UP
                                )
                            )
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.ITEM_FRAME, create(
                ItemDisplay.Builder.forItem(ItemKeys.ITEM_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeKeys.ITEM_FRAME))
                            .allowItemData()
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_ITEM_FRAME, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOW_ITEM_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeKeys.GLOW_ITEM_FRAME))
                            .allowItemData()
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMOR_STAND, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARMOR_STAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(EntityItemComponent.ofDispensing(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeKeys.ARMOR_STAND))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                SideCheckPredicate.builder(Direction.DOWN)
                            )
                            .spawnRule(FitsInVolumeEntitySpawnRule.entityDimensions())
                            .spawnRule(AlignYawEntitySpawnRule.of(8))
                            .spawnSound(this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_STAND_PLACE))
                            .build(),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_CRYSTAL, create(
                ItemDisplay.Builder.forItem(ItemKeys.END_CRYSTAL)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeKeys.END_CRYSTAL))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                InvertedLootCondition.builder(
                                    LocationCheckLootConditionUtil.builder(
                                        PositionTarget.INTERACTED,
                                        LocationPredicate.Builder.create()
                                            .block(BlockPredicate.Builder.create()
                                                .tag(this.blocks, ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)),
                                        new BlockPos(0, -1, 0)
                                    )
                                )
                            )
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                InvertedLootCondition.builder(
                                    LocationCheckLootConditionUtil.builder(
                                        PositionTarget.INTERACTED,
                                        LocationPredicate.Builder.create()
                                            .block(BlockPredicate.Builder.create()
                                                .tag(this.blocks, BlockTags.AIR))
                                    )
                                )
                            )
                            .spawnRule(
                                FitsInVolumeEntitySpawnRule.of(
                                    false,
                                    true,
                                    new Vec3d(1.0d, 2.0d, 1.0d)
                                )
                            )
                            .build()
                    ))
                    .build()
            ));
        }

        private void bootstrapSpawnEggs() {
            this.registerable.register(ItemKeys.ARMADILLO_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARMADILLO_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ARMADILLO), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ALLAY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ALLAY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ALLAY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.AXOLOTL_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.AXOLOTL_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.BAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BEE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.BEE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLAZE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.BLAZE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOGGED_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BOGGED_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.BOGGED), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREEZE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BREEZE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.BREEZE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAMEL_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CAMEL_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CAMEL), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAVE_SPIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CAVE_SPIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CAVE_SPIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHICKEN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHICKEN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CHICKEN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COD_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.COD_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.COD), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COW_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.COW_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.COW), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREAKING_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CREAKING_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CREAKING), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.CREEPER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.CREEPER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DOLPHIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.DOLPHIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.DOLPHIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DONKEY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.DONKEY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.DONKEY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DROWNED_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.DROWNED_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.DROWNED), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ELDER_GUARDIAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_DRAGON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENDER_DRAGON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ENDER_DRAGON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDERMAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENDERMAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ENDERMAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDERMITE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENDERMITE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ENDERMITE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.EVOKER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.EVOKER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.EVOKER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.FOX_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.FOX_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.FOX), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.FROG_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.FROG_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.FROG), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GHAST_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.GHAST_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.GHAST), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_SQUID_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOW_SQUID_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.GLOW_SQUID), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.GOAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUARDIAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.GUARDIAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.GUARDIAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.HOGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.HOGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HUSK_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.HUSK_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.HUSK), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_GOLEM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_GOLEM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.IRON_GOLEM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.LLAMA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.LLAMA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.LLAMA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_CUBE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.MAGMA_CUBE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.MAGMA_CUBE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOOSHROOM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.MOOSHROOM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.MOOSHROOM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MULE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.MULE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.MULE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.OCELOT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.OCELOT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.OCELOT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PANDA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PANDA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PANDA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PARROT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PARROT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PARROT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PHANTOM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PHANTOM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PHANTOM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIG_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PIG_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PIG), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PIGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PIGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PIGLIN_BRUTE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLAR_BEAR_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.POLAR_BEAR_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.POLAR_BEAR), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.PUFFERFISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.RABBIT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.RABBIT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAVAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.RAVAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.RAVAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SALMON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SALMON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEEP_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHEEP_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SHEEP), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHULKER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHULKER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SHULKER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SILVERFISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SILVERFISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SILVERFISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKELETON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SKELETON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SKELETON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKELETON_HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SKELETON_HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SKELETON_HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SLIME_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SLIME), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNIFFER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SNIFFER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SNIFFER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW_GOLEM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SNOW_GOLEM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SNOW_GOLEM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SPIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SQUID_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.SQUID_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.SQUID), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRAY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.STRAY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.STRAY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.STRIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.STRIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TADPOLE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.TADPOLE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRADER_LLAMA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.TRADER_LLAMA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.TRADER_LLAMA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.TURTLE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.TURTLE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VEX_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.VEX_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.VEX), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.VILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.VILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VINDICATOR_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.VINDICATOR_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.VINDICATOR), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WANDERING_TRADER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WANDERING_TRADER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WANDERING_TRADER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARDEN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WARDEN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WARDEN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITCH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WITCH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WITCH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WITHER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WITHER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SKELETON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WITHER_SKELETON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WITHER_SKELETON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOLF_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOLF_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.WOLF), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ZOGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ZOGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ZOMBIE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_VILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.spawnEgg(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIFIED_PIGLIN), this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapCompostables() {
            this.registerable.register(ItemKeys.OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZALEA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AZALEA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZALEA_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_OAK_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_SPRUCE_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BIRCH_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_JUNGLE_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ACACIA_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CHERRY_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DARK_OAK_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_SAPLING)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_PALE_OAK_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PROPAGULE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_PROPAGULE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PROPAGULE)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_MANGROVE_PROPAGULE))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHORT_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SHORT_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHORT_GRASS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.KELP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.KELP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.KELP)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSS_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSS_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSS_CARPET)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_MOSS_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_MOSS_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_MOSS_CARPET)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_PETALS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_PETALS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_PETALS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.HANGING_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HANGING_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HANGING_ROOTS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMALL_DRIPLEAF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMALL_DRIPLEAF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMALL_DRIPLEAF)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHEAT_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemKeys.WHEAT_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHEAT)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemKeys.PUMPKIN_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PUMPKIN_STEM)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemKeys.MELON_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MELON_STEM)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.TORCHFLOWER_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemKeys.TORCHFLOWER_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TORCHFLOWER_CROP)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PITCHER_POD, create(
                ItemDisplay.Builder.forItem(ItemKeys.PITCHER_POD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PITCHER_CROP)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemKeys.BEETROOT_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEETROOTS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_ROOTS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEAGRASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SEAGRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEAGRASS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_HANGING_MOSS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_HANGING_MOSS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_HANGING_MOSS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WILDFLOWERS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WILDFLOWERS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WILDFLOWERS)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUSH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUSH)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIREFLY_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FIREFLY_BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FIREFLY_BUSH)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.CACTUS_FLOWER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CACTUS_FLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CACTUS_FLOWER)))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWERING_AZALEA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FLOWERING_AZALEA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWERING_AZALEA_LEAVES)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_SPROUTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_SPROUTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_SPROUTS)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEEPING_VINES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WEEPING_VINES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEEPING_VINES)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.TWISTING_VINES, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TWISTING_VINES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TWISTING_VINES)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUGAR_CANE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SUGAR_CANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUGAR_CANE)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.VINE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.VINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VINE)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_LICHEN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GLOW_LICHEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLOW_LICHEN)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.TALL_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TALL_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TALL_GRASS)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CACTUS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CACTUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CACTUS)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CACTUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIED_KELP_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DRIED_KELP_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRIED_KELP_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.FIFTY_FIFTY))
                    .with(FuelItemComponent.of(FuelTimes.DRIED_KELP_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.FERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FERN)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_FERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILY_PAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LILY_PAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILY_PAD), BlockItemComponent.Pass.FLUID))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_WART, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHER_WART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_WART)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.COCOA_BEANS, create(
                ItemDisplay.Builder.forItem(ItemKeys.COCOA_BEANS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COCOA)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIG_DRIPLEAF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIG_DRIPLEAF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIG_DRIPLEAF)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PUMPKIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PUMPKIN)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARVED_PUMPKIN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CARVED_PUMPKIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARVED_PUMPKIN)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(EquipmentItemComponent.of(EquippableComponent.builder(EquipmentSlot.HEAD)
                        .swappable(false)
                        .cameraOverlay(Identifier.ofVanilla("misc/pumpkinblur"))
                        .build()))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_CARVED_PUMPKIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MELON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MELON)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEA_PICKLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SEA_PICKLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEA_PICKLE)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHEAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.WHEAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.DANDELION, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DANDELION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DANDELION)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DANDELION))
                    .build()
            ));
            this.registerable.register(ItemKeys.OPEN_EYEBLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OPEN_EYEBLOSSOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OPEN_EYEBLOSSOM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.BLINDNESS), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_OPEN_EYEBLOSSOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLOSED_EYEBLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CLOSED_EYEBLOSSOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CLOSED_EYEBLOSSOM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.NAUSEA), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CLOSED_EYEBLOSSOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.POPPY, create(
                ItemDisplay.Builder.forBlock(ItemKeys.POPPY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POPPY)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_POPPY))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_ORCHID, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_ORCHID).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_ORCHID)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BLUE_ORCHID))
                    .build()
            ));
            this.registerable.register(ItemKeys.ALLIUM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ALLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ALLIUM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.FIRE_RESISTANCE), 80)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ALLIUM))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZURE_BLUET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AZURE_BLUET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZURE_BLUET)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.BLINDNESS), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_AZURE_BLUET))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_TULIP)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_RED_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_TULIP)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ORANGE_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_TULIP)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WHITE_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_TULIP)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_PINK_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXEYE_DAISY, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OXEYE_DAISY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXEYE_DAISY)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_OXEYE_DAISY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CORNFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CORNFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CORNFLOWER)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.JUMP_BOOST), 120)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CORNFLOWER))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILY_OF_THE_VALLEY, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LILY_OF_THE_VALLEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILY_OF_THE_VALLEY)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 240)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_LILY_OF_THE_VALLEY))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_ROSE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WITHER_ROSE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WITHER_ROSE)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WITHER), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WITHER_ROSE))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZALEA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AZALEA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZALEA)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_AZALEA_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUNFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SUNFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUNFLOWER)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILAC, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LILAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILAC)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROSE_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ROSE_BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ROSE_BUSH)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.PEONY, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PEONY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PEONY)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.LARGE_FERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LARGE_FERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LARGE_FERN)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPORE_BLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPORE_BLOSSOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPORE_BLOSSOM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_MUSHROOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_MUSHROOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_MUSHROOM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BROWN_MUSHROOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_MUSHROOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_MUSHROOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_MUSHROOM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_RED_MUSHROOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FUNGUS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_FUNGUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FUNGUS)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CRIMSON_FUNGUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FUNGUS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_FUNGUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FUNGUS)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WARPED_FUNGUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRIMSON_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_ROOTS)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CRIMSON_ROOTS))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_ROOTS)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WARPED_ROOTS))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MOSS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSS_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_MOSS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_MOSS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_MOSS_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSHROOM_STEM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MUSHROOM_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUSHROOM_STEM)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHROOMLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SHROOMLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHROOMLIGHT)))
                    .with(CompostableItemComponent.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_WART_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHER_WART_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_WART_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_WART_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WARPED_WART_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_WART_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.HAY_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.HAY_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HAY_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWERING_AZALEA, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FLOWERING_AZALEA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWERING_AZALEA)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_FLOWERING_AZALEA_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.TORCHFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TORCHFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TORCHFLOWER)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_TORCHFLOWER))
                    .build()
            ));
            this.registerable.register(ItemKeys.PITCHER_PLANT, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PITCHER_PLANT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PITCHER_PLANT)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_MUSHROOM_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_MUSHROOM_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_MUSHROOM_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_MUSHROOM_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_MUSHROOM_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_MUSHROOM_BLOCK)))
                    .with(CompostableItemComponent.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAKE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CAKE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAKE)))
                    .with(CompostableItemComponent.of(CompostChances.GUARANTEED))
                    .build()
            ));
        }

        private void bootstrapEquipment() {
            this.bootstrapArmor();
            this.bootstrapSkulls();
            this.registerable.register(ItemKeys.ELYTRA, create(
                ItemDisplay.Builder.forItem(ItemKeys.ELYTRA)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.ofPreserved(432))
                    .with(GliderItemComponent.of(ItemPredicate.Builder.create()
                        .components(ComponentsPredicate.Builder.create()
                            .partial(
                                ComponentPredicateTypes.DAMAGE,
                                DamagePredicate.durability(NumberRange.IntRange.atLeast(2))
                            ).build()
                        ).build()
                    ))
                    .with(EquipmentItemComponent.of(EquippableComponent.builder(EquipmentSlot.CHEST)
                        .swappable(true)
                        .equipSound(this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_ELYTRA))
                        .model(EquipmentAssetKeys.ELYTRA)
                        .build()))
                    .with(RepairableItemComponent.of(RegistryEntryList.of(
                        this.items.getOrThrow(ItemKeys.PHANTOM_MEMBRANE)
                    )))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHIELD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHIELD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(UseableItemComponent.builder()
                        .useIndefinitely()
                        .animation(UseAction.BLOCK)
                        .build()
                    )
                    .with(DamageableItemComponent.of(336))
                    .with(AttackBlockingItemComponent.of(new BlocksAttacksComponent(
                        0.25f,
                        1.0f,
                        List.of(
                            new BlocksAttacksComponent.DamageReduction(90.0f, Optional.empty(), 0.0f, 1.0f)
                        ),
                        new BlocksAttacksComponent.ItemDamage(3.0f, 1.0f, 1.0f),
                        Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                        Optional.of(this.soundEvents.getOrThrow(SoundEventKeys.SHIELD_BLOCK)),
                        Optional.of(this.soundEvents.getOrThrow(SoundEventKeys.SHIELD_BREAK))
                    )))
                    .with(EquipmentItemComponent.of(EquippableComponent.builder(EquipmentSlot.OFFHAND)
                        .equipSound(this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC))
                        .swappable(false)
                        .build()
                    ))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(BannerPatternHolderItemComponent.of())
                    .build()
            ));
        }

        private void bootstrapArmor() {
            this.registerable.register(ItemKeys.LEATHER_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.LEATHER, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.LEATHER, EquipmentType.CHESTPLATE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.LEATHER, EquipmentType.LEGGINGS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.LEATHER, EquipmentType.BOOTS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHAINMAIL_HELMET)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAIN, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.CHAIN, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHAINMAIL_CHESTPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAIN, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.CHAIN, EquipmentType.CHESTPLATE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHAINMAIL_LEGGINGS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAIN, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.CHAIN, EquipmentType.LEGGINGS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHAINMAIL_BOOTS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAIN, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.CHAIN, EquipmentType.BOOTS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.IRON, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.IRON, EquipmentType.CHESTPLATE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.IRON, EquipmentType.LEGGINGS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.IRON, EquipmentType.BOOTS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.DIAMOND, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.DIAMOND, EquipmentType.CHESTPLATE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.DIAMOND, EquipmentType.LEGGINGS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.DIAMOND, EquipmentType.BOOTS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.GOLD, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_GOLDEN_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.GOLD, EquipmentType.CHESTPLATE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_GOLDEN_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.GOLD, EquipmentType.LEGGINGS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_GOLDEN_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.GOLD, EquipmentType.BOOTS))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_GOLDEN_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.NETHERITE, EquipmentType.HELMET))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, EquipmentType.CHESTPLATE),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.NETHERITE, EquipmentType.CHESTPLATE))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, EquipmentType.LEGGINGS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.NETHERITE, EquipmentType.LEGGINGS))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, EquipmentType.BOOTS),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.NETHERITE, EquipmentType.BOOTS))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_HELMET, create(
                ItemDisplay.Builder.forItem(ItemKeys.TURTLE_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.TURTLE_SCUTE, EquipmentType.HELMET),
                ItemComponentSet.builder()
                    .with(EquipmentItemComponent.forArmor(ArmorMaterials.TURTLE_SCUTE, EquipmentType.HELMET))
                    .with(EnchantableItemComponent.of(ArmorMaterials.TURTLE_SCUTE))
                    .with(RepairableItemComponent.of(this.items.getOrThrow(ItematicItemTags.REPAIRS_TURTLE_ARMOR)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, EquipmentType.BODY),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EquipmentItemComponent.ofHorseArmor(ArmorMaterials.LEATHER, this.soundEvents, this.entityTypes))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, EquipmentType.BODY),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EquipmentItemComponent.ofHorseArmor(ArmorMaterials.IRON, this.soundEvents, this.entityTypes))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLDEN_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, EquipmentType.BODY),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EquipmentItemComponent.ofHorseArmor(ArmorMaterials.GOLD, this.soundEvents, this.entityTypes))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, EquipmentType.BODY),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EquipmentItemComponent.ofHorseArmor(ArmorMaterials.DIAMOND, this.soundEvents, this.entityTypes))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOLF_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.WOLF_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.ARMADILLO_SCUTE, EquipmentType.BODY),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(
                        EquipmentType.BODY.getMaxDamage(ArmorMaterials.ARMADILLO_SCUTE.durability()),
                        this.soundEvents.getOrThrow(SoundEventKeys.WOLF_ARMOR_BREAK)
                    ))
                    .with(RepairableItemComponent.of(
                        this.items.getOrThrow(ArmorMaterials.ARMADILLO_SCUTE.repairIngredient())
                    ))
                    .with(EquipmentItemComponent.of(EquippableComponent.builder(EquipmentSlot.BODY)
                        .equipSound(ArmorMaterials.ARMADILLO_SCUTE.equipSound())
                        .model(ArmorMaterials.ARMADILLO_SCUTE.assetId())
                        .allowedEntities(RegistryEntryList.of(
                            this.entityTypes.getOrThrow(EntityTypeKeys.WOLF)
                        ))
                        .build()
                    ))
                    .with(DyeableItemComponent.of(0x000000))
                    .build()
            ));
        }

        private void bootstrapSkulls() {
            this.registerable.register(ItemKeys.SKELETON_SKULL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.SKELETON_SKULL),
                        this.blocks.getOrThrow(BlockKeys.SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SKELETON_SKULL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WITHER_SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.WITHER_SKELETON_SKULL),
                        this.blocks.getOrThrow(BlockKeys.WITHER_SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.PLAYER_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PLAYER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.PLAYER_HEAD),
                        this.blocks.getOrThrow(BlockKeys.PLAYER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ZOMBIE_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.ZOMBIE_HEAD),
                        this.blocks.getOrThrow(BlockKeys.ZOMBIE_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CREEPER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.CREEPER_HEAD),
                        this.blocks.getOrThrow(BlockKeys.CREEPER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DRAGON_HEAD)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.DRAGON_HEAD),
                        this.blocks.getOrThrow(BlockKeys.DRAGON_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PIGLIN_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.forSkull(
                        this.blocks.getOrThrow(BlockKeys.PIGLIN_HEAD),
                        this.blocks.getOrThrow(BlockKeys.PIGLIN_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
        }

        private void bootstrapFuel() {
            this.registerable.register(ItemKeys.COAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COAL_BLOCK)))
                    .with(FuelItemComponent.of(FuelTimes.COAL_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_ROD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLAZE_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FuelTimes.BLAZE_ROD))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAL, create(
                ItemDisplay.Builder.forItem(ItemKeys.COAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FuelTimes.COAL))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHARCOAL, create(
                ItemDisplay.Builder.forItem(ItemKeys.CHARCOAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FuelTimes.COAL))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SPRUCE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.SPRUCE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BIRCH_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.BIRCH_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.JUNGLE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.JUNGLE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ACACIA_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.ACACIA_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CHERRY_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.CHERRY_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DARK_OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.DARK_OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PALE_OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.PALE_OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MANGROVE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.MANGROVE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BAMBOO_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.BAMBOO_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.OAK_SIGN), this.blocks.getOrThrow(BlockKeys.OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SPRUCE_SIGN), this.blocks.getOrThrow(BlockKeys.SPRUCE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BIRCH_SIGN), this.blocks.getOrThrow(BlockKeys.BIRCH_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.JUNGLE_SIGN), this.blocks.getOrThrow(BlockKeys.JUNGLE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ACACIA_SIGN), this.blocks.getOrThrow(BlockKeys.ACACIA_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CHERRY_SIGN), this.blocks.getOrThrow(BlockKeys.CHERRY_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SIGN), this.blocks.getOrThrow(BlockKeys.DARK_OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PALE_OAK_SIGN), this.blocks.getOrThrow(BlockKeys.PALE_OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MANGROVE_SIGN), this.blocks.getOrThrow(BlockKeys.MANGROVE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BAMBOO_SIGN), this.blocks.getOrThrow(BlockKeys.BAMBOO_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_PLANKS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_BLOCK)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_SPRUCE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BIRCH_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_JUNGLE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_ACACIA_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CHERRY_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_DARK_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_PALE_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_PALE_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_PALE_OAK_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_MANGROVE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_LOG)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_SPRUCE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BIRCH_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_JUNGLE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_ACACIA_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CHERRY_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_DARK_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_PALE_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_PALE_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_PALE_OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_MANGROVE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BAMBOO_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.STRIPPED_BAMBOO_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BAMBOO_BLOCK)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_WOOD)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_FENCE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_STAIRS)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_TRAPDOOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_FENCE_GATE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOOKSHELF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BOOKSHELF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BOOKSHELF)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_BOOKSHELF, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHISELED_BOOKSHELF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_BOOKSHELF)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.LECTERN, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LECTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LECTERN)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHEST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHEST)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRAPPED_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TRAPPED_CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRAPPED_CHEST)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.LADDER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LADDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LADDER)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRAFTING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CRAFTING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRAFTING_TABLE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUKEBOX, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUKEBOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUKEBOX)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.NOTE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NOTE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NOTE_BLOCK)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.LOOM, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LOOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LOOM)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPOSTER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.COMPOSTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COMPOSTER)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.BARREL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BARREL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BARREL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARTOGRAPHY_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CARTOGRAPHY_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARTOGRAPHY_TABLE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLETCHING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.FLETCHING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLETCHING_TABLE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMITHING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SMITHING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMITHING_TABLE)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.DAYLIGHT_DETECTOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DAYLIGHT_DETECTOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DAYLIGHT_DETECTOR)))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_DOOR)))
                    .with(FuelItemComponent.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_SLAB)))
                    .with(FuelItemComponent.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DEAD_BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUSH)))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DEAD_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.OAK_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SPRUCE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BIRCH_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.JUNGLE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ACACIA_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CHERRY_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.DARK_OAK_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.PALE_OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PALE_OAK_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PALE_OAK_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MANGROVE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_BUTTON)))
                    .with(FuelItemComponent.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemKeys.STICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.STICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FuelTimes.SMALL_WOODEN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOWL, create(
                ItemDisplay.Builder.forItem(ItemKeys.BOWL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FuelTimes.SMALL_WOODEN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BAMBOO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO)))
                    .with(FuelItemComponent.of(FuelTimes.BAMBOO))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BAMBOO))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCAFFOLDING, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SCAFFOLDING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCAFFOLDING)))
                    .with(FuelItemComponent.of(FuelTimes.SCAFFOLDING))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_WOOL)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.WHITE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.ORANGE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.MAGENTA)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.LIGHT_BLUE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.YELLOW)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.LIME)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.PINK)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.GRAY)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.LIGHT_GRAY)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.CYAN)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.PURPLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.BLUE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.BROWN)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.GREEN)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.RED)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CARPET)))
                    .with(FuelItemComponent.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemComponent.of(EquippableComponent.ofCarpet(DyeColor.BLACK)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHORT_DRY_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.SHORT_DRY_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHORT_DRY_GRASS)))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.TALL_DRY_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.TALL_DRY_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TALL_DRY_GRASS)))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEAF_LITTER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LEAF_LITTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LEAF_LITTER)))
                    .with(FuelItemComponent.of(FuelTimes.PLANT))
                    .with(CompostableItemComponent.of(CompostChances.SMALL))
                    .build()
            ));
        }

        private void bootstrapProjectiles() {
            this.registerable.register(ItemKeys.ARROW, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.ARROW)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOWBALL, create(
                ItemDisplay.Builder.forItem(ItemKeys.SNOWBALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.SNOWBALL)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(
                        this.entityTypes.getOrThrow(EntityTypeKeys.EGG),
                        ComponentChanges.builder()
                            .add(
                                DataComponentTypes.CHICKEN_VARIANT,
                                new LazyRegistryEntryReference<>(
                                    this.chickenVariants.getOrThrow(ChickenVariants.TEMPERATE)
                                )
                            )
                            .build()
                    ))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLUE_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(
                        this.entityTypes.getOrThrow(EntityTypeKeys.EGG),
                        ComponentChanges.builder()
                            .add(
                                DataComponentTypes.CHICKEN_VARIANT,
                                new LazyRegistryEntryReference<>(
                                    this.chickenVariants.getOrThrow(ChickenVariants.COLD)
                                )
                            )
                            .build()
                    ))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemKeys.BROWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(
                        this.entityTypes.getOrThrow(EntityTypeKeys.EGG),
                        ComponentChanges.builder()
                            .add(
                                DataComponentTypes.CHICKEN_VARIANT,
                                new LazyRegistryEntryReference<>(
                                    this.chickenVariants.getOrThrow(ChickenVariants.WARM)
                                )
                            )
                            .build()
                    ))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_PEARL, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENDER_PEARL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.ENDER_PEARL)))
                    .with(CooldownItemComponent.of(20))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ThrowableItemComponent.of())
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.EYE_OF_ENDER)))
                    .with(PreventUseWhenUsedOnTargetItemComponent.forBlock())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckLootConditionUtil.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.create()
                                .block(BlockPredicate.Builder.create()
                                    .blocks(this.blocks, this.blocks.getOrThrow(BlockKeys.END_PORTAL_FRAME).value())
                                    .state(StatePredicate.Builder.create()
                                        .exactMatch(Properties.EYE, false)))
                        ),
                        PassingSequenceHandler.builder()
                            .add(ModifyBlockStateAction.builder(PositionTarget.INTERACTED)
                                .property(Properties.EYE, true)
                                .pushEntitiesUpwards()
                                .build())
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventKeys.END_PORTAL_FRAME_FILL), SoundCategory.BLOCKS))
                            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, ParticleTypes.SMOKE)
                                .count(16)
                                .offset(Vec3dProvider.of(
                                    -0.1875d, 0.1875d,
                                    0.8125d, 0.8125d,
                                    -0.1875d, 0.1875d))
                                .build())
                            .addOptional(LightEndPortalAction.of(PositionTarget.INTERACTED))
                    ))
                    .add(ItemEvents.THROW_PROJECTILE, ActionEntry.of(
                        PlaySoundAction.builder(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventKeys.ENDER_EYE_LAUNCH), SoundCategory.NEUTRAL)
                            .pitch(0.33f, 0.5f)
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPERIENCE_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.EXPERIENCE_BOTTLE)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ThrowableItemComponent.of(0.7f, -20.0f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.EXPERIENCE_BOTTLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CHARGE, create(
                ItemDisplay.Builder.forItem(ItemKeys.FIRE_CHARGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.LARGE_BALL))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.SMALL_FIREBALL)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_CHARGE)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DecrementItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventKeys.FIRE_CHARGE_USE), SoundCategory.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.WIND_CHARGE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WIND_CHARGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.WIND_CHARGE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .with(CooldownItemComponent.of(10))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIREWORK_ROCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.FIREWORK_ROCKET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkItemComponent.INSTANCE)
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.FIREWORK_ROCKET)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_FIREWORK_ROCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPLASH_POTION, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPLASH_POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PotionHolderItemComponent.of(1.0f))
                    .with(ThrowableItemComponent.of(0.5f, -20.0f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.SPLASH_POTION)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPECTRAL_ARROW, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPECTRAL_ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.SPECTRAL_ARROW)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TIPPED_ARROW, create(
                ItemDisplay.Builder.forItem(ItemKeys.TIPPED_ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(PotionHolderItemComponent.of(0.125f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.ARROW)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LINGERING_POTION, create(
                ItemDisplay.Builder.forItem(ItemKeys.LINGERING_POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PotionHolderItemComponent.of(0.25f))
                    .with(ThrowableItemComponent.of(0.5f, -20.0f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.LINGERING_POTION)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
        }

        private void bootstrapDyes() {
            this.registerable.register(ItemKeys.WHITE_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WHITE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.ORANGE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MAGENTA_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIGHT_BLUE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.YELLOW_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIME_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PINK_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GRAY_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIGHT_GRAY_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.CYAN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PURPLE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLUE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BROWN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GREEN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.RED_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_DYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLACK_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapRecords() {
            this.registerable.register(ItemKeys.MUSIC_DISC_13, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_13)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.THIRTEEN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CAT, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CAT)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CAT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_BLOCKS, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_BLOCKS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.BLOCKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CHIRP, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CHIRP)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CHIRP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CREATOR, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CREATOR)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CREATOR_MUSIC_BOX, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_CREATOR_MUSIC_BOX)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR_MUSIC_BOX)))
                    .build()
            ));

            this.registerable.register(ItemKeys.MUSIC_DISC_FAR, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_FAR)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_MALL, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_MALL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_MELLOHI, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_MELLOHI)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MELLOHI)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_STAL, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_STAL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_STRAD, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_STRAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STRAD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_WARD, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_WARD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WARD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_11, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_11)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.ELEVEN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_WAIT, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_WAIT)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WAIT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_OTHERSIDE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_OTHERSIDE)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.OTHERSIDE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_RELIC, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_RELIC)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.RELIC)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_PIGSTEP, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_PIGSTEP)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PIGSTEP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_PRECIPICE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_PRECIPICE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PRECIPICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_5, create(
                ItemDisplay.Builder.forItem(ItemKeys.MUSIC_DISC_5)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FIVE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DISC_FRAGMENT_5, create(
                ItemDisplay.Builder.forItem(ItemKeys.DISC_FRAGMENT_5)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.description(ItemKeys.DISC_FRAGMENT_5))
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
        }

        private void bootstrapBuckets() {
            this.registerable.register(ItemKeys.BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.drainFluid(this.dispenseBehaviors))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WATER_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.WATER_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluid(this.fluids.getOrThrow(FluidKeys.WATER), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAVA_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.LAVA_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluid(this.fluids.getOrThrow(FluidKeys.LAVA), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_LAVA), this.items, this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FuelTimes.LAVA, this.items.getOrThrow(ItemKeys.BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POWDER_SNOW_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.POWDER_SNOW_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeBlock(this.blocks.getOrThrow(BlockKeys.POWDER_SNOW), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_POWDER_SNOW), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.PUFFERFISH_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.SALMON_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.SALMON), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COD_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.COD_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.COD), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.TROPICAL_FISH_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.AXOLOTL_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.AXOLOTL_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_AXOLOTL), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TADPOLE_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemKeys.TADPOLE_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.placeFluidWithEntity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_TADPOLE), this.items, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapSmithingTemplates() {
            this.registerable.register(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingUpgrade(Identifier.ofVanilla("netherite_upgrade")))
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.ITEM_UPGRADE))
                    .build()
            ));
            this.registerable.register(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.EPIC)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SmithingTemplateProviderItemComponent.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
        }

        private void bootstrapBanners() {
            this.bootstrapBannerPatterns();
            this.registerable.register(ItemKeys.WHITE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.WHITE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WHITE_BANNER), this.blocks.getOrThrow(BlockKeys.WHITE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ORANGE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ORANGE_BANNER), this.blocks.getOrThrow(BlockKeys.ORANGE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.MAGENTA_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MAGENTA_BANNER), this.blocks.getOrThrow(BlockKeys.MAGENTA_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_BLUE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_BANNER), this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.YELLOW_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.YELLOW_BANNER), this.blocks.getOrThrow(BlockKeys.YELLOW_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIME_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIME_BANNER), this.blocks.getOrThrow(BlockKeys.LIME_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PINK_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PINK_BANNER), this.blocks.getOrThrow(BlockKeys.PINK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GRAY_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.GRAY_BANNER), this.blocks.getOrThrow(BlockKeys.GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.LIGHT_GRAY_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_BANNER), this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.CYAN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CYAN_BANNER), this.blocks.getOrThrow(BlockKeys.CYAN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.PURPLE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PURPLE_BANNER), this.blocks.getOrThrow(BlockKeys.PURPLE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLUE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BLUE_BANNER), this.blocks.getOrThrow(BlockKeys.BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BROWN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BROWN_BANNER), this.blocks.getOrThrow(BlockKeys.BROWN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.GREEN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.GREEN_BANNER), this.blocks.getOrThrow(BlockKeys.GREEN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.RED_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.RED_BANNER), this.blocks.getOrThrow(BlockKeys.RED_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemKeys.BLACK_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BLACK_BANNER), this.blocks.getOrThrow(BlockKeys.BLACK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapBannerPatterns() {
            this.registerable.register(ItemKeys.FLOWER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLOWER_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.FLOWER_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.CREEPER_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.CREEPER_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKULL_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.SKULL_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.SKULL_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOJANG_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.MOJANG_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.MOJANG_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOBE_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOBE_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.GLOBE_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.PIGLIN_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.PIGLIN_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLOW_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.FLOW_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUSTER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.GUSTER_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.GUSTER_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIELD_MASONED_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.FIELD_MASONED_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.FIELD_MASONED_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.BORDURE_INDENTED_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemKeys.BORDURE_INDENTED_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.BORDURE_INDENTED_PATTERN_ITEM))
                    .build()
            ));
        }

        private void bootstrapDecoratedPotPatterns() {
            this.registerable.register(ItemKeys.BRICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.BRICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLANK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANGLER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.ANGLER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ANGLER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARCHER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARCHER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARCHER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMS_UP_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARMS_UP_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARMS_UP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLADE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLADE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLADE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREWER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BREWER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BREWER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BURN_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BURN_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BURN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DANGER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.DANGER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.DANGER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPLORER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.EXPLORER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.EXPLORER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLOW_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FLOW)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FRIEND_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.FRIEND_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FRIEND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUSTER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.GUSTER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.GUSTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEART_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.HEART_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEART)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEARTBREAK_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.HEARTBREAK_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEARTBREAK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOWL_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.HOWL_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HOWL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MINER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.MINER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MINER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOURNER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.MOURNER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MOURNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PLENTY_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.PLENTY_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PLENTY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRIZE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.PRIZE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PRIZE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCRAPE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SCRAPE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SCRAPE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEAF_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHEAF_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHEAF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHELTER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHELTER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHELTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKULL_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SKULL_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SKULL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNORT_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemKeys.SNORT_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SNORT)))
                    .build()
            ));
        }

        private void bootstrapImmuneToDamage() {
            this.registerable.register(ItemKeys.ANCIENT_DEBRIS, create(
                ItemDisplay.Builder.forBlock(ItemKeys.ANCIENT_DEBRIS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANCIENT_DEBRIS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemKeys.NETHERITE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHERITE_BLOCK)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_INGOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.NETHERITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SCRAP, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHERITE_SCRAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_STAR, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHER_STAR)
                    .rarity(Rarity.RARE)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_EXPLOSION))
                    .build()
            ));
        }

        private void bootstrapTrimMaterialProviders() {
            this.registerable.register(ItemKeys.REDSTONE, create(
                ItemDisplay.Builder.forItem(ItemKeys.REDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_WIRE)))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.REDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND, create(
                ItemDisplay.Builder.forItem(ItemKeys.DIAMOND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.DIAMOND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD, create(
                ItemDisplay.Builder.forItem(ItemKeys.EMERALD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.EMERALD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_LAZULI, create(
                ItemDisplay.Builder.forItem(ItemKeys.LAPIS_LAZULI).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.LAPIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ, create(
                ItemDisplay.Builder.forItem(ItemKeys.QUARTZ).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.QUARTZ)))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_SHARD, create(
                ItemDisplay.Builder.forItem(ItemKeys.AMETHYST_SHARD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.AMETHYST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_INGOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.IRON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_INGOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.COPPER_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_INGOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLD_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.GOLD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESIN_BRICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.RESIN_BRICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(TrimMaterialProviderItemComponent.of(this.trimMaterials.getOrThrow(ArmorTrimMaterials.RESIN)))
                    .build()
            ));
        }

        private void bootstrapMiscellaneous() {
            this.registerable.register(ItemKeys.AIR, create(
                ItemDisplay.Builder.forBlock(ItemKeys.AIR).build()
            ));
            this.registerable.register(ItemKeys.SADDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.SADDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EquipmentItemComponent.of(EquippableComponent.builder(EquipmentSlot.SADDLE)
                        .equipSound(SoundEvents.ENTITY_HORSE_SADDLE)
                        .model(EquipmentAssetKeys.SADDLE)
                        .allowedEntities(this.entityTypes.getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
                        .equipOnInteract(true)
                        .build()
                    ))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_SCUTE, create(
                ItemDisplay.Builder.forItem(ItemKeys.TURTLE_SCUTE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMADILLO_SCUTE, create(
                ItemDisplay.Builder.forItem(ItemKeys.ARMADILLO_SCUTE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_IRON, create(
                ItemDisplay.Builder.forItem(ItemKeys.RAW_IRON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_COPPER, create(
                ItemDisplay.Builder.forItem(ItemKeys.RAW_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_GOLD, create(
                ItemDisplay.Builder.forItem(ItemKeys.RAW_GOLD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FEATHER, create(
                ItemDisplay.Builder.forItem(ItemKeys.FEATHER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.BURST))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUNPOWDER, create(
                ItemDisplay.Builder.forItem(ItemKeys.GUNPOWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLINT, create(
                ItemDisplay.Builder.forItem(ItemKeys.FLINT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEATHER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLAY_BALL, create(
                ItemDisplay.Builder.forItem(ItemKeys.CLAY_BALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PAPER, create(
                ItemDisplay.Builder.forItem(ItemKeys.PAPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOOK, create(
                ItemDisplay.Builder.forItem(ItemKeys.BOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EnchantableItemComponent.ofTransforming(1, this.items.getOrThrow(ItemKeys.ENCHANTED_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_BALL, create(
                ItemDisplay.Builder.forItem(ItemKeys.SLIME_BALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPASS, create(
                ItemDisplay.Builder.forItem(ItemKeys.COMPASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckLootConditionUtil.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.create()
                                .block(BlockPredicate.Builder.create()
                                    .blocks(this.blocks, this.blocks.getOrThrow(BlockKeys.LODESTONE).value()))
                        ),
                        PassingSequenceHandler.builder()
                            .add(ModifyItemAction.of(
                                ItemStackTarget.TOOL,
                                SplitItemModifier.builder(1),
                                SetItemPointerLocationItemModifier.builder(PositionTarget.INTERACTED),
                                SetNameLootFunction.builder(
                                    Text.translatable(Util.createTranslationKey("item", Identifier.ofVanilla("lodestone_compass"))),
                                    SetNameLootFunction.Target.ITEM_NAME
                                ),
                                SetComponentsLootFunction.builder(
                                    DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
                                    true
                                )
                            ))
                            .add(PlaySoundAction.of(
                                PositionTarget.INTERACTED,
                                this.soundEvents.getOrThrow(SoundEventKeys.LODESTONE_COMPASS_LOCK),
                                SoundCategory.PLAYERS
                            ))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.RECOVERY_COMPASS, create(
                ItemDisplay.Builder.forItem(ItemKeys.RECOVERY_COMPASS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.WHITE_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.ORANGE_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.MAGENTA_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIGHT_BLUE_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.YELLOW_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIME_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PINK_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GRAY_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.LIGHT_GRAY_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.CYAN_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PURPLE_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLUE_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BROWN_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GREEN_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.RED_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLACK_BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(ItemHolderItemComponent.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLOCK, create(
                ItemDisplay.Builder.forItem(ItemKeys.CLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPYGLASS, create(
                ItemDisplay.Builder.forItem(ItemKeys.SPYGLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ZoomItemComponent.of(SpyglassItem.FOV_MULTIPLIER, this.soundEvents.getOrThrow(SoundEventKeys.SPYGLASS_USE), this.soundEvents.getOrThrow(SoundEventKeys.SPYGLASS_STOP_USING)))
                    .with(UseableItemComponent.builder()
                        .useFor(SpyglassItem.MAX_USE_TIME)
                        .animation(UseAction.SPYGLASS)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOWSTONE_DUST, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOWSTONE_DUST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.INK_SAC, create(
                ItemDisplay.Builder.forItem(ItemKeys.INK_SAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(this.blocks, false))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_INK_SAC, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLOW_INK_SAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(this.blocks, true))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE_MEAL, create(
                ItemDisplay.Builder.forItem(ItemKeys.BONE_MEAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(FertilizeAction.of(PositionTarget.INTERACTED))
                            .add(InvokeGameEventAction.of(
                                GameEvent.ITEM_INTERACT_FINISH,
                                PositionTarget.ORIGIN,
                                LootContext.EntityTarget.THIS
                            ))
                            .add(DecrementItemAction.of(1))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE, create(
                ItemDisplay.Builder.forItem(ItemKeys.BONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUGAR, create(
                ItemDisplay.Builder.forItem(ItemKeys.SUGAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FILLED_MAP, create(
                ItemDisplay.Builder.forItem(ItemKeys.FILLED_MAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(MapHolderItemComponent.INSTANCE)
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(MarkBannerOnItemAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.GHAST_TEAR, create(
                ItemDisplay.Builder.forItem(ItemKeys.GHAST_TEAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_NUGGET, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOLD_NUGGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.STAR))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLASS_BOTTLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.GLASS_BOTTLE)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckLootConditionUtil.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.create()
                                .fluid(FluidPredicate.Builder.create()
                                    .tag(this.fluids.getOrThrow(FluidTags.WATER)))
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(ExchangeItemAction.of(
                                this.items.getOrThrow(ItemKeys.POTION),
                                ComponentChanges.builder()
                                    .add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(this.potions.getOrThrow(PotionKeys.WATER)))
                                    .build()))
                            .add(InvokeGameEventAction.of(GameEvent.FLUID_PICKUP, PositionTarget.INTERACTED, LootContext.EntityTarget.THIS))
                            .add(PlaySoundAction.of(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventKeys.BOTTLE_FILL), SoundCategory.NEUTRAL))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.FERMENTED_SPIDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemKeys.FERMENTED_SPIDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_POWDER, create(
                ItemDisplay.Builder.forItem(ItemKeys.BLAZE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_CREAM, create(
                ItemDisplay.Builder.forItem(ItemKeys.MAGMA_CREAM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLISTERING_MELON_SLICE, create(
                ItemDisplay.Builder.forItem(ItemKeys.GLISTERING_MELON_SLICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.WRITABLE_BOOK, create(
                ItemDisplay.Builder.forItem(ItemKeys.WRITABLE_BOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(WritableItemComponent.of(this.items.getOrThrow(ItemKeys.WRITTEN_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WRITTEN_BOOK, create(
                ItemDisplay.Builder.forItem(ItemKeys.WRITTEN_BOOK)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(TextHolderItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.MAP, create(
                ItemDisplay.Builder.forItem(ItemKeys.MAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(MappableItemComponent.of(this.items.getOrThrow(ItemKeys.FILLED_MAP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIREWORK_STAR, create(
                ItemDisplay.Builder.forItem(ItemKeys.FIREWORK_STAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkExplosionHolderItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTED_BOOK, create(
                ItemDisplay.Builder.forItem(ItemKeys.ENCHANTED_BOOK)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EnchantmentHolderItemComponent.of(this.items.getOrThrow(ItemKeys.BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.NETHER_BRICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_SHARD, create(
                ItemDisplay.Builder.forItem(ItemKeys.PRISMARINE_SHARD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_CRYSTALS, create(
                ItemDisplay.Builder.forItem(ItemKeys.PRISMARINE_CRYSTALS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_FOOT, create(
                ItemDisplay.Builder.forItem(ItemKeys.RABBIT_FOOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_HIDE, create(
                ItemDisplay.Builder.forItem(ItemKeys.RABBIT_HIDE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEAD, create(
                ItemDisplay.Builder.forItem(ItemKeys.LEAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(AttachLeashedEntitiesOnBlockAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.NAME_TAG, create(
                ItemDisplay.Builder.forItem(ItemKeys.NAME_TAG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_ENTITY, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(SetEntityNameFromItemAction.of(LootContext.EntityTarget.ITEMATIC_TARGET_ENTITY))
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.POPPED_CHORUS_FRUIT, create(
                ItemDisplay.Builder.forItem(ItemKeys.POPPED_CHORUS_FRUIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_BREATH, create(
                ItemDisplay.Builder.forItem(ItemKeys.DRAGON_BREATH)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.TOTEM_OF_UNDYING, create(
                ItemDisplay.Builder.forItem(ItemKeys.TOTEM_OF_UNDYING)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.BEFORE_DEATH_HOLDER, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 900, 1),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.ABSORPTION), 100, 1),
                            new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.FIRE_RESISTANCE), 800, 0)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHULKER_SHELL, create(
                ItemDisplay.Builder.forItem(ItemKeys.SHULKER_SHELL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_NUGGET, create(
                ItemDisplay.Builder.forItem(ItemKeys.IRON_NUGGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.KNOWLEDGE_BOOK, create(
                ItemDisplay.Builder.forItem(ItemKeys.KNOWLEDGE_BOOK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(UnlockRecipesItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.DEBUG_STICK, create(
                ItemDisplay.Builder.forItem(ItemKeys.DEBUG_STICK)
                    .rarity(Rarity.EPIC)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DebugStickItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.PHANTOM_MEMBRANE, create(
                ItemDisplay.Builder.forItem(ItemKeys.PHANTOM_MEMBRANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.NAUTILUS_SHELL, create(
                ItemDisplay.Builder.forItem(ItemKeys.NAUTILUS_SHELL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEART_OF_THE_SEA, create(
                ItemDisplay.Builder.forItem(ItemKeys.HEART_OF_THE_SEA)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOAT_HORN, create(
                ItemDisplay.Builder.forItem(ItemKeys.GOAT_HORN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PlayableItemComponent.of(this.instruments.getOrThrow(Instruments.PONDER_GOAT_HORN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEYCOMB, create(
                ItemDisplay.Builder.forItem(ItemKeys.HONEYCOMB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.WAX_BLOCK)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        FirstToPassRequirementsSequenceHandler.builder()
                            .add(Actions.waxSign(this.blocks, true))
                            .add(PassingSequenceHandler.builder()
                                .add(WaxBlockAction.of(PositionTarget.INTERACTED))
                                .add(DecrementItemAction.of(1))
                                .add(SwingHandAction.of(LootContext.EntityTarget.THIS)))))
                    .build()
            ));
            this.registerable.register(ItemKeys.ECHO_SHARD, create(
                ItemDisplay.Builder.forItem(ItemKeys.ECHO_SHARD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIAL_KEY, create(
                ItemDisplay.Builder.forItem(ItemKeys.TRIAL_KEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.OMINOUS_TRIAL_KEY, create(
                ItemDisplay.Builder.forItem(ItemKeys.OMINOUS_TRIAL_KEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREEZE_ROD, create(
                ItemDisplay.Builder.forItem(ItemKeys.BREEZE_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
        }
    }
}
