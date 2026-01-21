package net.errorcraft.itematic.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.block.ComposterBlockUtil;
import net.errorcraft.itematic.block.entity.FurnaceBlockEntityUtil;
import net.errorcraft.itematic.component.type.ItemDamageRulesDataComponent;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.entity.ItematicEntityTypeTags;
import net.errorcraft.itematic.entity.effect.StatusEffectKeys;
import net.errorcraft.itematic.entity.initializer.initializers.*;
import net.errorcraft.itematic.fluid.FluidKeys;
import net.errorcraft.itematic.item.armor.ArmorMaterial;
import net.errorcraft.itematic.item.armor.ArmorMaterialKeys;
import net.errorcraft.itematic.item.color.colors.*;
import net.errorcraft.itematic.item.component.ItemComponentSet;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.component.components.*;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEventMap;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.item.holder.rule.rules.RejectItemHolderRule;
import net.errorcraft.itematic.item.pointer.Pointer;
import net.errorcraft.itematic.item.pointer.PointerKeys;
import net.errorcraft.itematic.item.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.item.shooter.method.methods.DirectShooterMethod;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.loot.predicate.SideCheckPredicate;
import net.errorcraft.itematic.mixin.component.type.BundleContentsComponentAccessor;
import net.errorcraft.itematic.mixin.item.*;
import net.errorcraft.itematic.potion.PotionKeys;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.util.Vec3dProvider;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionRequirements;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameters;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
import net.minecraft.entity.decoration.GlowItemFrameEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.entity.vehicle.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.*;
import net.minecraft.loot.condition.*;
import net.minecraft.loot.context.LootContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.FluidPredicate;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.*;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.math.Direction;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.event.GameEvent;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;

public class ItemUtil {
    public static final int UNSTACKABLE_MAX_STACK_SIZE = 1;
    public static final Codec<Item> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
        ItemBase.CODEC.fieldOf("base").forGetter(Item::itematic$itemBase),
        ItemComponentSet.CODEC.optionalFieldOf("components", ItemComponentSet.EMPTY).forGetter(Item::itematic$components),
        ItemEventMap.CODEC.optionalFieldOf("events", ItemEventMap.EMPTY).forGetter(Item::itematic$events)
    ).apply(instance, ItemUtil::create));

    public static void bootstrap(Registerable<Item> registerable) {
        new Bootstrapper(registerable).bootstrap();
    }

    public static RegistryKey<Item> keyFromBlock(Block block) {
        Identifier id = Registries.BLOCK.getId(block);
        return RegistryKey.of(RegistryKeys.ITEM, id);
    }

    private static Item create(ItemBase base) {
        return create(base, ItemComponentSet.EMPTY);
    }

    private static Item create(ItemBase base, ItemComponentSet components) {
        return create(base, components, ItemEventMap.EMPTY);
    }

    private static Item create(ItemBase base, ItemComponentSet components, ItemEventMap events) {
        Item item = new Item(new Item.Settings());
        item.itematic$setItemBase(base);
        item.itematic$setComponents(components);
        item.itematic$setEvents(events);
        return item;
    }

    private static class Bootstrapper {
        private final Registerable<Item> registerable;
        private final RegistryEntryLookup<ArmorMaterial> armorMaterials;
        private final RegistryEntryLookup<Item> items;
        private final RegistryEntryLookup<EntityType<?>> entityTypes;
        private final RegistryEntryLookup<Biome> biomes;
        private final RegistryEntryLookup<Block> blocks;
        private final RegistryEntryLookup<DispenseBehavior> dispenseBehaviors;
        private final RegistryEntryLookup<SoundEvent> soundEvents;
        private final RegistryEntryLookup<Fluid> fluids;
        private final RegistryEntryLookup<Pointer> pointers;
        private final RegistryEntryLookup<ActionEntry> actions;
        private final RegistryEntryLookup<SmithingTemplate> smithingTemplates;
        private final RegistryEntryLookup<DecoratedPotPattern> decoratedPotPatterns;
        private final RegistryEntryLookup<StatusEffect> statusEffects;
        private final RegistryEntryLookup<Potion> potions;
        private final RegistryEntryLookup<Enchantment> enchantments;
        private final RegistryEntryLookup<JukeboxSong> jukeboxSongs;

        private Bootstrapper(Registerable<Item> registerable) {
            this.registerable = registerable;
            this.armorMaterials = registerable.getRegistryLookup(ItematicRegistryKeys.ARMOR_MATERIAL);
            this.items = registerable.getRegistryLookup(RegistryKeys.ITEM);
            this.entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);
            this.biomes = registerable.getRegistryLookup(RegistryKeys.BIOME);
            this.blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
            this.dispenseBehaviors = registerable.getRegistryLookup(ItematicRegistryKeys.DISPENSE_BEHAVIOR);
            this.soundEvents = registerable.getRegistryLookup(RegistryKeys.SOUND_EVENT);
            this.fluids = registerable.getRegistryLookup(RegistryKeys.FLUID);
            this.pointers = registerable.getRegistryLookup(ItematicRegistryKeys.POINTER);
            this.actions = registerable.getRegistryLookup(ItematicRegistryKeys.ACTION);
            this.smithingTemplates = registerable.getRegistryLookup(ItematicRegistryKeys.SMITHING_TEMPLATE);
            this.decoratedPotPatterns = registerable.getRegistryLookup(RegistryKeys.DECORATED_POT_PATTERN);
            this.statusEffects = registerable.getRegistryLookup(RegistryKeys.STATUS_EFFECT);
            this.potions = registerable.getRegistryLookup(RegistryKeys.POTION);
            this.enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
            this.jukeboxSongs = registerable.getRegistryLookup(RegistryKeys.JUKEBOX_SONG);
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
            this.bootstrapMiscellaneous();
        }

        private void bootstrapConsumables() {
            this.bootstrapFood();
            this.registerable.register(ItemKeys.MILK_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.MILK_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(MilkBucketItemAccessor.getMaxUseTime())
                        .useAnimation(UseAction.DRINK)
                        .resultItem(this.items.getOrThrow(ItemKeys.BUCKET))
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventKeys.GENERIC_DRINK))
                        .build())
                    .with(RecipeRemainderItemComponent.of(this.items.getOrThrow(ItemKeys.BUCKET)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(ClearStatusEffectsAction.of(ActionContextParameter.THIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POTION, create(
                ItemBase.Builder.forItem(ItemKeys.POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(PotionItemAccessor.getMaxUseTime())
                        .useAnimation(UseAction.DRINK)
                        .resultItem(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventKeys.GENERIC_DRINK))
                        .build())
                    .with(PotionItemComponent.INSTANCE)
                    .with(PotionHolderItemComponent.of(1.0f))
                    .with(TintedItemComponent.of(PotionItemColor.INSTANCE))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            AllOfLootCondition.builder(
                                InvertedLootCondition.builder(
                                    SideCheckPredicate.builder(Direction.DOWN)
                                ),
                                LocationCheckLootCondition.builder(
                                    LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                            .tag(BlockTags.CONVERTABLE_TO_MUD))
                                ),
                                MatchToolLootCondition.builder(
                                    ItemPredicate.Builder.create()
                                        .subPredicate(ItemSubPredicateTypes.POTION_CONTENTS, new PotionContentsPredicate(RegistryEntryList.of(
                                            this.potions.getOrThrow(PotionKeys.WATER)
                                        )))
                                )
                            ).build()
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(PlaySoundAction.of(ActionContextParameter.TARGET, this.soundEvents.getOrThrow(SoundEventKeys.GENERIC_SPLASH), SoundCategory.BLOCKS))
                            .add(ExchangeItemAction.of(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                            .add(DisplayParticleAction.builder(ActionContextParameter.TARGET, ParticleTypes.SPLASH)
                                .count(5)
                                .offset(Vec3dProvider.of(
                                    -0.5d, 0.5d,
                                    1.0d, 1.0d,
                                    -0.5d, 0.5d
                                ))
                                .speed(1.0d)
                                .build())
                            .add(PlaySoundAction.of(ActionContextParameter.TARGET, this.soundEvents.getOrThrow(SoundEventKeys.BOTTLE_EMPTY), SoundCategory.BLOCKS))
                            .add(SetBlockStateAction.of(ActionContextParameter.TARGET, this.blocks.getOrThrow(BlockKeys.MUD)))
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.OMINOUS_BOTTLE, create(
                ItemBase.Builder.forItem(ItemKeys.OMINOUS_BOTTLE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(OminousBottleItemAccessor.useDuration())
                        .useAnimation(UseAction.DRINK)
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventKeys.GENERIC_DRINK))
                        .build())
                    .with(OminousEffectProviderItemComponent.INSTANCE)
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(PlaySoundAction.of(ActionContextParameter.THIS, this.soundEvents.getOrThrow(SoundEventKeys.OMINOUS_BOTTLE_DISPOSE))))
                    .build()
            ));
        }

        private void bootstrapFood() {
            this.registerable.register(ItemKeys.APPLE, create(
                ItemBase.Builder.forItem(ItemKeys.APPLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.APPLE).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON_SLICE, create(
                ItemBase.Builder.forItem(ItemKeys.MELON_SLICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.MELON_SLICE).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIED_KELP, create(
                ItemBase.Builder.forItem(ItemKeys.DRIED_KELP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.DRIED_KELP).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARROT, create(
                ItemBase.Builder.forItem(ItemKeys.CARROT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.CARROT).build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARROTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.POTATO, create(
                ItemBase.Builder.forItem(ItemKeys.POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.POTATO).build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POTATOES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAKED_POTATO, create(
                ItemBase.Builder.forItem(ItemKeys.BAKED_POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.BAKED_POTATO).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_FRUIT, create(
                ItemBase.Builder.forItem(ItemKeys.CHORUS_FRUIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.CHORUS_FRUIT).build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(TeleportAction.of(16, ActionContextParameter.THIS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT, create(
                ItemBase.Builder.forItem(ItemKeys.BEETROOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.BEETROOT).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.SWEET_BERRIES, create(
                ItemBase.Builder.forItem(ItemKeys.SWEET_BERRIES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.SWEET_BERRIES).build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SWEET_BERRY_BUSH)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_BERRIES, create(
                ItemBase.Builder.forItem(ItemKeys.GLOW_BERRIES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.GLOW_BERRIES).build())
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAVE_VINES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREAD, create(
                ItemBase.Builder.forItem(ItemKeys.BREAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.BREAD).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKIE, create(
                ItemBase.Builder.forItem(ItemKeys.COOKIE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKIE).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRAFTER, create(
                ItemBase.Builder.forBlock(ItemKeys.CRAFTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRAFTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PORKCHOP, create(
                ItemBase.Builder.forItem(ItemKeys.PORKCHOP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.PORKCHOP).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_PORKCHOP, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_PORKCHOP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_PORKCHOP).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.BEEF, create(
                ItemBase.Builder.forItem(ItemKeys.BEEF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.BEEF).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_BEEF, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_BEEF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_BEEF).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.CHICKEN, create(
                ItemBase.Builder.forItem(ItemKeys.CHICKEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.CHICKEN).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_CHICKEN, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_CHICKEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_CHICKEN).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT, create(
                ItemBase.Builder.forItem(ItemKeys.RABBIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.RABBIT).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_RABBIT, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_RABBIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_RABBIT).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.MUTTON, create(
                ItemBase.Builder.forItem(ItemKeys.MUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.MUTTON).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_MUTTON, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_MUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_MUTTON).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COD, create(
                ItemBase.Builder.forItem(ItemKeys.COD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COD).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON, create(
                ItemBase.Builder.forItem(ItemKeys.SALMON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.SALMON).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH, create(
                ItemBase.Builder.forItem(ItemKeys.TROPICAL_FISH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.TROPICAL_FISH).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH, create(
                ItemBase.Builder.forItem(ItemKeys.PUFFERFISH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.PUFFERFISH).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_COD, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_COD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_COD).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.COOKED_SALMON, create(
                ItemBase.Builder.forItem(ItemKeys.COOKED_SALMON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.COOKED_SALMON).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSHROOM_STEW, create(
                ItemBase.Builder.forItem(ItemKeys.MUSHROOM_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(FoodComponents.MUSHROOM_STEW)
                        .resultItem(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_STEW, create(
                ItemBase.Builder.forItem(ItemKeys.RABBIT_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(FoodComponents.RABBIT_STEW)
                        .resultItem(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT_SOUP, create(
                ItemBase.Builder.forItem(ItemKeys.BEETROOT_SOUP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(FoodComponents.BEETROOT_SOUP)
                        .resultItem(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_STEW, create(
                ItemBase.Builder.forItem(ItemKeys.SUSPICIOUS_STEW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ConsumableItemComponent.builder(FoodComponents.SUSPICIOUS_STEW)
                        .resultItem(this.items.getOrThrow(ItemKeys.BOWL))
                        .build())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(ApplySuspiciousStewEffectsFromItemAction.of(ActionContextParameter.THIS))
                            .add(ExchangeItemAction.of(this.items.getOrThrow(ItemKeys.BOWL)))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROTTEN_FLESH, create(
                ItemBase.Builder.forItem(ItemKeys.ROTTEN_FLESH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.ROTTEN_FLESH).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIDER_EYE, create(
                ItemBase.Builder.forItem(ItemKeys.SPIDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.SPIDER_EYE).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.POISONOUS_POTATO, create(
                ItemBase.Builder.forItem(ItemKeys.POISONOUS_POTATO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.POISONOUS_POTATO).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_APPLE, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_APPLE)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.GOLDEN_APPLE).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTED_GOLDEN_APPLE, create(
                ItemBase.Builder.forItem(ItemKeys.ENCHANTED_GOLDEN_APPLE)
                    .rarity(Rarity.EPIC)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.ENCHANTED_GOLDEN_APPLE).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_CARROT, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_CARROT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.GOLDEN_CARROT).build())
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN_PIE, create(
                ItemBase.Builder.forItem(ItemKeys.PUMPKIN_PIE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ConsumableItemComponent.builder(FoodComponents.PUMPKIN_PIE).build())
                    .with(CompostableItemComponent.of(ComposterBlockUtil.GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEY_BOTTLE, create(
                ItemBase.Builder.forItem(ItemKeys.HONEY_BOTTLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ConsumableItemComponent.builder(HoneyBottleItemAccessor.useDuration())
                        .food(FoodComponents.HONEY_BOTTLE)
                        .useAnimation(UseAction.DRINK)
                        .resultItem(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE))
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventKeys.HONEY_BOTTLE_DRINK))
                        .build())
                    .with(RecipeRemainderItemComponent.of(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.CONSUME_ITEM, ActionEntry.of(
                        RemoveStatusEffectsAction.of(
                            ActionContextParameter.THIS,
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
                ItemBase.Builder.forBlock(ItemKeys.STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE, create(
                ItemBase.Builder.forBlock(ItemKeys.GRANITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_GRANITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE, create(
                ItemBase.Builder.forBlock(ItemKeys.DIORITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DIORITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE, create(
                ItemBase.Builder.forBlock(ItemKeys.ANDESITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_ANDESITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CALCITE, create(
                ItemBase.Builder.forBlock(ItemKeys.CALCITE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CALCITE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_TUFF, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_TUFF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_TUFF_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_TUFF_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_TUFF_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_TUFF_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUFF_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.TUFF_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUFF_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_TUFF_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_TUFF_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIPSTONE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DRIPSTONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRIPSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRASS_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.GRASS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRASS_BLOCK)))
                    .with(TintedItemComponent.of(GrassItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIRT, create(
                ItemBase.Builder.forBlock(ItemKeys.DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COARSE_DIRT, create(
                ItemBase.Builder.forBlock(ItemKeys.COARSE_DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COARSE_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PODZOL, create(
                ItemBase.Builder.forBlock(ItemKeys.PODZOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PODZOL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROOTED_DIRT, create(
                ItemBase.Builder.forBlock(ItemKeys.ROOTED_DIRT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ROOTED_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD, create(
                ItemBase.Builder.forBlock(ItemKeys.MUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_NYLIUM, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_NYLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_NYLIUM, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_NYLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEDROCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BEDROCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEDROCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SAND, create(
                ItemBase.Builder.forBlock(ItemKeys.SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_SAND, create(
                ItemBase.Builder.forBlock(ItemKeys.SUSPICIOUS_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUSPICIOUS_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUSPICIOUS_GRAVEL, create(
                ItemBase.Builder.forBlock(ItemKeys.SUSPICIOUS_GRAVEL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUSPICIOUS_GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SAND, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAVEL, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAVEL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAL_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.COAL_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_COAL_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_COAL_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.IRON_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_IRON_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_IRON_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_COPPER_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_COPPER_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_GOLD_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.REDSTONE_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_REDSTONE_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_REDSTONE_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.EMERALD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_EMERALD_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_EMERALD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.LAPIS_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_LAPIS_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_LAPIS_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DIAMOND_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_DIAMOND_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_DIAMOND_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_GOLD_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_GOLD_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_QUARTZ_ORE, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_QUARTZ_ORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_QUARTZ_ORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_IRON_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.RAW_IRON_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_COPPER_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.RAW_COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_GOLD_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.RAW_GOLD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAW_GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEAVY_CORE, create(
                ItemBase.Builder.forBlock(ItemKeys.HEAVY_CORE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HEAVY_CORE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.AMETHYST_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AMETHYST_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUDDING_AMETHYST, create(
                ItemBase.Builder.forBlock(ItemKeys.BUDDING_AMETHYST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUDDING_AMETHYST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.IRON_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.GOLD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DIAMOND_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIAMOND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_COPPER_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CHISELED_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CHISELED_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUDDY_MANGROVE_ROOTS, create(
                ItemBase.Builder.forBlock(ItemKeys.MUDDY_MANGROVE_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUDDY_MANGROVE_ROOTS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_STEM, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_STEM, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CRIMSON_STEM, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_WARPED_STEM, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_WARPED_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CRIMSON_HYPHAE, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_CRIMSON_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_WARPED_HYPHAE, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_WARPED_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_HYPHAE, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_HYPHAE, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_HYPHAE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPONGE, create(
                ItemBase.Builder.forBlock(ItemKeys.SPONGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WET_SPONGE, create(
                ItemBase.Builder.forBlock(ItemKeys.WET_SPONGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WET_SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TINTED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.TINTED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TINTED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.LAPIS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LAPIS_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBWEB, create(
                ItemBase.Builder.forBlock(ItemKeys.COBWEB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBWEB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_STONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_STONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PETRIFIED_OAK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.PETRIFIED_OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PETRIFIED_OAK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLESTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.MUD_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.QUARTZ_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_RED_SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPUR_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_PRISMARINE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_STONE, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DECORATED_POT, create(
                ItemBase.Builder.forBlock(ItemKeys.DECORATED_POT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DECORATED_POT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OBSIDIAN, create(
                ItemBase.Builder.forBlock(ItemKeys.OBSIDIAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_ROD, create(
                ItemBase.Builder.forBlock(ItemKeys.END_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_ROD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_PLANT, create(
                ItemBase.Builder.forBlock(ItemKeys.CHORUS_PLANT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHORUS_PLANT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHORUS_FLOWER, create(
                ItemBase.Builder.forBlock(ItemKeys.CHORUS_FLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHORUS_FLOWER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPUR_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_PILLAR, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPUR_PILLAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPUR_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPUR_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPUR_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPAWNER, create(
                ItemBase.Builder.forBlock(ItemKeys.SPAWNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FARMLAND, create(
                ItemBase.Builder.forBlock(ItemKeys.FARMLAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FARMLAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FURNACE, create(
                ItemBase.Builder.forBlock(ItemKeys.FURNACE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLESTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW, create(
                ItemBase.Builder.forBlock(ItemKeys.SNOW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNOW)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ICE, create(
                ItemBase.Builder.forBlock(ItemKeys.ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.SNOW_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNOW_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLAY, create(
                ItemBase.Builder.forBlock(ItemKeys.CLAY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CLAY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.JACK_O_LANTERN, create(
                ItemBase.Builder.forBlock(ItemKeys.JACK_O_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JACK_O_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERRACK, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHERRACK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHERRACK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_SAND, create(
                ItemBase.Builder.forBlock(ItemKeys.SOUL_SAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_SAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_SOIL, create(
                ItemBase.Builder.forBlock(ItemKeys.SOUL_SOIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_SOIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BASALT, create(
                ItemBase.Builder.forBlock(ItemKeys.BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BASALT, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_BASALT, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_BASALT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOWSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.GLOWSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLOWSTONE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.CHARGE_RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_STONE, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_COBBLESTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_COBBLESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_MOSSY_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_MOSSY_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_CRACKED_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_CRACKED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_CHISELED_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_CHISELED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.INFESTED_DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.INFESTED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.INFESTED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRACKED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PACKED_MUD, create(
                ItemBase.Builder.forBlock(ItemKeys.PACKED_MUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PACKED_MUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.MUD_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_DEEPSLATE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRACKED_DEEPSLATE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILES, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_TILES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_DEEPSLATE_TILES, create(
                ItemBase.Builder.forBlock(ItemKeys.CRACKED_DEEPSLATE_TILES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REINFORCED_DEEPSLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.REINFORCED_DEEPSLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REINFORCED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BARS, create(
                ItemBase.Builder.forBlock(ItemKeys.IRON_BARS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_BARS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAIN, create(
                ItemBase.Builder.forBlock(ItemKeys.CHAIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.MUD_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MYCELIUM, create(
                ItemBase.Builder.forBlock(ItemKeys.MYCELIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MYCELIUM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_NETHER_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRACKED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_NETHER_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_BRICK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK, create(
                ItemBase.Builder.forBlock(ItemKeys.SCULK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_VEIN, create(
                ItemBase.Builder.forBlock(ItemKeys.SCULK_VEIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_VEIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_CATALYST, create(
                ItemBase.Builder.forBlock(ItemKeys.SCULK_CATALYST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_CATALYST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_SHRIEKER, create(
                ItemBase.Builder.forBlock(ItemKeys.SCULK_SHRIEKER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_SHRIEKER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTING_TABLE, create(
                ItemBase.Builder.forBlock(ItemKeys.ENCHANTING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ENCHANTING_TABLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_PORTAL_FRAME, create(
                ItemBase.Builder.forBlock(ItemKeys.END_PORTAL_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_PORTAL_FRAME)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE, create(
                ItemBase.Builder.forBlock(ItemKeys.END_STONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.END_STONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_EGG, create(
                ItemBase.Builder.forBlock(ItemKeys.DRAGON_EGG)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRAGON_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_CHEST, create(
                ItemBase.Builder.forBlock(ItemKeys.ENDER_CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ENDER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.EMERALD_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EMERALD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEACON, create(
                ItemBase.Builder.forBlock(ItemKeys.BEACON)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEACON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLESTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLESTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SANDSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.GRANITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUD_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.MUD_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUD_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.ANDESITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SANDSTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.SANDSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.END_STONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.DIORITE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACKSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_WALL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_WALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANVIL, create(
                ItemBase.Builder.forBlock(ItemKeys.ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHIPPED_ANVIL, create(
                ItemBase.Builder.forBlock(ItemKeys.CHIPPED_ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHIPPED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DAMAGED_ANVIL, create(
                ItemBase.Builder.forBlock(ItemKeys.DAMAGED_ANVIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DAMAGED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_QUARTZ_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_QUARTZ_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.QUARTZ_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.QUARTZ_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_PILLAR, create(
                ItemBase.Builder.forBlock(ItemKeys.QUARTZ_PILLAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.QUARTZ_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BARRIER, create(
                ItemBase.Builder.forBlock(ItemKeys.BARRIER)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BARRIER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PACKED_ICE, create(
                ItemBase.Builder.forBlock(ItemKeys.PACKED_ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PACKED_ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIRT_PATH, create(
                ItemBase.Builder.forBlock(ItemKeys.DIRT_PATH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIRT_PATH)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_PRISMARINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.PRISMARINE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PRISMARINE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_PRISMARINE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_PRISMARINE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEA_LANTERN, create(
                ItemBase.Builder.forBlock(ItemKeys.SEA_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEA_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_RED_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CUT_RED_SANDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.CUT_RED_SANDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CUT_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SANDSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGMA_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGMA_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_NETHER_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRUCTURE_VOID, create(
                ItemBase.Builder.forBlock(ItemKeys.STRUCTURE_VOID)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRUCTURE_VOID)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_EGG, create(
                ItemBase.Builder.forBlock(ItemKeys.TURTLE_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TURTLE_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNIFFER_EGG, create(
                ItemBase.Builder.forBlock(ItemKeys.SNIFFER_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SNIFFER_EGG)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.TUBE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BRAIN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BUBBLE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.FIRE_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.HORN_CORAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.TUBE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.BRAIN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.BUBBLE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.FIRE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.HORN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_ICE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_ICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_ICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CONDUIT, create(
                ItemBase.Builder.forBlock(ItemKeys.CONDUIT)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CONDUIT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_GRANITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DIORITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.END_STONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.GRANITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.ANDESITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_ANDESITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.DIORITE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_GRANITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_GRANITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_RED_SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_RED_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_STONE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DIORITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DIORITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSSY_COBBLESTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSSY_COBBLESTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSSY_COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_STONE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.END_STONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.END_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_SANDSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_SANDSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOOTH_QUARTZ_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOOTH_QUARTZ_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOOTH_QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRANITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.GRANITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANDESITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.ANDESITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_NETHER_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_NETHER_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_ANDESITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_ANDESITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIORITE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.DIORITE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COBBLED_DEEPSLATE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.COBBLED_DEEPSLATE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COBBLED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_DEEPSLATE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_DEEPSLATE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEEPSLATE_TILE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.DEEPSLATE_TILE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEEPSLATE_TILE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.REDSTONE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REPEATER, create(
                ItemBase.Builder.forBlock(ItemKeys.REPEATER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REPEATER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPARATOR, create(
                ItemBase.Builder.forBlock(ItemKeys.COMPARATOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COMPARATOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PISTON, create(
                ItemBase.Builder.forBlock(ItemKeys.PISTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PISTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STICKY_PISTON, create(
                ItemBase.Builder.forBlock(ItemKeys.STICKY_PISTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STICKY_PISTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.SLIME_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SLIME_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEY_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.HONEY_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HONEY_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OBSERVER, create(
                ItemBase.Builder.forBlock(ItemKeys.OBSERVER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OBSERVER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.HOPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HOPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DISPENSER, create(
                ItemBase.Builder.forBlock(ItemKeys.DISPENSER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DISPENSER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DROPPER, create(
                ItemBase.Builder.forBlock(ItemKeys.DROPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DROPPER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TARGET, create(
                ItemBase.Builder.forBlock(ItemKeys.TARGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TARGET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEVER, create(
                ItemBase.Builder.forBlock(ItemKeys.LEVER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LEVER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHTNING_ROD, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHTNING_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCULK_SENSOR, create(
                ItemBase.Builder.forBlock(ItemKeys.SCULK_SENSOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CALIBRATED_SCULK_SENSOR, create(
                ItemBase.Builder.forBlock(ItemKeys.CALIBRATED_SCULK_SENSOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CALIBRATED_SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIPWIRE_HOOK, create(
                ItemBase.Builder.forBlock(ItemKeys.TRIPWIRE_HOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIPWIRE_HOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TNT, create(
                ItemBase.Builder.forBlock(ItemKeys.TNT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TNT)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_TNT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_LAMP, create(
                ItemBase.Builder.forBlock(ItemKeys.REDSTONE_LAMP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_LAMP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.STONE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_WEIGHTED_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_WEIGHTED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEAVY_WEIGHTED_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.HEAVY_WEIGHTED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HEAVY_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.IRON_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.IRON_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.IRON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POWERED_RAIL, create(
                ItemBase.Builder.forBlock(ItemKeys.POWERED_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POWERED_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DETECTOR_RAIL, create(
                ItemBase.Builder.forBlock(ItemKeys.DETECTOR_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DETECTOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAIL, create(
                ItemBase.Builder.forBlock(ItemKeys.RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACTIVATOR_RAIL, create(
                ItemBase.Builder.forBlock(ItemKeys.ACTIVATOR_RAIL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACTIVATOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREWING_STAND, create(
                ItemBase.Builder.forBlock(ItemKeys.BREWING_STAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BREWING_STAND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAULDRON, create(
                ItemBase.Builder.forBlock(ItemKeys.CAULDRON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAULDRON)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWER_POT, create(
                ItemBase.Builder.forBlock(ItemKeys.FLOWER_POT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWER_POT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMOKER, create(
                ItemBase.Builder.forBlock(ItemKeys.SMOKER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMOKER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAST_FURNACE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLAST_FURNACE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLAST_FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRINDSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.GRINDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRINDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONECUTTER, create(
                ItemBase.Builder.forBlock(ItemKeys.STONECUTTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STONECUTTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BELL, create(
                ItemBase.Builder.forBlock(ItemKeys.BELL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BELL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LANTERN, create(
                ItemBase.Builder.forBlock(ItemKeys.LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_LANTERN, create(
                ItemBase.Builder.forBlock(ItemKeys.SOUL_LANTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAMPFIRE, create(
                ItemBase.Builder.forBlock(ItemKeys.CAMPFIRE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_CAMPFIRE, create(
                ItemBase.Builder.forBlock(ItemKeys.SOUL_CAMPFIRE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SOUL_CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEE_NEST, create(
                ItemBase.Builder.forBlock(ItemKeys.BEE_NEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEE_NEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEEHIVE, create(
                ItemBase.Builder.forBlock(ItemKeys.BEEHIVE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEEHIVE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEYCOMB_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.HONEYCOMB_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HONEYCOMB_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LODESTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.LODESTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LODESTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRYING_OBSIDIAN, create(
                ItemBase.Builder.forBlock(ItemKeys.CRYING_OBSIDIAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRYING_OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACKSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACKSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACKSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GILDED_BLACKSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.GILDED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GILDED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_POLISHED_BLACKSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_POLISHED_BLACKSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLISHED_BLACKSTONE_BRICK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.POLISHED_BLACKSTONE_BRICK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POLISHED_BLACKSTONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRACKED_POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RESPAWN_ANCHOR, create(
                ItemBase.Builder.forBlock(ItemKeys.RESPAWN_ANCHOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMALL_AMETHYST_BUD, create(
                ItemBase.Builder.forBlock(ItemKeys.SMALL_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMALL_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MEDIUM_AMETHYST_BUD, create(
                ItemBase.Builder.forBlock(ItemKeys.MEDIUM_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MEDIUM_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LARGE_AMETHYST_BUD, create(
                ItemBase.Builder.forBlock(ItemKeys.LARGE_AMETHYST_BUD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LARGE_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_CLUSTER, create(
                ItemBase.Builder.forBlock(ItemKeys.AMETHYST_CLUSTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AMETHYST_CLUSTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POINTED_DRIPSTONE, create(
                ItemBase.Builder.forBlock(ItemKeys.POINTED_DRIPSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POINTED_DRIPSTONE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OCHRE_FROGLIGHT, create(
                ItemBase.Builder.forBlock(ItemKeys.OCHRE_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OCHRE_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.VERDANT_FROGLIGHT, create(
                ItemBase.Builder.forBlock(ItemKeys.VERDANT_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VERDANT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PEARLESCENT_FROGLIGHT, create(
                ItemBase.Builder.forBlock(ItemKeys.PEARLESCENT_FROGLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PEARLESCENT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FROGSPAWN, create(
                ItemBase.Builder.forBlock(ItemKeys.FROGSPAWN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FROGSPAWN), BlockItemComponent.Pass.FLUID))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_GRATE, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_GRATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPOSED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.EXPOSED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEATHERED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.WEATHERED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXIDIZED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.OXIDIZED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_EXPOSED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_EXPOSED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_WEATHERED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_WEATHERED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAXED_OXIDIZED_COPPER_BULB, create(
                ItemBase.Builder.forBlock(ItemKeys.WAXED_OXIDIZED_COPPER_BULB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WAXED_OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIAL_SPAWNER, create(
                ItemBase.Builder.forBlock(ItemKeys.TRIAL_SPAWNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIAL_SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.VAULT, create(
                ItemBase.Builder.forBlock(ItemKeys.VAULT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VAULT)))
                    .build()
            ));
        }

        private void bootstrapAttachedToSideBlocks() {
            this.registerable.register(ItemKeys.TORCH, create(
                ItemBase.Builder.forBlock(ItemKeys.TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.TORCH), this.blocks.getOrThrow(BlockKeys.WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.SOUL_TORCH, create(
                ItemBase.Builder.forBlock(ItemKeys.SOUL_TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SOUL_TORCH), this.blocks.getOrThrow(BlockKeys.SOUL_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.TUBE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.TUBE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRAIN_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.BRAIN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUBBLE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.BUBBLE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIRE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.FIRE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORN_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.HORN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.HORN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_TUBE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_TUBE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BRAIN_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BRAIN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUBBLE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BUBBLE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_FIRE_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_FIRE_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_HORN_CORAL_FAN, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_HORN_CORAL_FAN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_FAN), this.blocks.getOrThrow(BlockKeys.DEAD_HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.REDSTONE_TORCH, create(
                ItemBase.Builder.forBlock(ItemKeys.REDSTONE_TORCH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.REDSTONE_TORCH), this.blocks.getOrThrow(BlockKeys.REDSTONE_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CRIMSON_SIGN), this.blocks.getOrThrow(BlockKeys.CRIMSON_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WARPED_SIGN), this.blocks.getOrThrow(BlockKeys.WARPED_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CRIMSON_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.CRIMSON_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WARPED_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.WARPED_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
        }

        private void bootstrapColoredBlocks() {
            this.bootstrapShulkerBoxes();
            this.registerable.register(ItemKeys.WHITE_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_STAINED_GLASS, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_STAINED_GLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_STAINED_GLASS_PANE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_STAINED_GLASS_PANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_GLAZED_TERRACOTTA, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_GLAZED_TERRACOTTA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CONCRETE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_CONCRETE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CONCRETE_POWDER, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_CONCRETE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_BED, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_BED).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_BED)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CANDLE, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_CANDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CANDLE)))
                    .build()
            ));
        }

        private void bootstrapShulkerBoxes() {
            this.registerable.register(ItemKeys.SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_SHULKER_BOX, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_SHULKER_BOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_SHULKER_BOX)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
        }

        private void bootstrapItemNameBlocks() {
            this.registerable.register(ItemKeys.REDSTONE, create(
                ItemBase.Builder.forItem(ItemKeys.REDSTONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.REDSTONE_WIRE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRING, create(
                ItemBase.Builder.forItem(ItemKeys.STRING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRIPWIRE)))
                    .build()
            ));
        }

        private void bootstrapOperatorOnlyBlocks() {
            this.registerable.register(ItemKeys.COMMAND_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.REPEATING_COMMAND_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.REPEATING_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.REPEATING_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAIN_COMMAND_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.CHAIN_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.CHAIN_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRUCTURE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.STRUCTURE_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.operator(this.blocks.getOrThrow(BlockKeys.STRUCTURE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.JIGSAW, create(
                ItemBase.Builder.forBlock(ItemKeys.JIGSAW)
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
                ItemBase.Builder.forItem(ItemKeys.WOODEN_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.WOODEN_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.WOODEN_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.WOOD, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.WOODEN_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.WOOD, 7.0d, 0.2d, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOODEN_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.WOODEN_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.WOOD, 1.0d, 0.25d, ItematicItemTags.REPAIRS_WOODEN_TOOL))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.TOOL_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SWORD, create(
                ItemBase.Builder.forItem(ItemKeys.STONE_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.STONE_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.STONE_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.STONE, ItematicItemTags.REPAIRS_STONE_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.STONE_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.STONE, 8.0d, 0.2d, ItematicItemTags.REPAIRS_STONE_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.STONE_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.STONE_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.STONE, 2.0d, 0.5d, ItematicItemTags.REPAIRS_STONE_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_SWORD, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL, ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL, ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.GOLD, ItematicItemTags.REPAIRS_GOLDEN_TOOL, ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.GOLD, 7.0d, 0.25d, ItematicItemTags.REPAIRS_GOLDEN_TOOL, ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.GOLD, 1.0d, 0.25d, ItematicItemTags.REPAIRS_GOLDEN_TOOL, ItematicEntityTypeTags.PROFICIENT_WITH_GOLDEN_WEAPONS))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_SWORD, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.IRON, ItematicItemTags.REPAIRS_IRON_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.IRON, 7.0d, 0.225d, ItematicItemTags.REPAIRS_IRON_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.IRON, 3.0d, 0.75d, ItematicItemTags.REPAIRS_IRON_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_SWORD, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.DIAMOND, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.DIAMOND, 6.0d, 0.25d, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.DIAMOND, 4.0d, 1.0d, ItematicItemTags.REPAIRS_DIAMOND_TOOL))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SWORD, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_SWORD).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.sword(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SHOVEL, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_SHOVEL).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.shovel(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_PICKAXE, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_PICKAXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.pickaxe(ToolMaterials.NETHERITE, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_AXE, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_AXE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.axe(ToolMaterials.NETHERITE, 6.0d, 0.25d, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_HOE, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_HOE).build(),
                ItemComponentSet.builder()
                    .with(DamageableItemComponent.hoe(ToolMaterials.NETHERITE, 5.0d, 1.0d, ItematicItemTags.REPAIRS_NETHERITE_TOOL))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemKeys.FISHING_ROD, create(
                ItemBase.Builder.forItem(ItemKeys.FISHING_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(CastableItemComponent.INSTANCE)
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEARS, create(
                ItemBase.Builder.forItem(ItemKeys.SHEARS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(238))
                    .with(ToolItemComponent.builder(1)
                        .rule(ToolComponent.Rule.ofAlwaysDropping(List.of(Blocks.COBWEB), 15.0f))
                        .rule(ToolComponent.Rule.of(BlockTags.LEAVES, 15.0f))
                        .rule(ToolComponent.Rule.of(BlockTags.WOOL, 5.0f))
                        .rule(ToolComponent.Rule.of(List.of(Blocks.VINE, Blocks.GLOW_LICHEN), 2.0f))
                        .build())
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHEAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOW, create(
                ItemBase.Builder.forItem(ItemKeys.BOW).build(),
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
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CROSSBOW, create(
                ItemBase.Builder.forItem(ItemKeys.CROSSBOW).build(),
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
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIDENT, create(
                ItemBase.Builder.forItem(ItemKeys.TRIDENT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.ofPreserved(250))
                    .with(ToolItemComponent.builder(2)
                        .build())
                    .with(WeaponItemComponent.of(1, TridentItem.ATTACK_DAMAGE + 1, 0.275d))
                    .with(ThrowableItemComponent.trident(TridentItem.THROW_SPEED, 0.0f, TridentItem.MIN_DRAW_DURATION))
                    .with(ProjectileItemComponent.of(TridentEntityInitializer.of(true)))
                    .with(EnchantableItemComponent.of(1))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.STOPPED_USING, ActionEntry.of(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.THIS),
                            AllOfLootCondition.builder(
                                EntityPropertiesLootCondition.builder(
                                    LootContext.EntityTarget.THIS,
                                    EntityPredicate.Builder.create()
                                        .itematic$usedItemAtLeast(TridentItem.MIN_DRAW_DURATION)
                                        .itematic$inWaterOrRain(true)
                                ),
                                MatchToolLootCondition.builder(
                                    ItemPredicate.Builder.create()
                                        .subPredicate(ItemSubPredicateTypes.ENCHANTMENTS, EnchantmentsPredicate.enchantments(List.of(
                                            new EnchantmentPredicate(this.enchantments.getOrThrow(Enchantments.RIPTIDE), NumberRange.IntRange.ANY)
                                        ))))
                            ).build()
                        ),
                        PassingSequenceHandler.builder()
                            .add(TwirlPlayerAction.INSTANCE)
                            .add(DamageItemAction.of(1))
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARROT_ON_A_STICK, create(
                ItemBase.Builder.forItem(ItemKeys.CARROT_ON_A_STICK).build(),
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
                ItemBase.Builder.forItem(ItemKeys.WARPED_FUNGUS_ON_A_STICK).build(),
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
                ItemBase.Builder.forItem(ItemKeys.FLINT_AND_STEEL).build(),
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
                            .add(PlaySoundAction.builder(ActionContextParameter.TARGET, this.soundEvents.getOrThrow(SoundEventKeys.FLINT_AND_STEEL_USE), SoundCategory.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.BRUSH, create(
                ItemBase.Builder.forItem(ItemKeys.BRUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BrushItemComponent.of(BrushItemAccessor.maxBrushTime()))
                    .with(DamageableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.BRUSH)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MACE, create(
                ItemBase.Builder.forItem(ItemKeys.MACE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.of(500))
                    .with(ToolItemComponent.builder(2).build())
                    .with(WeaponItemComponent.ofSmashing(1, 6.0d, 0.15d))
                    .with(EnchantableItemComponent.of(15))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_MACE))
                    .build()
            ));
        }

        private void bootstrapEntities() {
            this.bootstrapSpawnEggs();
            this.registerable.register(ItemKeys.MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.MINECART, MinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHEST_MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.CHEST_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.CHEST_MINECART, ChestMinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.FURNACE_MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.FURNACE_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.FURNACE_MINECART, FurnaceMinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TNT_MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.TNT_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.TNT_MINECART, TntMinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOPPER_MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.HOPPER_MINECART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.HOPPER_MINECART, HopperMinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMMAND_BLOCK_MINECART, create(
                ItemBase.Builder.forItem(ItemKeys.COMMAND_BLOCK_MINECART)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new MinecartEntityInitializer<>(EntityType.COMMAND_BLOCK_MINECART, CommandBlockMinecartEntity::new), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.OAK_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.OAK), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.OAK_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.OAK), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.SPRUCE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.SPRUCE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.SPRUCE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.SPRUCE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.BIRCH_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.BIRCH), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.BIRCH_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.BIRCH), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.JUNGLE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.JUNGLE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.JUNGLE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.JUNGLE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.ACACIA_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.ACACIA), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.ACACIA_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.ACACIA), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.CHERRY_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.CHERRY), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.CHERRY_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.CHERRY), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.DARK_OAK_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.DARK_OAK), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.DARK_OAK_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.DARK_OAK), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.MANGROVE_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.MANGROVE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_CHEST_BOAT, create(
                ItemBase.Builder.forItem(ItemKeys.MANGROVE_CHEST_BOAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.MANGROVE), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_RAFT, create(
                ItemBase.Builder.forItem(ItemKeys.BAMBOO_RAFT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new BoatEntityInitializer(BoatEntity.Type.BAMBOO), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_CHEST_RAFT, create(
                ItemBase.Builder.forItem(ItemKeys.BAMBOO_CHEST_RAFT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EntityItemComponent.from(new ChestBoatEntityInitializer(BoatEntity.Type.BAMBOO), this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BOAT_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PAINTING, create(
                ItemBase.Builder.forItem(ItemKeys.PAINTING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(DecorationEntityInitializer.createPainting(EntityType.PAINTING)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ITEM_FRAME, create(
                ItemBase.Builder.forItem(ItemKeys.ITEM_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(DecorationEntityInitializer.createItemFrame(EntityType.ITEM_FRAME, ItemFrameEntity::new)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_ITEM_FRAME, create(
                ItemBase.Builder.forItem(ItemKeys.GLOW_ITEM_FRAME).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(DecorationEntityInitializer.createItemFrame(EntityType.GLOW_ITEM_FRAME, GlowItemFrameEntity::new)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMOR_STAND, create(
                ItemBase.Builder.forItem(ItemKeys.ARMOR_STAND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(EntityItemComponent.from(new ArmorStandEntityInitializer(), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.END_CRYSTAL, create(
                ItemBase.Builder.forItem(ItemKeys.END_CRYSTAL)
                    .rarity(Rarity.RARE)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EntityItemComponent.of(EndCrystalEntityInitializer.INSTANCE))
                    .build()
            ));
        }

        private void bootstrapSpawnEggs() {
            this.registerable.register(ItemKeys.ARMADILLO_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ARMADILLO_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ARMADILLO), 0xad716d, 0x824848, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ALLAY_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ALLAY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ALLAY), 0x00daff, 0x00adff, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.AXOLOTL_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.AXOLOTL_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), 0xfbc1e3, 0xa62d74, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.BAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.BAT), 0x4c3e30, 0x0f0f0f, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.BEE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.BEE), 0xedc343, 0x43241b, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.BLAZE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.BLAZE), 0xf6b201, 0xfff87e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOGGED_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.BOGGED_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.BOGGED), 0x8a9c72, 0x314d1b, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREEZE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.BREEZE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.BREEZE), 0xaf94df, 0x9166df, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.CAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.CAT), 0xefc88e, 0x957256, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAMEL_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.CAMEL_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.CAMEL), 0xfcc369, 0xcb9337, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAVE_SPIDER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.CAVE_SPIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.CAVE_SPIDER), 0x0c424e, 0xa80e0e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHICKEN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.CHICKEN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.CHICKEN), 0xa1a1a1, 0xff0000, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COD_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.COD_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.COD), 0xc1a76a, 0xe5c48b, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COW_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.COW_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.COW), 0x443626, 0xa1a1a1, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.CREEPER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.CREEPER), 0x0da70b, 0x000000, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DOLPHIN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.DOLPHIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.DOLPHIN), 0x223b4d, 0xf9f9f9, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DONKEY_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.DONKEY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.DONKEY), 0x534539, 0x867566, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DROWNED_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.DROWNED_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.DROWNED), 0x8ff1d7, 0x799c65, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ELDER_GUARDIAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ELDER_GUARDIAN), 0xceccba, 0x747693, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_DRAGON_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ENDER_DRAGON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ENDER_DRAGON), 0x1c1c1c, 0xe079fa, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDERMAN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ENDERMAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ENDERMAN), 0x161616, 0x000000, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDERMITE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ENDERMITE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ENDERMITE), 0x161616, 0x6e6e6e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.EVOKER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.EVOKER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.EVOKER), 0x959b9b, 0x1e1c1a, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.FOX_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.FOX_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.FOX), 0xd5b69f, 0xcc6920, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.FROG_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.FROG_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.FROG), 0xd07444, 0xffc77c, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GHAST_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.GHAST_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.GHAST), 0xf9f9f9, 0xbcbcbc, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_SQUID_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.GLOW_SQUID_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.GLOW_SQUID), 0x095656, 0x85f1bc, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOAT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.GOAT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.GOAT), 0xa5947c, 0x55493e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUARDIAN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.GUARDIAN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.GUARDIAN), 0x5a8272, 0xf17d30, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOGLIN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.HOGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.HOGLIN), 0xc66e55, 0x5f6464, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HORSE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.HORSE), 0xc09e7d, 0xeee500, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.HUSK_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.HUSK_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.HUSK), 0x797061, 0xe6cc94, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_GOLEM_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_GOLEM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.IRON_GOLEM), 0xdbcdc2, 0x74a332, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.LLAMA_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.LLAMA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.LLAMA), 0xc09e7d, 0x995f40, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_CUBE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.MAGMA_CUBE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.MAGMA_CUBE), 0x340000, 0xfcfc00, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOOSHROOM_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.MOOSHROOM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.MOOSHROOM), 0xa00f10, 0xb7b7b7, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.MULE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.MULE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.MULE), 0x1b0200, 0x51331d, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.OCELOT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.OCELOT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.OCELOT), 0xefde7d, 0x564434, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PANDA_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PANDA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PANDA), 0xe7e7e7, 0x1b1b22, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PARROT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PARROT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PARROT), 0x0da70b, 0xff0000, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PHANTOM_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PHANTOM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PHANTOM), 0x43518a, 0x88ff00, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIG_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PIG_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PIG), 0xf0a5a2, 0xdb635f, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PIGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PIGLIN), 0x995f40, 0xf9f3a4, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PIGLIN_BRUTE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PIGLIN_BRUTE), 0x592a10, 0xf9f3a4, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PILLAGER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PILLAGER), 0x532f36, 0x959b9b, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.POLAR_BEAR_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.POLAR_BEAR_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.POLAR_BEAR), 0xeeeede, 0xd5d6cd, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.PUFFERFISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), 0xf6b201, 0x37c3f2, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.RABBIT_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.RABBIT), 0x995f40, 0x734831, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAVAGER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.RAVAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.RAVAGER), 0x757470, 0x5b5049, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SALMON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SALMON), 0xa00f10, 0x0e8474, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEEP_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SHEEP_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SHEEP), 0xe7e7e7, 0xffb5b5, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHULKER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SHULKER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SHULKER), 0x946794, 0x4d3852, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SILVERFISH_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SILVERFISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SILVERFISH), 0x6e6e6e, 0x303030, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKELETON_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SKELETON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SKELETON), 0xc1c1c1, 0x494949, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKELETON_HORSE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SKELETON_HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SKELETON_HORSE), 0x68684f, 0xe5e5d8, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SLIME_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SLIME), 0x51a03e, 0x7ebf6e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNIFFER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SNIFFER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SNIFFER), 0x871e09, 0x25ab70, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOW_GOLEM_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SNOW_GOLEM_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SNOW_GOLEM), 0xd9f2f2, 0x81a4a4, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIDER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SPIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SPIDER), 0x342d27, 0xa80e0e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SQUID_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.SQUID_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.SQUID), 0x223b4d, 0x708899, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRAY_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.STRAY_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.STRAY), 0x617677, 0xddeaea, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIDER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.STRIDER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.STRIDER), 0x9c3436, 0x4d494d, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TADPOLE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.TADPOLE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), 0x6d533d, 0x160a00, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRADER_LLAMA_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.TRADER_LLAMA_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.TRADER_LLAMA), 0xeaa430, 0x456296, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.TROPICAL_FISH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), 0xef6915, 0xfff9ef, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.TURTLE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.TURTLE), 0xe7e7e7, 0x00afaf, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VEX_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.VEX_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.VEX), 0x7a90a4, 0xe8edf1, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VILLAGER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.VILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.VILLAGER), 0x563c33, 0xbd8b72, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.VINDICATOR_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.VINDICATOR_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.VINDICATOR), 0x959b9b, 0x275e61, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WANDERING_TRADER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WANDERING_TRADER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WANDERING_TRADER), 0x456296, 0xeaa430, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARDEN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WARDEN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WARDEN), 0x0f4649, 0x39d6e0, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITCH_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WITCH_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WITCH), 0x340000, 0x51a03e, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WITHER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WITHER), 0x141414, 0x4d72a0, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SKELETON_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WITHER_SKELETON_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WITHER_SKELETON), 0x141414, 0x474d4d, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOLF_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.WOLF_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.WOLF), 0xd7d3d3, 0xceaf96, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOGLIN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ZOGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ZOGLIN), 0xc66e55, 0xe6e6e6, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ZOMBIE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE), 0x00afaf, 0x799c65, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ZOMBIE_HORSE_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_HORSE), 0x315234, 0x97c284, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ZOMBIE_VILLAGER_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIE_VILLAGER), 0x563c33, 0x799c65, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG, create(
                ItemBase.Builder.forItem(ItemKeys.ZOMBIFIED_PIGLIN_SPAWN_EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(SpawnEggItemComponent.from(this.entityTypes.getOrThrow(EntityTypeKeys.ZOMBIFIED_PIGLIN), 0xea9393, 0x4c7129, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapCompostables() {
            this.registerable.register(ItemKeys.OAK_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(FoliageItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(ConstantItemColor.of(FoliageColors.getSpruceColor())))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(ConstantItemColor.of(FoliageColors.getBirchColor())))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(FoliageItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(FoliageItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(FoliageItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(ConstantItemColor.of(FoliageColors.getMangroveColor())))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZALEA_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.AZALEA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZALEA_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_OAK_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_SPRUCE_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BIRCH_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_JUNGLE_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ACACIA_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CHERRY_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SAPLING, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_SAPLING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SAPLING)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DARK_OAK_SAPLING))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PROPAGULE, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_PROPAGULE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PROPAGULE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_MANGROVE_PROPAGULE))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHORT_GRASS, create(
                ItemBase.Builder.forBlock(ItemKeys.SHORT_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHORT_GRASS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(GrassItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.KELP, create(
                ItemBase.Builder.forBlock(ItemKeys.KELP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.KELP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSS_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSS_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSS_CARPET)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_PETALS, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_PETALS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_PETALS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.HANGING_ROOTS, create(
                ItemBase.Builder.forBlock(ItemKeys.HANGING_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HANGING_ROOTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMALL_DRIPLEAF, create(
                ItemBase.Builder.forBlock(ItemKeys.SMALL_DRIPLEAF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMALL_DRIPLEAF)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHEAT_SEEDS, create(
                ItemBase.Builder.forItem(ItemKeys.WHEAT_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHEAT)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN_SEEDS, create(
                ItemBase.Builder.forItem(ItemKeys.PUMPKIN_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PUMPKIN_STEM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON_SEEDS, create(
                ItemBase.Builder.forItem(ItemKeys.MELON_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MELON_STEM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.TORCHFLOWER_SEEDS, create(
                ItemBase.Builder.forItem(ItemKeys.TORCHFLOWER_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TORCHFLOWER_CROP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.PITCHER_POD, create(
                ItemBase.Builder.forItem(ItemKeys.PITCHER_POD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PITCHER_CROP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BEETROOT_SEEDS, create(
                ItemBase.Builder.forItem(ItemKeys.BEETROOT_SEEDS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BEETROOTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_ROOTS, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_ROOTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEAGRASS, create(
                ItemBase.Builder.forBlock(ItemKeys.SEAGRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEAGRASS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.SMALL_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWERING_AZALEA_LEAVES, create(
                ItemBase.Builder.forBlock(ItemKeys.FLOWERING_AZALEA_LEAVES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWERING_AZALEA_LEAVES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_SPROUTS, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_SPROUTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_SPROUTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.WEEPING_VINES, create(
                ItemBase.Builder.forBlock(ItemKeys.WEEPING_VINES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WEEPING_VINES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.TWISTING_VINES, create(
                ItemBase.Builder.forBlock(ItemKeys.TWISTING_VINES).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TWISTING_VINES)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUGAR_CANE, create(
                ItemBase.Builder.forBlock(ItemKeys.SUGAR_CANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUGAR_CANE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.VINE, create(
                ItemBase.Builder.forBlock(ItemKeys.VINE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.VINE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(FoliageItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_LICHEN, create(
                ItemBase.Builder.forBlock(ItemKeys.GLOW_LICHEN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GLOW_LICHEN)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.TALL_GRASS, create(
                ItemBase.Builder.forBlock(ItemKeys.TALL_GRASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TALL_GRASS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(GrassItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.CACTUS, create(
                ItemBase.Builder.forBlock(ItemKeys.CACTUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CACTUS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CACTUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRIED_KELP_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.DRIED_KELP_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DRIED_KELP_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.HALF_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DRIED_KELP_BLOCK_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.FERN, create(
                ItemBase.Builder.forBlock(ItemKeys.FERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FERN)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(GrassItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_FERN))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILY_PAD, create(
                ItemBase.Builder.forBlock(ItemKeys.LILY_PAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILY_PAD), BlockItemComponent.Pass.FLUID))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(ConstantItemColor.of(0xff71c35c)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_WART, create(
                ItemBase.Builder.forItem(ItemKeys.NETHER_WART).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_WART)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.COCOA_BEANS, create(
                ItemBase.Builder.forItem(ItemKeys.COCOA_BEANS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COCOA)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIG_DRIPLEAF, create(
                ItemBase.Builder.forBlock(ItemKeys.BIG_DRIPLEAF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIG_DRIPLEAF)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUMPKIN, create(
                ItemBase.Builder.forBlock(ItemKeys.PUMPKIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PUMPKIN)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARVED_PUMPKIN, create(
                ItemBase.Builder.forBlock(ItemKeys.CARVED_PUMPKIN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARVED_PUMPKIN)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(EquipmentItemComponent.of(EquipmentSlot.HEAD, false, this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_CARVED_PUMPKIN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MELON, create(
                ItemBase.Builder.forBlock(ItemKeys.MELON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MELON)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.SEA_PICKLE, create(
                ItemBase.Builder.forBlock(ItemKeys.SEA_PICKLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SEA_PICKLE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHEAT, create(
                ItemBase.Builder.forItem(ItemKeys.WHEAT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.DANDELION, create(
                ItemBase.Builder.forBlock(ItemKeys.DANDELION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DANDELION)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DANDELION))
                    .build()
            ));
            this.registerable.register(ItemKeys.POPPY, create(
                ItemBase.Builder.forBlock(ItemKeys.POPPY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.POPPY)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_POPPY))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_ORCHID, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_ORCHID).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_ORCHID)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.SATURATION), 140)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BLUE_ORCHID))
                    .build()
            ));
            this.registerable.register(ItemKeys.ALLIUM, create(
                ItemBase.Builder.forBlock(ItemKeys.ALLIUM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ALLIUM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.FIRE_RESISTANCE), 80)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ALLIUM))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZURE_BLUET, create(
                ItemBase.Builder.forBlock(ItemKeys.AZURE_BLUET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZURE_BLUET)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.BLINDNESS), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_AZURE_BLUET))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_TULIP, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_TULIP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_RED_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_TULIP, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_TULIP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_ORANGE_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_TULIP, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_TULIP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WHITE_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_TULIP, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_TULIP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_TULIP)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WEAKNESS), 180)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_PINK_TULIP))
                    .build()
            ));
            this.registerable.register(ItemKeys.OXEYE_DAISY, create(
                ItemBase.Builder.forBlock(ItemKeys.OXEYE_DAISY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OXEYE_DAISY)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_OXEYE_DAISY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CORNFLOWER, create(
                ItemBase.Builder.forBlock(ItemKeys.CORNFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CORNFLOWER)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.JUMP_BOOST), 120)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CORNFLOWER))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILY_OF_THE_VALLEY, create(
                ItemBase.Builder.forBlock(ItemKeys.LILY_OF_THE_VALLEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILY_OF_THE_VALLEY)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.POISON), 240)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_LILY_OF_THE_VALLEY))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_ROSE, create(
                ItemBase.Builder.forBlock(ItemKeys.WITHER_ROSE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WITHER_ROSE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.WITHER), 160)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WITHER_ROSE))
                    .build()
            ));
            this.registerable.register(ItemKeys.AZALEA, create(
                ItemBase.Builder.forBlock(ItemKeys.AZALEA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.AZALEA)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_AZALEA_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUNFLOWER, create(
                ItemBase.Builder.forBlock(ItemKeys.SUNFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SUNFLOWER)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.LILAC, create(
                ItemBase.Builder.forBlock(ItemKeys.LILAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LILAC)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.ROSE_BUSH, create(
                ItemBase.Builder.forBlock(ItemKeys.ROSE_BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ROSE_BUSH)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.PEONY, create(
                ItemBase.Builder.forBlock(ItemKeys.PEONY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PEONY)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.LARGE_FERN, create(
                ItemBase.Builder.forBlock(ItemKeys.LARGE_FERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LARGE_FERN)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .with(TintedItemComponent.of(GrassItemColor.of(this.biomes.getOrThrow(BiomeKeys.PLAINS))))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPORE_BLOSSOM, create(
                ItemBase.Builder.forBlock(ItemKeys.SPORE_BLOSSOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPORE_BLOSSOM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_MUSHROOM, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_MUSHROOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_MUSHROOM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BROWN_MUSHROOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_MUSHROOM, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_MUSHROOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_MUSHROOM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_RED_MUSHROOM))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_FUNGUS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_FUNGUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_FUNGUS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CRIMSON_FUNGUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_FUNGUS, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_FUNGUS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_FUNGUS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WARPED_FUNGUS))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRIMSON_ROOTS, create(
                ItemBase.Builder.forBlock(ItemKeys.CRIMSON_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRIMSON_ROOTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_CRIMSON_ROOTS))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_ROOTS, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_ROOTS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_ROOTS)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_WARPED_ROOTS))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOSS_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.MOSS_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MOSS_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSHROOM_STEM, create(
                ItemBase.Builder.forBlock(ItemKeys.MUSHROOM_STEM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MUSHROOM_STEM)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHROOMLIGHT, create(
                ItemBase.Builder.forBlock(ItemKeys.SHROOMLIGHT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SHROOMLIGHT)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.BIG_CHANCE_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_WART_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHER_WART_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHER_WART_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARPED_WART_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.WARPED_WART_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WARPED_WART_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.HAY_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.HAY_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.HAY_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOWERING_AZALEA, create(
                ItemBase.Builder.forBlock(ItemKeys.FLOWERING_AZALEA).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLOWERING_AZALEA)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_FLOWERING_AZALEA_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.TORCHFLOWER, create(
                ItemBase.Builder.forBlock(ItemKeys.TORCHFLOWER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TORCHFLOWER)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .with(SuspiciousEffectIngredientItemComponent.of(
                        new SuspiciousStewEffectsComponent.StewEffect(this.statusEffects.getOrThrow(StatusEffectKeys.NIGHT_VISION), 100)
                    ))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_TORCHFLOWER))
                    .build()
            ));
            this.registerable.register(ItemKeys.PITCHER_PLANT, create(
                ItemBase.Builder.forBlock(ItemKeys.PITCHER_PLANT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PITCHER_PLANT)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_MUSHROOM_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_MUSHROOM_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_MUSHROOM_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_MUSHROOM_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_MUSHROOM_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_MUSHROOM_BLOCK)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.ALMOST_GUARANTEED_TO_COMPOST))
                    .build()
            ));
            this.registerable.register(ItemKeys.CAKE, create(
                ItemBase.Builder.forBlock(ItemKeys.CAKE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CAKE)))
                    .with(CompostableItemComponent.of(ComposterBlockUtil.GUARANTEED_TO_COMPOST))
                    .build()
            ));
        }

        private void bootstrapEquipment() {
            this.bootstrapArmor();
            this.bootstrapSkulls();
            this.registerable.register(ItemKeys.ELYTRA, create(
                ItemBase.Builder.forItem(ItemKeys.ELYTRA)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DamageableItemComponent.ofPreserved(432))
                    .with(EquipmentItemComponent.of(EquipmentSlot.CHEST, true, this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_ELYTRA)))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_ELYTRA))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHIELD, create(
                ItemBase.Builder.forItem(ItemKeys.SHIELD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(UseableItemComponent.builder()
                        .animation(UseAction.BLOCK)
                        .build())
                    .with(DamageableItemComponent.of(336))
                    .with(EquipmentItemComponent.of(EquipmentSlot.OFFHAND, false, this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC)))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_SHIELD))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(BannerPatternHolderItemComponent.of())
                    .build()
            ));
        }

        private void bootstrapArmor() {
            this.registerable.register(ItemKeys.LEATHER_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 5, this.armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .with(TintedItemComponent.of(DyeableItemColor.of(0)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 5, this.armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .with(TintedItemComponent.of(DyeableItemColor.of(0)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 5, this.armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .with(TintedItemComponent.of(DyeableItemColor.of(0)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 5, this.armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_LEATHER_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .with(TintedItemComponent.of(DyeableItemColor.of(0)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.CHAINMAIL_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.CHAINMAIL_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.CHAINMAIL_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHAINMAIL_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.CHAINMAIL_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.CHAINMAIL), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_CHAIN)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.CHAIN))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_CHAINMAIL_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_IRON_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_IRON_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_IRON_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 15, this.armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.IRON))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_IRON_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 33, this.armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 33, this.armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 33, this.armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 33, this.armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_DIAMOND_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 7, this.armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 7, this.armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 7, this.armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 7, this.armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.GOLD))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_GOLDEN_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.HELMET, 37, this.armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_CHESTPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_CHESTPLATE).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.CHESTPLATE, 37, this.armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_LEGGINGS, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_LEGGINGS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.LEGGINGS, 37, this.armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_BOOTS, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_BOOTS).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(ArmorItem.Type.BOOTS, 37, this.armorMaterials.getOrThrow(ArmorMaterialKeys.NETHERITE), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_NETHERITE)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .with(EnchantableItemComponent.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_NETHERITE_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_HELMET, create(
                ItemBase.Builder.forItem(ItemKeys.TURTLE_HELMET).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.of(275, EquipmentSlot.HEAD, this.armorMaterials.getOrThrow(ArmorMaterialKeys.TURTLE), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_TURTLE)))
                    .with(EnchantableItemComponent.of(ArmorMaterials.TURTLE))
                    .with(RepairableItemComponent.of(ItematicItemTags.REPAIRS_TURTLE_ARMOR))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER_HORSE_ARMOR, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER_HORSE_ARMOR).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.ofAnimal(this.armorMaterials.getOrThrow(ArmorMaterialKeys.LEATHER), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_LEATHER), AnimalArmorItem.Type.EQUESTRIAN))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(DyeableItemComponent.of())
                    .with(TintedItemComponent.of(DyeableItemColor.of(0)))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_HORSE_ARMOR, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_HORSE_ARMOR).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.ofAnimal(this.armorMaterials.getOrThrow(ArmorMaterialKeys.IRON), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_IRON), AnimalArmorItem.Type.EQUESTRIAN))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLDEN_HORSE_ARMOR, create(
                ItemBase.Builder.forItem(ItemKeys.GOLDEN_HORSE_ARMOR).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.ofAnimal(this.armorMaterials.getOrThrow(ArmorMaterialKeys.GOLD), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GOLD), AnimalArmorItem.Type.EQUESTRIAN))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND_HORSE_ARMOR, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND_HORSE_ARMOR).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.ofAnimal(this.armorMaterials.getOrThrow(ArmorMaterialKeys.DIAMOND), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_DIAMOND), AnimalArmorItem.Type.EQUESTRIAN))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WOLF_ARMOR, create(
                ItemBase.Builder.forItem(ItemKeys.WOLF_ARMOR).build(),
                ItemComponentSet.builder()
                    .with(ArmorItemComponent.ofAnimal(this.armorMaterials.getOrThrow(ArmorMaterialKeys.ARMADILLO), this.soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_WOLF), AnimalArmorItem.Type.CANINE, 4, this.soundEvents.getOrThrow(SoundEventKeys.WOLF_ARMOR_BREAK)))
                    .with(DyeableItemComponent.of(0x000000))
                    .with(TintedItemComponent.of(DyeableItemColor.of(1)))
                    .build()
            ));
        }

        private void bootstrapSkulls() {
            this.registerable.register(ItemKeys.SKELETON_SKULL, create(
                ItemBase.Builder.forBlock(ItemKeys.SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.SKELETON_SKULL), this.blocks.getOrThrow(BlockKeys.SKELETON_WALL_SKULL), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.WITHER_SKELETON_SKULL, create(
                ItemBase.Builder.forBlock(ItemKeys.WITHER_SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.WITHER_SKELETON_SKULL), this.blocks.getOrThrow(BlockKeys.WITHER_SKELETON_WALL_SKULL), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PLAYER_HEAD, create(
                ItemBase.Builder.forBlock(ItemKeys.PLAYER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.PLAYER_HEAD), this.blocks.getOrThrow(BlockKeys.PLAYER_WALL_HEAD), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.ZOMBIE_HEAD, create(
                ItemBase.Builder.forBlock(ItemKeys.ZOMBIE_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.ZOMBIE_HEAD), this.blocks.getOrThrow(BlockKeys.ZOMBIE_WALL_HEAD), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_HEAD, create(
                ItemBase.Builder.forBlock(ItemKeys.CREEPER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.CREEPER_HEAD), this.blocks.getOrThrow(BlockKeys.CREEPER_WALL_HEAD), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_HEAD, create(
                ItemBase.Builder.forBlock(ItemKeys.DRAGON_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.DRAGON_HEAD), this.blocks.getOrThrow(BlockKeys.DRAGON_WALL_HEAD), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_HEAD, create(
                ItemBase.Builder.forBlock(ItemKeys.PIGLIN_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EquipmentItemComponent.skull(this.blocks.getOrThrow(BlockKeys.PIGLIN_HEAD), this.blocks.getOrThrow(BlockKeys.PIGLIN_WALL_HEAD), this.soundEvents, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapFuel() {
            this.registerable.register(ItemKeys.COAL_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.COAL_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COAL_BLOCK)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.COAL_BLOCK_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_ROD, create(
                ItemBase.Builder.forItem(ItemKeys.BLAZE_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BLAZE_ROD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAL, create(
                ItemBase.Builder.forItem(ItemKeys.COAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.COAL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHARCOAL, create(
                ItemBase.Builder.forItem(ItemKeys.CHARCOAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.COAL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SPRUCE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.SPRUCE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BIRCH_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.BIRCH_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.JUNGLE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.JUNGLE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ACACIA_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.ACACIA_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CHERRY_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.CHERRY_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DARK_OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.DARK_OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MANGROVE_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.MANGROVE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_HANGING_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_HANGING_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BAMBOO_HANGING_SIGN), this.blocks.getOrThrow(BlockKeys.BAMBOO_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.HANGING_SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.OAK_SIGN), this.blocks.getOrThrow(BlockKeys.OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.SPRUCE_SIGN), this.blocks.getOrThrow(BlockKeys.SPRUCE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BIRCH_SIGN), this.blocks.getOrThrow(BlockKeys.BIRCH_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.JUNGLE_SIGN), this.blocks.getOrThrow(BlockKeys.JUNGLE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ACACIA_SIGN), this.blocks.getOrThrow(BlockKeys.ACACIA_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CHERRY_SIGN), this.blocks.getOrThrow(BlockKeys.CHERRY_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SIGN), this.blocks.getOrThrow(BlockKeys.DARK_OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MANGROVE_SIGN), this.blocks.getOrThrow(BlockKeys.MANGROVE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_SIGN, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_SIGN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BAMBOO_SIGN), this.blocks.getOrThrow(BlockKeys.BAMBOO_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SIGN_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_PLANKS, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_PLANKS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_PLANKS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_BLOCK)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_OAK_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_OAK_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_SPRUCE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BIRCH_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_JUNGLE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_ACACIA_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CHERRY_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_DARK_OAK_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_MANGROVE_LOG, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_LOG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_LOG)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_OAK_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_OAK_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_SPRUCE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_SPRUCE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_SPRUCE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BIRCH_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_BIRCH_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BIRCH_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_JUNGLE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_JUNGLE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_JUNGLE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_ACACIA_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_ACACIA_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_ACACIA_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_CHERRY_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_CHERRY_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_CHERRY_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_DARK_OAK_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_DARK_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_DARK_OAK_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_MANGROVE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_MANGROVE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_MANGROVE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STRIPPED_BAMBOO_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.STRIPPED_BAMBOO_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.STRIPPED_BAMBOO_BLOCK)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_WOOD, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_WOOD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_WOOD)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_FENCE, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_FENCE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_FENCE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC_STAIRS, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_STAIRS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_STAIRS)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_PRESSURE_PLATE, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_PRESSURE_PLATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_PRESSURE_PLATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_TRAPDOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_TRAPDOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_TRAPDOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_FENCE_GATE, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_FENCE_GATE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_FENCE_GATE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOOKSHELF, create(
                ItemBase.Builder.forBlock(ItemKeys.BOOKSHELF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BOOKSHELF)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHISELED_BOOKSHELF, create(
                ItemBase.Builder.forBlock(ItemKeys.CHISELED_BOOKSHELF).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHISELED_BOOKSHELF)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LECTERN, create(
                ItemBase.Builder.forBlock(ItemKeys.LECTERN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LECTERN)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHEST, create(
                ItemBase.Builder.forBlock(ItemKeys.CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHEST)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRAPPED_CHEST, create(
                ItemBase.Builder.forBlock(ItemKeys.TRAPPED_CHEST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.TRAPPED_CHEST)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LADDER, create(
                ItemBase.Builder.forBlock(ItemKeys.LADDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LADDER)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CRAFTING_TABLE, create(
                ItemBase.Builder.forBlock(ItemKeys.CRAFTING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CRAFTING_TABLE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUKEBOX, create(
                ItemBase.Builder.forBlock(ItemKeys.JUKEBOX).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUKEBOX)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.NOTE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.NOTE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NOTE_BLOCK)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LOOM, create(
                ItemBase.Builder.forBlock(ItemKeys.LOOM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LOOM)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPOSTER, create(
                ItemBase.Builder.forBlock(ItemKeys.COMPOSTER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.COMPOSTER)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BARREL, create(
                ItemBase.Builder.forBlock(ItemKeys.BARREL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BARREL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CARTOGRAPHY_TABLE, create(
                ItemBase.Builder.forBlock(ItemKeys.CARTOGRAPHY_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CARTOGRAPHY_TABLE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLETCHING_TABLE, create(
                ItemBase.Builder.forBlock(ItemKeys.FLETCHING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.FLETCHING_TABLE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SMITHING_TABLE, create(
                ItemBase.Builder.forBlock(ItemKeys.SMITHING_TABLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SMITHING_TABLE)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DAYLIGHT_DETECTOR, create(
                ItemBase.Builder.forBlock(ItemKeys.DAYLIGHT_DETECTOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DAYLIGHT_DETECTOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_DOOR, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_DOOR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_DOOR)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.DOOR_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_MOSAIC_SLAB, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_MOSAIC_SLAB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_MOSAIC_SLAB)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SLAB_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DEAD_BUSH, create(
                ItemBase.Builder.forBlock(ItemKeys.DEAD_BUSH).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DEAD_BUSH)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.PLANT_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_DEAD_BUSH))
                    .build()
            ));
            this.registerable.register(ItemKeys.OAK_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.OAK_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.OAK_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPRUCE_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.SPRUCE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SPRUCE_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BIRCH_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.BIRCH_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BIRCH_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.JUNGLE_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.JUNGLE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.JUNGLE_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ACACIA_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.ACACIA_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ACACIA_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CHERRY_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.CHERRY_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CHERRY_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.DARK_OAK_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.DARK_OAK_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.DARK_OAK_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MANGROVE_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.MANGROVE_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MANGROVE_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO_BUTTON, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO_BUTTON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO_BUTTON)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BUTTON_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.STICK, create(
                ItemBase.Builder.forItem(ItemKeys.STICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SMALL_WOODEN_ITEM_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOWL, create(
                ItemBase.Builder.forItem(ItemKeys.BOWL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SMALL_WOODEN_ITEM_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BAMBOO, create(
                ItemBase.Builder.forBlock(ItemKeys.BAMBOO).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BAMBOO)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.BAMBOO_FUEL_TIME))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockKeys.POTTED_BAMBOO))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCAFFOLDING, create(
                ItemBase.Builder.forBlock(ItemKeys.SCAFFOLDING).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.SCAFFOLDING)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.SCAFFOLDING_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_WOOL, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_WOOL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_WOOL)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_FUEL_TIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.WHITE_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.WHITE_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ORANGE_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.MAGENTA_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.YELLOW_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIME_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PINK_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GRAY_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.CYAN_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.PURPLE_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLUE_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BROWN_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.GREEN_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.RED_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_CARPET, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_CARPET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.BLACK_CARPET)))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOL_CARPET_FUEL_TIME))
                    .with(EquipmentItemComponent.of(EquipmentSlot.BODY, false, this.soundEvents.getOrThrow(SoundEventKeys.HORSE_SADDLE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
        }

        private void bootstrapProjectiles() {
            this.registerable.register(ItemKeys.ARROW, create(
                ItemBase.Builder.forItem(ItemKeys.ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ProjectileItemComponent.persistentProjectile(EntityType.ARROW, ArrowEntity::new, ArrowEntity::new))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOWBALL, create(
                ItemBase.Builder.forItem(ItemKeys.SNOWBALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.SNOWBALL)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EGG, create(
                ItemBase.Builder.forItem(ItemKeys.EGG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.EGG)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_PEARL, create(
                ItemBase.Builder.forItem(ItemKeys.ENDER_PEARL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.ENDER_PEARL)))
                    .with(CooldownItemComponent.of(20))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENDER_EYE, create(
                ItemBase.Builder.forItem(ItemKeys.ENDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ThrowableItemComponent.of())
                    .with(ProjectileItemComponent.of(EyeOfEnderEntityInitializer.INSTANCE))
                    .with(PreventUseWhenUsedOnTargetItemComponent.forBlock())
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            LocationCheckLootCondition.builder(
                                    LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                            .blocks(this.blocks.getOrThrow(BlockKeys.END_PORTAL_FRAME).value())
                                            .state(StatePredicate.Builder.create()
                                                .exactMatch(Properties.EYE, false))))
                                .build()
                        ),
                        PassingSequenceHandler.builder()
                            .add(ModifyBlockStateAction.builder(ActionContextParameter.TARGET)
                                .property(Properties.EYE, true)
                                .pushEntitiesUpwards()
                                .build())
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.INSTANCE)
                            .add(PlaySoundAction.of(ActionContextParameter.TARGET, this.soundEvents.getOrThrow(SoundEventKeys.END_PORTAL_FRAME_FILL), SoundCategory.BLOCKS))
                            .add(DisplayParticleAction.builder(ActionContextParameter.TARGET, ParticleTypes.SMOKE)
                                .count(16)
                                .offset(Vec3dProvider.of(
                                    -0.1875d, 0.1875d,
                                    0.8125d, 0.8125d,
                                    -0.1875d, 0.1875d))
                                .build())
                            .addOptional(LightEndPortalAction.of(ActionContextParameter.TARGET))
                    ))
                    .add(ItemEvents.THROW_PROJECTILE, ActionEntry.of(
                        PlaySoundAction.builder(ActionContextParameter.THIS, this.soundEvents.getOrThrow(SoundEventKeys.ENDER_EYE_LAUNCH), SoundCategory.NEUTRAL)
                            .pitch(0.33f, 0.5f)
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPERIENCE_BOTTLE, create(
                ItemBase.Builder.forItem(ItemKeys.EXPERIENCE_BOTTLE)
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
                ItemBase.Builder.forItem(ItemKeys.FIRE_CHARGE).build(),
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
                            .add(PlaySoundAction.builder(ActionContextParameter.TARGET, this.soundEvents.getOrThrow(SoundEventKeys.FIRE_CHARGE_USE), SoundCategory.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.WIND_CHARGE, create(
                ItemBase.Builder.forItem(ItemKeys.WIND_CHARGE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ThrowableItemComponent.of(1.5f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.WIND_CHARGE)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .with(CooldownItemComponent.of(WindChargeItemAccessor.cooldown()))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIREWORK_ROCKET, create(
                ItemBase.Builder.forItem(ItemKeys.FIREWORK_ROCKET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkItemComponent.INSTANCE)
                    .with(ProjectileItemComponent.of(FireworkRocketEntityInitializer.INSTANCE))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_FIREWORK_ROCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPLASH_POTION, create(
                ItemBase.Builder.forItem(ItemKeys.SPLASH_POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PotionHolderItemComponent.of(1.0f))
                    .with(ThrowableItemComponent.of(0.5f, -20.0f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.POTION)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .with(TintedItemComponent.of(PotionItemColor.INSTANCE))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPECTRAL_ARROW, create(
                ItemBase.Builder.forItem(ItemKeys.SPECTRAL_ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ProjectileItemComponent.persistentProjectile(EntityType.SPECTRAL_ARROW, SpectralArrowEntity::new, SpectralArrowEntity::new))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TIPPED_ARROW, create(
                ItemBase.Builder.forItem(ItemKeys.TIPPED_ARROW).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(PotionHolderItemComponent.of(0.125f))
                    .with(ProjectileItemComponent.persistentProjectile(EntityType.ARROW, ArrowEntity::new, ArrowEntity::new))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .with(TintedItemComponent.of(PotionItemColor.INSTANCE))
                    .build()
            ));
            this.registerable.register(ItemKeys.LINGERING_POTION, create(
                ItemBase.Builder.forItem(ItemKeys.LINGERING_POTION).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PotionHolderItemComponent.of(0.25f))
                    .with(ThrowableItemComponent.of(0.5f, -20.0f))
                    .with(ProjectileItemComponent.of(this.entityTypes.getOrThrow(EntityTypeKeys.POTION)))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .with(TintedItemComponent.of(PotionItemColor.INSTANCE))
                    .build()
            ));
        }

        private void bootstrapDyes() {
            this.registerable.register(ItemKeys.WHITE_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.WHITE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.ORANGE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.MAGENTA_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.LIGHT_BLUE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.YELLOW_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.LIME_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.PINK_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.GRAY_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.LIGHT_GRAY_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.CYAN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.PURPLE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.BLUE_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.BROWN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.GREEN_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.RED_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_DYE, create(
                ItemBase.Builder.forItem(ItemKeys.BLACK_DYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DyeItemComponent.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapRecords() {
            this.registerable.register(ItemKeys.MUSIC_DISC_13, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_13)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.THIRTEEN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CAT, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_CAT)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CAT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_BLOCKS, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_BLOCKS)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.BLOCKS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CHIRP, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_CHIRP)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CHIRP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CREATOR, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_CREATOR)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_CREATOR_MUSIC_BOX, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_CREATOR_MUSIC_BOX)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR_MUSIC_BOX)))
                    .build()
            ));

            this.registerable.register(ItemKeys.MUSIC_DISC_FAR, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_FAR)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FAR)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_MALL, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_MALL)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MALL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_MELLOHI, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_MELLOHI)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MELLOHI)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_STAL, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_STAL)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STAL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_STRAD, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_STRAD)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STRAD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_WARD, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_WARD)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WARD)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_11, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_11)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.ELEVEN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_WAIT, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_WAIT)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WAIT)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_OTHERSIDE, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_OTHERSIDE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.OTHERSIDE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_RELIC, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_RELIC)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.RELIC)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_PIGSTEP, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_PIGSTEP)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PIGSTEP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_PRECIPICE, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_PRECIPICE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PRECIPICE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MUSIC_DISC_5, create(
                ItemBase.Builder.forItem(ItemKeys.MUSIC_DISC_5)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(PlayableSongItemComponent.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FIVE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DISC_FRAGMENT_5, create(
                ItemBase.Builder.forItem(ItemKeys.DISC_FRAGMENT_5).tooltip(ItemKeys.DISC_FRAGMENT_5).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
        }

        private void bootstrapBuckets() {
            this.registerable.register(ItemKeys.BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.fluid(this.fluids.getOrThrow(FluidKeys.EMPTY), this.dispenseBehaviors))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WATER_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.WATER_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.fluid(this.fluids.getOrThrow(FluidKeys.WATER), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY), this.items, this.dispenseBehaviors))
                    .with(RecipeRemainderItemComponent.of(this.items.getOrThrow(ItemKeys.BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAVA_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.LAVA_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.fluid(this.fluids.getOrThrow(FluidKeys.LAVA), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_LAVA), this.items, this.dispenseBehaviors))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.LAVA_FUEL_TIME))
                    .with(RecipeRemainderItemComponent.of(this.items.getOrThrow(ItemKeys.BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemKeys.POWDER_SNOW_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.POWDER_SNOW_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.block(this.blocks.getOrThrow(BlockKeys.POWDER_SNOW), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_POWDER_SNOW), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.PUFFERFISH_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.PUFFERFISH_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.PUFFERFISH), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.SALMON_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.SALMON_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.SALMON), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.COD_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.COD_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.COD), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TROPICAL_FISH_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.TROPICAL_FISH_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.TROPICAL_FISH), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.AXOLOTL_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.AXOLOTL_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.AXOLOTL), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_AXOLOTL), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemKeys.TADPOLE_BUCKET, create(
                ItemBase.Builder.forItem(ItemKeys.TADPOLE_BUCKET).build(),
                ItemComponentSet.builder()
                    .with(BucketItemComponent.entity(this.fluids.getOrThrow(FluidKeys.WATER), this.entityTypes.getOrThrow(EntityTypeKeys.TADPOLE), this.soundEvents.getOrThrow(SoundEventKeys.BUCKET_EMPTY_TADPOLE), this.items, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapSmithingTemplates() {
            this.registerable.register(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_UPGRADE_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.NETHERITE_UPGRADE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.SENTRY_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.DUNE_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.COAST_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.WILD_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.WARD_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.EYE_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.VEX_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.TIDE_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.SNOUT_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.RIB_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.SPIRE_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.WAYFINDER_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.SHAPER_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.SILENCE_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.RAISER_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.HOST_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.HOST_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.FLOW_PATTERN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemBase.Builder.forItem(ItemKeys.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE).build(),
                ItemComponentSet.builder()
                    .with(SmithingTemplateItemComponent.of(this.smithingTemplates.getOrThrow(SmithingTemplates.BOLT_PATTERN)))
                    .build()
            ));
        }

        private void bootstrapBanners() {
            this.bootstrapBannerPatterns();
            this.registerable.register(ItemKeys.WHITE_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.WHITE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.WHITE_BANNER), this.blocks.getOrThrow(BlockKeys.WHITE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemKeys.ORANGE_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.ORANGE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.ORANGE_BANNER), this.blocks.getOrThrow(BlockKeys.ORANGE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGENTA_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.MAGENTA_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.MAGENTA_BANNER), this.blocks.getOrThrow(BlockKeys.MAGENTA_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_BLUE_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_BLUE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_BANNER), this.blocks.getOrThrow(BlockKeys.LIGHT_BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.YELLOW_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.YELLOW_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.YELLOW_BANNER), this.blocks.getOrThrow(BlockKeys.YELLOW_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIME_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIME_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIME_BANNER), this.blocks.getOrThrow(BlockKeys.LIME_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemKeys.PINK_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.PINK_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PINK_BANNER), this.blocks.getOrThrow(BlockKeys.PINK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemKeys.GRAY_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.GRAY_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.GRAY_BANNER), this.blocks.getOrThrow(BlockKeys.GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.LIGHT_GRAY_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.LIGHT_GRAY_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_BANNER), this.blocks.getOrThrow(BlockKeys.LIGHT_GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemKeys.CYAN_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.CYAN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.CYAN_BANNER), this.blocks.getOrThrow(BlockKeys.CYAN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemKeys.PURPLE_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.PURPLE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.PURPLE_BANNER), this.blocks.getOrThrow(BlockKeys.PURPLE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLUE_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.BLUE_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BLUE_BANNER), this.blocks.getOrThrow(BlockKeys.BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BROWN_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.BROWN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BROWN_BANNER), this.blocks.getOrThrow(BlockKeys.BROWN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemKeys.GREEN_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.GREEN_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.GREEN_BANNER), this.blocks.getOrThrow(BlockKeys.GREEN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemKeys.RED_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.RED_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.RED_BANNER), this.blocks.getOrThrow(BlockKeys.RED_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLACK_BANNER, create(
                ItemBase.Builder.forBlock(ItemKeys.BLACK_BANNER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(BlockItemComponent.attachedToSide(this.blocks.getOrThrow(BlockKeys.BLACK_BANNER), this.blocks.getOrThrow(BlockKeys.BLACK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemComponent.of(FurnaceBlockEntityUtil.WOOD_FUEL_TIME))
                    .with(BannerPatternHolderItemComponent.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapBannerPatterns() {
            this.registerable.register(ItemKeys.FLOWER_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.FLOWER_BANNER_PATTERN).tooltip(ItemKeys.FLOWER_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.FLOWER_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.CREEPER_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.CREEPER_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(ItemKeys.CREEPER_BANNER_PATTERN)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.CREEPER_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKULL_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.SKULL_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(ItemKeys.SKULL_BANNER_PATTERN)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.SKULL_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOJANG_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.MOJANG_BANNER_PATTERN)
                    .rarity(Rarity.EPIC)
                    .tooltip(ItemKeys.MOJANG_BANNER_PATTERN)
                    .build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.MOJANG_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOBE_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.GLOBE_BANNER_PATTERN).tooltip(ItemKeys.GLOBE_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.GLOBE_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.PIGLIN_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.PIGLIN_BANNER_PATTERN).tooltip(ItemKeys.PIGLIN_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.PIGLIN_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.FLOW_BANNER_PATTERN).tooltip(ItemKeys.FLOW_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.FLOW_PATTERN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUSTER_BANNER_PATTERN, create(
                ItemBase.Builder.forItem(ItemKeys.GUSTER_BANNER_PATTERN).tooltip(ItemKeys.GUSTER_BANNER_PATTERN).build(),
                ItemComponentSet.builder()
                    .with(BannerPatternItemComponent.of(BannerPatternTags.GUSTER_PATTERN_ITEM))
                    .build()
            ));
        }

        private void bootstrapDecoratedPotPatterns() {
            this.registerable.register(ItemKeys.BRICK, create(
                ItemBase.Builder.forItem(ItemKeys.BRICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLANK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ANGLER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.ANGLER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ANGLER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARCHER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.ARCHER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARCHER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMS_UP_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.ARMS_UP_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARMS_UP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLADE_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.BLADE_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLADE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREWER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.BREWER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BREWER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BURN_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.BURN_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BURN)))
                    .build()
            ));
            this.registerable.register(ItemKeys.DANGER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.DANGER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.DANGER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.EXPLORER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.EXPLORER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.EXPLORER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLOW_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.FLOW_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FLOW)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FRIEND_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.FRIEND_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FRIEND)))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUSTER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.GUSTER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.GUSTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEART_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.HEART_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEART)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEARTBREAK_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.HEARTBREAK_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEARTBREAK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.HOWL_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.HOWL_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HOWL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MINER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.MINER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MINER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.MOURNER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.MOURNER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MOURNER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PLENTY_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.PLENTY_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PLENTY)))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRIZE_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.PRIZE_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PRIZE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SCRAPE_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.SCRAPE_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SCRAPE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHEAF_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.SHEAF_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHEAF)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHELTER_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.SHELTER_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHELTER)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SKULL_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.SKULL_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SKULL)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SNORT_POTTERY_SHERD, create(
                ItemBase.Builder.forItem(ItemKeys.SNORT_POTTERY_SHERD).build(),
                ItemComponentSet.builder()
                    .with(DecoratedPotPatternItemComponent.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SNORT)))
                    .build()
            ));
        }

        private void bootstrapImmuneToDamage() {
            this.registerable.register(ItemKeys.ANCIENT_DEBRIS, create(
                ItemBase.Builder.forBlock(ItemKeys.ANCIENT_DEBRIS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.ANCIENT_DEBRIS)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_BLOCK, create(
                ItemBase.Builder.forBlock(ItemKeys.NETHERITE_BLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(BlockItemComponent.of(this.blocks.getOrThrow(BlockKeys.NETHERITE_BLOCK)))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_INGOT, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHERITE_SCRAP, create(
                ItemBase.Builder.forItem(ItemKeys.NETHERITE_SCRAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_FIRE))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_STAR, create(
                ItemBase.Builder.forItem(ItemKeys.NETHER_STAR)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(ImmuneToDamageItemComponent.of(DamageTypeTags.IS_EXPLOSION))
                    .build()
            ));
        }

        private void bootstrapMiscellaneous() {
            this.registerable.register(ItemKeys.AIR, create(
                ItemBase.Builder.forBlock(ItemKeys.AIR).build()
            ));
            this.registerable.register(ItemKeys.SADDLE, create(
                ItemBase.Builder.forItem(ItemKeys.SADDLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(SaddleItemComponent.INSTANCE)
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SADDLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TURTLE_SCUTE, create(
                ItemBase.Builder.forItem(ItemKeys.TURTLE_SCUTE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.ARMADILLO_SCUTE, create(
                ItemBase.Builder.forItem(ItemKeys.ARMADILLO_SCUTE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.DIAMOND, create(
                ItemBase.Builder.forItem(ItemKeys.DIAMOND).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.EMERALD, create(
                ItemBase.Builder.forItem(ItemKeys.EMERALD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.LAPIS_LAZULI, create(
                ItemBase.Builder.forItem(ItemKeys.LAPIS_LAZULI).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.QUARTZ, create(
                ItemBase.Builder.forItem(ItemKeys.QUARTZ).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.AMETHYST_SHARD, create(
                ItemBase.Builder.forItem(ItemKeys.AMETHYST_SHARD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_IRON, create(
                ItemBase.Builder.forItem(ItemKeys.RAW_IRON).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_INGOT, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_COPPER, create(
                ItemBase.Builder.forItem(ItemKeys.RAW_COPPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.COPPER_INGOT, create(
                ItemBase.Builder.forItem(ItemKeys.COPPER_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RAW_GOLD, create(
                ItemBase.Builder.forItem(ItemKeys.RAW_GOLD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_INGOT, create(
                ItemBase.Builder.forItem(ItemKeys.GOLD_INGOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FEATHER, create(
                ItemBase.Builder.forItem(ItemKeys.FEATHER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.BURST))
                    .build()
            ));
            this.registerable.register(ItemKeys.GUNPOWDER, create(
                ItemBase.Builder.forItem(ItemKeys.GUNPOWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FLINT, create(
                ItemBase.Builder.forItem(ItemKeys.FLINT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEATHER, create(
                ItemBase.Builder.forItem(ItemKeys.LEATHER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLAY_BALL, create(
                ItemBase.Builder.forItem(ItemKeys.CLAY_BALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PAPER, create(
                ItemBase.Builder.forItem(ItemKeys.PAPER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BOOK, create(
                ItemBase.Builder.forItem(ItemKeys.BOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(EnchantableItemComponent.ofTransforming(1, this.items.getOrThrow(ItemKeys.ENCHANTED_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.SLIME_BALL, create(
                ItemBase.Builder.forItem(ItemKeys.SLIME_BALL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.COMPASS, create(
                ItemBase.Builder.forItem(ItemKeys.COMPASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(PointableItemComponent.of(this.pointers.getOrThrow(PointerKeys.SPAWN_LOCATION), Util.createTranslationKey("item", Identifier.ofVanilla("lodestone_compass"))))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            LocationCheckLootCondition.builder(
                                    LocationPredicate.Builder.create()
                                        .block(BlockPredicate.Builder.create()
                                            .blocks(this.blocks.getOrThrow(BlockKeys.LODESTONE).value())))
                                .build()
                        ),
                        PassingSequenceHandler.builder()
                            .add(SetItemPointerLocationAction.of(ActionContextParameter.TARGET))
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.RECOVERY_COMPASS, create(
                ItemBase.Builder.forItem(ItemKeys.RECOVERY_COMPASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(PointableItemComponent.of(this.pointers.getOrThrow(PointerKeys.LAST_DEATH)))
                    .build()
            ));
            this.registerable.register(ItemKeys.BUNDLE, create(
                ItemBase.Builder.forItem(ItemKeys.BUNDLE)
                    .itemBarStyle(ItemBarStyleKeys.BUNDLE)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ItemHolderItemComponent.of(
                        1,
                        ItemHolderRules.builder()
                            .rule(RejectItemHolderRule.INSTANCE, ItemPredicate.Builder.create()
                                .itematic$items(this.items.getOrThrow(ItematicItemTags.BANNED_BUNDLE_ITEMS))
                                .build())
                            .rule(OccupancyHeldItemsWithPenaltyItemHolderRule.of(BundleContentsComponentAccessor.nestedBundleOccupancy()), ItemPredicate.Builder.create()
                                .itematic$behavior(ItemComponentTypes.ITEM_HOLDER)
                                .build())
                            .rule(FractionItemHolderRule.of(Fraction.ONE), ItemPredicate.Builder.create()
                                .itematic$dataComponents(DataComponentTypes.BEES)
                                .build())
                            .build(),
                        this.soundEvents.getOrThrow(SoundEventKeys.BUNDLE_INSERT),
                        this.soundEvents.getOrThrow(SoundEventKeys.BUNDLE_REMOVE_ONE),
                        this.soundEvents.getOrThrow(SoundEventKeys.BUNDLE_DROP_CONTENTS)))
                    .build()
            ));
            this.registerable.register(ItemKeys.CLOCK, create(
                ItemBase.Builder.forItem(ItemKeys.CLOCK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.SPYGLASS, create(
                ItemBase.Builder.forItem(ItemKeys.SPYGLASS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(ZoomItemComponent.of(SpyglassItem.FOV_MULTIPLIER, this.soundEvents.getOrThrow(SoundEventKeys.SPYGLASS_USE), this.soundEvents.getOrThrow(SoundEventKeys.SPYGLASS_STOP_USING)))
                    .with(UseableItemComponent.builder()
                        .ticks(SpyglassItem.MAX_USE_TIME)
                        .animation(UseAction.SPYGLASS)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOWSTONE_DUST, create(
                ItemBase.Builder.forItem(ItemKeys.GLOWSTONE_DUST).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.INK_SAC, create(
                ItemBase.Builder.forItem(ItemKeys.INK_SAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(false))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLOW_INK_SAC, create(
                ItemBase.Builder.forItem(ItemKeys.GLOW_INK_SAC).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, Actions.glowSign(true))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE_MEAL, create(
                ItemBase.Builder.forItem(ItemKeys.BONE_MEAL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(FertilizeAction.INSTANCE))
                    .build()
            ));
            this.registerable.register(ItemKeys.BONE, create(
                ItemBase.Builder.forItem(ItemKeys.BONE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.SUGAR, create(
                ItemBase.Builder.forItem(ItemKeys.SUGAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.FILLED_MAP, create(
                ItemBase.Builder.forItem(ItemKeys.FILLED_MAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(MapHolderItemComponent.INSTANCE)
                    .with(TintedItemComponent.of(MapItemColor.INSTANCE))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(MarkBannerOnItemAction.of(ActionContextParameter.TARGET))
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.GHAST_TEAR, create(
                ItemBase.Builder.forItem(ItemKeys.GHAST_TEAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOLD_NUGGET, create(
                ItemBase.Builder.forItem(ItemKeys.GOLD_NUGGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.STAR))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLASS_BOTTLE, create(
                ItemBase.Builder.forItem(ItemKeys.GLASS_BOTTLE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.GLASS_BOTTLE)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        ActionRequirements.of(
                            ActionContextParameters.of(ActionContextParameter.THIS, ActionContextParameter.TARGET),
                            LocationCheckLootCondition.builder(
                                LocationPredicate.Builder.create()
                                    .fluid(FluidPredicate.Builder.create()
                                        .tag(this.fluids.getOrThrow(FluidTags.WATER)))
                            ).build()
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(ExchangeItemAction.of(
                                this.items.getOrThrow(ItemKeys.POTION),
                                ComponentChanges.builder()
                                    .add(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(potions.getOrThrow(PotionKeys.WATER)))
                                    .build()))
                            .add(InvokeGameEventAction.of(GameEvent.FLUID_PICKUP, ActionContextParameter.TARGET, ActionContextParameter.THIS))
                            .add(PlaySoundAction.of(ActionContextParameter.THIS, this.soundEvents.getOrThrow(SoundEventKeys.BOTTLE_FILL), SoundCategory.NEUTRAL))
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.FERMENTED_SPIDER_EYE, create(
                ItemBase.Builder.forItem(ItemKeys.FERMENTED_SPIDER_EYE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BLAZE_POWDER, create(
                ItemBase.Builder.forItem(ItemKeys.BLAZE_POWDER).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAGMA_CREAM, create(
                ItemBase.Builder.forItem(ItemKeys.MAGMA_CREAM).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GLISTERING_MELON_SLICE, create(
                ItemBase.Builder.forItem(ItemKeys.GLISTERING_MELON_SLICE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.WRITABLE_BOOK, create(
                ItemBase.Builder.forItem(ItemKeys.WRITABLE_BOOK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(WritableItemComponent.of(this.items.getOrThrow(ItemKeys.WRITTEN_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.WRITTEN_BOOK, create(
                ItemBase.Builder.forItem(ItemKeys.WRITTEN_BOOK)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(16))
                    .with(TextHolderItemComponent.INSTANCE)
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE, ActionEntry.of(OpenBookFromItemAction.INSTANCE))
                    .build()
            ));
            this.registerable.register(ItemKeys.MAP, create(
                ItemBase.Builder.forItem(ItemKeys.MAP).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(MappableItemComponent.of(this.items.getOrThrow(ItemKeys.FILLED_MAP)))
                    .build()
            ));
            this.registerable.register(ItemKeys.FIREWORK_STAR, create(
                ItemBase.Builder.forItem(ItemKeys.FIREWORK_STAR).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(FireworkExplosionHolderItemComponent.INSTANCE)
                    .with(TintedItemComponent.of(FireworkItemColor.INSTANCE))
                    .build()
            ));
            this.registerable.register(ItemKeys.ENCHANTED_BOOK, create(
                ItemBase.Builder.forItem(ItemKeys.ENCHANTED_BOOK)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(EnchantmentHolderItemComponent.of(this.items.getOrThrow(ItemKeys.BOOK)))
                    .build()
            ));
            this.registerable.register(ItemKeys.NETHER_BRICK, create(
                ItemBase.Builder.forItem(ItemKeys.NETHER_BRICK).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_SHARD, create(
                ItemBase.Builder.forItem(ItemKeys.PRISMARINE_SHARD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.PRISMARINE_CRYSTALS, create(
                ItemBase.Builder.forItem(ItemKeys.PRISMARINE_CRYSTALS).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_FOOT, create(
                ItemBase.Builder.forItem(ItemKeys.RABBIT_FOOT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.RABBIT_HIDE, create(
                ItemBase.Builder.forItem(ItemKeys.RABBIT_HIDE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.LEAD, create(
                ItemBase.Builder.forItem(ItemKeys.LEAD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(AttachLeashedEntitiesOnBlockAction.INSTANCE)
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.NAME_TAG, create(
                ItemBase.Builder.forItem(ItemKeys.NAME_TAG).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_ENTITY, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(SetEntityNameFromItemAction.of(ActionContextParameter.TARGET))
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.INSTANCE)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.POPPED_CHORUS_FRUIT, create(
                ItemBase.Builder.forItem(ItemKeys.POPPED_CHORUS_FRUIT).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.DRAGON_BREATH, create(
                ItemBase.Builder.forItem(ItemKeys.DRAGON_BREATH)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(RecipeRemainderItemComponent.of(this.items.getOrThrow(ItemKeys.GLASS_BOTTLE)))
                    .build()
            ));
            this.registerable.register(ItemKeys.TOTEM_OF_UNDYING, create(
                ItemBase.Builder.forItem(ItemKeys.TOTEM_OF_UNDYING)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(LifeSavingItemComponent.of(
                        new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.REGENERATION), 900, 1),
                        new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.ABSORPTION), 100, 1),
                        new StatusEffectInstance(this.statusEffects.getOrThrow(StatusEffectKeys.FIRE_RESISTANCE), 800, 0)
                    ))
                    .build()
            ));
            this.registerable.register(ItemKeys.SHULKER_SHELL, create(
                ItemBase.Builder.forItem(ItemKeys.SHULKER_SHELL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.IRON_NUGGET, create(
                ItemBase.Builder.forItem(ItemKeys.IRON_NUGGET).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.KNOWLEDGE_BOOK, create(
                ItemBase.Builder.forItem(ItemKeys.KNOWLEDGE_BOOK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(UnlockRecipesItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.DEBUG_STICK, create(
                ItemBase.Builder.forItem(ItemKeys.DEBUG_STICK)
                    .rarity(Rarity.EPIC)
                    .glint()
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(DebugStickItemComponent.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemKeys.PHANTOM_MEMBRANE, create(
                ItemBase.Builder.forItem(ItemKeys.PHANTOM_MEMBRANE).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.NAUTILUS_SHELL, create(
                ItemBase.Builder.forItem(ItemKeys.NAUTILUS_SHELL).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.HEART_OF_THE_SEA, create(
                ItemBase.Builder.forItem(ItemKeys.HEART_OF_THE_SEA)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.GOAT_HORN, create(
                ItemBase.Builder.forItem(ItemKeys.GOAT_HORN).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(1))
                    .with(PlayableItemComponent.of(InstrumentTags.GOAT_HORNS))
                    .build()
            ));
            this.registerable.register(ItemKeys.HONEYCOMB, create(
                ItemBase.Builder.forItem(ItemKeys.HONEYCOMB).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .with(DispensableItemComponent.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.WAX_BLOCK)))
                    .build(),
                ItemEventMap.builder()
                    .add(ItemEvents.USE_ON_BLOCK, ActionEntry.of(
                        FirstToPassRequirementsSequenceHandler.builder()
                            .add(Actions.waxSign(true))
                            .add(PassingSequenceHandler.builder()
                                .add(WaxBlockAction.of(ActionContextParameter.TARGET))
                                .add(DecrementItemAction.of(1))
                                .add(SwingHandAction.INSTANCE))))
                    .build()
            ));
            this.registerable.register(ItemKeys.ECHO_SHARD, create(
                ItemBase.Builder.forItem(ItemKeys.ECHO_SHARD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.TRIAL_KEY, create(
                ItemBase.Builder.forItem(ItemKeys.TRIAL_KEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.OMINOUS_TRIAL_KEY, create(
                ItemBase.Builder.forItem(ItemKeys.OMINOUS_TRIAL_KEY).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
            this.registerable.register(ItemKeys.BREEZE_ROD, create(
                ItemBase.Builder.forItem(ItemKeys.BREEZE_ROD).build(),
                ItemComponentSet.builder()
                    .with(StackableItemComponent.of(64))
                    .build()
            ));
        }
    }
}
