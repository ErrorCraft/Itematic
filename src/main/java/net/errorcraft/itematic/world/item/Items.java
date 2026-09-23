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
import net.errorcraft.itematic.references.ItemBarStyleIds;
import net.errorcraft.itematic.references.ItematicBlockItemIds;
import net.errorcraft.itematic.references.MobEffectIds;
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
import net.errorcraft.itematic.world.level.block.CoralCollection;
import net.errorcraft.itematic.world.level.block.CutoutCollection;
import net.errorcraft.itematic.world.level.block.FuelTimes;
import net.errorcraft.itematic.world.level.block.WoodCollection;
import net.errorcraft.itematic.world.level.storage.loot.functions.SetItemPointerLocationItemModifier;
import net.errorcraft.itematic.world.level.storage.loot.functions.SplitItemModifier;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.errorcraft.itematic.world.level.storage.loot.predicates.SideCheckPredicate;
import net.errorcraft.itematic.world.phys.Vec3Provider;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.FluidPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
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
import net.minecraft.references.BlockIds;
import net.minecraft.references.BlockItemId;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
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
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.chicken.ChickenVariant;
import net.minecraft.world.entity.animal.chicken.ChickenVariants;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
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
import net.minecraft.world.item.alchemy.PotionIds;
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
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPattern;
import net.minecraft.world.level.block.entity.DecoratedPotPatterns;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidIds;
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
import java.util.function.Consumer;

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

    public static class Bootstrapper {
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
            this.bootstrapRecords();
            this.bootstrapBuckets();
            this.bootstrapSmithingTemplates();
            this.bootstrapBannerPatterns();
            this.bootstrapDecoratedPotPatterns();
            this.bootstrapImmuneToDamage();
            this.bootstrapTrimMaterialProviders();
            this.bootstrapMiscellaneous();

            WoodCollection.registerItems(
                ItematicBlockItemIds.OAK,
                this,
                BlockIds.OAK_WALL_SIGN,
                BlockIds.OAK_WALL_HANGING_SIGN,
                BlockIds.POTTED_OAK_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.SPRUCE,
                this,
                BlockIds.SPRUCE_WALL_SIGN,
                BlockIds.SPRUCE_WALL_HANGING_SIGN,
                BlockIds.POTTED_SPRUCE_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.BIRCH,
                this,
                BlockIds.BIRCH_WALL_SIGN,
                BlockIds.BIRCH_WALL_HANGING_SIGN,
                BlockIds.POTTED_BIRCH_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.JUNGLE,
                this,
                BlockIds.JUNGLE_WALL_SIGN,
                BlockIds.JUNGLE_WALL_HANGING_SIGN,
                BlockIds.POTTED_JUNGLE_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.ACACIA,
                this,
                BlockIds.ACACIA_WALL_SIGN,
                BlockIds.ACACIA_WALL_HANGING_SIGN,
                BlockIds.POTTED_ACACIA_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.CHERRY,
                this,
                BlockIds.CHERRY_WALL_SIGN,
                BlockIds.CHERRY_WALL_HANGING_SIGN,
                BlockIds.POTTED_CHERRY_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.DARK_OAK,
                this,
                BlockIds.DARK_OAK_WALL_SIGN,
                BlockIds.DARK_OAK_WALL_HANGING_SIGN,
                BlockIds.POTTED_DARK_OAK_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.PALE_OAK,
                this,
                BlockIds.PALE_OAK_WALL_SIGN,
                BlockIds.PALE_OAK_WALL_HANGING_SIGN,
                BlockIds.POTTED_PALE_OAK_SAPLING,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.MANGROVE,
                this,
                BlockIds.MANGROVE_WALL_SIGN,
                BlockIds.MANGROVE_WALL_HANGING_SIGN,
                BlockIds.POTTED_MANGROVE_PROPAGULE,
                FuelTimes.PLANT
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.BAMBOO,
                this,
                BlockIds.BAMBOO_WALL_SIGN,
                BlockIds.BAMBOO_WALL_HANGING_SIGN,
                BlockIds.POTTED_BAMBOO,
                FuelTimes.BAMBOO
            );
            CutoutCollection.registerBurningItems(ItematicBlockItemIds.BAMBOO_MOSAIC, this);
            WoodCollection.registerItems(
                ItematicBlockItemIds.CRIMSON,
                this,
                BlockIds.CRIMSON_WALL_SIGN,
                BlockIds.CRIMSON_WALL_HANGING_SIGN,
                BlockIds.POTTED_CRIMSON_FUNGUS
            );
            WoodCollection.registerItems(
                ItematicBlockItemIds.WARPED,
                this,
                BlockIds.WARPED_WALL_SIGN,
                BlockIds.WARPED_WALL_HANGING_SIGN,
                BlockIds.POTTED_WARPED_FUNGUS
            );
            this.registerBlockAttachedToSide(BlockItemIds.TORCH, BlockIds.WALL_TORCH, Direction.DOWN);
            this.registerBlockAttachedToSide(BlockItemIds.SOUL_TORCH, BlockIds.SOUL_WALL_TORCH, Direction.DOWN);
            this.registerBlockAttachedToSide(BlockItemIds.COPPER_TORCH, BlockIds.COPPER_WALL_TORCH, Direction.DOWN);
            this.registerBlockAttachedToSide(BlockItemIds.REDSTONE_TORCH, BlockIds.REDSTONE_WALL_TORCH, Direction.DOWN);
            ColorCollection.zipApply(ItemIds.DYE, ColorCollection.VALUES, (dye, dyeColor) -> this.registerable.register(
                dye,
                create(
                    ItemDisplay.Builder.forItem(dye).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(DyeItemBehavior.of(dyeColor))
                        .build()
                )
            ));
            BlockItemIds.WOOL.forEach(wool -> this.registerable.register(
                wool.item(),
                create(
                    ItemDisplay.Builder.forBlock(wool.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(wool.block())))
                        .with(FuelItemBehavior.of(FuelTimes.WOOL))
                        .build()
                )
            ));
            ColorCollection.zipApply(BlockItemIds.CARPET, ColorCollection.VALUES, (carpet, dyeColor) -> this.registerable.register(
                carpet.item(),
                create(
                    ItemDisplay.Builder.forBlock(carpet.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(carpet.block())))
                        .with(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                        .with(EquipmentItemBehavior.of(Equippable.llamaSwag(dyeColor)))
                        .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                        .build()
                )
            ));
            BlockItemIds.BED.forEach(this::registerUnstackableBlock);
            this.registerBlock(BlockItemIds.GLASS);
            this.registerBlock(BlockItemIds.TINTED_GLASS);
            BlockItemIds.STAINED_GLASS.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.GLASS_PANE);
            BlockItemIds.STAINED_GLASS_PANE.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.TERRACOTTA);
            BlockItemIds.DYED_TERRACOTTA.forEach(this::registerBlock);
            BlockItemIds.GLAZED_TERRACOTTA.forEach(this::registerBlock);
            BlockItemIds.CONCRETE.forEach(this::registerBlock);
            BlockItemIds.CONCRETE_POWDER.forEach(this::registerBlock);
            this.registerShulkerBox(BlockItemIds.SHULKER_BOX);
            BlockItemIds.DYED_SHULKER_BOX.forEach(this::registerShulkerBox);
            this.registerBlock(BlockItemIds.CANDLE);
            BlockItemIds.DYED_CANDLE.forEach(this::registerBlock);
            ColorCollection.zipApply(BlockItemIds.BANNER, ColorCollection.VALUES, (banner, dyeColor) -> this.registerable.register(
                banner.item(),
                create(
                    ItemDisplay.Builder.forBlock(banner.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(16))
                        .with(
                            BlockItemBehavior.attachedToSide(
                                this.blocks.getOrThrow(banner.block()),
                                this.blocks.getOrThrow(BlockIds.WALL_BANNER.pick(dyeColor)),
                                Direction.DOWN
                            )
                        )
                        .with(FuelItemBehavior.of(FuelTimes.WOOD))
                        .with(BannerPatternHolderItemBehavior.of(dyeColor))
                        .build()
                )
            ));
            this.registerable.register(ItemIds.BUNDLE, create(
                ItemDisplay.Builder.forItem(ItemIds.BUNDLE)
                    .itemBarStyle(ItemBarStyleIds.BUNDLE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                    .build()
            ));
            ItemIds.DYED_BUNDLE.forEach(dyedBundle -> this.registerable.register(
                dyedBundle,
                create(
                    ItemDisplay.Builder.forItem(dyedBundle)
                        .itemBarStyle(ItemBarStyleIds.BUNDLE)
                        .build(),
                    ItemBehaviorSet.builder()
                        .with(ItemHolderItemBehavior.of(this.items, this.soundEvents))
                        .build()
                )
            ));
            CoralCollection.registerItems(ItematicBlockItemIds.TUBE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.BRAIN_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.BUBBLE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.FIRE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.HORN_CORAL, this);
            BlockItemIds.COPPER_BLOCK.forEach(this::registerBlock);
            BlockItemIds.COPPER_BARS.forEach(this::registerBlock);
            BlockItemIds.COPPER_CHAIN.forEach(this::registerBlock);
            BlockItemIds.COPPER_LANTERN.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER.forEach(this::registerBlock);
            BlockItemIds.CHISELED_COPPER.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER_STAIRS.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER_SLAB.forEach(this::registerBlock);
            BlockItemIds.COPPER_DOOR.forEach(this::registerBlock);
            BlockItemIds.COPPER_TRAPDOOR.forEach(this::registerBlock);
            BlockItemIds.COPPER_GRATE.forEach(this::registerBlock);
            BlockItemIds.COPPER_BULB.forEach(this::registerBlock);
            BlockItemIds.COPPER_CHEST.forEach(this::registerBlock);
            BlockItemIds.COPPER_GOLEM_STATUE.forEach(this::registerBlock);
            BlockItemIds.LIGHTNING_ROD.forEach(this::registerBlock);
            CutoutCollection.registerItems(ItematicBlockItemIds.CINNABAR, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_CINNABAR, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.CINNABAR_BRICKS, this);
            this.registerBlock(BlockItemIds.CHISELED_CINNABAR);
            CutoutCollection.registerItems(ItematicBlockItemIds.SULFUR, this);
            this.registerBlock(BlockItemIds.POTENT_SULFUR);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_SULFUR, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.SULFUR_BRICKS, this);
            this.registerBlock(BlockItemIds.CHISELED_SULFUR);
            this.registerBlock(BlockItemIds.SULFUR_SPIKE);
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
                            .add(SetBlockStateAction.of(PositionTarget.INTERACTED, this.blocks.getOrThrow(BlockItemIds.MUD.block())))
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
            this.registerable.register(BlockItemIds.CARROT_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.CARROT_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.CARROT)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CARROT_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.POTATO_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.POTATO_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.POTATO)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.POTATO_CROP.block())))
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
            this.registerable.register(BlockItemIds.SWEET_BERRY_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.SWEET_BERRY_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.SWEET_BERRIES)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SWEET_BERRY_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.GLOW_BERRY_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.GLOW_BERRY_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(ConsumableItemBehavior.builder(Consumables.DEFAULT_FOOD)
                        .food(Foods.GLOW_BERRIES)
                        .build())
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.GLOW_BERRY_CROP.block())))
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
            this.bootstrapItemNameBlocks();
            this.bootstrapOperatorOnlyBlocks();
            this.registerBlock(BlockItemIds.STONE);
            this.registerBlock(BlockItemIds.GRANITE);
            this.registerBlock(BlockItemIds.POLISHED_GRANITE);
            this.registerBlock(BlockItemIds.DIORITE);
            this.registerBlock(BlockItemIds.POLISHED_DIORITE);
            this.registerBlock(BlockItemIds.ANDESITE);
            this.registerBlock(BlockItemIds.POLISHED_ANDESITE);
            this.registerBlock(BlockItemIds.DEEPSLATE);
            this.registerBlock(BlockItemIds.COBBLED_DEEPSLATE);
            this.registerBlock(BlockItemIds.POLISHED_DEEPSLATE);
            this.registerBlock(BlockItemIds.CALCITE);
            this.registerBlock(BlockItemIds.TUFF);
            this.registerBlock(BlockItemIds.TUFF_SLAB);
            this.registerBlock(BlockItemIds.TUFF_STAIRS);
            this.registerBlock(BlockItemIds.TUFF_WALL);
            this.registerBlock(BlockItemIds.CHISELED_TUFF);
            this.registerBlock(BlockItemIds.POLISHED_TUFF);
            this.registerBlock(BlockItemIds.POLISHED_TUFF_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_TUFF_STAIRS);
            this.registerBlock(BlockItemIds.POLISHED_TUFF_WALL);
            this.registerBlock(BlockItemIds.TUFF_BRICKS);
            this.registerBlock(BlockItemIds.TUFF_BRICK_SLAB);
            this.registerBlock(BlockItemIds.TUFF_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.TUFF_BRICK_WALL);
            this.registerBlock(BlockItemIds.CHISELED_TUFF_BRICKS);
            this.registerBlock(BlockItemIds.DRIPSTONE_BLOCK);
            this.registerBlock(BlockItemIds.GRASS_BLOCK);
            this.registerBlock(BlockItemIds.DIRT);
            this.registerBlock(BlockItemIds.COARSE_DIRT);
            this.registerBlock(BlockItemIds.PODZOL);
            this.registerBlock(BlockItemIds.ROOTED_DIRT);
            this.registerBlock(BlockItemIds.MUD);
            this.registerBlock(BlockItemIds.CRIMSON_NYLIUM);
            this.registerBlock(BlockItemIds.WARPED_NYLIUM);
            this.registerBlock(BlockItemIds.COBBLESTONE);
            this.registerBlock(BlockItemIds.BEDROCK);
            this.registerBlock(BlockItemIds.SAND);
            this.registerBlock(BlockItemIds.SUSPICIOUS_SAND);
            this.registerBlock(BlockItemIds.SUSPICIOUS_GRAVEL);
            this.registerBlock(BlockItemIds.RED_SAND);
            this.registerBlock(BlockItemIds.GRAVEL);
            this.registerBlock(BlockItemIds.COAL_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_COAL_ORE);
            this.registerBlock(BlockItemIds.IRON_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_IRON_ORE);
            this.registerBlock(BlockItemIds.COPPER_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_COPPER_ORE);
            this.registerBlock(BlockItemIds.GOLD_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_GOLD_ORE);
            this.registerBlock(BlockItemIds.REDSTONE_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_REDSTONE_ORE);
            this.registerBlock(BlockItemIds.EMERALD_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_EMERALD_ORE);
            this.registerBlock(BlockItemIds.LAPIS_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_LAPIS_ORE);
            this.registerBlock(BlockItemIds.DIAMOND_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_DIAMOND_ORE);
            this.registerBlock(BlockItemIds.NETHER_GOLD_ORE);
            this.registerBlock(BlockItemIds.NETHER_QUARTZ_ORE);
            this.registerBlock(BlockItemIds.RAW_IRON_BLOCK);
            this.registerBlock(BlockItemIds.RAW_COPPER_BLOCK);
            this.registerBlock(BlockItemIds.RAW_GOLD_BLOCK);
            this.registerable.register(BlockItemIds.HEAVY_CORE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.HEAVY_CORE.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.HEAVY_CORE.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.AMETHYST_BLOCK);
            this.registerBlock(BlockItemIds.BUDDING_AMETHYST);
            this.registerBlock(BlockItemIds.IRON_BLOCK);
            this.registerBlock(BlockItemIds.GOLD_BLOCK);
            this.registerBlock(BlockItemIds.DIAMOND_BLOCK);
            this.registerBlock(BlockItemIds.MUDDY_MANGROVE_ROOTS);
            this.registerBlock(BlockItemIds.SPONGE);
            this.registerBlock(BlockItemIds.WET_SPONGE);
            this.registerBlock(BlockItemIds.LAPIS_BLOCK);
            this.registerBlock(BlockItemIds.SANDSTONE);
            this.registerBlock(BlockItemIds.CHISELED_SANDSTONE);
            this.registerBlock(BlockItemIds.CUT_SANDSTONE);
            this.registerBlock(BlockItemIds.COBWEB);
            this.registerBlock(BlockItemIds.STONE_SLAB);
            this.registerBlock(BlockItemIds.SMOOTH_STONE_SLAB);
            this.registerBlock(BlockItemIds.SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.CUT_SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.PETRIFIED_OAK_SLAB);
            this.registerBlock(BlockItemIds.COBBLESTONE_SLAB);
            this.registerBlock(BlockItemIds.BRICK_SLAB);
            this.registerBlock(BlockItemIds.STONE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.MUD_BRICK_SLAB);
            this.registerBlock(BlockItemIds.NETHER_BRICK_SLAB);
            this.registerBlock(BlockItemIds.QUARTZ_SLAB);
            this.registerBlock(BlockItemIds.RED_SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.CUT_RED_SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.PURPUR_SLAB);
            this.registerBlock(BlockItemIds.PRISMARINE_SLAB);
            this.registerBlock(BlockItemIds.PRISMARINE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.DARK_PRISMARINE_SLAB);
            this.registerBlock(BlockItemIds.SMOOTH_QUARTZ);
            this.registerBlock(BlockItemIds.SMOOTH_RED_SANDSTONE);
            this.registerBlock(BlockItemIds.SMOOTH_SANDSTONE);
            this.registerBlock(BlockItemIds.SMOOTH_STONE);
            this.registerBlock(BlockItemIds.BRICKS);
            this.registerBlock(BlockItemIds.DECORATED_POT);
            this.registerBlock(BlockItemIds.MOSSY_COBBLESTONE);
            this.registerBlock(BlockItemIds.OBSIDIAN);
            this.registerBlock(BlockItemIds.END_ROD);
            this.registerBlock(BlockItemIds.CHORUS_PLANT);
            this.registerBlock(BlockItemIds.CHORUS_FLOWER);
            this.registerBlock(BlockItemIds.PURPUR_BLOCK);
            this.registerBlock(BlockItemIds.PURPUR_PILLAR);
            this.registerBlock(BlockItemIds.PURPUR_STAIRS);
            this.registerBlock(BlockItemIds.SPAWNER);
            this.registerBlock(BlockItemIds.CREAKING_HEART);
            this.registerBlock(BlockItemIds.FARMLAND);
            this.registerBlock(BlockItemIds.FURNACE);
            this.registerBlock(BlockItemIds.COBBLESTONE_STAIRS);
            this.registerBlock(BlockItemIds.SNOW);
            this.registerBlock(BlockItemIds.ICE);
            this.registerBlock(BlockItemIds.SNOW_BLOCK);
            this.registerBlock(BlockItemIds.CLAY);
            this.registerBlock(BlockItemIds.JACK_O_LANTERN);
            this.registerBlock(BlockItemIds.NETHERRACK);
            this.registerBlock(BlockItemIds.SOUL_SAND);
            this.registerBlock(BlockItemIds.SOUL_SOIL);
            this.registerBlock(BlockItemIds.BASALT);
            this.registerBlock(BlockItemIds.POLISHED_BASALT);
            this.registerBlock(BlockItemIds.SMOOTH_BASALT);
            this.registerable.register(BlockItemIds.GLOWSTONE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.GLOWSTONE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.GLOWSTONE.block())))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.CHARGE_RESPAWN_ANCHOR)))
                    .build()
            ));
            this.registerBlock(BlockItemIds.INFESTED_STONE);
            this.registerBlock(BlockItemIds.INFESTED_COBBLESTONE);
            this.registerBlock(BlockItemIds.INFESTED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_MOSSY_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_CRACKED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_CHISELED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_DEEPSLATE);
            this.registerBlock(BlockItemIds.STONE_BRICKS);
            this.registerBlock(BlockItemIds.MOSSY_STONE_BRICKS);
            this.registerBlock(BlockItemIds.CRACKED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.CHISELED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.PACKED_MUD);
            this.registerBlock(BlockItemIds.MUD_BRICKS);
            this.registerBlock(BlockItemIds.DEEPSLATE_BRICKS);
            this.registerBlock(BlockItemIds.CRACKED_DEEPSLATE_BRICKS);
            this.registerBlock(BlockItemIds.DEEPSLATE_TILES);
            this.registerBlock(BlockItemIds.CRACKED_DEEPSLATE_TILES);
            this.registerBlock(BlockItemIds.CHISELED_DEEPSLATE);
            this.registerBlock(BlockItemIds.REINFORCED_DEEPSLATE);
            this.registerBlock(BlockItemIds.IRON_BARS);
            this.registerBlock(BlockItemIds.IRON_CHAIN);
            this.registerBlock(BlockItemIds.BRICK_STAIRS);
            this.registerBlock(BlockItemIds.STONE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.MUD_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.MYCELIUM);
            this.registerBlock(BlockItemIds.NETHER_BRICKS);
            this.registerBlock(BlockItemIds.CRACKED_NETHER_BRICKS);
            this.registerBlock(BlockItemIds.CHISELED_NETHER_BRICKS);
            this.registerBlock(BlockItemIds.NETHER_BRICK_FENCE);
            this.registerBlock(BlockItemIds.NETHER_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.SCULK);
            this.registerBlock(BlockItemIds.SCULK_VEIN);
            this.registerBlock(BlockItemIds.SCULK_CATALYST);
            this.registerBlock(BlockItemIds.SCULK_SHRIEKER);
            this.registerBlock(BlockItemIds.ENCHANTING_TABLE);
            this.registerBlock(BlockItemIds.END_PORTAL_FRAME);
            this.registerBlock(BlockItemIds.END_STONE);
            this.registerBlock(BlockItemIds.END_STONE_BRICKS);
            this.registerable.register(BlockItemIds.DRAGON_EGG.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DRAGON_EGG.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.DRAGON_EGG.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.SANDSTONE_STAIRS);
            this.registerBlock(BlockItemIds.ENDER_CHEST);
            this.registerBlock(BlockItemIds.EMERALD_BLOCK);
            this.registerable.register(BlockItemIds.BEACON.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BEACON.item())
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BEACON.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.COBBLESTONE_WALL);
            this.registerBlock(BlockItemIds.MOSSY_COBBLESTONE_WALL);
            this.registerBlock(BlockItemIds.BRICK_WALL);
            this.registerBlock(BlockItemIds.PRISMARINE_WALL);
            this.registerBlock(BlockItemIds.RED_SANDSTONE_WALL);
            this.registerBlock(BlockItemIds.MOSSY_STONE_BRICK_WALL);
            this.registerBlock(BlockItemIds.GRANITE_WALL);
            this.registerBlock(BlockItemIds.STONE_BRICK_WALL);
            this.registerBlock(BlockItemIds.MUD_BRICK_WALL);
            this.registerBlock(BlockItemIds.NETHER_BRICK_WALL);
            this.registerBlock(BlockItemIds.ANDESITE_WALL);
            this.registerBlock(BlockItemIds.RED_NETHER_BRICK_WALL);
            this.registerBlock(BlockItemIds.SANDSTONE_WALL);
            this.registerBlock(BlockItemIds.END_STONE_BRICK_WALL);
            this.registerBlock(BlockItemIds.DIORITE_WALL);
            this.registerBlock(BlockItemIds.BLACKSTONE_WALL);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_WALL);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BRICK_WALL);
            this.registerBlock(BlockItemIds.COBBLED_DEEPSLATE_WALL);
            this.registerBlock(BlockItemIds.POLISHED_DEEPSLATE_WALL);
            this.registerBlock(BlockItemIds.DEEPSLATE_BRICK_WALL);
            this.registerBlock(BlockItemIds.DEEPSLATE_TILE_WALL);
            this.registerBlock(BlockItemIds.ANVIL);
            this.registerBlock(BlockItemIds.CHIPPED_ANVIL);
            this.registerBlock(BlockItemIds.DAMAGED_ANVIL);
            this.registerBlock(BlockItemIds.CHISELED_QUARTZ_BLOCK);
            this.registerBlock(BlockItemIds.QUARTZ_BLOCK);
            this.registerBlock(BlockItemIds.QUARTZ_BRICKS);
            this.registerBlock(BlockItemIds.QUARTZ_PILLAR);
            this.registerBlock(BlockItemIds.QUARTZ_STAIRS);
            this.registerable.register(BlockItemIds.BARRIER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BARRIER.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BARRIER.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LIGHT.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LIGHT.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LIGHT.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.PACKED_ICE);
            this.registerBlock(BlockItemIds.DIRT_PATH);
            this.registerBlock(BlockItemIds.PRISMARINE);
            this.registerBlock(BlockItemIds.PRISMARINE_BRICKS);
            this.registerBlock(BlockItemIds.DARK_PRISMARINE);
            this.registerBlock(BlockItemIds.PRISMARINE_STAIRS);
            this.registerBlock(BlockItemIds.PRISMARINE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.DARK_PRISMARINE_STAIRS);
            this.registerBlock(BlockItemIds.SEA_LANTERN);
            this.registerBlock(BlockItemIds.RED_SANDSTONE);
            this.registerBlock(BlockItemIds.CHISELED_RED_SANDSTONE);
            this.registerBlock(BlockItemIds.CUT_RED_SANDSTONE);
            this.registerBlock(BlockItemIds.RED_SANDSTONE_STAIRS);
            this.registerBlock(BlockItemIds.MAGMA_BLOCK);
            this.registerBlock(BlockItemIds.RED_NETHER_BRICKS);
            this.registerBlock(BlockItemIds.BONE_BLOCK);
            this.registerable.register(BlockItemIds.STRUCTURE_VOID.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.STRUCTURE_VOID.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.STRUCTURE_VOID.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.TURTLE_EGG);
            this.registerable.register(BlockItemIds.SNIFFER_EGG.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SNIFFER_EGG.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SNIFFER_EGG.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.DRIED_GHAST);
            this.registerBlock(BlockItemIds.BLUE_ICE);
            this.registerable.register(BlockItemIds.CONDUIT.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CONDUIT.item())
                    .rarity(Rarity.RARE)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CONDUIT.block())))
                    .build()
            ));
            this.registerBlock(BlockItemIds.POLISHED_GRANITE_STAIRS);
            this.registerBlock(BlockItemIds.SMOOTH_RED_SANDSTONE_STAIRS);
            this.registerBlock(BlockItemIds.MOSSY_STONE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.POLISHED_DIORITE_STAIRS);
            this.registerBlock(BlockItemIds.MOSSY_COBBLESTONE_STAIRS);
            this.registerBlock(BlockItemIds.END_STONE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.STONE_STAIRS);
            this.registerBlock(BlockItemIds.SMOOTH_SANDSTONE_STAIRS);
            this.registerBlock(BlockItemIds.SMOOTH_QUARTZ_STAIRS);
            this.registerBlock(BlockItemIds.GRANITE_STAIRS);
            this.registerBlock(BlockItemIds.ANDESITE_STAIRS);
            this.registerBlock(BlockItemIds.RED_NETHER_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.POLISHED_ANDESITE_STAIRS);
            this.registerBlock(BlockItemIds.DIORITE_STAIRS);
            this.registerBlock(BlockItemIds.COBBLED_DEEPSLATE_STAIRS);
            this.registerBlock(BlockItemIds.POLISHED_DEEPSLATE_STAIRS);
            this.registerBlock(BlockItemIds.DEEPSLATE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.DEEPSLATE_TILE_STAIRS);
            this.registerBlock(BlockItemIds.POLISHED_GRANITE_SLAB);
            this.registerBlock(BlockItemIds.SMOOTH_RED_SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.MOSSY_STONE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_DIORITE_SLAB);
            this.registerBlock(BlockItemIds.MOSSY_COBBLESTONE_SLAB);
            this.registerBlock(BlockItemIds.END_STONE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.SMOOTH_SANDSTONE_SLAB);
            this.registerBlock(BlockItemIds.SMOOTH_QUARTZ_SLAB);
            this.registerBlock(BlockItemIds.GRANITE_SLAB);
            this.registerBlock(BlockItemIds.ANDESITE_SLAB);
            this.registerBlock(BlockItemIds.RED_NETHER_BRICK_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_ANDESITE_SLAB);
            this.registerBlock(BlockItemIds.DIORITE_SLAB);
            this.registerBlock(BlockItemIds.COBBLED_DEEPSLATE_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_DEEPSLATE_SLAB);
            this.registerBlock(BlockItemIds.DEEPSLATE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.DEEPSLATE_TILE_SLAB);
            this.registerBlock(BlockItemIds.REDSTONE_BLOCK);
            this.registerBlock(BlockItemIds.REPEATER);
            this.registerBlock(BlockItemIds.COMPARATOR);
            this.registerBlock(BlockItemIds.PISTON);
            this.registerBlock(BlockItemIds.STICKY_PISTON);
            this.registerBlock(BlockItemIds.SLIME_BLOCK);
            this.registerBlock(BlockItemIds.HONEY_BLOCK);
            this.registerBlock(BlockItemIds.OBSERVER);
            this.registerBlock(BlockItemIds.HOPPER);
            this.registerBlock(BlockItemIds.DISPENSER);
            this.registerBlock(BlockItemIds.DROPPER);
            this.registerBlock(BlockItemIds.TARGET);
            this.registerBlock(BlockItemIds.LEVER);
            this.registerBlock(BlockItemIds.SCULK_SENSOR);
            this.registerBlock(BlockItemIds.CALIBRATED_SCULK_SENSOR);
            this.registerBlock(BlockItemIds.TRIPWIRE_HOOK);
            this.registerable.register(BlockItemIds.TNT.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TNT.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TNT.block())))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_TNT)))
                    .build()
            ));
            this.registerBlock(BlockItemIds.REDSTONE_LAMP);
            this.registerBlock(BlockItemIds.STONE_BUTTON);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BUTTON);
            this.registerBlock(BlockItemIds.STONE_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.HEAVY_WEIGHTED_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.IRON_DOOR);
            this.registerBlock(BlockItemIds.IRON_TRAPDOOR);
            this.registerBlock(BlockItemIds.POWERED_RAIL);
            this.registerBlock(BlockItemIds.DETECTOR_RAIL);
            this.registerBlock(BlockItemIds.RAIL);
            this.registerBlock(BlockItemIds.ACTIVATOR_RAIL);
            this.registerBlock(BlockItemIds.BREWING_STAND);
            this.registerBlock(BlockItemIds.CAULDRON);
            this.registerBlock(BlockItemIds.FLOWER_POT);
            this.registerBlock(BlockItemIds.SMOKER);
            this.registerBlock(BlockItemIds.BLAST_FURNACE);
            this.registerBlock(BlockItemIds.GRINDSTONE);
            this.registerBlock(BlockItemIds.STONECUTTER);
            this.registerBlock(BlockItemIds.BELL);
            this.registerBlock(BlockItemIds.LANTERN);
            this.registerBlock(BlockItemIds.SOUL_LANTERN);
            this.registerBlock(BlockItemIds.CAMPFIRE);
            this.registerBlock(BlockItemIds.SOUL_CAMPFIRE);
            this.registerBlock(BlockItemIds.BEE_NEST);
            this.registerBlock(BlockItemIds.BEEHIVE);
            this.registerBlock(BlockItemIds.HONEYCOMB_BLOCK);
            this.registerBlock(BlockItemIds.LODESTONE);
            this.registerBlock(BlockItemIds.CRYING_OBSIDIAN);
            this.registerBlock(BlockItemIds.BLACKSTONE);
            this.registerBlock(BlockItemIds.BLACKSTONE_SLAB);
            this.registerBlock(BlockItemIds.BLACKSTONE_STAIRS);
            this.registerBlock(BlockItemIds.GILDED_BLACKSTONE);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_STAIRS);
            this.registerBlock(BlockItemIds.CHISELED_POLISHED_BLACKSTONE);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BRICKS);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BRICK_SLAB);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.CRACKED_POLISHED_BLACKSTONE_BRICKS);
            this.registerBlock(BlockItemIds.RESPAWN_ANCHOR);
            this.registerBlock(BlockItemIds.SMALL_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.MEDIUM_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.LARGE_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.AMETHYST_CLUSTER);
            this.registerBlock(BlockItemIds.POINTED_DRIPSTONE);
            this.registerBlock(BlockItemIds.OCHRE_FROGLIGHT);
            this.registerBlock(BlockItemIds.VERDANT_FROGLIGHT);
            this.registerBlock(BlockItemIds.PEARLESCENT_FROGLIGHT);
            this.registerable.register(BlockItemIds.FROGSPAWN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FROGSPAWN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FROGSPAWN.block()), BlockItemBehavior.Pass.FLUID))
                    .build()
            ));
            this.registerBlock(BlockItemIds.TRIAL_SPAWNER);
            this.registerBlock(BlockItemIds.VAULT);
            this.registerBlock(BlockItemIds.CRAFTER);
            this.registerBlock(BlockItemIds.RESIN_BLOCK);
            this.registerBlock(BlockItemIds.RESIN_BRICKS);
            this.registerBlock(BlockItemIds.RESIN_BRICK_STAIRS);
            this.registerBlock(BlockItemIds.RESIN_BRICK_SLAB);
            this.registerBlock(BlockItemIds.RESIN_BRICK_WALL);
            this.registerBlock(BlockItemIds.CHISELED_RESIN_BRICKS);
            this.registerBlock(BlockItemIds.TEST_BLOCK);
            this.registerBlock(BlockItemIds.TEST_INSTANCE_BLOCK);
            this.registerBlock(BlockItemIds.GOLDEN_DANDELION);
        }

        private void bootstrapItemNameBlocks() {
            this.registerable.register(BlockItemIds.TRIPWIRE.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.TRIPWIRE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TRIPWIRE.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.RESIN_CLUMP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.RESIN_CLUMP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.RESIN_CLUMP.block())))
                    .build()
            ));
        }

        private void bootstrapOperatorOnlyBlocks() {
            this.registerable.register(BlockItemIds.COMMAND_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.COMMAND_BLOCK.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockItemIds.COMMAND_BLOCK.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.REPEATING_COMMAND_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.REPEATING_COMMAND_BLOCK.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockItemIds.REPEATING_COMMAND_BLOCK.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CHAIN_COMMAND_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CHAIN_COMMAND_BLOCK.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockItemIds.CHAIN_COMMAND_BLOCK.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.STRUCTURE_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.STRUCTURE_BLOCK.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockItemIds.STRUCTURE_BLOCK.block())))
                    .build()
            ));
            this.registerable.register(BlockItemIds.JIGSAW.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.JIGSAW.item())
                    .rarity(Rarity.EPIC)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.operator(this.blocks.getOrThrow(BlockItemIds.JIGSAW.block())))
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
                        .rule(Tool.Rule.minesAndDrops(HolderSet.direct(this.blocks.getOrThrow(BlockItemIds.COBWEB.block())), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.LEAVES), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.WOOL), 5.0f))
                        .rule(Tool.Rule.overrideSpeed(HolderSet.direct(this.blocks.getOrThrow(BlockItemIds.VINE.block()), this.blocks.getOrThrow(BlockItemIds.GLOW_LICHEN.block())), 2.0f))
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
            this.registerable.register(ItemIds.SULFUR_CUBE_SPAWN_EGG, create(
                ItemDisplay.Builder.forItem(ItemIds.SULFUR_CUBE_SPAWN_EGG).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EntityItemBehavior.spawnEgg(this.entityTypes.getOrThrow(EntityTypeIds.SULFUR_CUBE), this.dispenseBehaviors))
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
            this.registerable.register(BlockItemIds.AZALEA_LEAVES.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.AZALEA_LEAVES.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.AZALEA_LEAVES.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SHORT_GRASS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SHORT_GRASS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SHORT_GRASS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.KELP.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.KELP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.KELP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.MOSS_CARPET.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.MOSS_CARPET.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MOSS_CARPET.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PALE_MOSS_CARPET.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PALE_MOSS_CARPET.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PALE_MOSS_CARPET.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PINK_PETALS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PINK_PETALS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PINK_PETALS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.HANGING_ROOTS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.HANGING_ROOTS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.HANGING_ROOTS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SMALL_DRIPLEAF.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SMALL_DRIPLEAF.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SMALL_DRIPLEAF.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.WHEAT_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.WHEAT_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WHEAT_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PUMPKIN_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.PUMPKIN_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PUMPKIN_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.MELON_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.MELON_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MELON_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.TORCHFLOWER_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.TORCHFLOWER_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TORCHFLOWER_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PITCHER_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.PITCHER_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PITCHER_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BEETROOT_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.BEETROOT_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BEETROOT_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.MANGROVE_ROOTS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.MANGROVE_ROOTS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MANGROVE_ROOTS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SEAGRASS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SEAGRASS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SEAGRASS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PALE_HANGING_MOSS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PALE_HANGING_MOSS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PALE_HANGING_MOSS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.WILDFLOWERS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WILDFLOWERS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WILDFLOWERS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BUSH.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BUSH.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BUSH.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.FIREFLY_BUSH.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FIREFLY_BUSH.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FIREFLY_BUSH.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CACTUS_FLOWER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CACTUS_FLOWER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CACTUS_FLOWER.block())))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.FLOWERING_AZALEA_LEAVES.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FLOWERING_AZALEA_LEAVES.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FLOWERING_AZALEA_LEAVES.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.NETHER_SPROUTS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.NETHER_SPROUTS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.NETHER_SPROUTS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.WEEPING_VINES.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WEEPING_VINES.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WEEPING_VINES.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.TWISTING_VINES.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TWISTING_VINES.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TWISTING_VINES.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SUGAR_CANE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SUGAR_CANE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SUGAR_CANE.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.VINE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.VINE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.VINE.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.GLOW_LICHEN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.GLOW_LICHEN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.GLOW_LICHEN.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.TALL_GRASS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TALL_GRASS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TALL_GRASS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CACTUS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CACTUS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CACTUS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CACTUS)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.DRIED_KELP_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DRIED_KELP_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.DRIED_KELP_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                    .with(FuelItemBehavior.of(FuelTimes.DRIED_KELP_BLOCK))
                    .build()
            ));
            this.registerable.register(BlockItemIds.FERN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FERN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FERN.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_FERN)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.LILY_PAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LILY_PAD.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LILY_PAD.block()), BlockItemBehavior.Pass.FLUID))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.NETHER_WART.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.NETHER_WART.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.NETHER_WART.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.COCOA_CROP.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.COCOA_CROP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.COCOA_CROP.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BIG_DRIPLEAF.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BIG_DRIPLEAF.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BIG_DRIPLEAF.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PUMPKIN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PUMPKIN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PUMPKIN.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CARVED_PUMPKIN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CARVED_PUMPKIN.item()).build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CARVED_PUMPKIN.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .with(EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.HEAD)
                        .setSwappable(false)
                        .setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur"))
                        .build()))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_CARVED_PUMPKIN)))
                    .build()
            ));
            this.registerable.register(BlockItemIds.MELON.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.MELON.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MELON.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SEA_PICKLE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SEA_PICKLE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SEA_PICKLE.block())))
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
            this.registerable.register(BlockItemIds.DANDELION.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DANDELION.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.DANDELION.block())))
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
            this.registerable.register(BlockItemIds.OPEN_EYEBLOSSOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.OPEN_EYEBLOSSOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.OPEN_EYEBLOSSOM.block())))
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
            this.registerable.register(BlockItemIds.CLOSED_EYEBLOSSOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CLOSED_EYEBLOSSOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CLOSED_EYEBLOSSOM.block())))
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
            this.registerable.register(BlockItemIds.POPPY.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.POPPY.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.POPPY.block())))
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
            this.registerable.register(BlockItemIds.BLUE_ORCHID.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BLUE_ORCHID.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BLUE_ORCHID.block())))
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
            this.registerable.register(BlockItemIds.ALLIUM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.ALLIUM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.ALLIUM.block())))
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
            this.registerable.register(BlockItemIds.AZURE_BLUET.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.AZURE_BLUET.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.AZURE_BLUET.block())))
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
            this.registerable.register(BlockItemIds.RED_TULIP.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.RED_TULIP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.RED_TULIP.block())))
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
            this.registerable.register(BlockItemIds.ORANGE_TULIP.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.ORANGE_TULIP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.ORANGE_TULIP.block())))
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
            this.registerable.register(BlockItemIds.WHITE_TULIP.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WHITE_TULIP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WHITE_TULIP.block())))
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
            this.registerable.register(BlockItemIds.PINK_TULIP.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PINK_TULIP.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PINK_TULIP.block())))
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
            this.registerable.register(BlockItemIds.OXEYE_DAISY.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.OXEYE_DAISY.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.OXEYE_DAISY.block())))
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
            this.registerable.register(BlockItemIds.CORNFLOWER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CORNFLOWER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CORNFLOWER.block())))
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
            this.registerable.register(BlockItemIds.LILY_OF_THE_VALLEY.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LILY_OF_THE_VALLEY.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LILY_OF_THE_VALLEY.block())))
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
            this.registerable.register(BlockItemIds.WITHER_ROSE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WITHER_ROSE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WITHER_ROSE.block())))
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
            this.registerable.register(BlockItemIds.AZALEA.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.AZALEA.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.AZALEA.block())))
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
            this.registerable.register(BlockItemIds.SUNFLOWER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SUNFLOWER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SUNFLOWER.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LILAC.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LILAC.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LILAC.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.ROSE_BUSH.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.ROSE_BUSH.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.ROSE_BUSH.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PEONY.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PEONY.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PEONY.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LARGE_FERN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LARGE_FERN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LARGE_FERN.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SPORE_BLOSSOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SPORE_BLOSSOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SPORE_BLOSSOM.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BROWN_MUSHROOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BROWN_MUSHROOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BROWN_MUSHROOM.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_BROWN_MUSHROOM)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.RED_MUSHROOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.RED_MUSHROOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.RED_MUSHROOM.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_RED_MUSHROOM)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.CRIMSON_ROOTS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CRIMSON_ROOTS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CRIMSON_ROOTS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_CRIMSON_ROOTS)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.WARPED_ROOTS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WARPED_ROOTS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WARPED_ROOTS.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_WARPED_ROOTS)
                    )
                    .build()
            ));
            this.registerable.register(BlockItemIds.MOSS_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.MOSS_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MOSS_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PALE_MOSS_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PALE_MOSS_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PALE_MOSS_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.MUSHROOM_STEM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.MUSHROOM_STEM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.MUSHROOM_STEM.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SHROOMLIGHT.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SHROOMLIGHT.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SHROOMLIGHT.block())))
                    .with(CompostableItemBehavior.of(CompostChances.BIG))
                    .build()
            ));
            this.registerable.register(BlockItemIds.NETHER_WART_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.NETHER_WART_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.NETHER_WART_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.WARPED_WART_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WARPED_WART_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.WARPED_WART_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.HAY_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.HAY_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.HAY_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.FLOWERING_AZALEA.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FLOWERING_AZALEA.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FLOWERING_AZALEA.block())))
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
            this.registerable.register(BlockItemIds.TORCHFLOWER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TORCHFLOWER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TORCHFLOWER.block())))
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
            this.registerable.register(BlockItemIds.PITCHER_PLANT.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PITCHER_PLANT.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.PITCHER_PLANT.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BROWN_MUSHROOM_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BROWN_MUSHROOM_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BROWN_MUSHROOM_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.RED_MUSHROOM_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.RED_MUSHROOM_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.RED_MUSHROOM_BLOCK.block())))
                    .with(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CAKE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CAKE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(1))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CAKE.block())))
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
            ColorCollection.zipApply(ItemIds.HARNESS, ColorCollection.VALUES, (harness, dyeColor) -> this.registerable.register(
                harness,
                create(
                    ItemDisplay.Builder.forItem(harness).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(1))
                        .with(
                            EquipmentItemBehavior.ofHarness(
                                dyeColor,
                                this.soundEvents,
                                this.entityTypes,
                                this.dispenseBehaviors
                            )
                        )
                        .build()
                )
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
            this.registerable.register(BlockItemIds.SKELETON_SKULL.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SKELETON_SKULL.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.SKELETON_SKULL.block()),
                        this.blocks.getOrThrow(BlockIds.SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.WITHER_SKELETON_SKULL.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.WITHER_SKELETON_SKULL.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.WITHER_SKELETON_SKULL.block()),
                        this.blocks.getOrThrow(BlockIds.WITHER_SKELETON_WALL_SKULL),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PLAYER_HEAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PLAYER_HEAD.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.PLAYER_HEAD.block()),
                        this.blocks.getOrThrow(BlockIds.PLAYER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.ZOMBIE_HEAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.ZOMBIE_HEAD.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.ZOMBIE_HEAD.block()),
                        this.blocks.getOrThrow(BlockIds.ZOMBIE_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CREEPER_HEAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CREEPER_HEAD.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.CREEPER_HEAD.block()),
                        this.blocks.getOrThrow(BlockIds.CREEPER_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.DRAGON_HEAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DRAGON_HEAD.item())
                    .rarity(Rarity.RARE)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.DRAGON_HEAD.block()),
                        this.blocks.getOrThrow(BlockIds.DRAGON_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
            this.registerable.register(BlockItemIds.PIGLIN_HEAD.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.PIGLIN_HEAD.item())
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                AttributeModifiers.hideFromLocatorBar(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(EquipmentItemBehavior.forSkull(
                        this.blocks.getOrThrow(BlockItemIds.PIGLIN_HEAD.block()),
                        this.blocks.getOrThrow(BlockIds.PIGLIN_WALL_HEAD),
                        this.dispenseBehaviors
                    ))
                    .build()
            ));
        }

        private void bootstrapFuel() {
            this.registerable.register(BlockItemIds.COAL_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.COAL_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.COAL_BLOCK.block())))
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
            this.registerable.register(BlockItemIds.BOOKSHELF.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BOOKSHELF.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BOOKSHELF.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CHISELED_BOOKSHELF.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CHISELED_BOOKSHELF.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CHISELED_BOOKSHELF.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LECTERN.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LECTERN.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LECTERN.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CHEST.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CHEST.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CHEST.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_CHEST)))
                    .build()
            ));
            this.registerable.register(BlockItemIds.TRAPPED_CHEST.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TRAPPED_CHEST.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TRAPPED_CHEST.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LADDER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LADDER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LADDER.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CRAFTING_TABLE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CRAFTING_TABLE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CRAFTING_TABLE.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.JUKEBOX.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.JUKEBOX.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.JUKEBOX.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.NOTE_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.NOTE_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.NOTE_BLOCK.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LOOM.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LOOM.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LOOM.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.COMPOSTER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.COMPOSTER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.COMPOSTER.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.BARREL.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.BARREL.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.BARREL.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.CARTOGRAPHY_TABLE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.CARTOGRAPHY_TABLE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CARTOGRAPHY_TABLE.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.FLETCHING_TABLE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.FLETCHING_TABLE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.FLETCHING_TABLE.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SMITHING_TABLE.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SMITHING_TABLE.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SMITHING_TABLE.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.DAYLIGHT_DETECTOR.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DAYLIGHT_DETECTOR.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.DAYLIGHT_DETECTOR.block())))
                    .with(FuelItemBehavior.of(FuelTimes.WOOD))
                    .build()
            ));
            this.registerable.register(BlockItemIds.DEAD_BUSH.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.DEAD_BUSH.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.DEAD_BUSH.block())))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .build(),
                ActionEventMap.Builder.item()
                    .addCancellable(
                        ItemEvent.BEFORE_USE_ON_BLOCK,
                        Actions.potBlock(this.blocks, BlockIds.POTTED_DEAD_BUSH)
                    )
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
            this.registerable.register(BlockItemIds.SCAFFOLDING.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SCAFFOLDING.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SCAFFOLDING.block())))
                    .with(FuelItemBehavior.of(FuelTimes.SCAFFOLDING))
                    .build()
            ));
            this.registerable.register(BlockItemIds.SHORT_DRY_GRASS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.SHORT_DRY_GRASS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SHORT_DRY_GRASS.block())))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.TALL_DRY_GRASS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.TALL_DRY_GRASS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TALL_DRY_GRASS.block())))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
                    .build()
            ));
            this.registerable.register(BlockItemIds.LEAF_LITTER.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.LEAF_LITTER.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.LEAF_LITTER.block())))
                    .with(FuelItemBehavior.of(FuelTimes.PLANT))
                    .with(CompostableItemBehavior.of(CompostChances.SMALL))
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
                                    .of(this.blocks, this.blocks.getOrThrow(BlockItemIds.END_PORTAL_FRAME.block()).value())
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
            this.registerable.register(ItemIds.MUSIC_DISC_BOUNCE, create(
                ItemDisplay.Builder.forItem(ItemIds.MUSIC_DISC_BOUNCE)
                    .rarity(Rarity.UNCOMMON)
                    .build(),
                ItemBehaviorSet.builder()
                    .with(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(JukeboxSongs.BOUNCE)))
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
                    .with(
                        BucketItemBehavior.placeFluid(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.LAVA_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.LAVA_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluid(
                            this.fluids.getOrThrow(FluidIds.LAVA),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_LAVA),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .with(FuelItemBehavior.of(FuelTimes.LAVA, this.items.getOrThrow(ItemIds.BUCKET)))
                    .build()
            ));
            this.registerable.register(BlockItemIds.POWDER_SNOW.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.POWDER_SNOW.item()).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeBlock(
                            this.blocks.getOrThrow(BlockItemIds.POWDER_SNOW.block()),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_POWDER_SNOW),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.PUFFERFISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.PUFFERFISH_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.PUFFERFISH),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .with(FoodItemBehavior.of(Foods.PUFFERFISH))
                    .build()
            ));
            this.registerable.register(ItemIds.SALMON_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.SALMON_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.SALMON),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .with(FoodItemBehavior.of(Foods.SALMON))
                    .build()
            ));
            this.registerable.register(ItemIds.COD_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.COD_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.COD),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .with(FoodItemBehavior.of(Foods.COD))
                    .build()
            ));
            this.registerable.register(ItemIds.TROPICAL_FISH_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.TROPICAL_FISH_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.TROPICAL_FISH),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_FISH),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .with(FoodItemBehavior.of(Foods.TROPICAL_FISH))
                    .build()
            ));
            this.registerable.register(ItemIds.AXOLOTL_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.AXOLOTL_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.AXOLOTL),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_AXOLOTL),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.TADPOLE_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.TADPOLE_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeFluidWithEntity(
                            this.fluids.getOrThrow(FluidIds.WATER),
                            this.entityTypes.getOrThrow(EntityTypeIds.TADPOLE),
                            this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_TADPOLE),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
                    .build()
            ));
            this.registerable.register(ItemIds.SULFUR_CUBE_BUCKET, create(
                ItemDisplay.Builder.forItem(ItemIds.SULFUR_CUBE_BUCKET).build(),
                ItemBehaviorSet.builder()
                    .with(
                        BucketItemBehavior.placeEntity(
                            this.entityTypes.getOrThrow(EntityTypeIds.SULFUR_CUBE),
                            this.items,
                            this.dispenseBehaviors
                        )
                    )
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
            this.registerable.register(BlockItemIds.ANCIENT_DEBRIS.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.ANCIENT_DEBRIS.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.ANCIENT_DEBRIS.block())))
                    .with(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                    .build()
            ));
            this.registerable.register(BlockItemIds.NETHERITE_BLOCK.item(), create(
                ItemDisplay.Builder.forBlock(BlockItemIds.NETHERITE_BLOCK.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.NETHERITE_BLOCK.block())))
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
            this.registerable.register(BlockItemIds.REDSTONE_DUST.item(), create(
                ItemDisplay.Builder.forItem(BlockItemIds.REDSTONE_DUST.item()).build(),
                ItemBehaviorSet.builder()
                    .with(StackableItemBehavior.of(64))
                    .with(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.REDSTONE_DUST.block())))
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
            this.registerable.register(
                BlockItemIds.AIR.item(),
                create(
                    ItemDisplay.Builder.forBlock(BlockItemIds.AIR.item()).build()
                )
            );
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
                                    .of(this.blocks, this.blocks.getOrThrow(BlockItemIds.LODESTONE.block()).value()))
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

        public void registerBlock(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .build()
                )
            );
        }

        public void registerUnstackableBlock(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(1))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .build()
                )
            );
        }

        public void registerShulkerBox(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(1))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                        .build()
                )
            );
        }

        public void registerBurningWoodBlock(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(FuelItemBehavior.of(FuelTimes.WOOD))
                        .build()
                )
            );
        }

        public void registerBurningSlab(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(FuelItemBehavior.of(FuelTimes.SLAB))
                        .build()
                )
            );
        }

        public Consumer<BlockItemId> registerBurningBlock(int fuelTicks) {
            return blockItem -> this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(FuelItemBehavior.of(fuelTicks))
                        .build()
                )
            );
        }

        public void registerPottableSapling(BlockItemId blockItem, ResourceKey<Block> pottedBlock, int fuelTicks, float compostChance) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(compostChance > 0.0f, CompostableItemBehavior.of(compostChance))
                        .with(fuelTicks > 0, FuelItemBehavior.of(fuelTicks))
                        .build(),
                    ActionEventMap.Builder.item()
                        .addCancellable(
                            ItemEvent.BEFORE_USE_ON_BLOCK,
                            Actions.potBlock(this.blocks, pottedBlock)
                        )
                        .build()
                )
            );
        }

        public void registerCompostableLeaves(BlockItemId blockItem) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())))
                        .with(CompostableItemBehavior.of(CompostChances.SMALL))
                        .build()
                )
            );
        }

        public void registerBlockAttachedToSide(BlockItemId blockItem, ResourceKey<Block> otherBlock, Direction direction) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(Item.DEFAULT_MAX_STACK_SIZE))
                        .with(
                            BlockItemBehavior.attachedToSide(
                                this.blocks.getOrThrow(blockItem.block()),
                                this.blocks.getOrThrow(otherBlock),
                                direction
                            )
                        )
                        .build()
                )
            );
        }

        public void registerSign(BlockItemId blockItem, ResourceKey<Block> wallSign, boolean burns) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(16))
                        .with(
                            BlockItemBehavior.attachedToSide(
                                this.blocks.getOrThrow(blockItem.block()),
                                this.blocks.getOrThrow(wallSign),
                                Direction.DOWN
                            )
                        )
                        .with(burns, FuelItemBehavior.of(FuelTimes.SIGN))
                        .build()
                )
            );
        }

        public void registerHangingSign(BlockItemId blockItem, ResourceKey<Block> hangingWallSign, boolean burns) {
            this.registerable.register(
                blockItem.item(),
                create(
                    ItemDisplay.Builder.forBlock(blockItem.item()).build(),
                    ItemBehaviorSet.builder()
                        .with(StackableItemBehavior.of(16))
                        .with(
                            BlockItemBehavior.attachedToSide(
                                this.blocks.getOrThrow(blockItem.block()),
                                this.blocks.getOrThrow(hangingWallSign),
                                Direction.UP
                            )
                        )
                        .with(burns, FuelItemBehavior.of(FuelTimes.HANGING_SIGN))
                        .build()
                )
            );
        }
    }
}
