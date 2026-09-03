package net.errorcraft.itematic.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.item.BrushItemAccessor;
import net.errorcraft.itematic.mixin.world.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.world.item.MaceItemAccessor;
import net.errorcraft.itematic.references.BlockIds;
import net.errorcraft.itematic.references.EntityTypeIds;
import net.errorcraft.itematic.references.FluidIds;
import net.errorcraft.itematic.references.ItemBarStyleIds;
import net.errorcraft.itematic.references.ItemIds;
import net.errorcraft.itematic.references.MobEffectIds;
import net.errorcraft.itematic.references.PotionIds;
import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.tags.ItematicBlockTags;
import net.errorcraft.itematic.tags.ItematicItemTags;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.ActionEventMap;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.action.actions.AddStatusEffectsAction;
import net.errorcraft.itematic.world.action.actions.ApplySuspiciousStewEffectsFromItemAction;
import net.errorcraft.itematic.world.action.actions.AttachLeashedEntitiesOnBlockAction;
import net.errorcraft.itematic.world.action.actions.ClearStatusEffectsAction;
import net.errorcraft.itematic.world.action.actions.DamageItemAction;
import net.errorcraft.itematic.world.action.actions.DecrementItemAction;
import net.errorcraft.itematic.world.action.actions.DisplayParticleAction;
import net.errorcraft.itematic.world.action.actions.ExchangeItemAction;
import net.errorcraft.itematic.world.action.actions.FertilizeAction;
import net.errorcraft.itematic.world.action.actions.InvokeGameEventAction;
import net.errorcraft.itematic.world.action.actions.LightEndPortalAction;
import net.errorcraft.itematic.world.action.actions.MarkBannerOnItemAction;
import net.errorcraft.itematic.world.action.actions.ModifyBlockStateAction;
import net.errorcraft.itematic.world.action.actions.ModifyItemAction;
import net.errorcraft.itematic.world.action.actions.PlaySoundAction;
import net.errorcraft.itematic.world.action.actions.RemoveStatusEffectsAction;
import net.errorcraft.itematic.world.action.actions.SetBlockStateAction;
import net.errorcraft.itematic.world.action.actions.SetEntityNameFromItemAction;
import net.errorcraft.itematic.world.action.actions.SwingHandAction;
import net.errorcraft.itematic.world.action.actions.TeleportAction;
import net.errorcraft.itematic.world.action.actions.TwirlPlayerAction;
import net.errorcraft.itematic.world.action.actions.WaxBlockAction;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.errorcraft.itematic.world.entity.spawn.EntitySpawner;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.AlignYawEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.DiscardEntitySpawnRule;
import net.errorcraft.itematic.world.entity.spawn.rule.rules.FitsInVolumeEntitySpawnRule;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorSet;
import net.errorcraft.itematic.world.item.behavior.behaviors.AttackBlockingItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BannerPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BlockItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BrushItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.BucketItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CastableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CompostableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ConsumableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.CooldownItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DamageableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DebugStickItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DecoratedPotPatternItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DispensableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.DyeItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EnchantmentHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EntityItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.EquipmentItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkExplosionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FireworkItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FoodItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.FuelItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.GliderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ImmuneToDamageItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ItemHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.MapHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.MappableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.OminousEffectProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PlayableSongItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PotionHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.PreventUseWhenUsedOnTargetItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ProjectileItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.RepairableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ShooterItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SmithingTemplateProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.StackableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SteeringItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.SuspiciousEffectIngredientItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.TextHolderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ThrowableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ToolItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.TrimMaterialProviderItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.UnlockRecipesItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.UseableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WeaponItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.WritableItemBehavior;
import net.errorcraft.itematic.world.item.behavior.behaviors.ZoomItemBehavior;
import net.errorcraft.itematic.world.item.component.ItemDamageRules;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.weapon.melee.SmashingWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.SmashingMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.ChargeableShooterMethod;
import net.errorcraft.itematic.world.item.weapon.shooter.method.methods.DirectShooterMethod;
import net.errorcraft.itematic.world.level.block.CompostChances;
import net.errorcraft.itematic.world.level.block.FuelTimes;
import net.errorcraft.itematic.world.level.storage.loot.functions.SetItemPointerLocationItemModifier;
import net.errorcraft.itematic.world.level.storage.loot.functions.SplitItemModifier;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.errorcraft.itematic.world.level.storage.loot.predicates.SideCheckPredicate;
import net.errorcraft.itematic.world.phys.Vec3Provider;
import net.minecraft.advancements.criterion.BlockPredicate;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.FluidPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DamagePredicate;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.component.predicates.PotionsPredicate;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Instrument;
import net.minecraft.world.item.Instruments;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpyglassItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.minecraft.world.item.equipment.trim.TrimMaterials;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetNameFunction;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class Items {
    public static final int UNSTACKABLE_MAX_STACK_SIZE = 1;
    public static final Codec<Item> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemDisplay.CODEC.fieldOf("display").forGetter(Item::itematic$display),
        ItemAttributeModifiers.CODEC.optionalFieldOf("attribute_modifiers", ItemAttributeModifiers.EMPTY).forGetter(Item::itematic$attributeModifiers),
        ItemBehaviorSet.CODEC.optionalFieldOf("behavior", ItemBehaviorSet.EMPTY).forGetter(Item::itematic$behavior),
        ActionEventMap.codec(ItematicBuiltInRegistries.ITEM_EVENT).optionalFieldOf("events", ActionEventMap.empty()).forGetter(Item::itematic$events)
    ).apply(instance, Items::create));
    public static final Codec<HolderSet<Item>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.ITEM);
    public static final StreamCodec<RegistryFriendlyByteBuf, HolderSet<Item>> LIST_STREAM_CODEC = ByteBufCodecs.holderSet(Registries.ITEM);

    public static void bootstrap(BootstrapContext<Item> registerable) {
        new Bootstrapper(registerable).bootstrap();
    }

    public static ResourceKey<Item> keyFromBlock(Block block) {
        Identifier id = BuiltInRegistries.BLOCK.getKey(block);
        return ResourceKey.create(Registries.ITEM, id);
    }

    private static Item create(ItemDisplay display) {
        return create(display, ItemBehaviorSet.EMPTY);
    }

    private static Item create(ItemDisplay display, ItemBehaviorSet behavior) {
        return create(display, behavior, ActionEventMap.empty());
    }

    private static Item create(ItemDisplay display, ItemBehaviorSet behavior, ActionEventMap<ItemEvent> events) {
        return create(display, ItemAttributeModifiers.EMPTY, behavior, events);
    }

    private static Item create(ItemDisplay display, ItemAttributeModifiers attributeModifiers, ItemBehaviorSet behavior) {
        return create(display, attributeModifiers, behavior, ActionEventMap.empty());
    }

    private static Item create(ItemDisplay display, ItemAttributeModifiers attributeModifiers, ItemBehaviorSet behavior, ActionEventMap<ItemEvent> events) {
        Item item = new Item(new Item.Properties());
        item.itematic$setDisplay(display);
        item.itematic$setAttributeModifiers(attributeModifiers);
        item.itematic$setBehavior(behavior);
        item.itematic$setEvents(events);
        return item;
    }

    private static class Bootstrapper {
        private final BootstrapContext<Item> registerable;
        private final HolderGetter<Item> items;
        private final HolderGetter<EntityType<?>> entityTypes;
        private final HolderGetter<Block> blocks;
        private final HolderGetter<DispenseBehavior> dispenseBehaviors;
        private final HolderGetter<SoundEvent> soundEvents;
        private final HolderGetter<Fluid> fluids;
        private final HolderGetter<ActionEntry> actions;
        private final HolderGetter<DecoratedPotPattern> decoratedPotPatterns;
        private final HolderGetter<MobEffect> statusEffects;
        private final HolderGetter<Potion> potions;
        private final HolderGetter<Enchantment> enchantments;
        private final HolderGetter<JukeboxSong> jukeboxSongs;
        private final HolderGetter<Instrument> instruments;
        private final HolderGetter<TrimMaterial> trimMaterials;
        private final HolderGetter<ChickenVariant> chickenVariants;
        private final HolderGetter<DamageType> damageTypes;
        private final HolderGetter<BannerPattern> bannerPatterns;

        private Bootstrapper(BootstrapContext<Item> registerable) {
            this.registerable = registerable;
            this.items = registerable.lookup(Registries.ITEM);
            this.entityTypes = registerable.lookup(Registries.ENTITY_TYPE);
            this.blocks = registerable.lookup(Registries.BLOCK);
            this.dispenseBehaviors = registerable.lookup(ItematicRegistries.DISPENSE_BEHAVIOR);
            this.soundEvents = registerable.lookup(Registries.SOUND_EVENT);
            this.fluids = registerable.lookup(Registries.FLUID);
            this.actions = registerable.lookup(ItematicRegistries.ACTION);
            this.decoratedPotPatterns = registerable.lookup(Registries.DECORATED_POT_PATTERN);
            this.statusEffects = registerable.lookup(Registries.MOB_EFFECT);
            this.potions = registerable.lookup(Registries.POTION);
            this.enchantments = registerable.lookup(Registries.ENCHANTMENT);
            this.jukeboxSongs = registerable.lookup(Registries.JUKEBOX_SONG);
            this.instruments = registerable.lookup(Registries.INSTRUMENT);
            this.trimMaterials = registerable.lookup(Registries.TRIM_MATERIAL);
            this.chickenVariants = registerable.lookup(Registries.CHICKEN_VARIANT);
            this.damageTypes = registerable.lookup(Registries.DAMAGE_TYPE);
            this.bannerPatterns = registerable.lookup(Registries.BANNER_PATTERN);
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
            this.registerable.register(ItemIds.MILK_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.MILK_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.MILK_BUCKET)
                        .remainder(this.items.getOrThrow(ItemIds.BUCKET))
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(ClearStatusEffectsAction.of(LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POTION, create(
                ItemDisplay.Builder.forItem(ItemIds.POTION).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_DRINK)
                        .remainder(this.items.getOrThrow(ItemIds.GLASS_BOTTLE))
                        .build())
                    .with(PotionHolderItemBehavior.of(1.0f))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        AllOfCondition.allOf(
                            InvertedLootItemCondition.invert(
                                SideCheckPredicate.builder(Direction.DOWN)
                            ),
                            LocationCheckPredicates.builder(
                                PositionTarget.INTERACTED,
                                LocationPredicate.Builder.location()
                                    .setBlock(BlockPredicate.Builder.block()
                                        .of(this.blocks, BlockTags.CONVERTABLE_TO_MUD))
                            ),
                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                .withComponents(DataComponentMatchers.Builder.components()
                                    .partial(
                                        DataComponentPredicates.POTIONS,
                                        new PotionsPredicate(HolderSet.direct(
                                            this.potions.getOrThrow(PotionIds.WATER)
                                        ))
                                    ).build()
                                )
                            )
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.GENERIC_SPLASH), SoundSource.BLOCKS))
                            .add(ExchangeItemAction.of(this.items.getOrThrow(ItemIds.GLASS_BOTTLE)))
                            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, ParticleTypes.SPLASH)
                                .count(5)
                                .offset(Vec3Provider.of(
                                    -0.5d, 0.5d,
                                    1.0d, 1.0d,
                                    -0.5d, 0.5d
                                ))
                                .speed(1.0d)
                                .build())
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.BOTTLE_EMPTY), SoundSource.BLOCKS))
                            .add(SetBlockStateAction.of(PositionTarget.INTERACTED, this.blocks.getOrThrow(BlockIds.MUD)))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.OMINOUS_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemIds.OMINOUS_BOTTLE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.OMINOUS_BOTTLE).build())
                    .with(OminousEffectProviderItemBehavior.INSTANCE)
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(PlaySoundAction.of(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventIds.OMINOUS_BOTTLE_DISPOSE))))
                    .build()
            ));
        }

        private void bootstrapFood() {
            this.registerable.register(ItemIds.APPLE, create(
                ItemDisplay.Builder.forItem(ItemIds.APPLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.APPLE)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.MELON_SLICE, create(
                ItemDisplay.Builder.forItem(ItemIds.MELON_SLICE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.MELON_SLICE)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.DRIED_KELP, create(
                ItemDisplay.Builder.forItem(ItemIds.DRIED_KELP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DRIED_KELP)
                        .food(Foods.DRIED_KELP)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.CARROT, create(
                ItemDisplay.Builder.forItem(ItemIds.CARROT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.CARROT)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CARROTS)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.POTATO, create(
                ItemDisplay.Builder.forItem(ItemIds.POTATO).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.POTATO)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POTATOES)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.BAKED_POTATO, create(
                ItemDisplay.Builder.forItem(ItemIds.BAKED_POTATO).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.BAKED_POTATO)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.CHORUS_FRUIT, create(
                ItemDisplay.Builder.forItem(ItemIds.CHORUS_FRUIT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.CHORUS_FRUIT)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(TeleportAction.of(16, LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BEETROOT, create(
                ItemDisplay.Builder.forItem(ItemIds.BEETROOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.BEETROOT)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.SWEET_BERRIES, create(
                ItemDisplay.Builder.forItem(ItemIds.SWEET_BERRIES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.SWEET_BERRIES)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SWEET_BERRY_BUSH)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOW_BERRIES, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOW_BERRIES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.GLOW_BERRIES)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CAVE_VINES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.BREAD, create(
                ItemDisplay.Builder.forItem(ItemIds.BREAD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.BREAD)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.COOKIE, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKIE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKIE)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.PORKCHOP, create(
                ItemDisplay.Builder.forItem(ItemIds.PORKCHOP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.PORKCHOP)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_PORKCHOP, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_PORKCHOP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_PORKCHOP)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.BEEF, create(
                ItemDisplay.Builder.forItem(ItemIds.BEEF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.BEEF)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_BEEF, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_BEEF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_BEEF)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.CHICKEN, create(
                ItemDisplay.Builder.forItem(ItemIds.CHICKEN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.CHICKEN)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.3f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 600)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_CHICKEN, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_CHICKEN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_CHICKEN)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.RABBIT, create(
                ItemDisplay.Builder.forItem(ItemIds.RABBIT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.RABBIT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_RABBIT, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_RABBIT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_RABBIT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.MUTTON, create(
                ItemDisplay.Builder.forItem(ItemIds.MUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.MUTTON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_MUTTON, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_MUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_MUTTON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COD, create(
                ItemDisplay.Builder.forItem(ItemIds.COD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COD)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.SALMON, create(
                ItemDisplay.Builder.forItem(ItemIds.SALMON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.SALMON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.TROPICAL_FISH, create(
                ItemDisplay.Builder.forItem(ItemIds.TROPICAL_FISH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.TROPICAL_FISH)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.PUFFERFISH, create(
                ItemDisplay.Builder.forItem(ItemIds.PUFFERFISH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.PUFFERFISH)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 1200, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.HUNGER), 300, 2),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.NAUSEA), 300)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_COD, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_COD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_COD)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.COOKED_SALMON, create(
                ItemDisplay.Builder.forItem(ItemIds.COOKED_SALMON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.COOKED_SALMON)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.MUSHROOM_STEW, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSHROOM_STEW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.MUSHROOM_STEW)
                        .remainder(this.items.getOrThrow(ItemIds.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.RABBIT_STEW, create(
                ItemDisplay.Builder.forItem(ItemIds.RABBIT_STEW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.RABBIT_STEW)
                        .remainder(this.items.getOrThrow(ItemIds.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.BEETROOT_SOUP, create(
                ItemDisplay.Builder.forItem(ItemIds.BEETROOT_SOUP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.BEETROOT_SOUP)
                        .remainder(this.items.getOrThrow(ItemIds.BOWL))
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.SUSPICIOUS_STEW, create(
                ItemDisplay.Builder.forItem(ItemIds.SUSPICIOUS_STEW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.SUSPICIOUS_STEW)
                        .remainder(this.items.getOrThrow(ItemIds.BOWL))
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(ApplySuspiciousStewEffectsFromItemAction.of(LootContext.EntityTarget.THIS)))
                    .build()
            ));
            this.registerable.register(ItemIds.ROTTEN_FLESH, create(
                ItemDisplay.Builder.forItem(ItemIds.ROTTEN_FLESH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.ROTTEN_FLESH)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.8f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.HUNGER), 600)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.SPIDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemIds.SPIDER_EYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.SPIDER_EYE)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 100)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.POISONOUS_POTATO, create(
                ItemDisplay.Builder.forItem(ItemIds.POISONOUS_POTATO).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.POISONOUS_POTATO)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.6f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 100)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_APPLE, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_APPLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.GOLDEN_APPLE)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 100, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 2400)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.ENCHANTED_GOLDEN_APPLE, create(
                ItemDisplay.Builder.forItem(ItemIds.ENCHANTED_GOLDEN_APPLE)
                    .rarity(Rarity.RARE)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.ENCHANTED_GOLDEN_APPLE)
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 400, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.RESISTANCE), 6000),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.FIRE_RESISTANCE), 6000),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 2400)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_CARROT, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_CARROT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.GOLDEN_CARROT)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.PUMPKIN_PIE, create(
                ItemDisplay.Builder.forItem(ItemIds.PUMPKIN_PIE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.PUMPKIN_PIE)
                        .build())
                    .with(CompostableItemBehavior.of(CompostChances.GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.HONEY_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemIds.HONEY_BOTTLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ConsumableItemBehavior.builder(Consumables.HONEY_BOTTLE)
                        .food(Foods.HONEY_BOTTLE)
                        .useAnimation(ItemUseAnimation.DRINK)
                        .remainder(this.items.getOrThrow(ItemIds.GLASS_BOTTLE))
                        .noConsumeParticles()
                        .consumeSound(this.soundEvents.getOrThrow(SoundEventIds.HONEY_BOTTLE_DRINK))
                        .build())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.CONSUME_ITEM, ActionEntry.of(
                        RemoveStatusEffectsAction.of(
                            LootContext.EntityTarget.THIS,
                            this.statusEffects.getOrThrow(MobEffectIds.POISON)
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
            this.registerable.register(ItemIds.STONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRANITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRANITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_GRANITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_GRANITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_GRANITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIORITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIORITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DIORITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DIORITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DIORITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANDESITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANDESITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_ANDESITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_ANDESITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_ANDESITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLED_DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CALCITE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CALCITE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CALCITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_TUFF, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_TUFF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_TUFF, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_TUFF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_TUFF)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_TUFF_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_TUFF_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_TUFF_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_TUFF_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_TUFF_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_TUFF_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_TUFF_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_TUFF_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_TUFF_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUFF_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUFF_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUFF_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_TUFF_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_TUFF_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_TUFF_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DRIPSTONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DRIPSTONE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DRIPSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRASS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRASS_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRASS_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIRT, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIRT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIRT)))
                    .build()
            ));
            this.registerable.register(ItemIds.COARSE_DIRT, create(
                ItemDisplay.Builder.forBlock(ItemIds.COARSE_DIRT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COARSE_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemIds.PODZOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.PODZOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PODZOL)))
                    .build()
            ));
            this.registerable.register(ItemIds.ROOTED_DIRT, create(
                ItemDisplay.Builder.forBlock(ItemIds.ROOTED_DIRT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ROOTED_DIRT)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUD, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUD)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_NYLIUM, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_NYLIUM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_NYLIUM, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_NYLIUM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_NYLIUM)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLESTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_PLANKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BEDROCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BEDROCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BEDROCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.SAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.SAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.SUSPICIOUS_SAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.SUSPICIOUS_SAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SUSPICIOUS_SAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.SUSPICIOUS_GRAVEL, create(
                ItemDisplay.Builder.forBlock(ItemIds.SUSPICIOUS_GRAVEL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SUSPICIOUS_GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAVEL, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAVEL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAVEL)))
                    .build()
            ));
            this.registerable.register(ItemIds.COAL_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COAL_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_COAL_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_COAL_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_COAL_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_IRON_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_IRON_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_IRON_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_COPPER_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_COPPER_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_COPPER_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GOLD_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_GOLD_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.REDSTONE_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.REDSTONE_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_REDSTONE_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_REDSTONE_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_REDSTONE_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.EMERALD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.EMERALD_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_EMERALD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_EMERALD_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_EMERALD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LAPIS_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LAPIS_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_LAPIS_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_LAPIS_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_LAPIS_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIAMOND_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_DIAMOND_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_DIAMOND_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_DIAMOND_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_GOLD_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_GOLD_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_GOLD_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_QUARTZ_ORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_QUARTZ_ORE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_QUARTZ_ORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_IRON_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.RAW_IRON_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RAW_IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.RAW_COPPER_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RAW_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_GOLD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.RAW_GOLD_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RAW_GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.HEAVY_CORE, create(
                ItemDisplay.Builder.forBlock(ItemIds.HEAVY_CORE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HEAVY_CORE)))
                    .build()
            ));
            this.registerable.register(ItemIds.AMETHYST_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.AMETHYST_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.AMETHYST_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.BUDDING_AMETHYST, create(
                ItemDisplay.Builder.forBlock(ItemIds.BUDDING_AMETHYST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BUDDING_AMETHYST)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.GOLD_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GOLD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIAMOND_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIAMOND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_CHISELED_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_CHISELED_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_CHISELED_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_CUT_COPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_CUT_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_CUT_COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_CUT_COPPER_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_CUT_COPPER_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_CUT_COPPER_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_CUT_COPPER_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_CUT_COPPER_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_CUT_COPPER_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUDDY_MANGROVE_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUDDY_MANGROVE_ROOTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUDDY_MANGROVE_ROOTS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_STEM, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_STEM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_STEM, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_STEM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_CRIMSON_STEM, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_CRIMSON_STEM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_CRIMSON_STEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_WARPED_STEM, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_WARPED_STEM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_WARPED_STEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_CRIMSON_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_CRIMSON_HYPHAE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_WARPED_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_WARPED_HYPHAE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_HYPHAE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_HYPHAE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_HYPHAE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_HYPHAE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SPONGE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPONGE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WET_SPONGE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WET_SPONGE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WET_SPONGE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.TINTED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TINTED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TINTED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.LAPIS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.LAPIS_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LAPIS_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBWEB, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBWEB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBWEB)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_STONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_STONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_STONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.PETRIFIED_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.PETRIFIED_OAK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PETRIFIED_OAK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLESTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLESTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUD_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUD_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUD_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.QUARTZ_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_RED_SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPUR_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPUR_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPUR_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_PRISMARINE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_PRISMARINE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_PRISMARINE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_QUARTZ, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_QUARTZ).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_QUARTZ)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_RED_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_STONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_STONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_STONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DECORATED_POT, create(
                ItemDisplay.Builder.forBlock(ItemIds.DECORATED_POT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DECORATED_POT)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_COBBLESTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.OBSIDIAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.OBSIDIAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHORUS_PLANT, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHORUS_PLANT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHORUS_PLANT)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHORUS_FLOWER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHORUS_FLOWER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHORUS_FLOWER)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPUR_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPUR_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPUR_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPUR_PILLAR, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPUR_PILLAR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPUR_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPUR_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPUR_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPUR_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SPAWNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPAWNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CREAKING_HEART, create(
                ItemDisplay.Builder.forBlock(ItemIds.CREAKING_HEART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CREAKING_HEART)))
                    .build()
            ));
            this.registerable.register(ItemIds.FARMLAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.FARMLAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FARMLAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.FURNACE, create(
                ItemDisplay.Builder.forBlock(ItemIds.FURNACE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLESTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLESTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SNOW, create(
                ItemDisplay.Builder.forBlock(ItemIds.SNOW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SNOW)))
                    .build()
            ));
            this.registerable.register(ItemIds.ICE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ICE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ICE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SNOW_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.SNOW_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SNOW_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.CLAY, create(
                ItemDisplay.Builder.forBlock(ItemIds.CLAY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CLAY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemIds.JACK_O_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.JACK_O_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JACK_O_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERRACK, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHERRACK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHERRACK)))
                    .build()
            ));
            this.registerable.register(ItemIds.SOUL_SAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.SOUL_SAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SOUL_SAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.SOUL_SOIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.SOUL_SOIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SOUL_SOIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BASALT, create(
                ItemDisplay.Builder.forBlock(ItemIds.BASALT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BASALT)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BASALT, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BASALT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_BASALT, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_BASALT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_BASALT)))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOWSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GLOWSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GLOWSTONE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.CHARGE_RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_STONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_STONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_STONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_COBBLESTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_COBBLESTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_COBBLESTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_MOSSY_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_MOSSY_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_CRACKED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_CRACKED_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_CHISELED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_CHISELED_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.INFESTED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.INFESTED_DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.INFESTED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRACKED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRACKED_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRACKED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.PACKED_MUD, create(
                ItemDisplay.Builder.forBlock(ItemIds.PACKED_MUD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PACKED_MUD)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUD_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUD_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUD_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRACKED_DEEPSLATE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRACKED_DEEPSLATE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRACKED_DEEPSLATE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_TILES, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_TILES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRACKED_DEEPSLATE_TILES, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRACKED_DEEPSLATE_TILES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRACKED_DEEPSLATE_TILES)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.REINFORCED_DEEPSLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.REINFORCED_DEEPSLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REINFORCED_DEEPSLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_BARS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_BARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_BARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_CHAIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_CHAIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_CHAIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUD_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUD_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUD_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MYCELIUM, create(
                ItemDisplay.Builder.forBlock(ItemIds.MYCELIUM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MYCELIUM)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRACKED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRACKED_NETHER_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRACKED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_NETHER_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_BRICK_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_BRICK_FENCE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCULK, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCULK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCULK)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCULK_VEIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCULK_VEIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCULK_VEIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCULK_CATALYST, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCULK_CATALYST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCULK_CATALYST)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCULK_SHRIEKER, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCULK_SHRIEKER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCULK_SHRIEKER)))
                    .build()
            ));
            this.registerable.register(ItemIds.ENCHANTING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ENCHANTING_TABLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ENCHANTING_TABLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_PORTAL_FRAME, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_PORTAL_FRAME).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_PORTAL_FRAME)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_STONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_STONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_STONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_STONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_STONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_STONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DRAGON_EGG, create(
                ItemDisplay.Builder.forBlock(ItemIds.DRAGON_EGG)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DRAGON_EGG)))
                    .build()
            ));
            this.registerable.register(ItemIds.SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SANDSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.ENDER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ENDER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.EMERALD_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.EMERALD_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EMERALD_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BEACON, create(
                ItemDisplay.Builder.forBlock(ItemIds.BEACON)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BEACON)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLESTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLESTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_COBBLESTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_COBBLESTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_COBBLESTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SANDSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SANDSTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_STONE_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRANITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRANITE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRANITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUD_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUD_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUD_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANDESITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANDESITE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANDESITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_NETHER_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_NETHER_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_NETHER_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.SANDSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.SANDSTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SANDSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_STONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_STONE_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_STONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIORITE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIORITE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIORITE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACKSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACKSTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLED_DEEPSLATE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLED_DEEPSLATE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DEEPSLATE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DEEPSLATE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DEEPSLATE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_TILE_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_TILE_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_TILE_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANVIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHIPPED_ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHIPPED_ANVIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHIPPED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DAMAGED_ANVIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DAMAGED_ANVIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DAMAGED_ANVIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_QUARTZ_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_QUARTZ_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.QUARTZ_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.QUARTZ_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.QUARTZ_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.QUARTZ_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ_PILLAR, create(
                ItemDisplay.Builder.forBlock(ItemIds.QUARTZ_PILLAR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.QUARTZ_PILLAR)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.QUARTZ_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BARRIER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BARRIER)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BARRIER)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT)))
                    .build()
            ));
            this.registerable.register(ItemIds.TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.PACKED_ICE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PACKED_ICE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PACKED_ICE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIRT_PATH, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIRT_PATH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIRT_PATH)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_PRISMARINE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_PRISMARINE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_PRISMARINE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PRISMARINE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PRISMARINE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_PRISMARINE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_PRISMARINE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_PRISMARINE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SEA_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.SEA_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SEA_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_RED_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CUT_RED_SANDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CUT_RED_SANDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CUT_RED_SANDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SANDSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGMA_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGMA_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGMA_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_NETHER_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_NETHER_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_NETHER_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BONE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRUCTURE_VOID, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRUCTURE_VOID)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRUCTURE_VOID)))
                    .build()
            ));
            this.registerable.register(ItemIds.TURTLE_EGG, create(
                ItemDisplay.Builder.forBlock(ItemIds.TURTLE_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TURTLE_EGG)))
                    .build()
            ));
            this.registerable.register(ItemIds.SNIFFER_EGG, create(
                ItemDisplay.Builder.forBlock(ItemIds.SNIFFER_EGG)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SNIFFER_EGG)))
                    .build()
            ));
            this.registerable.register(ItemIds.DRIED_GHAST, create(
                ItemDisplay.Builder.forBlock(ItemIds.DRIED_GHAST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DRIED_GHAST)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_TUBE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_TUBE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BRAIN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BRAIN_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BUBBLE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BUBBLE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_FIRE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_FIRE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_HORN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_HORN_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUBE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUBE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUBE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRAIN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRAIN_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRAIN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.BUBBLE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BUBBLE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BUBBLE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.FIRE_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.FIRE_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FIRE_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.HORN_CORAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.HORN_CORAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HORN_CORAL_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.TUBE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUBE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BRAIN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRAIN_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BUBBLE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BUBBLE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.FIRE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.FIRE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.HORN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.HORN_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BRAIN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BRAIN_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_BRAIN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BUBBLE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BUBBLE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_BUBBLE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_FIRE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_FIRE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_FIRE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_HORN_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_HORN_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_HORN_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_TUBE_CORAL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_TUBE_CORAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_TUBE_CORAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_ICE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_ICE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_ICE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CONDUIT, create(
                ItemDisplay.Builder.forBlock(ItemIds.CONDUIT)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CONDUIT)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_GRANITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_GRANITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_RED_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_RED_SANDSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_RED_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_STONE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DIORITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DIORITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_COBBLESTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_COBBLESTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_COBBLESTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_STONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_STONE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_STONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_SANDSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_SANDSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_SANDSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_QUARTZ_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_QUARTZ_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_QUARTZ_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRANITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRANITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRANITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANDESITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANDESITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_NETHER_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_NETHER_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_NETHER_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_ANDESITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_ANDESITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_ANDESITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIORITE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIORITE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIORITE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLED_DEEPSLATE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLED_DEEPSLATE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DEEPSLATE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DEEPSLATE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DEEPSLATE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_TILE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_TILE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_TILE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_GRANITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_GRANITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_RED_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_RED_SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_RED_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_STONE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DIORITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DIORITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSSY_COBBLESTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSSY_COBBLESTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSSY_COBBLESTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.END_STONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.END_STONE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.END_STONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_SANDSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_SANDSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_SANDSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOOTH_QUARTZ_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOOTH_QUARTZ_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOOTH_QUARTZ_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRANITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRANITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRANITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANDESITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANDESITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_NETHER_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_NETHER_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_NETHER_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_ANDESITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_ANDESITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_ANDESITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIORITE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.DIORITE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DIORITE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.COBBLED_DEEPSLATE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.COBBLED_DEEPSLATE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COBBLED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_DEEPSLATE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_DEEPSLATE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_DEEPSLATE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.DEEPSLATE_TILE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEEPSLATE_TILE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEEPSLATE_TILE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.REDSTONE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.REDSTONE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REDSTONE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.REPEATER, create(
                ItemDisplay.Builder.forBlock(ItemIds.REPEATER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REPEATER)))
                    .build()
            ));
            this.registerable.register(ItemIds.COMPARATOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.COMPARATOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COMPARATOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.PISTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.PISTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PISTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.STICKY_PISTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.STICKY_PISTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STICKY_PISTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.SLIME_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.SLIME_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SLIME_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.HONEY_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.HONEY_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HONEY_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.OBSERVER, create(
                ItemDisplay.Builder.forBlock(ItemIds.OBSERVER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OBSERVER)))
                    .build()
            ));
            this.registerable.register(ItemIds.HOPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.HOPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HOPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.DISPENSER, create(
                ItemDisplay.Builder.forBlock(ItemIds.DISPENSER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DISPENSER)))
                    .build()
            ));
            this.registerable.register(ItemIds.DROPPER, create(
                ItemDisplay.Builder.forBlock(ItemIds.DROPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DROPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.TARGET, create(
                ItemDisplay.Builder.forBlock(ItemIds.TARGET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TARGET)))
                    .build()
            ));
            this.registerable.register(ItemIds.LEVER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LEVER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LEVER)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_LIGHTNING_ROD, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_LIGHTNING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_LIGHTNING_ROD)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCULK_SENSOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCULK_SENSOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.CALIBRATED_SCULK_SENSOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.CALIBRATED_SCULK_SENSOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CALIBRATED_SCULK_SENSOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.TRIPWIRE_HOOK, create(
                ItemDisplay.Builder.forBlock(ItemIds.TRIPWIRE_HOOK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TRIPWIRE_HOOK)))
                    .build()
            ));
            this.registerable.register(ItemIds.TNT, create(
                ItemDisplay.Builder.forBlock(ItemIds.TNT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TNT)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_TNT)))
                    .build()
            ));
            this.registerable.register(ItemIds.REDSTONE_LAMP, create(
                ItemDisplay.Builder.forBlock(ItemIds.REDSTONE_LAMP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REDSTONE_LAMP)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_BUTTON)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONE_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.HEAVY_WEIGHTED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.HEAVY_WEIGHTED_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HEAVY_WEIGHTED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_PRESSURE_PLATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_DOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.IRON_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.IRON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_TRAPDOOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_FENCE_GATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POWERED_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.POWERED_RAIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POWERED_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.DETECTOR_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.DETECTOR_RAIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DETECTOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.RAIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.RAIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RAIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.ACTIVATOR_RAIL, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACTIVATOR_RAIL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACTIVATOR_RAIL)))
                    .build()
            ));
            this.registerable.register(ItemIds.BREWING_STAND, create(
                ItemDisplay.Builder.forBlock(ItemIds.BREWING_STAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BREWING_STAND)))
                    .build()
            ));
            this.registerable.register(ItemIds.CAULDRON, create(
                ItemDisplay.Builder.forBlock(ItemIds.CAULDRON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CAULDRON)))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOWER_POT, create(
                ItemDisplay.Builder.forBlock(ItemIds.FLOWER_POT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FLOWER_POT)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMOKER, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMOKER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMOKER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLAST_FURNACE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLAST_FURNACE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLAST_FURNACE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRINDSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRINDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRINDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONECUTTER, create(
                ItemDisplay.Builder.forBlock(ItemIds.STONECUTTER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STONECUTTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BELL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BELL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BELL)))
                    .build()
            ));
            this.registerable.register(ItemIds.LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.SOUL_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.SOUL_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SOUL_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_LANTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_LANTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_LANTERN)))
                    .build()
            ));
            this.registerable.register(ItemIds.CAMPFIRE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CAMPFIRE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SOUL_CAMPFIRE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SOUL_CAMPFIRE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SOUL_CAMPFIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BEE_NEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.BEE_NEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BEE_NEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.BEEHIVE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BEEHIVE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BEEHIVE)))
                    .build()
            ));
            this.registerable.register(ItemIds.HONEYCOMB_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.HONEYCOMB_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HONEYCOMB_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.LODESTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LODESTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LODESTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRYING_OBSIDIAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRYING_OBSIDIAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRYING_OBSIDIAN)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACKSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACKSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACKSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACKSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACKSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GILDED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GILDED_BLACKSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GILDED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_POLISHED_BLACKSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_POLISHED_BLACKSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_POLISHED_BLACKSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.POLISHED_BLACKSTONE_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.POLISHED_BLACKSTONE_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POLISHED_BLACKSTONE_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRACKED_POLISHED_BLACKSTONE_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRACKED_POLISHED_BLACKSTONE_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRACKED_POLISHED_BLACKSTONE_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESPAWN_ANCHOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESPAWN_ANCHOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SMALL_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMALL_AMETHYST_BUD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMALL_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemIds.MEDIUM_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemIds.MEDIUM_AMETHYST_BUD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MEDIUM_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemIds.LARGE_AMETHYST_BUD, create(
                ItemDisplay.Builder.forBlock(ItemIds.LARGE_AMETHYST_BUD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LARGE_AMETHYST_BUD)))
                    .build()
            ));
            this.registerable.register(ItemIds.AMETHYST_CLUSTER, create(
                ItemDisplay.Builder.forBlock(ItemIds.AMETHYST_CLUSTER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.AMETHYST_CLUSTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.POINTED_DRIPSTONE, create(
                ItemDisplay.Builder.forBlock(ItemIds.POINTED_DRIPSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POINTED_DRIPSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.OCHRE_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemIds.OCHRE_FROGLIGHT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OCHRE_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemIds.VERDANT_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemIds.VERDANT_FROGLIGHT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.VERDANT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemIds.PEARLESCENT_FROGLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemIds.PEARLESCENT_FROGLIGHT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PEARLESCENT_FROGLIGHT)))
                    .build()
            ));
            this.registerable.register(ItemIds.FROGSPAWN, create(
                ItemDisplay.Builder.forBlock(ItemIds.FROGSPAWN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FROGSPAWN), BlockItemBehavior.Pass.FLUID))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_GRATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_GRATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_GRATE)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_BULB, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_BULB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_BULB)))
                    .build()
            ));
            this.registerable.register(ItemIds.TRIAL_SPAWNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.TRIAL_SPAWNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TRIAL_SPAWNER)))
                    .build()
            ));
            this.registerable.register(ItemIds.VAULT, create(
                ItemDisplay.Builder.forBlock(ItemIds.VAULT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.VAULT)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRAFTER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRAFTER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRAFTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESIN_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESIN_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BRICK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESIN_BRICK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_BRICK_STAIRS)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BRICK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESIN_BRICK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_BRICK_SLAB)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BRICK_WALL, create(
                ItemDisplay.Builder.forBlock(ItemIds.RESIN_BRICK_WALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_BRICK_WALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_RESIN_BRICKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_RESIN_BRICKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_RESIN_BRICKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.TEST_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.TEST_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TEST_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.TEST_INSTANCE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.TEST_INSTANCE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TEST_INSTANCE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPOSED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.EXPOSED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.EXPOSED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WEATHERED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEATHERED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEATHERED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.OXIDIZED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXIDIZED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXIDIZED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_EXPOSED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_EXPOSED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_EXPOSED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_WEATHERED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_WEATHERED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_WEATHERED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WAXED_OXIDIZED_COPPER_GOLEM_STATUE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WAXED_OXIDIZED_COPPER_GOLEM_STATUE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WAXED_OXIDIZED_COPPER_GOLEM_STATUE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_SHELF)))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_SHELF)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_DANDELION, create(
                ItemDisplay.Builder.forBlock(ItemIds.GOLDEN_DANDELION).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GOLDEN_DANDELION)))
                    .build()
            ));
        }

        private void bootstrapAttachedToSideBlocks() {
            this.registerable.register(ItemIds.TORCH, create(
                ItemDisplay.Builder.forBlock(ItemIds.TORCH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.TORCH), this.blocks.getOrThrow(BlockIds.WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.SOUL_TORCH, create(
                ItemDisplay.Builder.forBlock(ItemIds.SOUL_TORCH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.SOUL_TORCH), this.blocks.getOrThrow(BlockIds.SOUL_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_TORCH, create(
                ItemDisplay.Builder.forBlock(ItemIds.COPPER_TORCH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.COPPER_TORCH), this.blocks.getOrThrow(BlockIds.COPPER_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.TUBE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.TUBE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.BRAIN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BRAIN_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockIds.BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.BUBBLE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BUBBLE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.FIRE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.FIRE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.HORN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.HORN_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.HORN_CORAL_FAN), this.blocks.getOrThrow(BlockIds.HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_TUBE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_TUBE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DEAD_TUBE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.DEAD_TUBE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BRAIN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BRAIN_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DEAD_BRAIN_CORAL_FAN), this.blocks.getOrThrow(BlockIds.DEAD_BRAIN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BUBBLE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BUBBLE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DEAD_BUBBLE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.DEAD_BUBBLE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_FIRE_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_FIRE_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DEAD_FIRE_CORAL_FAN), this.blocks.getOrThrow(BlockIds.DEAD_FIRE_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_HORN_CORAL_FAN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_HORN_CORAL_FAN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DEAD_HORN_CORAL_FAN), this.blocks.getOrThrow(BlockIds.DEAD_HORN_CORAL_WALL_FAN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.REDSTONE_TORCH, create(
                ItemDisplay.Builder.forBlock(ItemIds.REDSTONE_TORCH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.REDSTONE_TORCH), this.blocks.getOrThrow(BlockIds.REDSTONE_WALL_TORCH), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.CRIMSON_SIGN), this.blocks.getOrThrow(BlockIds.CRIMSON_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.WARPED_SIGN), this.blocks.getOrThrow(BlockIds.WARPED_WALL_SIGN), Direction.DOWN))
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.CRIMSON_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.CRIMSON_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.WARPED_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.WARPED_WALL_HANGING_SIGN), Direction.UP))
                    .build()
            ));
        }

        private void bootstrapColoredBlocks() {
            this.bootstrapShulkerBoxes();
            this.registerable.register(ItemIds.WHITE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_STAINED_GLASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_STAINED_GLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_STAINED_GLASS)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_STAINED_GLASS_PANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_STAINED_GLASS_PANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_STAINED_GLASS_PANE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_GLAZED_TERRACOTTA, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_GLAZED_TERRACOTTA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_GLAZED_TERRACOTTA)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_CONCRETE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_CONCRETE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_CONCRETE)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_CONCRETE_POWDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_CONCRETE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_CONCRETE_POWDER)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_BED, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_BED).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_BED)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_CANDLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_CANDLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_CANDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_CANDLE)))
                    .build()
            ));
        }

        private void bootstrapShulkerBoxes() {
            this.registerable.register(ItemIds.SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_SHULKER_BOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_SHULKER_BOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_SHULKER_BOX)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                    .build()
            ));
        }

        private void bootstrapItemNameBlocks() {
            this.registerable.register(ItemIds.STRING, create(
                ItemDisplay.Builder.forItem(ItemIds.STRING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TRIPWIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_CLUMP, create(
                ItemDisplay.Builder.forItem(ItemIds.RESIN_CLUMP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RESIN_CLUMP)))
                    .build()
            ));
        }

        private void bootstrapOperatorOnlyBlocks() {
            this.registerable.register(ItemIds.COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockIds.COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.REPEATING_COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.REPEATING_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockIds.REPEATING_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHAIN_COMMAND_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHAIN_COMMAND_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockIds.CHAIN_COMMAND_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.STRUCTURE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRUCTURE_BLOCK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockIds.STRUCTURE_BLOCK)))
                    .build()
            ));
            this.registerable.register(ItemIds.JIGSAW, create(
                ItemDisplay.Builder.forBlock(ItemIds.JIGSAW)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockIds.JIGSAW)))
                    .build()
            ));
        }

        private void bootstrapToolsAndWeapons() {
            this.registerable.register(ItemIds.WOODEN_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.WOODEN_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.WOODEN_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.WOOD, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.WOODEN_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.WOOD, 7.0d, 0.2d, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.WOODEN_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.WOOD, 1.0d, 0.25d, this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.WOODEN_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.WOODEN_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.WOOD,
                        this.damageTypes,
                        0.65f,
                        0.7f,
                        0.75f,
                        5.0f,
                        14.0f,
                        10.0f,
                        5.1f,
                        15.0f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .with(FuelItemBehavior.of(FuelTimes.TOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.STONE,
                        this.damageTypes,
                        0.75f,
                        0.82f,
                        0.7f,
                        4.5f,
                        13.0f,
                        9.0f,
                        5.1f,
                        13.75f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.COPPER,
                        this.damageTypes,
                        0.85f,
                        0.82f,
                        0.65f,
                        4.0f,
                        12.0f,
                        8.25f,
                        5.1f,
                        12.5f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.IRON,
                        this.damageTypes,
                        0.95f,
                        0.95f,
                        0.6f,
                        2.5f,
                        11.0f,
                        6.75f,
                        5.1f,
                        11.25f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.GOLD,
                        this.damageTypes,
                        0.95f,
                        0.7f,
                        0.7f,
                        3.5f,
                        13.0f,
                        8.5f,
                        5.1f,
                        13.75f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.DIAMOND,
                        this.damageTypes,
                        1.05f,
                        1.075f,
                        0.5f,
                        3.0f,
                        10.0f,
                        6.5f,
                        5.1f,
                        10.0f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_SPEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_SPEAR).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.spear(
                        ToolMaterial.NETHERITE,
                        this.damageTypes,
                        1.15f,
                        1.2f,
                        0.4f,
                        2.5f,
                        9.0f,
                        5.5f,
                        5.1f,
                        8.75f,
                        4.6f,
                        this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS),
                        this.soundEvents
                    ))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.STONE, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.STONE, 8.0d, 0.2d, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.STONE_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.STONE_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.STONE, 2.0d, 0.5d, this.items.getOrThrow(ItemTags.STONE_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.GOLD, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.GOLD, 7.0d, 0.25d, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.GOLD, 1.0d, 0.25d, this.items.getOrThrow(ItemTags.GOLD_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.COPPER, this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.COPPER, this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.COPPER, this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.COPPER, 8.0d, 0.2d, this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.COPPER, 2.0d, 0.5d, this.items.getOrThrow(ItemTags.COPPER_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.IRON, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.IRON, 7.0d, 0.225d, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.IRON, 3.0d, 0.75d, this.items.getOrThrow(ItemTags.IRON_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.DIAMOND, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.DIAMOND, 6.0d, 0.25d, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.DIAMOND, 4.0d, 1.0d, this.items.getOrThrow(ItemTags.DIAMOND_TOOL_MATERIALS)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_SWORD, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_SWORD).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.sword(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_SHOVEL, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_SHOVEL).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.shovel(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_PICKAXE, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_PICKAXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.pickaxe(this.blocks, ToolMaterial.NETHERITE, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_AXE, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_AXE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.axe(this.blocks, ToolMaterial.NETHERITE, 6.0d, 0.25d, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_HOE, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_HOE).build(),
                ItemBehaviorSet.builder()
                    .with(DamageableItemBehavior.hoe(this.blocks, ToolMaterial.NETHERITE, 5.0d, 1.0d, this.items.getOrThrow(ItemTags.NETHERITE_TOOL_MATERIALS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.FISHING_ROD, create(
                ItemDisplay.Builder.forItem(ItemIds.FISHING_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(CastableItemBehavior.INSTANCE)
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SHEARS, create(
                ItemDisplay.Builder.forItem(ItemIds.SHEARS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(238))
                    .with(ToolItemBehavior.builder(1)
                        .rule(Tool.Rule.minesAndDrops(HolderSet.direct(this.blocks.getOrThrow(BlockIds.COBWEB)), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.LEAVES), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.WOOL), 5.0f))
                        .rule(Tool.Rule.overrideSpeed(HolderSet.direct(this.blocks.getOrThrow(BlockIds.VINE), this.blocks.getOrThrow(BlockIds.GLOW_LICHEN)), 2.0f))
                        .build())
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHEAR)))
                    .build()
            ));
            this.registerable.register(ItemIds.BOW, create(
                ItemDisplay.Builder.forItem(ItemIds.BOW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(384))
                    .with(ShooterItemBehavior.of(
                        ItemUseAnimation.BOW,
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        BowItem.DEFAULT_RANGE,
                        DirectShooterMethod.of()
                    ))
                    .with(EnchantableItemBehavior.of(1))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CROSSBOW, create(
                ItemDisplay.Builder.forItem(ItemIds.CROSSBOW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(465))
                    .with(ShooterItemBehavior.of(
                        ItemUseAnimation.CROSSBOW,
                        this.items.getOrThrow(ItematicItemTags.CROSSBOW_AMMUNITION),
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        CrossbowItem.DEFAULT_RANGE,
                        ChargeableShooterMethod.of(
                            CrossbowItemAccessor.defaultChargingSounds(),
                            ChargeableShooterMethod.ChargedPowerRules.Rule.of(
                                HolderSet.direct(this.items.getOrThrow(ItemIds.FIREWORK_ROCKET)),
                                CrossbowItemAccessor.fireworkRocketPower()
                            )
                        ),
                        ItemDamageRules.Rule.of(
                            HolderSet.direct(this.items.getOrThrow(ItemIds.FIREWORK_ROCKET)),
                            3
                        )
                    ))
                    .with(EnchantableItemBehavior.of(1))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.TRIDENT, create(
                ItemDisplay.Builder.forItem(ItemIds.TRIDENT)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.ofPreserved(250))
                    .with(ToolItemBehavior.builder(2)
                        .preventCreativeDestruction()
                        .build())
                    .with(WeaponItemBehavior.builder(1, TridentItem.BASE_DAMAGE, 0.275d)
                        .build())
                    .with(ThrowableItemBehavior.trident(TridentItem.PROJECTILE_SHOOT_POWER, 0.0f, TridentItem.THROW_THRESHOLD_TIME))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.TRIDENT)))
                    .with(EnchantableItemBehavior.of(1))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.STOPPED_USING, ActionEntry.of(
                        AllOfCondition.allOf(
                            LootItemEntityPropertyCondition.hasProperties(
                                LootContext.EntityTarget.THIS,
                                EntityPredicate.Builder.entity()
                                    .itematic$usedItemAtLeast(TridentItem.THROW_THRESHOLD_TIME)
                                    .itematic$inWaterOrRain(true)
                            ),
                            MatchTool.toolMatches(ItemPredicate.Builder.item()
                                .withComponents(DataComponentMatchers.Builder.components()
                                    .partial(
                                        DataComponentPredicates.ENCHANTMENTS,
                                        EnchantmentsPredicate.enchantments(List.of(
                                            new EnchantmentPredicate(this.enchantments.getOrThrow(Enchantments.RIPTIDE), MinMaxBounds.Ints.ANY)
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
            this.registerable.register(ItemIds.CARROT_ON_A_STICK, create(
                ItemDisplay.Builder.forItem(ItemIds.CARROT_ON_A_STICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(25))
                    .with(SteeringItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.PIG), 7))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.BREAK_ITEM, ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemIds.FISHING_ROD))))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_FUNGUS_ON_A_STICK, create(
                ItemDisplay.Builder.forItem(ItemIds.WARPED_FUNGUS_ON_A_STICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(100))
                    .with(SteeringItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.STRIDER), 1))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.BREAK_ITEM, ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemIds.FISHING_ROD))))
                    .build()
            ));
            this.registerable.register(ItemIds.FLINT_AND_STEEL, create(
                ItemDisplay.Builder.forItem(ItemIds.FLINT_AND_STEEL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(64))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DamageItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.FLINT_AND_STEEL_USE), SoundSource.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.BRUSH, create(
                ItemDisplay.Builder.forItem(ItemIds.BRUSH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(UseableItemBehavior.builder()
                        .useFor(BrushItemAccessor.useDuration())
                        .animation(ItemUseAnimation.BRUSH)
                        .passes(UseableItemBehavior.Pass.BLOCK)
                        .build())
                    .with(BrushItemBehavior.INSTANCE)
                    .with(DamageableItemBehavior.of(64))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.BRUSH)))
                    .build()
            ));
            this.registerable.register(ItemIds.MACE, create(
                ItemDisplay.Builder.forItem(ItemIds.MACE)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(500))
                    .with(ToolItemBehavior.builder(2).build())
                    .with(WeaponItemBehavior.builder(1, 5.0d, 0.15d)
                        .type(MeleeWeaponComponents.SMASHING, SmashingMeleeWeapon.of(SmashingWeapon.of(
                            SmashingWeapon.HitSounds.of(
                                this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_AIR),
                                this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_GROUND),
                                this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_GROUND_HEAVY)
                            ),
                            MaceItem.SMASH_ATTACK_FALL_THRESHOLD,
                            MaceItemAccessor.heavySmashAttackFallDistance(),
                            MaceItemAccessor.knockbackPower()
                        )))
                        .build())
                    .with(EnchantableItemBehavior.of(15))
                    .with(RepairableItemBehavior.of(HolderSet.direct(
                        this.items.getOrThrow(ItemIds.BREEZE_ROD)
                    )))
                    .build()
            ));
        }

        private void bootstrapEntities() {
            this.bootstrapSpawnEggs();
            this.registerable.register(ItemIds.MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.MINECART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.CHEST_MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.CHEST_MINECART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.CHEST_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.FURNACE_MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.FURNACE_MINECART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.FURNACE_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.TNT_MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.TNT_MINECART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.TNT_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.HOPPER_MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.HOPPER_MINECART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.HOPPER_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.COMMAND_BLOCK_MINECART, create(
                ItemDisplay.Builder.forItem(ItemIds.COMMAND_BLOCK_MINECART)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.minecart(
                        this.entityTypes.getOrThrow(EntityTypeIds.COMMAND_BLOCK_MINECART),
                        this.blocks,
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.OAK_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.OAK_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.SPRUCE_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.SPRUCE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.SPRUCE_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.SPRUCE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.BIRCH_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.BIRCH_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.BIRCH_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.BIRCH_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.JUNGLE_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.JUNGLE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.JUNGLE_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.JUNGLE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.ACACIA_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.ACACIA_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.ACACIA_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.ACACIA_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.CHERRY_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.CHERRY_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.CHERRY_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.CHERRY_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.DARK_OAK_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.DARK_OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.DARK_OAK_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.DARK_OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.PALE_OAK_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.PALE_OAK_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.PALE_OAK_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.PALE_OAK_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.MANGROVE_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.MANGROVE_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_CHEST_BOAT, create(
                ItemDisplay.Builder.forItem(ItemIds.MANGROVE_CHEST_BOAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.MANGROVE_CHEST_BOAT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_RAFT, create(
                ItemDisplay.Builder.forItem(ItemIds.BAMBOO_RAFT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.BAMBOO_RAFT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_CHEST_RAFT, create(
                ItemDisplay.Builder.forItem(ItemIds.BAMBOO_CHEST_RAFT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EntityItemBehavior.ofDispensing(this.entityTypes.getOrThrow(EntityTypeIds.BAMBOO_CHEST_RAFT), this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.BOAT))
                    .build()
            ));
            this.registerable.register(ItemIds.PAINTING, create(
                ItemDisplay.Builder.forItem(ItemIds.PAINTING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.PAINTING))
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
            this.registerable.register(ItemIds.ITEM_FRAME, create(
                ItemDisplay.Builder.forItem(ItemIds.ITEM_FRAME).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.ITEM_FRAME))
                            .allowItemData()
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOW_ITEM_FRAME, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOW_ITEM_FRAME).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.GLOW_ITEM_FRAME))
                            .allowItemData()
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.ARMOR_STAND, create(
                ItemDisplay.Builder.forItem(ItemIds.ARMOR_STAND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(EntityItemBehavior.ofDispensing(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.ARMOR_STAND))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                SideCheckPredicate.builder(Direction.DOWN)
                            )
                            .spawnRule(FitsInVolumeEntitySpawnRule.entityDimensions())
                            .spawnRule(AlignYawEntitySpawnRule.of(8))
                            .spawnSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_STAND_PLACE))
                            .build(),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.END_CRYSTAL, create(
                ItemDisplay.Builder.forItem(ItemIds.END_CRYSTAL)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.END_CRYSTAL))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                InvertedLootItemCondition.invert(
                                    LocationCheckPredicates.builder(
                                        PositionTarget.INTERACTED,
                                        LocationPredicate.Builder.location()
                                            .setBlock(BlockPredicate.Builder.block()
                                                .of(this.blocks, ItematicBlockTags.END_CRYSTAL_SPAWNABLE_ON)),
                                        new BlockPos(0, -1, 0)
                                    )
                                )
                            )
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                InvertedLootItemCondition.invert(
                                    LocationCheckPredicates.builder(
                                        PositionTarget.INTERACTED,
                                        LocationPredicate.Builder.location()
                                            .setBlock(BlockPredicate.Builder.block()
                                                .of(this.blocks, BlockTags.AIR))
                                    )
                                )
                            )
                            .spawnRule(
                                FitsInVolumeEntitySpawnRule.of(
                                    false,
                                    true,
                                    new Vec3(1.0d, 2.0d, 1.0d)
                                )
                            )
                            .build()
                    ))
                    .build()
            ));
        }

        private void bootstrapSpawnEggs() {
            this.registerable.register(ItemIds.ARMADILLO_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ARMADILLO_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ARMADILLO), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ALLAY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ALLAY_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ALLAY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.AXOLOTL_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.AXOLOTL_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.AXOLOTL), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BAT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.BAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BEE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BEE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.BEE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BLAZE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BLAZE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.BLAZE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BOGGED_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BOGGED_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.BOGGED), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BREEZE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BREEZE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.BREEZE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CAT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CAMEL_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CAMEL_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CAMEL), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CAMEL_HUSK_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CAMEL_HUSK_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CAMEL_HUSK), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CAVE_SPIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CAVE_SPIDER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CAVE_SPIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CHICKEN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CHICKEN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CHICKEN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.COD_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.COD_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.COD), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_GOLEM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_GOLEM_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.COPPER_GOLEM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.COW_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.COW_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.COW), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CREAKING_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CREAKING_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CREAKING), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CREEPER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.CREEPER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.CREEPER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.DOLPHIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.DOLPHIN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.DOLPHIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.DONKEY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.DONKEY_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.DONKEY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.DROWNED_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.DROWNED_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.DROWNED), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ELDER_GUARDIAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ELDER_GUARDIAN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ELDER_GUARDIAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDER_DRAGON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ENDER_DRAGON_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ENDER_DRAGON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDERMAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ENDERMAN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ENDERMAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDERMITE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ENDERMITE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ENDERMITE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.EVOKER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.EVOKER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.EVOKER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.FOX_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.FOX_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.FOX), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.FROG_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.FROG_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.FROG), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GHAST_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.GHAST_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.GHAST), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.HAPPY_GHAST_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.HAPPY_GHAST_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.HAPPY_GHAST), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOW_SQUID_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOW_SQUID_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.GLOW_SQUID), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GOAT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.GOAT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.GOAT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GUARDIAN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.GUARDIAN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.GUARDIAN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.HOGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.HOGLIN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.HOGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.HORSE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.HUSK_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.HUSK_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.HUSK), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_GOLEM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_GOLEM_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.IRON_GOLEM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.LLAMA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.LLAMA_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.LLAMA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGMA_CUBE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.MAGMA_CUBE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.MAGMA_CUBE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.MOOSHROOM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.MOOSHROOM_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.MOOSHROOM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.MULE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.MULE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.MULE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.NAUTILUS_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.NAUTILUS_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.NAUTILUS), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.OCELOT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.OCELOT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.OCELOT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PANDA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PANDA_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PANDA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PARCHED_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PARCHED_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PARCHED), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PARROT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PARROT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PARROT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PHANTOM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PHANTOM_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PHANTOM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PIG_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PIG_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PIG), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PIGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PIGLIN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PIGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PIGLIN_BRUTE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PIGLIN_BRUTE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PIGLIN_BRUTE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PILLAGER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.POLAR_BEAR_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.POLAR_BEAR_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.POLAR_BEAR), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PUFFERFISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.PUFFERFISH_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.PUFFERFISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.RABBIT_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.RABBIT_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.RABBIT), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.RAVAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.RAVAGER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.RAVAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SALMON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SALMON_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SALMON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SHEEP_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SHEEP_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SHEEP), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SHULKER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SHULKER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SHULKER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SILVERFISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SILVERFISH_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SILVERFISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SKELETON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SKELETON_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SKELETON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SKELETON_HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SKELETON_HORSE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SKELETON_HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SLIME_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SLIME_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SLIME), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SNIFFER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SNIFFER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SNIFFER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SNOW_GOLEM_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SNOW_GOLEM_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SNOW_GOLEM), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SPIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SPIDER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SPIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.SQUID_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SQUID_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SQUID), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.STRAY_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.STRAY_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.STRAY), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIDER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.STRIDER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.STRIDER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.TADPOLE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.TADPOLE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.TADPOLE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.TRADER_LLAMA_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.TRADER_LLAMA_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.TRADER_LLAMA), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.TROPICAL_FISH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.TROPICAL_FISH_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.TROPICAL_FISH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.TURTLE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.TURTLE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.TURTLE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.VEX_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.VEX_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.VEX), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.VILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.VILLAGER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.VILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.VINDICATOR_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.VINDICATOR_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.VINDICATOR), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WANDERING_TRADER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WANDERING_TRADER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WANDERING_TRADER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WARDEN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WARDEN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WARDEN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WITCH_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WITCH_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WITCH), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WITHER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WITHER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WITHER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WITHER_SKELETON_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WITHER_SKELETON_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WITHER_SKELETON), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.WOLF_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.WOLF_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.WOLF), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOGLIN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOGLIN), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOMBIE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOMBIE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIE_HORSE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOMBIE_HORSE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOMBIE_HORSE), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIE_NAUTILUS_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOMBIE_NAUTILUS_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOMBIE_NAUTILUS), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOMBIE_VILLAGER), this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.ZOMBIFIED_PIGLIN), this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapCompostables() {
            this.registerable.register(ItemIds.OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.AZALEA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.AZALEA_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.AZALEA_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_OAK_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_SPRUCE_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_BIRCH_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_JUNGLE_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_ACACIA_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CHERRY_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_DARK_OAK_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_SAPLING, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_SAPLING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_SAPLING)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_PALE_OAK_SAPLING)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_PROPAGULE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_PROPAGULE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_PROPAGULE)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_MANGROVE_PROPAGULE)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.SHORT_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SHORT_GRASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SHORT_GRASS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.KELP, create(
                ItemDisplay.Builder.forBlock(ItemIds.KELP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.KELP)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.MOSS_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSS_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSS_CARPET)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_MOSS_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_MOSS_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_MOSS_CARPET)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_PETALS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_PETALS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_PETALS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.HANGING_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.HANGING_ROOTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HANGING_ROOTS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.SMALL_DRIPLEAF, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMALL_DRIPLEAF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMALL_DRIPLEAF)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.WHEAT_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemIds.WHEAT_SEEDS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHEAT)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PUMPKIN_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemIds.PUMPKIN_SEEDS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PUMPKIN_STEM)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.MELON_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemIds.MELON_SEEDS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MELON_STEM)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.TORCHFLOWER_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemIds.TORCHFLOWER_SEEDS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TORCHFLOWER_CROP)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PITCHER_POD, create(
                ItemDisplay.Builder.forItem(ItemIds.PITCHER_POD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PITCHER_CROP)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.BEETROOT_SEEDS, create(
                ItemDisplay.Builder.forItem(ItemIds.BEETROOT_SEEDS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BEETROOTS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_ROOTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_ROOTS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SEAGRASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SEAGRASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SEAGRASS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_HANGING_MOSS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_HANGING_MOSS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_HANGING_MOSS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.WILDFLOWERS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WILDFLOWERS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WILDFLOWERS)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.BUSH, create(
                ItemDisplay.Builder.forBlock(ItemIds.BUSH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BUSH)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.FIREFLY_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemIds.FIREFLY_BUSH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FIREFLY_BUSH)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.CACTUS_FLOWER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CACTUS_FLOWER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CACTUS_FLOWER)))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOWERING_AZALEA_LEAVES, create(
                ItemDisplay.Builder.forBlock(ItemIds.FLOWERING_AZALEA_LEAVES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FLOWERING_AZALEA_LEAVES)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_SPROUTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_SPROUTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_SPROUTS)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.WEEPING_VINES, create(
                ItemDisplay.Builder.forBlock(ItemIds.WEEPING_VINES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WEEPING_VINES)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.TWISTING_VINES, create(
                ItemDisplay.Builder.forBlock(ItemIds.TWISTING_VINES).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TWISTING_VINES)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.SUGAR_CANE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SUGAR_CANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SUGAR_CANE)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.VINE, create(
                ItemDisplay.Builder.forBlock(ItemIds.VINE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.VINE)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOW_LICHEN, create(
                ItemDisplay.Builder.forBlock(ItemIds.GLOW_LICHEN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GLOW_LICHEN)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.TALL_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TALL_GRASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TALL_GRASS)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(ItemIds.CACTUS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CACTUS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CACTUS)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CACTUS)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.DRIED_KELP_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.DRIED_KELP_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DRIED_KELP_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .with(FuelItemBehavior.of(FuelTimes.DRIED_KELP_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.FERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.FERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FERN)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_FERN)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.LILY_PAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.LILY_PAD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LILY_PAD), BlockItemBehavior.Pass.FLUID))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_WART, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHER_WART).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_WART)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.COCOA_BEANS, create(
                ItemDisplay.Builder.forItem(ItemIds.COCOA_BEANS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COCOA)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.BIG_DRIPLEAF, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIG_DRIPLEAF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIG_DRIPLEAF)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.PUMPKIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.PUMPKIN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PUMPKIN)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.CARVED_PUMPKIN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CARVED_PUMPKIN).build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CARVED_PUMPKIN)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.HEAD)
                        .setSwappable(false)
                        .setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur"))
                        .build()))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_CARVED_PUMPKIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.MELON, create(
                ItemDisplay.Builder.forBlock(ItemIds.MELON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MELON)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.SEA_PICKLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SEA_PICKLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SEA_PICKLE)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.WHEAT, create(
                ItemDisplay.Builder.forItem(ItemIds.WHEAT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.DANDELION, create(
                ItemDisplay.Builder.forBlock(ItemIds.DANDELION).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DANDELION)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.SATURATION), 140)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_DANDELION)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.OPEN_EYEBLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.OPEN_EYEBLOSSOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OPEN_EYEBLOSSOM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.BLINDNESS), 140)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_OPEN_EYEBLOSSOM)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.CLOSED_EYEBLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.CLOSED_EYEBLOSSOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CLOSED_EYEBLOSSOM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.NAUSEA), 140)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CLOSED_EYEBLOSSOM)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.POPPY, create(
                ItemDisplay.Builder.forBlock(ItemIds.POPPY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.POPPY)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.NIGHT_VISION), 100)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_POPPY)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_ORCHID, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_ORCHID).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_ORCHID)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.SATURATION), 140)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_BLUE_ORCHID)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.ALLIUM, create(
                ItemDisplay.Builder.forBlock(ItemIds.ALLIUM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ALLIUM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.FIRE_RESISTANCE), 80)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_ALLIUM)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.AZURE_BLUET, create(
                ItemDisplay.Builder.forBlock(ItemIds.AZURE_BLUET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.AZURE_BLUET)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.BLINDNESS), 160)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_AZURE_BLUET)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.RED_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_TULIP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_TULIP)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.WEAKNESS), 180)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_RED_TULIP)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_TULIP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_TULIP)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.WEAKNESS), 180)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_ORANGE_TULIP)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_TULIP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_TULIP)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.WEAKNESS), 180)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_WHITE_TULIP)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_TULIP, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_TULIP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_TULIP)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.WEAKNESS), 180)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_PINK_TULIP)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.OXEYE_DAISY, create(
                ItemDisplay.Builder.forBlock(ItemIds.OXEYE_DAISY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OXEYE_DAISY)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 160)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_OXEYE_DAISY)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.CORNFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CORNFLOWER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CORNFLOWER)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.JUMP_BOOST), 120)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CORNFLOWER)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.LILY_OF_THE_VALLEY, create(
                ItemDisplay.Builder.forBlock(ItemIds.LILY_OF_THE_VALLEY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LILY_OF_THE_VALLEY)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.POISON), 240)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_LILY_OF_THE_VALLEY)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.WITHER_ROSE, create(
                ItemDisplay.Builder.forBlock(ItemIds.WITHER_ROSE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WITHER_ROSE)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.WITHER), 160)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_WITHER_ROSE)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.AZALEA, create(
                ItemDisplay.Builder.forBlock(ItemIds.AZALEA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.AZALEA)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_AZALEA_BUSH)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.SUNFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemIds.SUNFLOWER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SUNFLOWER)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.LILAC, create(
                ItemDisplay.Builder.forBlock(ItemIds.LILAC).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LILAC)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.ROSE_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemIds.ROSE_BUSH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ROSE_BUSH)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.PEONY, create(
                ItemDisplay.Builder.forBlock(ItemIds.PEONY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PEONY)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.LARGE_FERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.LARGE_FERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LARGE_FERN)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.SPORE_BLOSSOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPORE_BLOSSOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPORE_BLOSSOM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_MUSHROOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_MUSHROOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_MUSHROOM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_BROWN_MUSHROOM)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.RED_MUSHROOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_MUSHROOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_MUSHROOM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_RED_MUSHROOM)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_FUNGUS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_FUNGUS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_FUNGUS)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CRIMSON_FUNGUS)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_FUNGUS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_FUNGUS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_FUNGUS)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_WARPED_FUNGUS)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.CRIMSON_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRIMSON_ROOTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRIMSON_ROOTS)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CRIMSON_ROOTS)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_ROOTS, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_ROOTS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_ROOTS)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_WARPED_ROOTS)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.MOSS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.MOSS_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MOSS_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_MOSS_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_MOSS_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_MOSS_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSHROOM_STEM, create(
                ItemDisplay.Builder.forBlock(ItemIds.MUSHROOM_STEM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MUSHROOM_STEM)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.SHROOMLIGHT, create(
                ItemDisplay.Builder.forBlock(ItemIds.SHROOMLIGHT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SHROOMLIGHT)))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_WART_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHER_WART_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHER_WART_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.WARPED_WART_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.WARPED_WART_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WARPED_WART_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.HAY_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.HAY_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.HAY_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOWERING_AZALEA, create(
                ItemDisplay.Builder.forBlock(ItemIds.FLOWERING_AZALEA).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FLOWERING_AZALEA)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_FLOWERING_AZALEA_BUSH)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.TORCHFLOWER, create(
                ItemDisplay.Builder.forBlock(ItemIds.TORCHFLOWER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TORCHFLOWER)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .with(SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(MobEffectIds.NIGHT_VISION), 100)
                    ))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_TORCHFLOWER)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.PITCHER_PLANT, create(
                ItemDisplay.Builder.forBlock(ItemIds.PITCHER_PLANT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PITCHER_PLANT)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_MUSHROOM_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_MUSHROOM_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_MUSHROOM_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_MUSHROOM_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_MUSHROOM_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_MUSHROOM_BLOCK)))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(ItemIds.CAKE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CAKE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CAKE)))
                    .with(CompostableItemBehavior.of(CompostChances.GUARANTEED))
                    .build()
            ));
        }

        private void bootstrapEquipment() {
            this.bootstrapArmor();
            this.bootstrapSkulls();
            this.registerable.register(ItemIds.ELYTRA, create(
                ItemDisplay.Builder.forItem(ItemIds.ELYTRA)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.ofPreserved(432))
                    .with(GliderItemBehavior.of(ItemPredicate.Builder.item()
                        .withComponents(DataComponentMatchers.Builder.components()
                            .partial(
                                DataComponentPredicates.DAMAGE,
                                DamagePredicate.durability(MinMaxBounds.Ints.atLeast(2))
                            ).build()
                        ).build()
                    ))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.CHEST)
                        .setSwappable(true)
                        .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_ELYTRA))
                        .setAsset(EquipmentAssets.ELYTRA)
                        .build()))
                    .with(RepairableItemBehavior.of(HolderSet.direct(
                        this.items.getOrThrow(ItemIds.PHANTOM_MEMBRANE)
                    )))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.SHIELD, create(
                ItemDisplay.Builder.forItem(ItemIds.SHIELD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(UseableItemBehavior.builder()
                        .useIndefinitely()
                        .animation(ItemUseAnimation.BLOCK)
                        .build()
                    )
                    .with(DamageableItemBehavior.of(336))
                    .with(AttackBlockingItemBehavior.of(new BlocksAttacks(
                        0.25f,
                        1.0f,
                        List.of(
                            new BlocksAttacks.DamageReduction(90.0f, Optional.empty(), 0.0f, 1.0f)
                        ),
                        new BlocksAttacks.ItemDamageFunction(3.0f, 1.0f, 1.0f),
                        Optional.of(this.damageTypes.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                        Optional.of(this.soundEvents.getOrThrow(SoundEventIds.SHIELD_BLOCK)),
                        Optional.of(this.soundEvents.getOrThrow(SoundEventIds.SHIELD_BREAK))
                    )))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.OFFHAND)
                        .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_GENERIC))
                        .setSwappable(false)
                        .build()
                    ))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .with(BannerPatternHolderItemBehavior.of())
                    .build()
            ));
        }

        private void bootstrapArmor() {
            this.registerable.register(ItemIds.LEATHER_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.LEATHER, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LEATHER_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.LEATHER, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LEATHER_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.LEATHER, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LEATHER_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.LEATHER, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.LEATHER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_LEATHER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.COPPER, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.COPPER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_COPPER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.COPPER, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.COPPER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_COPPER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.COPPER, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.COPPER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_COPPER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.COPPER, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.COPPER))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_COPPER_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHAINMAIL_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.CHAINMAIL_HELMET)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.CHAINMAIL))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_CHAIN_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHAINMAIL_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.CHAINMAIL_CHESTPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.CHAINMAIL))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_CHAIN_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHAINMAIL_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.CHAINMAIL_LEGGINGS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.CHAINMAIL))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_CHAIN_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CHAINMAIL_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.CHAINMAIL_BOOTS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.armor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.CHAINMAIL, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.CHAINMAIL))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_CHAIN_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.IRON, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.IRON))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.IRON, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.IRON))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.IRON, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.IRON))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.IRON, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.IRON))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_IRON_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.DIAMOND, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.DIAMOND, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.DIAMOND, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.DIAMOND))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_DIAMOND_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.GOLD, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.GOLD))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_GOLD_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.GOLD, ArmorType.CHESTPLATE))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.GOLD))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_GOLD_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.GOLD, ArmorType.LEGGINGS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.GOLD))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_GOLD_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.GOLD, ArmorType.BOOTS))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.GOLD))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_GOLD_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.NETHERITE, ArmorType.HELMET))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_CHESTPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_CHESTPLATE).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.CHESTPLATE),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.NETHERITE, ArmorType.CHESTPLATE))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_LEGGINGS, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_LEGGINGS).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.LEGGINGS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.NETHERITE, ArmorType.LEGGINGS))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_BOOTS, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_BOOTS).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.BOOTS),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.NETHERITE, ArmorType.BOOTS))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.NETHERITE))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_NETHERITE_ARMOR)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.TURTLE_HELMET, create(
                ItemDisplay.Builder.forItem(ItemIds.TURTLE_HELMET).build(),
                AttributeModifiers.armor(ArmorMaterials.TURTLE_SCUTE, ArmorType.HELMET),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.forArmor(ArmorMaterials.TURTLE_SCUTE, ArmorType.HELMET))
                    .with(EnchantableItemBehavior.of(ArmorMaterials.TURTLE_SCUTE))
                    .with(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.REPAIRS_TURTLE_HELMET)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LEATHER_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.LEATHER, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.LEATHER, this.soundEvents, this.entityTypes))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.COPPER, this.soundEvents, this.entityTypes))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.IRON, this.soundEvents, this.entityTypes))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.GOLD, this.soundEvents, this.entityTypes))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.DIAMOND, this.soundEvents, this.entityTypes))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_HORSE_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_HORSE_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHorseArmor(ArmorMaterials.NETHERITE, this.soundEvents, this.entityTypes))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.WOLF_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.WOLF_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.ARMADILLO_SCUTE, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DamageableItemBehavior.of(
                        ArmorType.BODY.getDurability(ArmorMaterials.ARMADILLO_SCUTE.durability()),
                        this.soundEvents.getOrThrow(SoundEventIds.WOLF_ARMOR_BREAK)
                    ))
                    .with(RepairableItemBehavior.of(
                        this.items.getOrThrow(ArmorMaterials.ARMADILLO_SCUTE.repairIngredient())
                    ))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.BODY)
                        .setEquipSound(ArmorMaterials.ARMADILLO_SCUTE.equipSound())
                        .setAsset(ArmorMaterials.ARMADILLO_SCUTE.assetId())
                        .setAllowedEntities(HolderSet.direct(
                            this.entityTypes.getOrThrow(EntityTypeIds.WOLF)
                        ))
                        .setCanBeSheared(true)
                        .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_UNEQUIP_WOLF))
                        .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.WHITE_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.WHITE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.ORANGE_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.ORANGE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.MAGENTA_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.MAGENTA, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_BLUE_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.LIGHT_BLUE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.YELLOW_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.YELLOW, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.LIME_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.LIME, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.PINK_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.PINK, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.GRAY_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.GRAY, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_GRAY_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.LIGHT_GRAY, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.CYAN_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.CYAN, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.PURPLE_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.PURPLE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.BLUE_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.BLUE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.BROWN_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.BROWN, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.GREEN_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.GREEN, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.RED_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.RED, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_HARNESS, create(
                ItemDisplay.Builder.forItem(ItemIds.BLACK_HARNESS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.ofHarness(DyeColor.BLACK, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_NAUTILUS_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_NAUTILUS_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.COPPER, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.ofNautilusArmor(ArmorMaterials.COPPER, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_NAUTILUS_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_NAUTILUS_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.IRON, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.ofNautilusArmor(ArmorMaterials.IRON, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLDEN_NAUTILUS_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLDEN_NAUTILUS_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.GOLD, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.ofNautilusArmor(ArmorMaterials.GOLD, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND_NAUTILUS_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND_NAUTILUS_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.DIAMOND, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.ofNautilusArmor(ArmorMaterials.DIAMOND, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_NAUTILUS_ARMOR, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_NAUTILUS_ARMOR).build(),
                AttributeModifiers.armor(ArmorMaterials.NETHERITE, ArmorType.BODY),
                ItemBehaviorSet.builder()
                    .with(EquipmentItemBehavior.ofNautilusArmor(ArmorMaterials.NETHERITE, this.soundEvents, this.entityTypes, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapSkulls() {
            this.registerable.register(ItemIds.SKELETON_SKULL, create(
                ItemDisplay.Builder.forBlock(ItemIds.SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.SKELETON_SKULL),
                        this.blocks.getOrThrow(BlockIds.SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.WITHER_SKELETON_SKULL, create(
                ItemDisplay.Builder.forBlock(ItemIds.WITHER_SKELETON_SKULL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.WITHER_SKELETON_SKULL),
                        this.blocks.getOrThrow(BlockIds.WITHER_SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.PLAYER_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.PLAYER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.PLAYER_HEAD),
                        this.blocks.getOrThrow(BlockIds.PLAYER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.ZOMBIE_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.ZOMBIE_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.ZOMBIE_HEAD),
                        this.blocks.getOrThrow(BlockIds.ZOMBIE_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.CREEPER_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.CREEPER_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.CREEPER_HEAD),
                        this.blocks.getOrThrow(BlockIds.CREEPER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.DRAGON_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.DRAGON_HEAD)
                    .rarity(Rarity.RARE)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.DRAGON_HEAD),
                        this.blocks.getOrThrow(BlockIds.DRAGON_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.PIGLIN_HEAD, create(
                ItemDisplay.Builder.forBlock(ItemIds.PIGLIN_HEAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockIds.PIGLIN_HEAD),
                        this.blocks.getOrThrow(BlockIds.PIGLIN_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
        }

        private void bootstrapFuel() {
            this.registerable.register(ItemIds.COAL_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.COAL_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COAL_BLOCK)))
                    .with(FuelItemBehavior.of(FuelTimes.COAL_BLOCK))
                    .build()
            ));
            this.registerable.register(ItemIds.BLAZE_ROD, create(
                ItemDisplay.Builder.forItem(ItemIds.BLAZE_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FuelItemBehavior.of(FuelTimes.BLAZE_ROD))
                    .build()
            ));
            this.registerable.register(ItemIds.COAL, create(
                ItemDisplay.Builder.forItem(ItemIds.COAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FuelItemBehavior.of(FuelTimes.COAL))
                    .build()
            ));
            this.registerable.register(ItemIds.CHARCOAL, create(
                ItemDisplay.Builder.forItem(ItemIds.CHARCOAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FuelItemBehavior.of(FuelTimes.COAL))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.SPRUCE_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.SPRUCE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BIRCH_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.BIRCH_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.JUNGLE_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.JUNGLE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.ACACIA_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.ACACIA_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.CHERRY_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.CHERRY_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DARK_OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.DARK_OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.PALE_OAK_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.PALE_OAK_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.MANGROVE_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.MANGROVE_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_HANGING_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_HANGING_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BAMBOO_HANGING_SIGN), this.blocks.getOrThrow(BlockIds.BAMBOO_WALL_HANGING_SIGN), Direction.UP))
                    .with(FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.OAK_SIGN), this.blocks.getOrThrow(BlockIds.OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.SPRUCE_SIGN), this.blocks.getOrThrow(BlockIds.SPRUCE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BIRCH_SIGN), this.blocks.getOrThrow(BlockIds.BIRCH_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.JUNGLE_SIGN), this.blocks.getOrThrow(BlockIds.JUNGLE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.ACACIA_SIGN), this.blocks.getOrThrow(BlockIds.ACACIA_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.CHERRY_SIGN), this.blocks.getOrThrow(BlockIds.CHERRY_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.DARK_OAK_SIGN), this.blocks.getOrThrow(BlockIds.DARK_OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.PALE_OAK_SIGN), this.blocks.getOrThrow(BlockIds.PALE_OAK_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.MANGROVE_SIGN), this.blocks.getOrThrow(BlockIds.MANGROVE_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_SIGN, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_SIGN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BAMBOO_SIGN), this.blocks.getOrThrow(BlockIds.BAMBOO_WALL_SIGN), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.SIGN))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_PLANKS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_PLANKS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_PLANKS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_MOSAIC, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_MOSAIC).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_MOSAIC)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_BLOCK)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_SPRUCE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_SPRUCE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_SPRUCE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_BIRCH_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_BIRCH_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_BIRCH_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_JUNGLE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_JUNGLE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_JUNGLE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_ACACIA_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_ACACIA_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_ACACIA_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_CHERRY_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_CHERRY_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_CHERRY_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_DARK_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_DARK_OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_DARK_OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_PALE_OAK_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_PALE_OAK_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_PALE_OAK_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_MANGROVE_LOG, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_MANGROVE_LOG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_MANGROVE_LOG)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_SPRUCE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_SPRUCE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_SPRUCE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_BIRCH_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_BIRCH_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_BIRCH_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_JUNGLE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_JUNGLE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_JUNGLE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_ACACIA_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_ACACIA_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_ACACIA_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_CHERRY_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_CHERRY_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_CHERRY_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_DARK_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_DARK_OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_DARK_OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_PALE_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_PALE_OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_PALE_OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_MANGROVE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_MANGROVE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_MANGROVE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.STRIPPED_BAMBOO_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.STRIPPED_BAMBOO_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.STRIPPED_BAMBOO_BLOCK)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_WOOD, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_WOOD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_WOOD)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_FENCE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_FENCE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_FENCE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_MOSAIC_STAIRS, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_MOSAIC_STAIRS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_MOSAIC_STAIRS)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_PRESSURE_PLATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_PRESSURE_PLATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_PRESSURE_PLATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_TRAPDOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_TRAPDOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_TRAPDOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_FENCE_GATE, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_FENCE_GATE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_FENCE_GATE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BOOKSHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.BOOKSHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BOOKSHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHISELED_BOOKSHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHISELED_BOOKSHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHISELED_BOOKSHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.LECTERN, create(
                ItemDisplay.Builder.forBlock(ItemIds.LECTERN).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LECTERN)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHEST)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_CHEST)))
                    .build()
            ));
            this.registerable.register(ItemIds.TRAPPED_CHEST, create(
                ItemDisplay.Builder.forBlock(ItemIds.TRAPPED_CHEST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TRAPPED_CHEST)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.LADDER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LADDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LADDER)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CRAFTING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CRAFTING_TABLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CRAFTING_TABLE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUKEBOX, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUKEBOX).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUKEBOX)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.NOTE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.NOTE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NOTE_BLOCK)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.LOOM, create(
                ItemDisplay.Builder.forBlock(ItemIds.LOOM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LOOM)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.COMPOSTER, create(
                ItemDisplay.Builder.forBlock(ItemIds.COMPOSTER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.COMPOSTER)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BARREL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BARREL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BARREL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CARTOGRAPHY_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.CARTOGRAPHY_TABLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CARTOGRAPHY_TABLE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.FLETCHING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.FLETCHING_TABLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.FLETCHING_TABLE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SMITHING_TABLE, create(
                ItemDisplay.Builder.forBlock(ItemIds.SMITHING_TABLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SMITHING_TABLE)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DAYLIGHT_DETECTOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.DAYLIGHT_DETECTOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DAYLIGHT_DETECTOR)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_DOOR, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_DOOR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_DOOR)))
                    .with(FuelItemBehavior.of(FuelTimes.DOOR))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_MOSAIC_SLAB, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_MOSAIC_SLAB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_MOSAIC_SLAB)))
                    .with(FuelItemBehavior.of(FuelTimes.SLAB))
                    .build()
            ));
            this.registerable.register(ItemIds.DEAD_BUSH, create(
                ItemDisplay.Builder.forBlock(ItemIds.DEAD_BUSH).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DEAD_BUSH)))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_DEAD_BUSH)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_BUTTON, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_BUTTON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_BUTTON)))
                    .with(FuelItemBehavior.of(FuelTimes.BUTTON))
                    .build()
            ));
            this.registerable.register(ItemIds.STICK, create(
                ItemDisplay.Builder.forItem(ItemIds.STICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FuelItemBehavior.of(FuelTimes.SMALL_WOODEN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemIds.BOWL, create(
                ItemDisplay.Builder.forItem(ItemIds.BOWL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FuelItemBehavior.of(FuelTimes.SMALL_WOODEN_ITEM))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO)))
                    .with(FuelItemBehavior.of(FuelTimes.BAMBOO))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_BAMBOO)
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.SCAFFOLDING, create(
                ItemDisplay.Builder.forBlock(ItemIds.SCAFFOLDING).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SCAFFOLDING)))
                    .with(FuelItemBehavior.of(FuelTimes.SCAFFOLDING))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_WOOL, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_WOOL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_WOOL)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.WHITE_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.WHITE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ORANGE_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.ORANGE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MAGENTA_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.MAGENTA)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.LIGHT_BLUE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.YELLOW_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.YELLOW)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIME_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.LIME)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PINK_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.PINK)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GRAY_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.GRAY)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.LIGHT_GRAY)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CYAN_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.CYAN)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PURPLE_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.PURPLE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLUE_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.BLUE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BROWN_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.BROWN)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.GREEN_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.GREEN)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.RED_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.RED)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_CARPET, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_CARPET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BLACK_CARPET)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                    .with(EquipmentItemBehavior.of(Equippable.llamaSwag(DyeColor.BLACK)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.SHORT_DRY_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.SHORT_DRY_GRASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SHORT_DRY_GRASS)))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.TALL_DRY_GRASS, create(
                ItemDisplay.Builder.forBlock(ItemIds.TALL_DRY_GRASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.TALL_DRY_GRASS)))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.LEAF_LITTER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LEAF_LITTER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.LEAF_LITTER)))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(ItemIds.ACACIA_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.ACACIA_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ACACIA_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BAMBOO_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.BAMBOO_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BAMBOO_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.BIRCH_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.BIRCH_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.BIRCH_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.CHERRY_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.CHERRY_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.CHERRY_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.DARK_OAK_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.DARK_OAK_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.DARK_OAK_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.JUNGLE_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.JUNGLE_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.JUNGLE_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.MANGROVE_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.MANGROVE_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.MANGROVE_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.OAK_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.OAK_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.OAK_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.PALE_OAK_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.PALE_OAK_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.PALE_OAK_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(ItemIds.SPRUCE_SHELF, create(
                ItemDisplay.Builder.forBlock(ItemIds.SPRUCE_SHELF).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.SPRUCE_SHELF)))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
        }

        private void bootstrapProjectiles() {
            this.registerable.register(ItemIds.ARROW, create(
                ItemDisplay.Builder.forItem(ItemIds.ARROW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.ARROW)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SNOWBALL, create(
                ItemDisplay.Builder.forItem(ItemIds.SNOWBALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.SNOWBALL)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(
                        this.entityTypes.getOrThrow(EntityTypeIds.EGG),
                        DataComponentPatch.builder()
                            .set(
                                DataComponents.CHICKEN_VARIANT,
                                this.chickenVariants.getOrThrow(ChickenVariants.TEMPERATE)
                            )
                            .build()
                    ))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BLUE_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(
                        this.entityTypes.getOrThrow(EntityTypeIds.EGG),
                        DataComponentPatch.builder()
                            .set(
                                DataComponents.CHICKEN_VARIANT,
                                this.chickenVariants.getOrThrow(ChickenVariants.COLD)
                            )
                            .build()
                    ))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.BROWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(
                        this.entityTypes.getOrThrow(EntityTypeIds.EGG),
                        DataComponentPatch.builder()
                            .set(
                                DataComponents.CHICKEN_VARIANT,
                                this.chickenVariants.getOrThrow(ChickenVariants.WARM)
                            )
                            .build()
                    ))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDER_PEARL, create(
                ItemDisplay.Builder.forItem(ItemIds.ENDER_PEARL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.ENDER_PEARL)))
                    .with(CooldownItemBehavior.of(20))
                    .build()
            ));
            this.registerable.register(ItemIds.ENDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemIds.ENDER_EYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ThrowableItemBehavior.of())
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.EYE_OF_ENDER)))
                    .with(PreventUseWhenUsedOnTargetItemBehavior.forBlock())
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .of(this.blocks, this.blocks.getOrThrow(BlockIds.END_PORTAL_FRAME).value())
                                    .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(BlockStateProperties.EYE, false)))
                        ),
                        PassingSequenceHandler.builder()
                            .add(ModifyBlockStateAction.builder(PositionTarget.INTERACTED)
                                .property(BlockStateProperties.EYE, true)
                                .pushEntitiesUpwards()
                                .build())
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                            .add(PlaySoundAction.of(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.END_PORTAL_FRAME_FILL), SoundSource.BLOCKS))
                            .add(DisplayParticleAction.builder(PositionTarget.INTERACTED, ParticleTypes.SMOKE)
                                .count(16)
                                .offset(Vec3Provider.of(
                                    -0.1875d, 0.1875d,
                                    0.8125d, 0.8125d,
                                    -0.1875d, 0.1875d))
                                .build())
                            .addOptional(LightEndPortalAction.of(PositionTarget.INTERACTED))
                    ))
                    .add(ItemEvent.THROW_PROJECTILE, ActionEntry.of(
                        PlaySoundAction.builder(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventIds.ENDER_EYE_LAUNCH), SoundSource.NEUTRAL)
                            .pitch(0.33f, 0.5f)
                            .build()
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPERIENCE_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemIds.EXPERIENCE_BOTTLE)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ThrowableItemBehavior.of(0.7f, -20.0f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.EXPERIENCE_BOTTLE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.FIRE_CHARGE, create(
                ItemDisplay.Builder.forItem(ItemIds.FIRE_CHARGE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.SMALL_FIREBALL)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_CHARGE)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DecrementItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.FIRE_CHARGE_USE), SoundSource.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.WIND_CHARGE, create(
                ItemDisplay.Builder.forItem(ItemIds.WIND_CHARGE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ThrowableItemBehavior.of(1.5f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.WIND_CHARGE)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .with(CooldownItemBehavior.of(10))
                    .build()
            ));
            this.registerable.register(ItemIds.FIREWORK_ROCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.FIREWORK_ROCKET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FireworkItemBehavior.INSTANCE)
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.FIREWORK_ROCKET)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_FIREWORK_ROCKET)))
                    .build()
            ));
            this.registerable.register(ItemIds.SPLASH_POTION, create(
                ItemDisplay.Builder.forItem(ItemIds.SPLASH_POTION).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(PotionHolderItemBehavior.of(1.0f))
                    .with(ThrowableItemBehavior.of(0.5f, -20.0f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.SPLASH_POTION)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SPECTRAL_ARROW, create(
                ItemDisplay.Builder.forItem(ItemIds.SPECTRAL_ARROW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.SPECTRAL_ARROW)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.TIPPED_ARROW, create(
                ItemDisplay.Builder.forItem(ItemIds.TIPPED_ARROW).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(PotionHolderItemBehavior.of(0.125f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.ARROW)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                    .build()
            ));
            this.registerable.register(ItemIds.LINGERING_POTION, create(
                ItemDisplay.Builder.forItem(ItemIds.LINGERING_POTION).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(PotionHolderItemBehavior.of(0.25f))
                    .with(ThrowableItemBehavior.of(0.5f, -20.0f))
                    .with(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.LINGERING_POTION)))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                    .build()
            ));
        }

        private void bootstrapDyes() {
            this.registerable.register(ItemIds.WHITE_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.WHITE_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.ORANGE_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.MAGENTA_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_BLUE_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.YELLOW_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIME_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.PINK_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.GRAY_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_GRAY_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.CYAN_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.PURPLE_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.BLUE_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.BROWN_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.GREEN_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.RED_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_DYE, create(
                ItemDisplay.Builder.forItem(ItemIds.BLACK_DYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DyeItemBehavior.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapRecords() {
            this.registerable.register(ItemIds.MUSIC_DISC_13, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_13)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.THIRTEEN)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_CAT, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_CAT)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CAT)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_BLOCKS, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_BLOCKS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.BLOCKS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_CHIRP, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_CHIRP)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CHIRP)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_CREATOR, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_CREATOR)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_CREATOR_MUSIC_BOX, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_CREATOR_MUSIC_BOX)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.CREATOR_MUSIC_BOX)))
                    .build()
            ));

            this.registerable.register(ItemIds.MUSIC_DISC_FAR, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_FAR)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FAR)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_MALL, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_MALL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MALL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_MELLOHI, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_MELLOHI)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.MELLOHI)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_STAL, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_STAL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STAL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_STRAD, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_STRAD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.STRAD)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_WARD, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_WARD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WARD)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_11, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_11)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.ELEVEN)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_WAIT, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_WAIT)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.WAIT)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_OTHERSIDE, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_OTHERSIDE)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.OTHERSIDE)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_RELIC, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_RELIC)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.RELIC)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_PIGSTEP, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_PIGSTEP)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PIGSTEP)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_PRECIPICE, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_PRECIPICE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.PRECIPICE)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_5, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_5)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.FIVE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DISC_FRAGMENT_5, create(
                ItemDisplay.Builder.forItem(ItemIds.DISC_FRAGMENT_5)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.description(ItemIds.DISC_FRAGMENT_5))
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_TEARS, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_TEARS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.TEARS)))
                    .build()
            ));
            this.registerable.register(ItemIds.MUSIC_DISC_LAVA_CHICKEN, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_LAVA_CHICKEN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.LAVA_CHICKEN)))
                    .build()
            ));
        }

        private void bootstrapBuckets() {
            this.registerable.register(ItemIds.BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.drainFluid(this.dispenseBehaviors))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemIds.WATER_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.WATER_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluid(this.fluids.getOrThrow(FluidIds.WATER), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.LAVA_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.LAVA_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluid(this.fluids.getOrThrow(FluidIds.LAVA), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_LAVA), this.items, this.dispenseBehaviors))
                    .with(FuelItemBehavior.of(FuelTimes.LAVA, this.items.getOrThrow(ItemIds.BUCKET)))
                    .build()
            ));
            this.registerable.register(ItemIds.POWDER_SNOW_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.POWDER_SNOW_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeBlock(this.blocks.getOrThrow(BlockIds.POWDER_SNOW), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_POWDER_SNOW), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.PUFFERFISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.PUFFERFISH_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.PUFFERFISH), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .with(FoodItemBehavior.of(Foods.PUFFERFISH))
                    .build()
            ));
            this.registerable.register(ItemIds.SALMON_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.SALMON_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.SALMON), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .with(FoodItemBehavior.of(Foods.SALMON))
                    .build()
            ));
            this.registerable.register(ItemIds.COD_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.COD_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.COD), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .with(FoodItemBehavior.of(Foods.COD))
                    .build()
            ));
            this.registerable.register(ItemIds.TROPICAL_FISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.TROPICAL_FISH_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.TROPICAL_FISH), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH), this.items, this.dispenseBehaviors))
                    .with(FoodItemBehavior.of(Foods.TROPICAL_FISH))
                    .build()
            ));
            this.registerable.register(ItemIds.AXOLOTL_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.AXOLOTL_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.AXOLOTL), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_AXOLOTL), this.items, this.dispenseBehaviors))
                    .build()
            ));
            this.registerable.register(ItemIds.TADPOLE_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.TADPOLE_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(BucketItemBehavior.placeFluidWithEntity(this.fluids.getOrThrow(FluidIds.WATER), this.entityTypes.getOrThrow(EntityTypeIds.TADPOLE), this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_TADPOLE), this.items, this.dispenseBehaviors))
                    .build()
            ));
        }

        private void bootstrapSmithingTemplates() {
            this.registerable.register(ItemIds.NETHERITE_UPGRADE_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_UPGRADE_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingUpgrade(Identifier.withDefaultNamespace("netherite_upgrade")))
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.ITEM_UPGRADE))
                    .build()
            ));
            this.registerable.register(ItemIds.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.COAST_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.WILD_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.WARD_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.EYE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.VEX_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.RIB_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.EPIC)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.HOST_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
            this.registerable.register(ItemIds.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, create(
                ItemDisplay.Builder.forItem(ItemIds.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE)
                    .rarity(Rarity.UNCOMMON)
                    .tooltip(Tooltips.smithingTrimPattern())
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(SmithingTemplateProviderItemBehavior.of(SmithingTemplates.TRIM_PATTERN))
                    .build()
            ));
        }

        private void bootstrapBanners() {
            this.bootstrapBannerPatterns();
            this.registerable.register(ItemIds.WHITE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.WHITE_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.WHITE_BANNER), this.blocks.getOrThrow(BlockIds.WHITE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.WHITE))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.ORANGE_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.ORANGE_BANNER), this.blocks.getOrThrow(BlockIds.ORANGE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.ORANGE))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.MAGENTA_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.MAGENTA_BANNER), this.blocks.getOrThrow(BlockIds.MAGENTA_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.MAGENTA))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_BLUE_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_BANNER), this.blocks.getOrThrow(BlockIds.LIGHT_BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.LIGHT_BLUE))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.YELLOW_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.YELLOW_BANNER), this.blocks.getOrThrow(BlockIds.YELLOW_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.YELLOW))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIME_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.LIME_BANNER), this.blocks.getOrThrow(BlockIds.LIME_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.LIME))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.PINK_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.PINK_BANNER), this.blocks.getOrThrow(BlockIds.PINK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.PINK))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.GRAY_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.GRAY_BANNER), this.blocks.getOrThrow(BlockIds.GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.GRAY))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.LIGHT_GRAY_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_BANNER), this.blocks.getOrThrow(BlockIds.LIGHT_GRAY_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.LIGHT_GRAY))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.CYAN_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.CYAN_BANNER), this.blocks.getOrThrow(BlockIds.CYAN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.CYAN))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.PURPLE_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.PURPLE_BANNER), this.blocks.getOrThrow(BlockIds.PURPLE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.PURPLE))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLUE_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BLUE_BANNER), this.blocks.getOrThrow(BlockIds.BLUE_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.BLUE))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BROWN_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BROWN_BANNER), this.blocks.getOrThrow(BlockIds.BROWN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.BROWN))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.GREEN_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.GREEN_BANNER), this.blocks.getOrThrow(BlockIds.GREEN_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.GREEN))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.RED_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.RED_BANNER), this.blocks.getOrThrow(BlockIds.RED_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.RED))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_BANNER, create(
                ItemDisplay.Builder.forBlock(ItemIds.BLACK_BANNER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(BlockItemBehavior.attachedToSide(this.blocks.getOrThrow(BlockIds.BLACK_BANNER), this.blocks.getOrThrow(BlockIds.BLACK_WALL_BANNER), Direction.DOWN))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(BannerPatternHolderItemBehavior.of(DyeColor.BLACK))
                    .build()
            ));
        }

        private void bootstrapBannerPatterns() {
            this.registerable.register(ItemIds.FLOWER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.FLOWER_BANNER_PATTERN).build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_FLOWER)))
                    .build()
            ));
            this.registerable.register(ItemIds.CREEPER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.CREEPER_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_CREEPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.SKULL_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.SKULL_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_SKULL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOJANG_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.MOJANG_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_MOJANG)))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOBE_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOBE_BANNER_PATTERN).build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_GLOBE)))
                    .build()
            ));
            this.registerable.register(ItemIds.PIGLIN_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.PIGLIN_BANNER_PATTERN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_PIGLIN)))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOW_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.FLOW_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_FLOW)))
                    .build()
            ));
            this.registerable.register(ItemIds.GUSTER_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.GUSTER_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_GUSTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.FIELD_MASONED_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.FIELD_MASONED_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_FIELD_MASONED)))
                    .build()
            ));
            this.registerable.register(ItemIds.BORDURE_INDENTED_BANNER_PATTERN, create(
                ItemDisplay.Builder.forItem(ItemIds.BORDURE_INDENTED_BANNER_PATTERN)
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(BannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED)))
                    .build()
            ));
        }

        private void bootstrapDecoratedPotPatterns() {
            this.registerable.register(ItemIds.BRICK, create(
                ItemDisplay.Builder.forItem(ItemIds.BRICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLANK)))
                    .build()
            ));
            this.registerable.register(ItemIds.ANGLER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.ANGLER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ANGLER)))
                    .build()
            ));
            this.registerable.register(ItemIds.ARCHER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.ARCHER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARCHER)))
                    .build()
            ));
            this.registerable.register(ItemIds.ARMS_UP_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.ARMS_UP_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.ARMS_UP)))
                    .build()
            ));
            this.registerable.register(ItemIds.BLADE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.BLADE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BLADE)))
                    .build()
            ));
            this.registerable.register(ItemIds.BREWER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.BREWER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BREWER)))
                    .build()
            ));
            this.registerable.register(ItemIds.BURN_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.BURN_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.BURN)))
                    .build()
            ));
            this.registerable.register(ItemIds.DANGER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.DANGER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.DANGER)))
                    .build()
            ));
            this.registerable.register(ItemIds.EXPLORER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.EXPLORER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.EXPLORER)))
                    .build()
            ));
            this.registerable.register(ItemIds.FLOW_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.FLOW_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FLOW)))
                    .build()
            ));
            this.registerable.register(ItemIds.FRIEND_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.FRIEND_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.FRIEND)))
                    .build()
            ));
            this.registerable.register(ItemIds.GUSTER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.GUSTER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.GUSTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.HEART_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.HEART_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEART)))
                    .build()
            ));
            this.registerable.register(ItemIds.HEARTBREAK_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.HEARTBREAK_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HEARTBREAK)))
                    .build()
            ));
            this.registerable.register(ItemIds.HOWL_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.HOWL_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.HOWL)))
                    .build()
            ));
            this.registerable.register(ItemIds.MINER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.MINER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MINER)))
                    .build()
            ));
            this.registerable.register(ItemIds.MOURNER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.MOURNER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.MOURNER)))
                    .build()
            ));
            this.registerable.register(ItemIds.PLENTY_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.PLENTY_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PLENTY)))
                    .build()
            ));
            this.registerable.register(ItemIds.PRIZE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.PRIZE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.PRIZE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SCRAPE_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.SCRAPE_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SCRAPE)))
                    .build()
            ));
            this.registerable.register(ItemIds.SHEAF_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.SHEAF_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHEAF)))
                    .build()
            ));
            this.registerable.register(ItemIds.SHELTER_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.SHELTER_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SHELTER)))
                    .build()
            ));
            this.registerable.register(ItemIds.SKULL_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.SKULL_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SKULL)))
                    .build()
            ));
            this.registerable.register(ItemIds.SNORT_POTTERY_SHERD, create(
                ItemDisplay.Builder.forItem(ItemIds.SNORT_POTTERY_SHERD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(DecoratedPotPatterns.SNORT)))
                    .build()
            ));
        }

        private void bootstrapImmuneToDamage() {
            this.registerable.register(ItemIds.ANCIENT_DEBRIS, create(
                ItemDisplay.Builder.forBlock(ItemIds.ANCIENT_DEBRIS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.ANCIENT_DEBRIS)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_BLOCK, create(
                ItemDisplay.Builder.forBlock(ItemIds.NETHERITE_BLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.NETHERITE_BLOCK)))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_INGOT, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_INGOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.NETHERITE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHERITE_SCRAP, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHERITE_SCRAP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_STAR, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHER_STAR)
                    .rarity(Rarity.RARE)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_EXPLOSION)))
                    .build()
            ));
        }

        private void bootstrapTrimMaterialProviders() {
            this.registerable.register(ItemIds.REDSTONE, create(
                ItemDisplay.Builder.forItem(ItemIds.REDSTONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockIds.REDSTONE_WIRE)))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.REDSTONE)))
                    .build()
            ));
            this.registerable.register(ItemIds.DIAMOND, create(
                ItemDisplay.Builder.forItem(ItemIds.DIAMOND).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.DIAMOND)))
                    .build()
            ));
            this.registerable.register(ItemIds.EMERALD, create(
                ItemDisplay.Builder.forItem(ItemIds.EMERALD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.EMERALD)))
                    .build()
            ));
            this.registerable.register(ItemIds.LAPIS_LAZULI, create(
                ItemDisplay.Builder.forItem(ItemIds.LAPIS_LAZULI).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.LAPIS)))
                    .build()
            ));
            this.registerable.register(ItemIds.QUARTZ, create(
                ItemDisplay.Builder.forItem(ItemIds.QUARTZ).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.QUARTZ)))
                    .build()
            ));
            this.registerable.register(ItemIds.AMETHYST_SHARD, create(
                ItemDisplay.Builder.forItem(ItemIds.AMETHYST_SHARD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.AMETHYST)))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_INGOT, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_INGOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.IRON)))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_INGOT, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_INGOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.COPPER)))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLD_INGOT, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLD_INGOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.GOLD)))
                    .build()
            ));
            this.registerable.register(ItemIds.RESIN_BRICK, create(
                ItemDisplay.Builder.forItem(ItemIds.RESIN_BRICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.RESIN)))
                    .build()
            ));
        }

        private void bootstrapMiscellaneous() {
            this.registerable.register(ItemIds.AIR, create(
                ItemDisplay.Builder.forBlock(ItemIds.AIR).build()
            ));
            this.registerable.register(ItemIds.SADDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.SADDLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.SADDLE)
                        .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.HORSE_SADDLE))
                        .setAsset(EquipmentAssets.SADDLE)
                        .setAllowedEntities(this.entityTypes.getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
                        .setEquipOnInteract(true)
                        .setCanBeSheared(true)
                        .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.SADDLE_UNEQUIP))
                        .build()
                    ))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                    .build()
            ));
            this.registerable.register(ItemIds.TURTLE_SCUTE, create(
                ItemDisplay.Builder.forItem(ItemIds.TURTLE_SCUTE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.ARMADILLO_SCUTE, create(
                ItemDisplay.Builder.forItem(ItemIds.ARMADILLO_SCUTE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_IRON, create(
                ItemDisplay.Builder.forItem(ItemIds.RAW_IRON).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_COPPER, create(
                ItemDisplay.Builder.forItem(ItemIds.RAW_COPPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.RAW_GOLD, create(
                ItemDisplay.Builder.forItem(ItemIds.RAW_GOLD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.FEATHER, create(
                ItemDisplay.Builder.forItem(ItemIds.FEATHER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.GUNPOWDER, create(
                ItemDisplay.Builder.forItem(ItemIds.GUNPOWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.FLINT, create(
                ItemDisplay.Builder.forItem(ItemIds.FLINT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.LEATHER, create(
                ItemDisplay.Builder.forItem(ItemIds.LEATHER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.CLAY_BALL, create(
                ItemDisplay.Builder.forItem(ItemIds.CLAY_BALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.PAPER, create(
                ItemDisplay.Builder.forItem(ItemIds.PAPER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.BOOK, create(
                ItemDisplay.Builder.forItem(ItemIds.BOOK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EnchantableItemBehavior.ofTransforming(1, this.items.getOrThrow(ItemIds.ENCHANTED_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemIds.SLIME_BALL, create(
                ItemDisplay.Builder.forItem(ItemIds.SLIME_BALL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.COMPASS, create(
                ItemDisplay.Builder.forItem(ItemIds.COMPASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(BlockPredicate.Builder.block()
                                    .of(this.blocks, this.blocks.getOrThrow(BlockIds.LODESTONE).value()))
                        ),
                        PassingSequenceHandler.builder()
                            .add(ModifyItemAction.of(
                                LootContext.ItemStackTarget.TOOL,
                                SplitItemModifier.builder(1),
                                SetItemPointerLocationItemModifier.builder(PositionTarget.INTERACTED),
                                SetNameFunction.setName(
                                    Component.translatable(Util.makeDescriptionId("item", Identifier.withDefaultNamespace("lodestone_compass"))),
                                    SetNameFunction.Target.ITEM_NAME
                                ),
                                SetComponentsFunction.setComponent(
                                    DataComponents.ENCHANTMENT_GLINT_OVERRIDE,
                                    true
                                )
                            ))
                            .add(PlaySoundAction.of(
                                PositionTarget.INTERACTED,
                                this.soundEvents.getOrThrow(SoundEventIds.LODESTONE_COMPASS_LOCK),
                                SoundSource.PLAYERS
                            ))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.RECOVERY_COMPASS, create(
                ItemDisplay.Builder.forItem(ItemIds.RECOVERY_COMPASS)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.WHITE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.WHITE_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.ORANGE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.ORANGE_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGENTA_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.MAGENTA_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_BLUE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_BLUE_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.YELLOW_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.YELLOW_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.LIME_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIME_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.PINK_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.PINK_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.GRAY_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.GRAY_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.LIGHT_GRAY_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.LIGHT_GRAY_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.CYAN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.CYAN_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.PURPLE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.PURPLE_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.BLUE_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.BLUE_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.BROWN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.BROWN_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.GREEN_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.GREEN_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.RED_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.RED_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.BLACK_BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.BLACK_BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            this.registerable.register(ItemIds.CLOCK, create(
                ItemDisplay.Builder.forItem(ItemIds.CLOCK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.SPYGLASS, create(
                ItemDisplay.Builder.forItem(ItemIds.SPYGLASS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(ZoomItemBehavior.of(SpyglassItem.ZOOM_FOV_MODIFIER, this.soundEvents.getOrThrow(SoundEventIds.SPYGLASS_USE), this.soundEvents.getOrThrow(SoundEventIds.SPYGLASS_STOP_USING)))
                    .with(UseableItemBehavior.builder()
                        .useFor(SpyglassItem.USE_DURATION)
                        .animation(ItemUseAnimation.SPYGLASS)
                        .build())
                    .build()
            ));
            this.registerable.register(ItemIds.GLOWSTONE_DUST, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOWSTONE_DUST).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.INK_SAC, create(
                ItemDisplay.Builder.forItem(ItemIds.INK_SAC).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, Actions.glowSign(this.blocks, false))
                    .build()
            ));
            this.registerable.register(ItemIds.GLOW_INK_SAC, create(
                ItemDisplay.Builder.forItem(ItemIds.GLOW_INK_SAC).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, Actions.glowSign(this.blocks, true))
                    .build()
            ));
            this.registerable.register(ItemIds.BONE_MEAL, create(
                ItemDisplay.Builder.forItem(ItemIds.BONE_MEAL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
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
            this.registerable.register(ItemIds.BONE, create(
                ItemDisplay.Builder.forItem(ItemIds.BONE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.SUGAR, create(
                ItemDisplay.Builder.forItem(ItemIds.SUGAR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.FILLED_MAP, create(
                ItemDisplay.Builder.forItem(ItemIds.FILLED_MAP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(MapHolderItemBehavior.INSTANCE)
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(MarkBannerOnItemAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.GHAST_TEAR, create(
                ItemDisplay.Builder.forItem(ItemIds.GHAST_TEAR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.GOLD_NUGGET, create(
                ItemDisplay.Builder.forItem(ItemIds.GOLD_NUGGET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.GLASS_BOTTLE, create(
                ItemDisplay.Builder.forItem(ItemIds.GLASS_BOTTLE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.GLASS_BOTTLE)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setFluid(FluidPredicate.Builder.fluid()
                                    .of(this.fluids.getOrThrow(FluidTags.WATER)))
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(ExchangeItemAction.of(
                                this.items.getOrThrow(ItemIds.POTION),
                                DataComponentPatch.builder()
                                    .set(DataComponents.POTION_CONTENTS, new PotionContents(this.potions.getOrThrow(PotionIds.WATER)))
                                    .build()))
                            .add(InvokeGameEventAction.of(GameEvent.FLUID_PICKUP, PositionTarget.INTERACTED, LootContext.EntityTarget.THIS))
                            .add(PlaySoundAction.of(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventIds.BOTTLE_FILL), SoundSource.NEUTRAL))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.FERMENTED_SPIDER_EYE, create(
                ItemDisplay.Builder.forItem(ItemIds.FERMENTED_SPIDER_EYE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.BLAZE_POWDER, create(
                ItemDisplay.Builder.forItem(ItemIds.BLAZE_POWDER).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.MAGMA_CREAM, create(
                ItemDisplay.Builder.forItem(ItemIds.MAGMA_CREAM).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.GLISTERING_MELON_SLICE, create(
                ItemDisplay.Builder.forItem(ItemIds.GLISTERING_MELON_SLICE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.WRITABLE_BOOK, create(
                ItemDisplay.Builder.forItem(ItemIds.WRITABLE_BOOK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(WritableItemBehavior.of(this.items.getOrThrow(ItemIds.WRITTEN_BOOK)))
                    .build()
            ));
            this.registerable.register(ItemIds.WRITTEN_BOOK, create(
                ItemDisplay.Builder.forItem(ItemIds.WRITTEN_BOOK)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(16))
                    .with(TextHolderItemBehavior.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemIds.MAP, create(
                ItemDisplay.Builder.forItem(ItemIds.MAP).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(MappableItemBehavior.of(this.items.getOrThrow(ItemIds.FILLED_MAP)))
                    .build()
            ));
            this.registerable.register(ItemIds.FIREWORK_STAR, create(
                ItemDisplay.Builder.forItem(ItemIds.FIREWORK_STAR).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(FireworkExplosionHolderItemBehavior.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemIds.ENCHANTED_BOOK, create(
                ItemDisplay.Builder.forItem(ItemIds.ENCHANTED_BOOK)
                    .rarity(Rarity.UNCOMMON)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(EnchantmentHolderItemBehavior.of(this.items.getOrThrow(ItemIds.BOOK)))
                    .build()
            ));
            this.registerable.register(ItemIds.NETHER_BRICK, create(
                ItemDisplay.Builder.forItem(ItemIds.NETHER_BRICK).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_SHARD, create(
                ItemDisplay.Builder.forItem(ItemIds.PRISMARINE_SHARD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.PRISMARINE_CRYSTALS, create(
                ItemDisplay.Builder.forItem(ItemIds.PRISMARINE_CRYSTALS).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.RABBIT_FOOT, create(
                ItemDisplay.Builder.forItem(ItemIds.RABBIT_FOOT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.RABBIT_HIDE, create(
                ItemDisplay.Builder.forItem(ItemIds.RABBIT_HIDE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.LEAD, create(
                ItemDisplay.Builder.forItem(ItemIds.LEAD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(AttachLeashedEntitiesOnBlockAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.NAME_TAG, create(
                ItemDisplay.Builder.forItem(ItemIds.NAME_TAG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_ENTITY, ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(SetEntityNameFromItemAction.of(LootContext.EntityTarget.TARGET_ENTITY))
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.POPPED_CHORUS_FRUIT, create(
                ItemDisplay.Builder.forItem(ItemIds.POPPED_CHORUS_FRUIT).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.DRAGON_BREATH, create(
                ItemDisplay.Builder.forItem(ItemIds.DRAGON_BREATH)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.TOTEM_OF_UNDYING, create(
                ItemDisplay.Builder.forItem(ItemIds.TOTEM_OF_UNDYING)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.BEFORE_DEATH_HOLDER, ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 900, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 100, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.FIRE_RESISTANCE), 800, 0)
                        )
                    ))
                    .build()
            ));
            this.registerable.register(ItemIds.SHULKER_SHELL, create(
                ItemDisplay.Builder.forItem(ItemIds.SHULKER_SHELL).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.IRON_NUGGET, create(
                ItemDisplay.Builder.forItem(ItemIds.IRON_NUGGET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.KNOWLEDGE_BOOK, create(
                ItemDisplay.Builder.forItem(ItemIds.KNOWLEDGE_BOOK)
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(UnlockRecipesItemBehavior.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemIds.DEBUG_STICK, create(
                ItemDisplay.Builder.forItem(ItemIds.DEBUG_STICK)
                    .rarity(Rarity.EPIC)
                    .glint()
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(DebugStickItemBehavior.INSTANCE)
                    .build()
            ));
            this.registerable.register(ItemIds.PHANTOM_MEMBRANE, create(
                ItemDisplay.Builder.forItem(ItemIds.PHANTOM_MEMBRANE).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.NAUTILUS_SHELL, create(
                ItemDisplay.Builder.forItem(ItemIds.NAUTILUS_SHELL)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.HEART_OF_THE_SEA, create(
                ItemDisplay.Builder.forItem(ItemIds.HEART_OF_THE_SEA)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.GOAT_HORN, create(
                ItemDisplay.Builder.forItem(ItemIds.GOAT_HORN)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(PlayableItemBehavior.of(this.instruments.getOrThrow(Instruments.PONDER_GOAT_HORN)))
                    .build()
            ));
            this.registerable.register(ItemIds.HONEYCOMB, create(
                ItemDisplay.Builder.forItem(ItemIds.HONEYCOMB).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.WAX_BLOCK)))
                    .build(),
                ActionEventMap.Builder.item()
                    .add(ItemEvent.USE_ON_BLOCK, ActionEntry.of(
                        FirstToPassRequirementsSequenceHandler.builder()
                            .add(Actions.waxSign(this.blocks, true))
                            .add(PassingSequenceHandler.builder()
                                .add(WaxBlockAction.of(PositionTarget.INTERACTED))
                                .add(DecrementItemAction.of(1))
                                .add(SwingHandAction.of(LootContext.EntityTarget.THIS)))))
                    .build()
            ));
            this.registerable.register(ItemIds.ECHO_SHARD, create(
                ItemDisplay.Builder.forItem(ItemIds.ECHO_SHARD)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.TRIAL_KEY, create(
                ItemDisplay.Builder.forItem(ItemIds.TRIAL_KEY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.OMINOUS_TRIAL_KEY, create(
                ItemDisplay.Builder.forItem(ItemIds.OMINOUS_TRIAL_KEY).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.BREEZE_ROD, create(
                ItemDisplay.Builder.forItem(ItemIds.BREEZE_ROD).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
            this.registerable.register(ItemIds.COPPER_NUGGET, create(
                ItemDisplay.Builder.forItem(ItemIds.COPPER_NUGGET).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .build()
            ));
        }
    }
}
