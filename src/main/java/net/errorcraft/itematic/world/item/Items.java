package net.errorcraft.itematic.world.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.mixin.world.item.BrushItemAccessor;
import net.errorcraft.itematic.mixin.world.item.BundleItemAccessor;
import net.errorcraft.itematic.mixin.world.item.CrossbowItemAccessor;
import net.errorcraft.itematic.mixin.world.item.MaceItemAccessor;
import net.errorcraft.itematic.mixin.world.item.component.BundleContentsAccessor;
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
import net.errorcraft.itematic.world.entity.spawn.rule.rules.OffsetSpawnPositionEntitySpawnRule;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
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
import net.errorcraft.itematic.world.item.behavior.behaviors.SpawnEggItemBehavior;
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
import net.errorcraft.itematic.world.item.holder.rule.ItemHolderRules;
import net.errorcraft.itematic.world.item.holder.rule.rules.FractionItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.OccupancyHeldItemsWithPenaltyItemHolderRule;
import net.errorcraft.itematic.world.item.holder.rule.rules.RejectItemHolderRule;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplate;
import net.errorcraft.itematic.world.item.smithing.template.SmithingTemplates;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.PlayableUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.ShooterUseDurationProvider;
import net.errorcraft.itematic.world.item.use.duration.provider.providers.TridentUseDurationProvider;
import net.errorcraft.itematic.world.item.weapon.melee.SmashingWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.MeleeWeaponComponents;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.KineticMeleeWeapon;
import net.errorcraft.itematic.world.item.weapon.melee.behavior.component.PiercingMeleeWeapon;
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
import net.minecraft.SharedConstants;
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
import net.minecraft.core.Holder;
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
import net.minecraft.tags.TagKey;
import net.minecraft.util.Util;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.item.SwingAnimationType;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.PotionIds;
import net.minecraft.world.item.component.AttackRange;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.UseEffects;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.ArmorMaterial;
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
import net.minecraft.world.level.block.state.properties.RailShape;
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
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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

    private static Item create(ItemDisplay display, ItemAttributeModifiers attributeModifiers, ItemBehaviorSet behavior, ActionEventMap<ItemEvent> events) {
        Item item = new Item(new Item.Properties());
        item.itematic$setDisplay(display);
        item.itematic$setAttributeModifiers(attributeModifiers);
        item.itematic$setBehavior(behavior);
        item.itematic$setEvents(events);
        return item;
    }

    public static class Builder {
        private final ResourceKey<Item> item;
        private final BootstrapContext<Item> bootstrapper;
        private final ItemDisplay.Builder display = ItemDisplay.builder();
        private final ItemAttributeModifiers.Builder attributeModifiers = ItemAttributeModifiers.builder();
        private final ItemBehaviorSet.Builder behavior = ItemBehaviorSet.builder();
        private final ActionEventMap.Builder<ItemEvent> events = ItemEvent.mapBuilder();

        private Builder(ResourceKey<Item> item, BootstrapContext<Item> bootstrapper) {
            this.item = item;
            this.bootstrapper = bootstrapper;
        }

        public void register() {
            this.bootstrapper.register(
                this.item,
                create(
                    this.display.build(this.item),
                    this.attributeModifiers.build(),
                    this.behavior.build(),
                    this.events.build()
                )
            );
        }

        public Builder display(UnaryOperator<ItemDisplay.Builder> display) {
            display.apply(this.display);
            return this;
        }

        public Builder attributeModifiers(UnaryOperator<ItemAttributeModifiers.Builder> attributeModifiers) {
            attributeModifiers.apply(this.attributeModifiers);
            return this;
        }

        public Builder behavior(boolean condition, ItemBehavior<?> behavior) {
            if (condition) {
                this.behavior.add(behavior);
            }

            return this;
        }

        public Builder behavior(ItemBehavior<?> behavior) {
            this.behavior.add(behavior);
            return this;
        }

        public Builder event(ItemEvent event, ActionEntry action) {
            this.events.add(event, action);
            return this;
        }

        public Builder event(ItemEvent event, Holder<ActionEntry> action) {
            this.events.add(event, action);
            return this;
        }

        public Builder cancellableEvent(ItemEvent event, ActionEntry action) {
            this.events.addCancellable(event, action);
            return this;
        }

        public Builder apply(UnaryOperator<Builder> builder) {
            return builder.apply(this);
        }

        public Builder apply(UnaryOperator<Builder> builder, boolean condition) {
            if (condition) {
                return this.apply(builder);
            }

            return this;
        }
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

        private Builder builder(ResourceKey<Item> item) {
            return this.builder(item, Item.DEFAULT_MAX_STACK_SIZE);
        }

        private Builder builder(ResourceKey<Item> item, int maxStackSize) {
            return new Builder(item, this.registerable)
                .behavior(StackableItemBehavior.of(maxStackSize));
        }

        public void registerItem(ResourceKey<Item> item) {
            this.builder(item).register();
        }

        public void registerBlock(BlockItemId blockItem) {
            this.builderForBlock(blockItem).register();
        }

        private Builder builderForBlock(BlockItemId blockItem) {
            return this.builderForBlock(blockItem, Item.DEFAULT_MAX_STACK_SIZE);
        }

        private Builder builderForBlock(BlockItemId blockItem, int maxStackSize) {
            return this.builder(blockItem.item(), maxStackSize)
                .display(ItemDisplay.Builder::blockName)
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block())));
        }

        private void registerOperatorBlock(BlockItemId blockItem) {
            this.builder(blockItem.item())
                .display(display -> display.blockName().rarity(Rarity.EPIC))
                .behavior(BlockItemBehavior.operator(this.blocks.getOrThrow(blockItem.block())))
                .register();
        }

        private Builder builderForBlockOnFluid(BlockItemId blockItem) {
            return this.builder(blockItem.item())
                .display(ItemDisplay.Builder::blockName)
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(blockItem.block()), BlockItemBehavior.Pass.FLUID));
        }

        private Builder builderForBlockAttachedToSide(BlockItemId blockItem, ResourceKey<Block> otherBlock, Direction side) {
            return this.builderForBlockAttachedToSide(blockItem, otherBlock, side, Item.DEFAULT_MAX_STACK_SIZE);
        }

        public Builder builderForBlockAttachedToSide(BlockItemId blockItem, ResourceKey<Block> otherBlock, Direction side, int maxStackSize) {
            return this.builder(blockItem.item(), maxStackSize)
                .display(ItemDisplay.Builder::blockName)
                .behavior(
                    BlockItemBehavior.attachedToSide(
                        this.blocks.getOrThrow(blockItem.block()),
                        this.blocks.getOrThrow(otherBlock),
                        side
                    )
                );
        }

        private void bootstrap() {
            new Builder(BlockItemIds.AIR.item(), this.registerable)
                .display(ItemDisplay.Builder::blockName)
                .register();
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
                ItematicBlockItemIds.DARK_OAK,
                this,
                BlockIds.DARK_OAK_WALL_SIGN,
                BlockIds.DARK_OAK_WALL_HANGING_SIGN,
                BlockIds.POTTED_DARK_OAK_SAPLING,
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
            this.registerBlock(BlockItemIds.MUDDY_MANGROVE_ROOTS);
            this.builderForBlock(BlockItemIds.MANGROVE_ROOTS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            WoodCollection.registerItems(
                ItematicBlockItemIds.CHERRY,
                this,
                BlockIds.CHERRY_WALL_SIGN,
                BlockIds.CHERRY_WALL_HANGING_SIGN,
                BlockIds.POTTED_CHERRY_SAPLING,
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
            this.builderForBlock(BlockItemIds.NETHER_WART_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            WoodCollection.registerItems(
                ItematicBlockItemIds.WARPED,
                this,
                BlockIds.WARPED_WALL_SIGN,
                BlockIds.WARPED_WALL_HANGING_SIGN,
                BlockIds.POTTED_WARPED_FUNGUS
            );
            this.builderForBlock(BlockItemIds.WARPED_WART_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            CutoutCollection.registerItems(ItematicBlockItemIds.STONE, this);
            this.registerBlock(BlockItemIds.STONE_BUTTON);
            this.registerBlock(BlockItemIds.STONE_PRESSURE_PLATE);
            CutoutCollection.registerItems(ItematicBlockItemIds.COBBLESTONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.MOSSY_COBBLESTONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.SMOOTH_STONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.STONE_BRICKS, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.MOSSY_STONE_BRICKS, this);
            this.registerBlock(BlockItemIds.CRACKED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.CHISELED_STONE_BRICKS);
            CutoutCollection.registerItems(ItematicBlockItemIds.GRANITE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_GRANITE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.DIORITE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_DIORITE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.ANDESITE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_ANDESITE, this);
            this.registerBlock(BlockItemIds.DEEPSLATE);
            CutoutCollection.registerItems(ItematicBlockItemIds.COBBLED_DEEPSLATE, this);
            this.registerBlock(BlockItemIds.CHISELED_DEEPSLATE);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_DEEPSLATE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.DEEPSLATE_BRICKS, this);
            this.registerBlock(BlockItemIds.CRACKED_DEEPSLATE_BRICKS);
            CutoutCollection.registerItems(ItematicBlockItemIds.DEEPSLATE_TILES, this);
            this.registerBlock(BlockItemIds.CRACKED_DEEPSLATE_TILES);
            this.registerBlock(BlockItemIds.REINFORCED_DEEPSLATE);
            CutoutCollection.registerItems(ItematicBlockItemIds.TUFF, this);
            this.registerBlock(BlockItemIds.CHISELED_TUFF);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_TUFF, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.TUFF_BRICKS, this);
            this.registerBlock(BlockItemIds.CHISELED_TUFF_BRICKS);
            CutoutCollection.registerItems(ItematicBlockItemIds.BRICKS, this);
            this.registerBlock(BlockItemIds.MUD);
            this.registerBlock(BlockItemIds.PACKED_MUD);
            CutoutCollection.registerItems(ItematicBlockItemIds.MUD_BRICK, this);
            this.registerBlock(BlockItemIds.RESIN_BLOCK);
            this.builder(BlockItemIds.RESIN_CLUMP.item())
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.RESIN_CLUMP.block())))
                .register();
            CutoutCollection.registerItems(ItematicBlockItemIds.RESIN_BRICKS, this);
            this.registerBlock(BlockItemIds.CHISELED_RESIN_BRICKS);
            CutoutCollection.registerItems(ItematicBlockItemIds.SANDSTONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.SMOOTH_SANDSTONE, this);
            this.registerBlock(BlockItemIds.CHISELED_SANDSTONE);
            CutoutCollection.registerItems(ItematicBlockItemIds.CUT_SANDSTONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.RED_SANDSTONE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.SMOOTH_RED_SANDSTONE, this);
            this.registerBlock(BlockItemIds.CHISELED_RED_SANDSTONE);
            CutoutCollection.registerItems(ItematicBlockItemIds.CUT_RED_SANDSTONE, this);
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
            CutoutCollection.registerItems(ItematicBlockItemIds.PRISMARINE, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.PRISMARINE_BRICKS, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.DARK_PRISMARINE, this);
            this.registerBlock(BlockItemIds.SEA_LANTERN);
            CoralCollection.registerItems(ItematicBlockItemIds.TUBE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.BRAIN_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.BUBBLE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.FIRE_CORAL, this);
            CoralCollection.registerItems(ItematicBlockItemIds.HORN_CORAL, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.NETHER_BRICKS, this);
            this.registerBlock(BlockItemIds.CRACKED_NETHER_BRICKS);
            this.registerBlock(BlockItemIds.CHISELED_NETHER_BRICKS);
            this.registerBlock(BlockItemIds.NETHER_BRICK_FENCE);
            CutoutCollection.registerItems(ItematicBlockItemIds.RED_NETHER_BRICKS, this);
            CutoutCollection.registerItems(ItematicBlockItemIds.BLACKSTONE, this);
            this.registerBlock(BlockItemIds.GILDED_BLACKSTONE);
            this.registerBlock(BlockItemIds.CHISELED_POLISHED_BLACKSTONE);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_BLACKSTONE, this);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_BUTTON);
            this.registerBlock(BlockItemIds.POLISHED_BLACKSTONE_PRESSURE_PLATE);
            CutoutCollection.registerItems(ItematicBlockItemIds.POLISHED_BLACKSTONE_BRICKS, this);
            this.registerBlock(BlockItemIds.CRACKED_POLISHED_BLACKSTONE_BRICKS);
            this.registerBlock(BlockItemIds.END_STONE);
            CutoutCollection.registerItems(ItematicBlockItemIds.END_STONE_BRICKS, this);
            this.registerBlock(BlockItemIds.END_PORTAL_FRAME);
            this.registerBlock(BlockItemIds.END_ROD);
            CutoutCollection.registerItems(ItematicBlockItemIds.PURPUR, this);
            this.registerBlock(BlockItemIds.PURPUR_PILLAR);
            this.builderForBlock(BlockItemIds.COAL_BLOCK)
                .behavior(FuelItemBehavior.of(FuelTimes.COAL_BLOCK))
                .register();
            this.registerBlock(BlockItemIds.COAL_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_COAL_ORE);
            this.registerBlock(BlockItemIds.IRON_BLOCK);
            this.registerBlock(BlockItemIds.IRON_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_IRON_ORE);
            this.registerBlock(BlockItemIds.RAW_IRON_BLOCK);
            this.registerBlock(BlockItemIds.IRON_BARS);
            this.registerBlock(BlockItemIds.IRON_DOOR);
            this.registerBlock(BlockItemIds.IRON_TRAPDOOR);
            this.registerBlock(BlockItemIds.HEAVY_WEIGHTED_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.IRON_CHAIN);
            this.registerBlock(BlockItemIds.GOLD_BLOCK);
            this.registerBlock(BlockItemIds.GOLD_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_GOLD_ORE);
            this.registerBlock(BlockItemIds.NETHER_GOLD_ORE);
            this.registerBlock(BlockItemIds.RAW_GOLD_BLOCK);
            this.registerBlock(BlockItemIds.LIGHT_WEIGHTED_PRESSURE_PLATE);
            this.registerBlock(BlockItemIds.REDSTONE_BLOCK);
            this.registerBlock(BlockItemIds.REDSTONE_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_REDSTONE_ORE);
            this.registerBlock(BlockItemIds.EMERALD_BLOCK);
            this.registerBlock(BlockItemIds.EMERALD_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_EMERALD_ORE);
            this.registerBlock(BlockItemIds.LAPIS_BLOCK);
            this.registerBlock(BlockItemIds.LAPIS_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_LAPIS_ORE);
            this.registerBlock(BlockItemIds.DIAMOND_BLOCK);
            this.registerBlock(BlockItemIds.DIAMOND_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_DIAMOND_ORE);
            this.builderForBlock(BlockItemIds.NETHERITE_BLOCK)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForBlock(BlockItemIds.ANCIENT_DEBRIS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            CutoutCollection.registerItems(ItematicBlockItemIds.QUARTZ, this);
            this.registerBlock(BlockItemIds.NETHER_QUARTZ_ORE);
            this.registerBlock(BlockItemIds.CHISELED_QUARTZ_BLOCK);
            this.registerBlock(BlockItemIds.QUARTZ_BRICKS);
            this.registerBlock(BlockItemIds.QUARTZ_PILLAR);
            CutoutCollection.registerItems(ItematicBlockItemIds.SMOOTH_QUARTZ, this);
            this.registerBlock(BlockItemIds.AMETHYST_BLOCK);
            this.registerBlock(BlockItemIds.BUDDING_AMETHYST);
            this.registerBlock(BlockItemIds.SMALL_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.MEDIUM_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.LARGE_AMETHYST_BUD);
            this.registerBlock(BlockItemIds.AMETHYST_CLUSTER);
            BlockItemIds.COPPER_BLOCK.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.COPPER_ORE);
            this.registerBlock(BlockItemIds.DEEPSLATE_COPPER_ORE);
            this.registerBlock(BlockItemIds.RAW_COPPER_BLOCK);
            BlockItemIds.CHISELED_COPPER.forEach(this::registerBlock);
            BlockItemIds.COPPER_GRATE.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER_STAIRS.forEach(this::registerBlock);
            BlockItemIds.CUT_COPPER_SLAB.forEach(this::registerBlock);
            BlockItemIds.COPPER_BARS.forEach(this::registerBlock);
            BlockItemIds.COPPER_DOOR.forEach(this::registerBlock);
            BlockItemIds.COPPER_TRAPDOOR.forEach(this::registerBlock);
            BlockItemIds.COPPER_BULB.forEach(this::registerBlock);
            BlockItemIds.COPPER_CHAIN.forEach(this::registerBlock);
            BlockItemIds.COPPER_LANTERN.forEach(this::registerBlock);
            BlockItemIds.COPPER_GOLEM_STATUE.forEach(this::registerBlock);
            BlockItemIds.LIGHTNING_ROD.forEach(this::registerBlock);
            BlockItemIds.WOOL.forEach(wool -> this.builderForBlock(wool)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOL))
                .register()
            );
            ColorCollection.zipApply(BlockItemIds.CARPET, ColorCollection.VALUES, (carpet, dyeColor) -> this.builderForBlock(carpet)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOL_CARPET))
                .behavior(EquipmentItemBehavior.of(Equippable.llamaSwag(dyeColor)))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                .register()
            );
            this.registerBlock(BlockItemIds.TERRACOTTA);
            BlockItemIds.DYED_TERRACOTTA.forEach(this::registerBlock);
            BlockItemIds.GLAZED_TERRACOTTA.forEach(this::registerBlock);
            BlockItemIds.CONCRETE.forEach(this::registerBlock);
            BlockItemIds.CONCRETE_POWDER.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.GLASS);
            this.registerBlock(BlockItemIds.TINTED_GLASS);
            BlockItemIds.STAINED_GLASS.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.GLASS_PANE);
            BlockItemIds.STAINED_GLASS_PANE.forEach(this::registerBlock);
            BlockItemIds.BED.forEach(bed -> this.builderForBlock(bed, 1).register());
            this.registerBlock(BlockItemIds.CANDLE);
            BlockItemIds.DYED_CANDLE.forEach(this::registerBlock);
            ColorCollection.zipApply(
                BlockItemIds.BANNER,
                ColorCollection.VALUES,
                (banner, dyeColor) -> this.builderForBlockAttachedToSide(banner, BlockIds.WALL_BANNER.pick(dyeColor), Direction.DOWN, 16)
                    .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                    .behavior(BannerPatternHolderItemBehavior.of(dyeColor))
                    .register()
            );
            this.registerBlock(BlockItemIds.CALCITE);
            this.registerBlock(BlockItemIds.DRIPSTONE_BLOCK);
            this.registerBlock(BlockItemIds.POINTED_DRIPSTONE);
            this.registerBlock(BlockItemIds.OBSIDIAN);
            this.registerBlock(BlockItemIds.CRYING_OBSIDIAN);
            this.builderForBlock(BlockItemIds.GLOWSTONE)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.CHARGE_RESPAWN_ANCHOR)))
                .register();
            this.registerBlock(BlockItemIds.BASALT);
            this.registerBlock(BlockItemIds.POLISHED_BASALT);
            this.registerBlock(BlockItemIds.SMOOTH_BASALT);
            this.registerBlock(BlockItemIds.SAND);
            this.registerBlock(BlockItemIds.SUSPICIOUS_SAND);
            this.registerBlock(BlockItemIds.RED_SAND);
            this.registerBlock(BlockItemIds.SOUL_SAND);
            this.registerBlock(BlockItemIds.SOUL_SOIL);
            this.registerBlock(BlockItemIds.GRAVEL);
            this.registerBlock(BlockItemIds.SUSPICIOUS_GRAVEL);
            this.registerBlock(BlockItemIds.BEDROCK);
            this.registerBlock(BlockItemIds.SCULK);
            this.registerBlock(BlockItemIds.SCULK_VEIN);
            this.registerBlock(BlockItemIds.SCULK_CATALYST);
            this.registerBlock(BlockItemIds.SCULK_SHRIEKER);
            this.registerBlock(BlockItemIds.INFESTED_STONE);
            this.registerBlock(BlockItemIds.INFESTED_COBBLESTONE);
            this.registerBlock(BlockItemIds.INFESTED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_MOSSY_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_CRACKED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_CHISELED_STONE_BRICKS);
            this.registerBlock(BlockItemIds.INFESTED_DEEPSLATE);
            this.registerBlock(BlockItemIds.GRASS_BLOCK);
            this.registerBlock(BlockItemIds.DIRT);
            this.registerBlock(BlockItemIds.DIRT_PATH);
            this.registerBlock(BlockItemIds.FARMLAND);
            this.registerBlock(BlockItemIds.COARSE_DIRT);
            this.registerBlock(BlockItemIds.PODZOL);
            this.registerBlock(BlockItemIds.ROOTED_DIRT);
            this.registerBlock(BlockItemIds.CLAY);
            this.registerBlock(BlockItemIds.MYCELIUM);
            this.registerBlock(BlockItemIds.CRIMSON_NYLIUM);
            this.registerBlock(BlockItemIds.WARPED_NYLIUM);
            this.registerBlock(BlockItemIds.NETHERRACK);
            this.builderForBlock(BlockItemIds.CRAFTING_TABLE)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.registerBlock(BlockItemIds.CRAFTER);
            this.registerBlock(BlockItemIds.FURNACE);
            this.registerBlock(BlockItemIds.SMOKER);
            this.registerBlock(BlockItemIds.BLAST_FURNACE);
            this.registerBlock(BlockItemIds.CAMPFIRE);
            this.registerBlock(BlockItemIds.SOUL_CAMPFIRE);
            this.builderForBlock(BlockItemIds.CHEST)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_CHEST)))
                .register();
            this.builderForBlock(BlockItemIds.TRAPPED_CHEST)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            BlockItemIds.COPPER_CHEST.forEach(this::registerBlock);
            this.registerBlock(BlockItemIds.ENDER_CHEST);
            this.builderForBlock(BlockItemIds.BARREL)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.registerShulkerBox(BlockItemIds.SHULKER_BOX);
            BlockItemIds.DYED_SHULKER_BOX.forEach(this::registerShulkerBox);
            this.registerItem(ItemIds.SHULKER_SHELL);
            this.registerBlock(BlockItemIds.ANVIL);
            this.registerBlock(BlockItemIds.CHIPPED_ANVIL);
            this.registerBlock(BlockItemIds.DAMAGED_ANVIL);
            this.registerBlock(BlockItemIds.ENCHANTING_TABLE);
            this.builderForBlock(BlockItemIds.BOOKSHELF)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.CHISELED_BOOKSHELF)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.LECTERN)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.LADDER)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.SCAFFOLDING)
                .behavior(FuelItemBehavior.of(FuelTimes.SCAFFOLDING))
                .register();
            this.registerBlock(BlockItemIds.BREWING_STAND);
            this.registerBlock(BlockItemIds.CAULDRON);
            this.builder(ItemIds.GLASS_BOTTLE)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.GLASS_BOTTLE)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
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
                    )
                )
                .register();
            this.builderForConsumable(ItemIds.POTION, Consumables.DEFAULT_DRINK, ItemIds.GLASS_BOTTLE)
                .behavior(PotionHolderItemBehavior.of(1.0f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
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
                    )
                )
                .register();
            this.builderForProjectile(ItemIds.SPLASH_POTION, 1, EntityTypeIds.SPLASH_POTION)
                .behavior(PotionHolderItemBehavior.of(1.0f))
                .behavior(ThrowableItemBehavior.of(0.5f, -20.0f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                .register();
            this.builderForProjectile(ItemIds.LINGERING_POTION, 1, EntityTypeIds.LINGERING_POTION)
                .behavior(PotionHolderItemBehavior.of(0.25f))
                .behavior(ThrowableItemBehavior.of(0.5f, -20.0f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                .register();
            this.builder(ItemIds.DRAGON_BREATH)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builderForConsumable(ItemIds.OMINOUS_BOTTLE, Consumables.DEFAULT_DRINK)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .behavior(OminousEffectProviderItemBehavior.INSTANCE)
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(PlaySoundAction.of(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventIds.OMINOUS_BOTTLE_DISPOSE)))
                )
                .register();
            this.builderForConsumable(ItemIds.MILK_BUCKET, Consumables.DEFAULT_DRINK, ItemIds.BUCKET)
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(ClearStatusEffectsAction.of(LootContext.EntityTarget.THIS))
                )
                .register();
            this.builderForBlock(BlockItemIds.BEACON)
                .display(display -> display.rarity(Rarity.RARE))
                .register();
            this.registerBlock(BlockItemIds.GRINDSTONE);
            this.registerBlock(BlockItemIds.STONECUTTER);
            this.builderForBlock(BlockItemIds.LOOM)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.CARTOGRAPHY_TABLE)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.SMITHING_TABLE)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.FLETCHING_TABLE)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.JUKEBOX)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.NOTE_BLOCK)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForBlock(BlockItemIds.COMPOSTER)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.registerBlock(BlockItemIds.LODESTONE);
            this.registerBlock(BlockItemIds.RESPAWN_ANCHOR);
            this.registerBlock(BlockItemIds.VAULT);
            this.builder(BlockItemIds.REDSTONE_DUST.item())
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.REDSTONE_DUST.block())))
                .behavior(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.REDSTONE)))
                .register();
            this.registerBlock(BlockItemIds.REPEATER);
            this.registerBlock(BlockItemIds.COMPARATOR);
            this.registerBlockAttachedToSide(BlockItemIds.REDSTONE_TORCH, BlockIds.REDSTONE_WALL_TORCH, Direction.DOWN);
            this.registerBlock(BlockItemIds.REDSTONE_LAMP);
            this.registerBlock(BlockItemIds.LEVER);
            this.registerBlock(BlockItemIds.PISTON);
            this.registerBlock(BlockItemIds.STICKY_PISTON);
            this.registerBlock(BlockItemIds.HOPPER);
            this.registerBlock(BlockItemIds.DISPENSER);
            this.registerBlock(BlockItemIds.DROPPER);
            this.registerBlock(BlockItemIds.TARGET);
            this.registerBlock(BlockItemIds.OBSERVER);
            this.builderForBlock(BlockItemIds.DAYLIGHT_DETECTOR)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.registerBlock(BlockItemIds.SCULK_SENSOR);
            this.registerBlock(BlockItemIds.CALIBRATED_SCULK_SENSOR);
            this.builder(BlockItemIds.TRIPWIRE.item())
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.TRIPWIRE.block())))
                .register();
            this.registerBlock(BlockItemIds.TRIPWIRE_HOOK);
            this.builderForBlock(BlockItemIds.TNT)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_TNT)))
                .register();
            this.builderForBlock(BlockItemIds.AZALEA)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_AZALEA_BUSH))
                .register();
            this.builderForBlock(BlockItemIds.AZALEA_LEAVES)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.FLOWERING_AZALEA)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_FLOWERING_AZALEA_BUSH))
                .register();
            this.builderForBlock(BlockItemIds.FLOWERING_AZALEA_LEAVES)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.MUSHROOM_STEM)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.BROWN_MUSHROOM_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForBlock(BlockItemIds.BROWN_MUSHROOM)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_BROWN_MUSHROOM))
                .register();
            this.builderForBlock(BlockItemIds.RED_MUSHROOM_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForBlock(BlockItemIds.RED_MUSHROOM)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_RED_MUSHROOM))
                .register();
            this.builderForBlock(BlockItemIds.SHROOMLIGHT)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.SHORT_GRASS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.FERN)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_FERN))
                .register();
            this.builderForBlock(BlockItemIds.SHORT_DRY_GRASS)
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.BUSH)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.DEAD_BUSH)
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_DEAD_BUSH))
                .register();
            this.registerSuspiciousEffectIngredient(BlockItemIds.DANDELION, CompostChances.BIG, MobEffectIds.SATURATION, 140, BlockIds.POTTED_DANDELION);
            this.registerSuspiciousEffectIngredient(BlockItemIds.POPPY, CompostChances.BIG, MobEffectIds.NIGHT_VISION, 100, BlockIds.POTTED_POPPY);
            this.registerSuspiciousEffectIngredient(BlockItemIds.BLUE_ORCHID, CompostChances.BIG, MobEffectIds.SATURATION, 140, BlockIds.POTTED_BLUE_ORCHID);
            this.registerSuspiciousEffectIngredient(BlockItemIds.ALLIUM, CompostChances.BIG, MobEffectIds.FIRE_RESISTANCE, 80, BlockIds.POTTED_ALLIUM);
            this.registerSuspiciousEffectIngredient(BlockItemIds.AZURE_BLUET, CompostChances.BIG, MobEffectIds.BLINDNESS, 160, BlockIds.POTTED_AZURE_BLUET);
            this.registerSuspiciousEffectIngredient(BlockItemIds.RED_TULIP, CompostChances.BIG, MobEffectIds.WEAKNESS, 180, BlockIds.POTTED_RED_TULIP);
            this.registerSuspiciousEffectIngredient(BlockItemIds.ORANGE_TULIP, CompostChances.BIG, MobEffectIds.WEAKNESS, 180, BlockIds.POTTED_ORANGE_TULIP);
            this.registerSuspiciousEffectIngredient(BlockItemIds.WHITE_TULIP, CompostChances.BIG, MobEffectIds.WEAKNESS, 180, BlockIds.POTTED_WHITE_TULIP);
            this.registerSuspiciousEffectIngredient(BlockItemIds.PINK_TULIP, CompostChances.BIG, MobEffectIds.WEAKNESS, 180, BlockIds.POTTED_PINK_TULIP);
            this.registerSuspiciousEffectIngredient(BlockItemIds.OXEYE_DAISY, CompostChances.BIG, MobEffectIds.REGENERATION, 160, BlockIds.POTTED_OXEYE_DAISY);
            this.registerSuspiciousEffectIngredient(BlockItemIds.CORNFLOWER, CompostChances.BIG, MobEffectIds.JUMP_BOOST, 120, BlockIds.POTTED_CORNFLOWER);
            this.registerSuspiciousEffectIngredient(BlockItemIds.LILY_OF_THE_VALLEY, CompostChances.BIG, MobEffectIds.POISON, 240, BlockIds.POTTED_LILY_OF_THE_VALLEY);
            this.registerSuspiciousEffectIngredient(BlockItemIds.TORCHFLOWER, CompostChances.ALMOST_GUARANTEED, MobEffectIds.NIGHT_VISION, 100, BlockIds.POTTED_TORCHFLOWER);
            this.builderForBlock(BlockItemIds.CACTUS_FLOWER)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.registerSuspiciousEffectIngredient(BlockItemIds.CLOSED_EYEBLOSSOM, CompostChances.BIG, MobEffectIds.NAUSEA, 140, BlockIds.POTTED_CLOSED_EYEBLOSSOM);
            this.registerSuspiciousEffectIngredient(BlockItemIds.OPEN_EYEBLOSSOM, CompostChances.BIG, MobEffectIds.BLINDNESS, 140, BlockIds.POTTED_OPEN_EYEBLOSSOM);
            this.registerSuspiciousEffectIngredient(BlockItemIds.WITHER_ROSE, CompostChances.BIG, MobEffectIds.WITHER, 160, BlockIds.POTTED_WITHER_ROSE);
            this.builderForBlock(BlockItemIds.PINK_PETALS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.WILDFLOWERS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.LEAF_LITTER)
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.SPORE_BLOSSOM)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.FIREFLY_BUSH)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.SUGAR_CANE)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.CACTUS)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_CACTUS))
                .register();
            this.builderForBlock(BlockItemIds.CRIMSON_ROOTS)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_CRIMSON_ROOTS))
                .register();
            this.builderForBlock(BlockItemIds.WARPED_ROOTS)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, BlockIds.POTTED_WARPED_ROOTS))
                .register();
            this.builderForBlock(BlockItemIds.NETHER_SPROUTS)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.WEEPING_VINES)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.TWISTING_VINES)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.VINE)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.TALL_GRASS)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.LARGE_FERN)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.TALL_DRY_GRASS)
                .behavior(FuelItemBehavior.of(FuelTimes.PLANT))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.SUNFLOWER)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.LILAC)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.ROSE_BUSH)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.PEONY)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.PITCHER_PLANT)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForBlock(BlockItemIds.BIG_DRIPLEAF)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.SMALL_DRIPLEAF)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.registerBlock(BlockItemIds.CHORUS_PLANT);
            this.registerBlock(BlockItemIds.CHORUS_FLOWER);
            this.builderForBlock(BlockItemIds.GLOW_LICHEN)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.builderForBlock(BlockItemIds.HANGING_ROOTS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.registerBlock(BlockItemIds.FLOWER_POT);
            this.registerBlock(BlockItemIds.BEE_NEST);
            this.registerBlock(BlockItemIds.BEEHIVE);
            this.builder(ItemIds.HONEY_BOTTLE, 16)
                .behavior(UseableItemBehavior.of(Consumables.HONEY_BOTTLE, this.items.getOrThrow(ItemIds.GLASS_BOTTLE)))
                .behavior(ConsumableItemBehavior.of(Consumables.HONEY_BOTTLE))
                .behavior(FoodItemBehavior.of(Foods.HONEY_BOTTLE))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        RemoveStatusEffectsAction.of(
                            LootContext.EntityTarget.THIS,
                            this.statusEffects.getOrThrow(MobEffectIds.POISON)
                        )
                    )
                )
                .register();
            this.registerBlock(BlockItemIds.HONEY_BLOCK);
            this.registerBlock(BlockItemIds.HONEYCOMB_BLOCK);
            this.builder(ItemIds.HONEYCOMB)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.WAX_BLOCK)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        FirstToPassRequirementsSequenceHandler.builder()
                            .add(Actions.waxSign(this.blocks, true))
                            .add(PassingSequenceHandler.builder()
                                .add(WaxBlockAction.of(PositionTarget.INTERACTED))
                                .add(DecrementItemAction.of(1))
                                .add(SwingHandAction.of(LootContext.EntityTarget.THIS)))
                    )
                )
                .register();
            this.registerBlock(BlockItemIds.GOLDEN_DANDELION);
            this.builderForBlockOnFluid(BlockItemIds.LILY_PAD)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.SEAGRASS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.SEA_PICKLE)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.KELP)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.DRIED_KELP_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .behavior(FuelItemBehavior.of(FuelTimes.DRIED_KELP_BLOCK))
                .register();
            this.builderForBlock(BlockItemIds.MOSS_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.MOSS_CARPET)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.PALE_MOSS_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.PALE_MOSS_CARPET)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.PALE_HANGING_MOSS)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.TORCHFLOWER_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForBlock(BlockItemIds.PITCHER_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.registerBlock(BlockItemIds.SPONGE);
            this.registerBlock(BlockItemIds.WET_SPONGE);
            this.registerBlock(BlockItemIds.COBWEB);
            this.registerBlock(BlockItemIds.PETRIFIED_OAK_SLAB);
            this.registerBlock(BlockItemIds.ICE);
            this.registerBlock(BlockItemIds.PACKED_ICE);
            this.registerBlock(BlockItemIds.BLUE_ICE);
            this.registerBlock(BlockItemIds.SNOW);
            this.registerBlock(BlockItemIds.SNOW_BLOCK);
            this.registerBlock(BlockItemIds.SPAWNER);
            this.registerBlock(BlockItemIds.TRIAL_SPAWNER);
            this.registerItem(ItemIds.TRIAL_KEY);
            this.registerItem(ItemIds.OMINOUS_TRIAL_KEY);
            this.builderForBlock(BlockItemIds.HEAVY_CORE)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.registerBlockAttachedToSide(BlockItemIds.TORCH, BlockIds.WALL_TORCH, Direction.DOWN);
            this.registerBlockAttachedToSide(BlockItemIds.SOUL_TORCH, BlockIds.SOUL_WALL_TORCH, Direction.DOWN);
            this.registerBlockAttachedToSide(BlockItemIds.COPPER_TORCH, BlockIds.COPPER_WALL_TORCH, Direction.DOWN);
            this.registerBlock(BlockItemIds.LANTERN);
            this.registerBlock(BlockItemIds.SOUL_LANTERN);
            this.registerBlock(BlockItemIds.BELL);
            this.registerBlock(BlockItemIds.DECORATED_POT);
            this.registerBlock(BlockItemIds.RAIL);
            this.registerBlock(BlockItemIds.POWERED_RAIL);
            this.registerBlock(BlockItemIds.DETECTOR_RAIL);
            this.registerBlock(BlockItemIds.ACTIVATOR_RAIL);
            this.builderForBlock(BlockItemIds.MELON)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.MELON_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(ItemIds.MELON_SLICE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.MELON_SLICE))
                .behavior(CompostableItemBehavior.of(CompostChances.FIFTY_FIFTY))
                .register();
            this.registerItem(ItemIds.GLISTERING_MELON_SLICE);
            this.builderForBlock(BlockItemIds.PUMPKIN)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.PUMPKIN_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(ItemIds.PUMPKIN_PIE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.PUMPKIN_PIE))
                .behavior(CompostableItemBehavior.of(CompostChances.GUARANTEED))
                .register();
            this.builderForBlock(BlockItemIds.CARVED_PUMPKIN)
                .attributeModifiers(AttributeModifiers::hideFromLocatorBar)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .behavior(
                    EquipmentItemBehavior.of(Equippable.builder(EquipmentSlot.HEAD)
                        .setSwappable(false)
                        .setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur"))
                        .build())
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_CARVED_PUMPKIN)))
                .register();
            this.registerBlock(BlockItemIds.JACK_O_LANTERN);
            this.builderForBlock(BlockItemIds.DRAGON_EGG)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.registerBlock(BlockItemIds.OCHRE_FROGLIGHT);
            this.registerBlock(BlockItemIds.VERDANT_FROGLIGHT);
            this.registerBlock(BlockItemIds.PEARLESCENT_FROGLIGHT);
            this.builderForBlockOnFluid(BlockItemIds.FROGSPAWN)
                .register();
            this.registerBlock(BlockItemIds.MAGMA_BLOCK);
            this.registerBlock(BlockItemIds.BONE_BLOCK);
            this.registerBlock(BlockItemIds.TURTLE_EGG);
            this.builderForBlock(BlockItemIds.SNIFFER_EGG)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.registerBlock(BlockItemIds.DRIED_GHAST);
            this.builderForBlock(BlockItemIds.CONDUIT)
                .display(display -> display.rarity(Rarity.RARE))
                .register();
            this.builder(ItemIds.NAUTILUS_SHELL)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builder(ItemIds.HEART_OF_THE_SEA)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.registerBlock(BlockItemIds.CREAKING_HEART);
            this.registerOperatorBlock(BlockItemIds.COMMAND_BLOCK);
            this.registerOperatorBlock(BlockItemIds.REPEATING_COMMAND_BLOCK);
            this.registerOperatorBlock(BlockItemIds.CHAIN_COMMAND_BLOCK);
            this.builderForBlock(BlockItemIds.BARRIER)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.builderForBlock(BlockItemIds.LIGHT)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.registerOperatorBlock(BlockItemIds.STRUCTURE_BLOCK);
            this.builderForBlock(BlockItemIds.STRUCTURE_VOID)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.registerOperatorBlock(BlockItemIds.JIGSAW);
            this.registerOperatorBlock(BlockItemIds.TEST_BLOCK);
            this.registerOperatorBlock(BlockItemIds.TEST_INSTANCE_BLOCK);
            this.builderForSword(ItemIds.WOODEN_SWORD, ToolMaterial.WOOD, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.builderForSpear(ItemIds.WOODEN_SPEAR, ToolMaterial.WOOD, 0.65f, 0.7f, 0.75f, 5.0f, 14.0f, 10.0f, 15.0f, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.builderForShovel(ItemIds.WOODEN_SHOVEL, ToolMaterial.WOOD, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.builderForPickaxe(ItemIds.WOODEN_PICKAXE, ToolMaterial.WOOD, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.builderForAxe(ItemIds.WOODEN_AXE, ToolMaterial.WOOD, 7.0d, 0.2d, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.builderForHoe(ItemIds.WOODEN_HOE, ToolMaterial.WOOD, 1.0d, 0.25d, ItemTags.WOODEN_TOOL_MATERIALS)
                .behavior(FuelItemBehavior.of(FuelTimes.TOOL))
                .register();
            this.registerSword(ItemIds.STONE_SWORD, ToolMaterial.STONE, ItemTags.STONE_TOOL_MATERIALS);
            this.registerSpear(ItemIds.STONE_SPEAR, ToolMaterial.STONE, 0.75f, 0.82f, 0.7f, 4.5f, 13.0f, 9.0f, 13.75f, ItemTags.STONE_TOOL_MATERIALS);
            this.registerShovel(ItemIds.STONE_SHOVEL, ToolMaterial.STONE, ItemTags.STONE_TOOL_MATERIALS);
            this.registerPickaxe(ItemIds.STONE_PICKAXE, ToolMaterial.STONE, ItemTags.STONE_TOOL_MATERIALS);
            this.registerAxe(ItemIds.STONE_AXE, ToolMaterial.STONE, 8.0d, 0.2d, ItemTags.STONE_TOOL_MATERIALS);
            this.registerHoe(ItemIds.STONE_HOE, ToolMaterial.STONE, 2.0d, 0.5d, ItemTags.STONE_TOOL_MATERIALS);
            this.registerSword(ItemIds.GOLDEN_SWORD, ToolMaterial.GOLD, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerSpear(ItemIds.GOLDEN_SPEAR, ToolMaterial.GOLD, 0.95f, 0.7f, 0.7f, 3.5f, 13.0f, 8.5f, 13.75f, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerShovel(ItemIds.GOLDEN_SHOVEL, ToolMaterial.GOLD, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerPickaxe(ItemIds.GOLDEN_PICKAXE, ToolMaterial.GOLD, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerAxe(ItemIds.GOLDEN_AXE, ToolMaterial.GOLD, 7.0d, 0.25d, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerHoe(ItemIds.GOLDEN_HOE, ToolMaterial.GOLD, 1.0d, 0.25d, ItemTags.GOLD_TOOL_MATERIALS);
            this.registerSword(ItemIds.COPPER_SWORD, ToolMaterial.COPPER, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerSpear(ItemIds.COPPER_SPEAR, ToolMaterial.COPPER, 0.85f, 0.82f, 0.65f, 4.0f, 12.0f, 8.25f, 12.5f, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerShovel(ItemIds.COPPER_SHOVEL, ToolMaterial.COPPER, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerPickaxe(ItemIds.COPPER_PICKAXE, ToolMaterial.COPPER, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerAxe(ItemIds.COPPER_AXE, ToolMaterial.COPPER, 8.0d, 0.2d, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerHoe(ItemIds.COPPER_HOE, ToolMaterial.COPPER, 2.0d, 0.5d, ItemTags.COPPER_TOOL_MATERIALS);
            this.registerSword(ItemIds.IRON_SWORD, ToolMaterial.IRON, ItemTags.IRON_TOOL_MATERIALS);
            this.registerSpear(ItemIds.IRON_SPEAR, ToolMaterial.IRON, 0.95f, 0.95f, 0.6f, 2.5f, 11.0f, 6.75f, 11.25f, ItemTags.IRON_TOOL_MATERIALS);
            this.registerShovel(ItemIds.IRON_SHOVEL, ToolMaterial.IRON, ItemTags.IRON_TOOL_MATERIALS);
            this.registerPickaxe(ItemIds.IRON_PICKAXE, ToolMaterial.IRON, ItemTags.IRON_TOOL_MATERIALS);
            this.registerAxe(ItemIds.IRON_AXE, ToolMaterial.IRON, 7.0d, 0.225d, ItemTags.IRON_TOOL_MATERIALS);
            this.registerHoe(ItemIds.IRON_HOE, ToolMaterial.IRON, 3.0d, 0.75d, ItemTags.IRON_TOOL_MATERIALS);
            this.registerSword(ItemIds.DIAMOND_SWORD, ToolMaterial.DIAMOND, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.registerSpear(ItemIds.DIAMOND_SPEAR, ToolMaterial.DIAMOND, 1.05f, 1.075f, 0.5f, 3.0f, 10.0f, 6.5f, 10.0f, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.registerShovel(ItemIds.DIAMOND_SHOVEL, ToolMaterial.DIAMOND, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.registerPickaxe(ItemIds.DIAMOND_PICKAXE, ToolMaterial.DIAMOND, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.registerAxe(ItemIds.DIAMOND_AXE, ToolMaterial.DIAMOND, 6.0d, 0.25d, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.registerHoe(ItemIds.DIAMOND_HOE, ToolMaterial.DIAMOND, 4.0d, 1.0d, ItemTags.DIAMOND_TOOL_MATERIALS);
            this.builderForSword(ItemIds.NETHERITE_SWORD, ToolMaterial.NETHERITE, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForSpear(ItemIds.NETHERITE_SPEAR, ToolMaterial.NETHERITE, 1.15f, 1.2f, 0.4f, 2.5f, 9.0f, 5.5f, 8.75f, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForShovel(ItemIds.NETHERITE_SHOVEL, ToolMaterial.NETHERITE, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForPickaxe(ItemIds.NETHERITE_PICKAXE, ToolMaterial.NETHERITE, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForAxe(ItemIds.NETHERITE_AXE, ToolMaterial.NETHERITE, 6.0d, 0.25d, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForHoe(ItemIds.NETHERITE_HOE, ToolMaterial.NETHERITE, 5.0d, 1.0d, ItemTags.NETHERITE_TOOL_MATERIALS)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builder(ItemIds.FISHING_ROD, 1)
                .behavior(DamageableItemBehavior.of(64))
                .behavior(EnchantableItemBehavior.of(1))
                .behavior(CastableItemBehavior.INSTANCE)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builder(ItemIds.SHEARS, 1)
                .behavior(DamageableItemBehavior.of(238))
                .behavior(
                    ToolItemBehavior.builder(1)
                        .rule(Tool.Rule.minesAndDrops(HolderSet.direct(this.blocks.getOrThrow(BlockItemIds.COBWEB.block())), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.SHEARS_EXTREME_BREAKING_SPEED), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.SHEARS_MAJOR_BREAKING_SPEED), 5.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.SHEARS_MINOR_BREAKING_SPEED), 2.0f))
                        .build()
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHEAR)))
                .register();
            this.builder(ItemIds.BOW, 1)
                .behavior(DamageableItemBehavior.of(384))
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(ShooterUseDurationProvider.INSTANCE)
                        .animation(ItemUseAnimation.BOW)
                        .build()
                )
                .behavior(
                    ShooterItemBehavior.of(
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        this.items.getOrThrow(ItematicItemTags.BOW_AMMUNITION),
                        BowItem.DEFAULT_RANGE,
                        DirectShooterMethod.of()
                    )
                )
                .behavior(EnchantableItemBehavior.of(1))
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builder(ItemIds.CROSSBOW, 1)
                .behavior(DamageableItemBehavior.of(465))
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(ShooterUseDurationProvider.INSTANCE)
                        .animation(ItemUseAnimation.CROSSBOW)
                        .build()
                )
                .behavior(
                    ShooterItemBehavior.of(
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
                    )
                )
                .behavior(EnchantableItemBehavior.of(1))
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
            this.builderForProjectile(ItemIds.ARROW, 64, EntityTypeIds.ARROW)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .register();
            this.builderForProjectile(ItemIds.SPECTRAL_ARROW, 64, EntityTypeIds.SPECTRAL_ARROW)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .register();
            this.builderForProjectile(ItemIds.TIPPED_ARROW, 64, EntityTypeIds.ARROW)
                .behavior(PotionHolderItemBehavior.of(0.125f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .register();
            this.builderForProjectile(ItemIds.TRIDENT, 1, EntityTypeIds.TRIDENT)
                .display(display -> display.rarity(Rarity.RARE))
                .behavior(DamageableItemBehavior.ofPreserved(250))
                .behavior(
                    ToolItemBehavior.builder(2)
                        .preventCreativeDestruction()
                        .build()
                )
                .behavior(
                    WeaponItemBehavior.builder(1, TridentItem.BASE_DAMAGE, 0.275d)
                        .build()
                )
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(TridentUseDurationProvider.INSTANCE)
                        .animation(ItemUseAnimation.TRIDENT)
                        .build()
                )
                .behavior(ThrowableItemBehavior.ofMinDrawDuration(TridentItem.PROJECTILE_SHOOT_POWER, 0.0f, TridentItem.THROW_THRESHOLD_TIME))
                .behavior(EnchantableItemBehavior.of(1))
                .event(
                    ItemEvent.STOPPED_USING,
                    ActionEntry.of(
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
                    )
                )
                .register();
            this.builder(ItemIds.CARROT_ON_A_STICK, 1)
                .behavior(DamageableItemBehavior.of(25))
                .behavior(SteeringItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.PIG), 7))
                .event(
                    ItemEvent.BREAK_ITEM,
                    ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemIds.FISHING_ROD)))
                )
                .register();
            this.builder(ItemIds.WARPED_FUNGUS_ON_A_STICK, 1)
                .behavior(DamageableItemBehavior.of(100))
                .behavior(SteeringItemBehavior.of(this.entityTypes.getOrThrow(EntityTypeIds.STRIDER), 1))
                .event(
                    ItemEvent.BREAK_ITEM,
                    ActionEntry.of(ExchangeItemAction.ofNoDecrement(this.items.getOrThrow(ItemIds.FISHING_ROD)))
                )
                .register();
            this.builder(ItemIds.FLINT_AND_STEEL, 1)
                .behavior(DamageableItemBehavior.of(64))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DamageItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.FLINT_AND_STEEL_USE), SoundSource.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    )
                )
                .register();
            this.builder(ItemIds.BRUSH, 1)
                .behavior(DamageableItemBehavior.of(64))
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(BrushItemAccessor.useDuration())
                        .animation(ItemUseAnimation.BRUSH)
                        .passes(UseableItemBehavior.Pass.BLOCK)
                        .build()
                )
                .behavior(BrushItemBehavior.INSTANCE)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.BRUSH)))
                .register();
            this.registerItem(ItemIds.BREEZE_ROD);
            this.builder(ItemIds.MACE, 1)
                .display(display -> display.rarity(Rarity.EPIC))
                .behavior(DamageableItemBehavior.of(500))
                .behavior(ToolItemBehavior.builder(2).build())
                .behavior(
                    WeaponItemBehavior.builder(1, 5.0d, 0.15d)
                        .type(
                            MeleeWeaponComponents.SMASHING,
                            SmashingMeleeWeapon.of(
                                SmashingWeapon.of(
                                    SmashingWeapon.HitSounds.of(
                                        this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_AIR),
                                        this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_GROUND),
                                        this.soundEvents.getOrThrow(SoundEventIds.MACE_SMASH_GROUND_HEAVY)
                                    ),
                                    MaceItem.SMASH_ATTACK_FALL_THRESHOLD,
                                    MaceItemAccessor.heavySmashAttackFallDistance(),
                                    MaceItemAccessor.knockbackPower()
                                )
                            )
                        )
                        .build()
                )
                .behavior(EnchantableItemBehavior.of(15))
                .behavior(RepairableItemBehavior.of(HolderSet.direct(this.items.getOrThrow(ItemIds.BREEZE_ROD))))
                .register();
            this.registerArmor(ItemIds.LEATHER_HELMET, ArmorMaterials.LEATHER, ArmorType.HELMET, ItemTags.REPAIRS_LEATHER_ARMOR);
            this.registerArmor(ItemIds.LEATHER_CHESTPLATE, ArmorMaterials.LEATHER, ArmorType.CHESTPLATE, ItemTags.REPAIRS_LEATHER_ARMOR);
            this.registerArmor(ItemIds.LEATHER_LEGGINGS, ArmorMaterials.LEATHER, ArmorType.LEGGINGS, ItemTags.REPAIRS_LEATHER_ARMOR);
            this.registerArmor(ItemIds.LEATHER_BOOTS, ArmorMaterials.LEATHER, ArmorType.BOOTS, ItemTags.REPAIRS_LEATHER_ARMOR);
            this.registerArmor(ItemIds.COPPER_HELMET, ArmorMaterials.COPPER, ArmorType.HELMET, ItemTags.REPAIRS_COPPER_ARMOR);
            this.registerArmor(ItemIds.COPPER_CHESTPLATE, ArmorMaterials.COPPER, ArmorType.CHESTPLATE, ItemTags.REPAIRS_COPPER_ARMOR);
            this.registerArmor(ItemIds.COPPER_LEGGINGS, ArmorMaterials.COPPER, ArmorType.LEGGINGS, ItemTags.REPAIRS_COPPER_ARMOR);
            this.registerArmor(ItemIds.COPPER_BOOTS, ArmorMaterials.COPPER, ArmorType.BOOTS, ItemTags.REPAIRS_COPPER_ARMOR);
            this.builderForArmor(ItemIds.CHAINMAIL_HELMET, ArmorMaterials.CHAINMAIL, ArmorType.HELMET, ItemTags.REPAIRS_CHAIN_ARMOR)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builderForArmor(ItemIds.CHAINMAIL_CHESTPLATE, ArmorMaterials.CHAINMAIL, ArmorType.CHESTPLATE, ItemTags.REPAIRS_CHAIN_ARMOR)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builderForArmor(ItemIds.CHAINMAIL_LEGGINGS, ArmorMaterials.CHAINMAIL, ArmorType.LEGGINGS, ItemTags.REPAIRS_CHAIN_ARMOR)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builderForArmor(ItemIds.CHAINMAIL_BOOTS, ArmorMaterials.CHAINMAIL, ArmorType.BOOTS, ItemTags.REPAIRS_CHAIN_ARMOR)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.registerArmor(ItemIds.IRON_HELMET, ArmorMaterials.IRON, ArmorType.HELMET, ItemTags.REPAIRS_IRON_ARMOR);
            this.registerArmor(ItemIds.IRON_CHESTPLATE, ArmorMaterials.IRON, ArmorType.CHESTPLATE, ItemTags.REPAIRS_IRON_ARMOR);
            this.registerArmor(ItemIds.IRON_LEGGINGS, ArmorMaterials.IRON, ArmorType.LEGGINGS, ItemTags.REPAIRS_IRON_ARMOR);
            this.registerArmor(ItemIds.IRON_BOOTS, ArmorMaterials.IRON, ArmorType.BOOTS, ItemTags.REPAIRS_IRON_ARMOR);
            this.registerArmor(ItemIds.DIAMOND_HELMET, ArmorMaterials.DIAMOND, ArmorType.HELMET, ItemTags.REPAIRS_DIAMOND_ARMOR);
            this.registerArmor(ItemIds.DIAMOND_CHESTPLATE, ArmorMaterials.DIAMOND, ArmorType.CHESTPLATE, ItemTags.REPAIRS_DIAMOND_ARMOR);
            this.registerArmor(ItemIds.DIAMOND_LEGGINGS, ArmorMaterials.DIAMOND, ArmorType.LEGGINGS, ItemTags.REPAIRS_DIAMOND_ARMOR);
            this.registerArmor(ItemIds.DIAMOND_BOOTS, ArmorMaterials.DIAMOND, ArmorType.BOOTS, ItemTags.REPAIRS_DIAMOND_ARMOR);
            this.registerArmor(ItemIds.GOLDEN_HELMET, ArmorMaterials.GOLD, ArmorType.HELMET, ItemTags.REPAIRS_GOLD_ARMOR);
            this.registerArmor(ItemIds.GOLDEN_CHESTPLATE, ArmorMaterials.GOLD, ArmorType.CHESTPLATE, ItemTags.REPAIRS_GOLD_ARMOR);
            this.registerArmor(ItemIds.GOLDEN_LEGGINGS, ArmorMaterials.GOLD, ArmorType.LEGGINGS, ItemTags.REPAIRS_GOLD_ARMOR);
            this.registerArmor(ItemIds.GOLDEN_BOOTS, ArmorMaterials.GOLD, ArmorType.BOOTS, ItemTags.REPAIRS_GOLD_ARMOR);
            this.builderForArmor(ItemIds.NETHERITE_HELMET, ArmorMaterials.NETHERITE, ArmorType.HELMET, ItemTags.REPAIRS_NETHERITE_ARMOR)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForArmor(ItemIds.NETHERITE_CHESTPLATE, ArmorMaterials.NETHERITE, ArmorType.CHESTPLATE, ItemTags.REPAIRS_NETHERITE_ARMOR)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForArmor(ItemIds.NETHERITE_LEGGINGS, ArmorMaterials.NETHERITE, ArmorType.LEGGINGS, ItemTags.REPAIRS_NETHERITE_ARMOR)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.builderForArmor(ItemIds.NETHERITE_BOOTS, ArmorMaterials.NETHERITE, ArmorType.BOOTS, ItemTags.REPAIRS_NETHERITE_ARMOR)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.registerItem(ItemIds.TURTLE_SCUTE);
            this.registerArmor(ItemIds.TURTLE_HELMET, ArmorMaterials.TURTLE_SCUTE, ArmorType.HELMET, ItemTags.REPAIRS_TURTLE_HELMET);
            this.builder(ItemIds.SADDLE, 1)
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.SADDLE)
                            .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.HORSE_SADDLE))
                            .setAsset(EquipmentAssets.SADDLE)
                            .setAllowedEntities(this.entityTypes.getOrThrow(EntityTypeTags.CAN_EQUIP_SADDLE))
                            .setEquipOnInteract(true)
                            .setCanBeSheared(true)
                            .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.SADDLE_UNEQUIP))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                .register();
            this.registerHorseArmor(ItemIds.LEATHER_HORSE_ARMOR, ArmorMaterials.LEATHER);
            this.registerHorseArmor(ItemIds.COPPER_HORSE_ARMOR, ArmorMaterials.COPPER);
            this.registerHorseArmor(ItemIds.IRON_HORSE_ARMOR, ArmorMaterials.IRON);
            this.registerHorseArmor(ItemIds.GOLDEN_HORSE_ARMOR, ArmorMaterials.GOLD);
            this.registerHorseArmor(ItemIds.DIAMOND_HORSE_ARMOR, ArmorMaterials.DIAMOND);
            this.builderForHorseArmor(ItemIds.NETHERITE_HORSE_ARMOR, ArmorMaterials.NETHERITE)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.registerItem(ItemIds.ARMADILLO_SCUTE);
            this.builder(ItemIds.WOLF_ARMOR, 1)
                .attributeModifiers(builder -> AttributeModifiers.armor(builder, ArmorMaterials.ARMADILLO_SCUTE, ArmorType.BODY))
                .behavior(
                    DamageableItemBehavior.of(
                        ArmorType.BODY.getDurability(ArmorMaterials.ARMADILLO_SCUTE.durability()),
                        this.soundEvents.getOrThrow(SoundEventIds.WOLF_ARMOR_BREAK)
                    )
                )
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(ArmorMaterials.ARMADILLO_SCUTE.repairIngredient())))
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.BODY)
                            .setEquipSound(ArmorMaterials.ARMADILLO_SCUTE.equipSound())
                            .setAsset(ArmorMaterials.ARMADILLO_SCUTE.assetId())
                            .setAllowedEntities(HolderSet.direct(this.entityTypes.getOrThrow(EntityTypeIds.WOLF)))
                            .setCanBeSheared(true)
                            .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_UNEQUIP_WOLF))
                            .build()
                    )
                )
                .register();
            ColorCollection.zipApply(ItemIds.HARNESS, ColorCollection.VALUES, (harness, dyeColor) -> this.builder(harness, 1)
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.BODY)
                            .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.HAPPY_GHAST_EQUIP))
                            .setAsset(EquipmentAssets.HARNESSES.get(dyeColor))
                            .setAllowedEntities(this.entityTypes.getOrThrow(EntityTypeTags.CAN_EQUIP_HARNESS))
                            .setEquipOnInteract(true)
                            .setCanBeSheared(true)
                            .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.HAPPY_GHAST_UNEQUIP))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                .register()
            );
            this.registerNautilusArmor(ItemIds.COPPER_NAUTILUS_ARMOR, ArmorMaterials.COPPER);
            this.registerNautilusArmor(ItemIds.IRON_NAUTILUS_ARMOR, ArmorMaterials.IRON);
            this.registerNautilusArmor(ItemIds.GOLDEN_NAUTILUS_ARMOR, ArmorMaterials.GOLD);
            this.registerNautilusArmor(ItemIds.DIAMOND_NAUTILUS_ARMOR, ArmorMaterials.DIAMOND);
            this.builderForNautilusArmor(ItemIds.NETHERITE_NAUTILUS_ARMOR, ArmorMaterials.NETHERITE)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.registerItem(ItemIds.PHANTOM_MEMBRANE);
            this.builder(ItemIds.ELYTRA, 1)
                .display(display -> display.rarity(Rarity.EPIC))
                .behavior(DamageableItemBehavior.ofPreserved(432))
                .behavior(
                    GliderItemBehavior.of(
                        ItemPredicate.Builder.item()
                            .withComponents(DataComponentMatchers.Builder.components()
                                .partial(
                                    DataComponentPredicates.DAMAGE,
                                    DamagePredicate.durability(MinMaxBounds.Ints.atLeast(2))
                                ).build()
                            ).build()
                    )
                )
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.CHEST)
                            .setSwappable(true)
                            .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_ELYTRA))
                            .setAsset(EquipmentAssets.ELYTRA)
                            .build()
                    )
                )
                .behavior(RepairableItemBehavior.of(HolderSet.direct(this.items.getOrThrow(ItemIds.PHANTOM_MEMBRANE))))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                .register();
            this.builder(ItemIds.SHIELD, 1)
                .behavior(
                    UseableItemBehavior.builder()
                        .useIndefinitely()
                        .animation(ItemUseAnimation.BLOCK)
                        .build()
                )
                .behavior(DamageableItemBehavior.of(336))
                .behavior(
                    AttackBlockingItemBehavior.of(
                        new BlocksAttacks(
                            0.25f,
                            1.0f,
                            List.of(new BlocksAttacks.DamageReduction(90.0f, Optional.empty(), 0.0f, 1.0f)),
                            new BlocksAttacks.ItemDamageFunction(3.0f, 1.0f, 1.0f),
                            Optional.of(this.damageTypes.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                            Optional.of(this.soundEvents.getOrThrow(SoundEventIds.SHIELD_BLOCK)),
                            Optional.of(this.soundEvents.getOrThrow(SoundEventIds.SHIELD_BREAK))
                        )
                    )
                )
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.OFFHAND)
                            .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_GENERIC))
                            .setSwappable(false)
                            .build()
                    )
                )
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(ItemTags.WOODEN_TOOL_MATERIALS)))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)))
                .behavior(BannerPatternHolderItemBehavior.of())
                .register();
            this.builder(ItemIds.TOTEM_OF_UNDYING, 1)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .event(
                    ItemEvent.BEFORE_DEATH_HOLDER,
                    ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 900, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 100, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.FIRE_RESISTANCE), 800, 0)
                        )
                    )
                )
                .register();
            this.builder(ItemIds.SPYGLASS, 1)
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(SpyglassItem.USE_DURATION)
                        .animation(ItemUseAnimation.SPYGLASS)
                        .build()
                )
                .behavior(
                    ZoomItemBehavior.of(
                        SpyglassItem.ZOOM_FOV_MODIFIER,
                        this.soundEvents.getOrThrow(SoundEventIds.SPYGLASS_USE),
                        this.soundEvents.getOrThrow(SoundEventIds.SPYGLASS_STOP_USING)
                    )
                )
                .register();
            this.builder(ItemIds.GOAT_HORN, 1)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .behavior(
                    UseableItemBehavior.builder()
                        .useFor(PlayableUseDurationProvider.INSTANCE)
                        .animation(ItemUseAnimation.TOOT_HORN)
                        .build()
                )
                .behavior(PlayableItemBehavior.of(this.instruments.getOrThrow(Instruments.PONDER_GOAT_HORN)))
                .register();
            this.builder(ItemIds.COMPASS)
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
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
                    )
                )
                .register();
            this.builder(ItemIds.ECHO_SHARD)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.builder(ItemIds.RECOVERY_COMPASS)
                .display(display -> display.rarity(Rarity.UNCOMMON))
                .register();
            this.registerItem(ItemIds.CLOCK);
            this.registerSkull(BlockItemIds.SKELETON_SKULL, Rarity.UNCOMMON, BlockIds.SKELETON_WALL_SKULL);
            this.registerSkull(BlockItemIds.WITHER_SKELETON_SKULL, Rarity.UNCOMMON, BlockIds.WITHER_SKELETON_WALL_SKULL);
            this.registerSkull(BlockItemIds.PLAYER_HEAD, Rarity.UNCOMMON, BlockIds.PLAYER_WALL_HEAD);
            this.registerSkull(BlockItemIds.ZOMBIE_HEAD, Rarity.UNCOMMON, BlockIds.ZOMBIE_WALL_HEAD);
            this.registerSkull(BlockItemIds.CREEPER_HEAD, Rarity.UNCOMMON, BlockIds.CREEPER_WALL_HEAD);
            this.registerSkull(BlockItemIds.DRAGON_HEAD, Rarity.RARE, BlockIds.DRAGON_WALL_HEAD);
            this.registerSkull(BlockItemIds.PIGLIN_HEAD, Rarity.UNCOMMON, BlockIds.PIGLIN_WALL_HEAD);
            this.registerBucket(ItemIds.BUCKET, 16, BucketItemBehavior.drainFluid());
            this.builderForBucketWithFluid(ItemIds.WATER_BUCKET, FluidIds.WATER, SoundEventIds.BUCKET_EMPTY)
                .register();
            this.builderForBucketWithFluid(ItemIds.LAVA_BUCKET, FluidIds.LAVA, SoundEventIds.BUCKET_EMPTY_LAVA)
                .behavior(FuelItemBehavior.of(FuelTimes.LAVA, this.items.getOrThrow(ItemIds.BUCKET)))
                .register();
            this.registerBucket(
                BlockItemIds.POWDER_SNOW.item(),
                1,
                BucketItemBehavior.placeBlock(
                    this.blocks.getOrThrow(BlockItemIds.POWDER_SNOW.block()),
                    this.soundEvents.getOrThrow(SoundEventIds.BUCKET_EMPTY_POWDER_SNOW),
                    this.items
                )
            );
            this.builderForBucketWithEntity(ItemIds.PUFFERFISH_BUCKET, EntityTypeIds.PUFFERFISH, SoundEventIds.BUCKET_EMPTY_FISH)
                .behavior(FoodItemBehavior.of(Foods.PUFFERFISH))
                .register();
            this.builderForBucketWithEntity(ItemIds.SALMON_BUCKET, EntityTypeIds.SALMON, SoundEventIds.BUCKET_EMPTY_FISH)
                .behavior(FoodItemBehavior.of(Foods.SALMON))
                .register();
            this.builderForBucketWithEntity(ItemIds.COD_BUCKET, EntityTypeIds.COD, SoundEventIds.BUCKET_EMPTY_FISH)
                .behavior(FoodItemBehavior.of(Foods.COD))
                .register();
            this.builderForBucketWithEntity(ItemIds.TROPICAL_FISH_BUCKET, EntityTypeIds.TROPICAL_FISH, SoundEventIds.BUCKET_EMPTY_FISH)
                .behavior(FoodItemBehavior.of(Foods.TROPICAL_FISH))
                .register();
            this.builderForBucketWithEntity(ItemIds.AXOLOTL_BUCKET, EntityTypeIds.AXOLOTL, SoundEventIds.BUCKET_EMPTY_AXOLOTL)
                .register();
            this.builderForBucketWithEntity(ItemIds.TADPOLE_BUCKET, EntityTypeIds.TADPOLE, SoundEventIds.BUCKET_EMPTY_TADPOLE)
                .register();
            this.registerBucket(
                ItemIds.SULFUR_CUBE_BUCKET,
                1,
                BucketItemBehavior.placeEntity(
                    this.entityTypes.getOrThrow(EntityTypeIds.SULFUR_CUBE),
                    this.items
                )
            );
            this.registerSpawnEgg(ItemIds.ALLAY_SPAWN_EGG, EntityTypeIds.ALLAY);
            this.registerSpawnEgg(ItemIds.ARMADILLO_SPAWN_EGG, EntityTypeIds.ARMADILLO);
            this.registerSpawnEgg(ItemIds.AXOLOTL_SPAWN_EGG, EntityTypeIds.AXOLOTL);
            this.registerSpawnEgg(ItemIds.BAT_SPAWN_EGG, EntityTypeIds.BAT);
            this.registerSpawnEgg(ItemIds.BEE_SPAWN_EGG, EntityTypeIds.BEE);
            this.registerSpawnEgg(ItemIds.BLAZE_SPAWN_EGG, EntityTypeIds.BLAZE);
            this.registerSpawnEgg(ItemIds.BOGGED_SPAWN_EGG, EntityTypeIds.BOGGED);
            this.registerSpawnEgg(ItemIds.BREEZE_SPAWN_EGG, EntityTypeIds.BREEZE);
            this.registerSpawnEgg(ItemIds.CAMEL_HUSK_SPAWN_EGG, EntityTypeIds.CAMEL_HUSK);
            this.registerSpawnEgg(ItemIds.CAMEL_SPAWN_EGG, EntityTypeIds.CAMEL);
            this.registerSpawnEgg(ItemIds.CAT_SPAWN_EGG, EntityTypeIds.CAT);
            this.registerSpawnEgg(ItemIds.CAVE_SPIDER_SPAWN_EGG, EntityTypeIds.CAVE_SPIDER);
            this.registerSpawnEgg(ItemIds.CHICKEN_SPAWN_EGG, EntityTypeIds.CHICKEN);
            this.registerSpawnEgg(ItemIds.COD_SPAWN_EGG, EntityTypeIds.COD);
            this.registerSpawnEgg(ItemIds.COPPER_GOLEM_SPAWN_EGG, EntityTypeIds.COPPER_GOLEM);
            this.registerSpawnEgg(ItemIds.COW_SPAWN_EGG, EntityTypeIds.COW);
            this.registerSpawnEgg(ItemIds.CREAKING_SPAWN_EGG, EntityTypeIds.CREAKING);
            this.registerSpawnEgg(ItemIds.CREEPER_SPAWN_EGG, EntityTypeIds.CREEPER);
            this.registerSpawnEgg(ItemIds.DOLPHIN_SPAWN_EGG, EntityTypeIds.DOLPHIN);
            this.registerSpawnEgg(ItemIds.DONKEY_SPAWN_EGG, EntityTypeIds.DONKEY);
            this.registerSpawnEgg(ItemIds.DROWNED_SPAWN_EGG, EntityTypeIds.DROWNED);
            this.registerSpawnEgg(ItemIds.ELDER_GUARDIAN_SPAWN_EGG, EntityTypeIds.ELDER_GUARDIAN);
            this.registerSpawnEgg(ItemIds.ENDERMAN_SPAWN_EGG, EntityTypeIds.ENDERMAN);
            this.registerSpawnEgg(ItemIds.ENDERMITE_SPAWN_EGG, EntityTypeIds.ENDERMITE);
            this.registerSpawnEgg(ItemIds.ENDER_DRAGON_SPAWN_EGG, EntityTypeIds.ENDER_DRAGON);
            this.registerSpawnEgg(ItemIds.EVOKER_SPAWN_EGG, EntityTypeIds.EVOKER);
            this.registerSpawnEgg(ItemIds.FOX_SPAWN_EGG, EntityTypeIds.FOX);
            this.registerSpawnEgg(ItemIds.FROG_SPAWN_EGG, EntityTypeIds.FROG);
            this.registerSpawnEgg(ItemIds.GHAST_SPAWN_EGG, EntityTypeIds.GHAST);
            this.registerSpawnEgg(ItemIds.GLOW_SQUID_SPAWN_EGG, EntityTypeIds.GLOW_SQUID);
            this.registerSpawnEgg(ItemIds.GOAT_SPAWN_EGG, EntityTypeIds.GOAT);
            this.registerSpawnEgg(ItemIds.GUARDIAN_SPAWN_EGG, EntityTypeIds.GUARDIAN);
            this.registerSpawnEgg(ItemIds.HAPPY_GHAST_SPAWN_EGG, EntityTypeIds.HAPPY_GHAST);
            this.registerSpawnEgg(ItemIds.HOGLIN_SPAWN_EGG, EntityTypeIds.HOGLIN);
            this.registerSpawnEgg(ItemIds.HORSE_SPAWN_EGG, EntityTypeIds.HORSE);
            this.registerSpawnEgg(ItemIds.HUSK_SPAWN_EGG, EntityTypeIds.HUSK);
            this.registerSpawnEgg(ItemIds.IRON_GOLEM_SPAWN_EGG, EntityTypeIds.IRON_GOLEM);
            this.registerSpawnEgg(ItemIds.LLAMA_SPAWN_EGG, EntityTypeIds.LLAMA);
            this.registerSpawnEgg(ItemIds.MAGMA_CUBE_SPAWN_EGG, EntityTypeIds.MAGMA_CUBE);
            this.registerSpawnEgg(ItemIds.MOOSHROOM_SPAWN_EGG, EntityTypeIds.MOOSHROOM);
            this.registerSpawnEgg(ItemIds.MULE_SPAWN_EGG, EntityTypeIds.MULE);
            this.registerSpawnEgg(ItemIds.NAUTILUS_SPAWN_EGG, EntityTypeIds.NAUTILUS);
            this.registerSpawnEgg(ItemIds.OCELOT_SPAWN_EGG, EntityTypeIds.OCELOT);
            this.registerSpawnEgg(ItemIds.PANDA_SPAWN_EGG, EntityTypeIds.PANDA);
            this.registerSpawnEgg(ItemIds.PARCHED_SPAWN_EGG, EntityTypeIds.PARCHED);
            this.registerSpawnEgg(ItemIds.PARROT_SPAWN_EGG, EntityTypeIds.PARROT);
            this.registerSpawnEgg(ItemIds.PHANTOM_SPAWN_EGG, EntityTypeIds.PHANTOM);
            this.registerSpawnEgg(ItemIds.PIGLIN_BRUTE_SPAWN_EGG, EntityTypeIds.PIGLIN_BRUTE);
            this.registerSpawnEgg(ItemIds.PIGLIN_SPAWN_EGG, EntityTypeIds.PIGLIN);
            this.registerSpawnEgg(ItemIds.PIG_SPAWN_EGG, EntityTypeIds.PIG);
            this.registerSpawnEgg(ItemIds.PILLAGER_SPAWN_EGG, EntityTypeIds.PILLAGER);
            this.registerSpawnEgg(ItemIds.POLAR_BEAR_SPAWN_EGG, EntityTypeIds.POLAR_BEAR);
            this.registerSpawnEgg(ItemIds.PUFFERFISH_SPAWN_EGG, EntityTypeIds.PUFFERFISH);
            this.registerSpawnEgg(ItemIds.RABBIT_SPAWN_EGG, EntityTypeIds.RABBIT);
            this.registerSpawnEgg(ItemIds.RAVAGER_SPAWN_EGG, EntityTypeIds.RAVAGER);
            this.registerSpawnEgg(ItemIds.SALMON_SPAWN_EGG, EntityTypeIds.SALMON);
            this.registerSpawnEgg(ItemIds.SHEEP_SPAWN_EGG, EntityTypeIds.SHEEP);
            this.registerSpawnEgg(ItemIds.SHULKER_SPAWN_EGG, EntityTypeIds.SHULKER);
            this.registerSpawnEgg(ItemIds.SILVERFISH_SPAWN_EGG, EntityTypeIds.SILVERFISH);
            this.registerSpawnEgg(ItemIds.SKELETON_HORSE_SPAWN_EGG, EntityTypeIds.SKELETON_HORSE);
            this.registerSpawnEgg(ItemIds.SKELETON_SPAWN_EGG, EntityTypeIds.SKELETON);
            this.registerSpawnEgg(ItemIds.SLIME_SPAWN_EGG, EntityTypeIds.SLIME);
            this.registerSpawnEgg(ItemIds.SNIFFER_SPAWN_EGG, EntityTypeIds.SNIFFER);
            this.registerSpawnEgg(ItemIds.SNOW_GOLEM_SPAWN_EGG, EntityTypeIds.SNOW_GOLEM);
            this.registerSpawnEgg(ItemIds.SPIDER_SPAWN_EGG, EntityTypeIds.SPIDER);
            this.registerSpawnEgg(ItemIds.SQUID_SPAWN_EGG, EntityTypeIds.SQUID);
            this.registerSpawnEgg(ItemIds.STRAY_SPAWN_EGG, EntityTypeIds.STRAY);
            this.registerSpawnEgg(ItemIds.STRIDER_SPAWN_EGG, EntityTypeIds.STRIDER);
            this.registerSpawnEgg(ItemIds.SULFUR_CUBE_SPAWN_EGG, EntityTypeIds.SULFUR_CUBE);
            this.registerSpawnEgg(ItemIds.TADPOLE_SPAWN_EGG, EntityTypeIds.TADPOLE);
            this.registerSpawnEgg(ItemIds.TRADER_LLAMA_SPAWN_EGG, EntityTypeIds.TRADER_LLAMA);
            this.registerSpawnEgg(ItemIds.TROPICAL_FISH_SPAWN_EGG, EntityTypeIds.TROPICAL_FISH);
            this.registerSpawnEgg(ItemIds.TURTLE_SPAWN_EGG, EntityTypeIds.TURTLE);
            this.registerSpawnEgg(ItemIds.VEX_SPAWN_EGG, EntityTypeIds.VEX);
            this.registerSpawnEgg(ItemIds.VILLAGER_SPAWN_EGG, EntityTypeIds.VILLAGER);
            this.registerSpawnEgg(ItemIds.VINDICATOR_SPAWN_EGG, EntityTypeIds.VINDICATOR);
            this.registerSpawnEgg(ItemIds.WANDERING_TRADER_SPAWN_EGG, EntityTypeIds.WANDERING_TRADER);
            this.registerSpawnEgg(ItemIds.WARDEN_SPAWN_EGG, EntityTypeIds.WARDEN);
            this.registerSpawnEgg(ItemIds.WITCH_SPAWN_EGG, EntityTypeIds.WITCH);
            this.registerSpawnEgg(ItemIds.WITHER_SKELETON_SPAWN_EGG, EntityTypeIds.WITHER_SKELETON);
            this.registerSpawnEgg(ItemIds.WITHER_SPAWN_EGG, EntityTypeIds.WITHER);
            this.registerSpawnEgg(ItemIds.WOLF_SPAWN_EGG, EntityTypeIds.WOLF);
            this.registerSpawnEgg(ItemIds.ZOGLIN_SPAWN_EGG, EntityTypeIds.ZOGLIN);
            this.registerSpawnEgg(ItemIds.ZOMBIE_HORSE_SPAWN_EGG, EntityTypeIds.ZOMBIE_HORSE);
            this.registerSpawnEgg(ItemIds.ZOMBIE_NAUTILUS_SPAWN_EGG, EntityTypeIds.ZOMBIE_NAUTILUS);
            this.registerSpawnEgg(ItemIds.ZOMBIE_SPAWN_EGG, EntityTypeIds.ZOMBIE);
            this.registerSpawnEgg(ItemIds.ZOMBIE_VILLAGER_SPAWN_EGG, EntityTypeIds.ZOMBIE_VILLAGER);
            this.registerSpawnEgg(ItemIds.ZOMBIFIED_PIGLIN_SPAWN_EGG, EntityTypeIds.ZOMBIFIED_PIGLIN);
            this.registerMinecart(ItemIds.MINECART, EntityTypeIds.MINECART);
            this.registerMinecart(ItemIds.CHEST_MINECART, EntityTypeIds.CHEST_MINECART);
            this.registerMinecart(ItemIds.FURNACE_MINECART, EntityTypeIds.FURNACE_MINECART);
            this.registerMinecart(ItemIds.TNT_MINECART, EntityTypeIds.TNT_MINECART);
            this.registerMinecart(ItemIds.HOPPER_MINECART, EntityTypeIds.HOPPER_MINECART);
            this.builderForMinecart(ItemIds.COMMAND_BLOCK_MINECART, EntityTypeIds.COMMAND_BLOCK_MINECART)
                .display(display -> display.rarity(Rarity.EPIC))
                .register();
            this.registerBoat(ItemIds.OAK_BOAT, EntityTypeIds.OAK_BOAT);
            this.registerBoat(ItemIds.OAK_CHEST_BOAT, EntityTypeIds.OAK_CHEST_BOAT);
            this.registerBoat(ItemIds.SPRUCE_BOAT, EntityTypeIds.SPRUCE_BOAT);
            this.registerBoat(ItemIds.SPRUCE_CHEST_BOAT, EntityTypeIds.SPRUCE_CHEST_BOAT);
            this.registerBoat(ItemIds.BIRCH_BOAT, EntityTypeIds.BIRCH_BOAT);
            this.registerBoat(ItemIds.BIRCH_CHEST_BOAT, EntityTypeIds.BIRCH_CHEST_BOAT);
            this.registerBoat(ItemIds.JUNGLE_BOAT, EntityTypeIds.JUNGLE_BOAT);
            this.registerBoat(ItemIds.JUNGLE_CHEST_BOAT, EntityTypeIds.JUNGLE_CHEST_BOAT);
            this.registerBoat(ItemIds.ACACIA_BOAT, EntityTypeIds.ACACIA_BOAT);
            this.registerBoat(ItemIds.ACACIA_CHEST_BOAT, EntityTypeIds.ACACIA_CHEST_BOAT);
            this.registerBoat(ItemIds.CHERRY_BOAT, EntityTypeIds.CHERRY_BOAT);
            this.registerBoat(ItemIds.CHERRY_CHEST_BOAT, EntityTypeIds.CHERRY_CHEST_BOAT);
            this.registerBoat(ItemIds.DARK_OAK_BOAT, EntityTypeIds.DARK_OAK_BOAT);
            this.registerBoat(ItemIds.DARK_OAK_CHEST_BOAT, EntityTypeIds.DARK_OAK_CHEST_BOAT);
            this.registerBoat(ItemIds.PALE_OAK_BOAT, EntityTypeIds.PALE_OAK_BOAT);
            this.registerBoat(ItemIds.PALE_OAK_CHEST_BOAT, EntityTypeIds.PALE_OAK_CHEST_BOAT);
            this.registerBoat(ItemIds.MANGROVE_BOAT, EntityTypeIds.MANGROVE_BOAT);
            this.registerBoat(ItemIds.MANGROVE_CHEST_BOAT, EntityTypeIds.MANGROVE_CHEST_BOAT);
            this.registerBoat(ItemIds.BAMBOO_RAFT, EntityTypeIds.BAMBOO_RAFT);
            this.registerBoat(ItemIds.BAMBOO_CHEST_RAFT, EntityTypeIds.BAMBOO_CHEST_RAFT);
            this.builder(ItemIds.ARMOR_STAND, 16)
                .behavior(
                    EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.ARMOR_STAND))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                SideCheckPredicate.builder(Direction.DOWN)
                            )
                            .spawnRule(FitsInVolumeEntitySpawnRule.entityDimensions())
                            .spawnRule(AlignYawEntitySpawnRule.of(8))
                            .spawnSound(this.soundEvents.getOrThrow(SoundEventIds.ARMOR_STAND_PLACE))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)))
                .register();
            this.builder(ItemIds.ITEM_FRAME)
                .behavior(
                    EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.ITEM_FRAME))
                            .allowItemData()
                            .build()
                    )
                )
                .register();
            this.builder(ItemIds.GLOW_ITEM_FRAME)
                .behavior(
                    EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(EntityTypeIds.GLOW_ITEM_FRAME))
                            .allowItemData()
                            .build()
                    )
                )
                .register();
            this.builder(ItemIds.PAINTING)
                .behavior(
                    EntityItemBehavior.of(
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
                    )
                )
                .register();
            this.builder(ItemIds.END_CRYSTAL)
                .display(ItemDisplay.Builder::glint)
                .behavior(
                    EntityItemBehavior.of(
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
                    )
                )
                .register();
            this.builderForProjectile(ItemIds.SNOWBALL, 16, EntityTypeIds.SNOWBALL)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .behavior(ThrowableItemBehavior.of(1.5f))
                .register();
            this.builderForProjectile(ItemIds.ENDER_PEARL, 16, EntityTypeIds.ENDER_PEARL)
                .behavior(ThrowableItemBehavior.of(1.5f))
                .behavior(CooldownItemBehavior.of(20))
                .register();
            this.builderForProjectile(ItemIds.ENDER_EYE, 64, EntityTypeIds.EYE_OF_ENDER)
                .behavior(ThrowableItemBehavior.of())
                .behavior(PreventUseWhenUsedOnTargetItemBehavior.forBlock())
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
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
                    )
                )
                .event(
                    ItemEvent.THROW_PROJECTILE,
                    ActionEntry.of(
                        PlaySoundAction.builder(PositionTarget.ORIGIN, this.soundEvents.getOrThrow(SoundEventIds.ENDER_EYE_LAUNCH), SoundSource.NEUTRAL)
                            .pitch(0.33f, 0.5f)
                            .build()
                    )
                )
                .register();
            this.builderForProjectile(ItemIds.EXPERIENCE_BOTTLE, 64, EntityTypeIds.EXPERIENCE_BOTTLE)
                .display(display -> display.rarity(Rarity.UNCOMMON).glint())
                .behavior(ThrowableItemBehavior.of(0.7f, -20.0f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_BOTTLE)))
                .register();
            this.builderForProjectile(ItemIds.FIRE_CHARGE, 64, EntityTypeIds.SMALL_FIREBALL)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_CHARGE)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(this.actions.getOrThrow(Actions.LIGHT_BLOCK))
                            .add(DecrementItemAction.of(1))
                            .add(PlaySoundAction.builder(PositionTarget.INTERACTED, this.soundEvents.getOrThrow(SoundEventIds.FIRE_CHARGE_USE), SoundSource.BLOCKS)
                                .pitch(0.8f, 1.2f)
                                .build())
                    )
                )
                .register();
            this.builderForProjectile(ItemIds.WIND_CHARGE, 64, EntityTypeIds.WIND_CHARGE)
                .behavior(ThrowableItemBehavior.of(1.5f))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .behavior(CooldownItemBehavior.of(10))
                .register();
            this.builder(ItemIds.DISC_FRAGMENT_5)
                .display(display -> display.rarity(Rarity.UNCOMMON).tooltip(Tooltips.description(ItemIds.DISC_FRAGMENT_5)))
                .register();
            this.registerMusicDisc(ItemIds.MUSIC_DISC_5, Rarity.UNCOMMON, JukeboxSongs.FIVE);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_11, Rarity.UNCOMMON, JukeboxSongs.ELEVEN);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_13, Rarity.UNCOMMON, JukeboxSongs.THIRTEEN);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_BLOCKS, Rarity.UNCOMMON, JukeboxSongs.BLOCKS);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_BOUNCE, Rarity.UNCOMMON, JukeboxSongs.BOUNCE);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_CAT, Rarity.UNCOMMON, JukeboxSongs.CAT);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_CHIRP, Rarity.UNCOMMON, JukeboxSongs.CHIRP);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_CREATOR, Rarity.RARE, JukeboxSongs.CREATOR);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_CREATOR_MUSIC_BOX, Rarity.UNCOMMON, JukeboxSongs.CREATOR_MUSIC_BOX);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_FAR, Rarity.UNCOMMON, JukeboxSongs.FAR);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_LAVA_CHICKEN, Rarity.RARE, JukeboxSongs.LAVA_CHICKEN);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_MALL, Rarity.UNCOMMON, JukeboxSongs.MALL);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_MELLOHI, Rarity.UNCOMMON, JukeboxSongs.MELLOHI);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_OTHERSIDE, Rarity.RARE, JukeboxSongs.OTHERSIDE);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_PIGSTEP, Rarity.RARE, JukeboxSongs.PIGSTEP);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_PRECIPICE, Rarity.UNCOMMON, JukeboxSongs.PRECIPICE);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_RELIC, Rarity.UNCOMMON, JukeboxSongs.RELIC);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_STAL, Rarity.UNCOMMON, JukeboxSongs.STAL);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_STRAD, Rarity.UNCOMMON, JukeboxSongs.STRAD);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_TEARS, Rarity.UNCOMMON, JukeboxSongs.TEARS);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_WAIT, Rarity.UNCOMMON, JukeboxSongs.WAIT);
            this.registerMusicDisc(ItemIds.MUSIC_DISC_WARD, Rarity.UNCOMMON, JukeboxSongs.WARD);
            this.registerBannerPattern(ItemIds.FLOWER_BANNER_PATTERN, Rarity.COMMON, BannerPatternTags.PATTERN_ITEM_FLOWER);
            this.registerBannerPattern(ItemIds.CREEPER_BANNER_PATTERN, Rarity.UNCOMMON, BannerPatternTags.PATTERN_ITEM_CREEPER);
            this.registerBannerPattern(ItemIds.SKULL_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_SKULL);
            this.registerBannerPattern(ItemIds.MOJANG_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_MOJANG);
            this.registerBannerPattern(ItemIds.GLOBE_BANNER_PATTERN, Rarity.COMMON, BannerPatternTags.PATTERN_ITEM_GLOBE);
            this.registerBannerPattern(ItemIds.PIGLIN_BANNER_PATTERN, Rarity.UNCOMMON, BannerPatternTags.PATTERN_ITEM_PIGLIN);
            this.registerBannerPattern(ItemIds.FLOW_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_FLOW);
            this.registerBannerPattern(ItemIds.GUSTER_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_GUSTER);
            this.registerBannerPattern(ItemIds.FIELD_MASONED_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_FIELD_MASONED);
            this.registerBannerPattern(ItemIds.BORDURE_INDENTED_BANNER_PATTERN, Rarity.RARE, BannerPatternTags.PATTERN_ITEM_BORDURE_INDENTED);
            this.registerSmithingTemplate(
                ItemIds.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
                Rarity.UNCOMMON,
                Tooltips.smithingUpgrade(Identifier.withDefaultNamespace("netherite_upgrade")),
                SmithingTemplates.ITEM_UPGRADE
            );
            this.registerTrimSmithingTemplate(ItemIds.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.EPIC);
            this.registerTrimSmithingTemplate(ItemIds.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerTrimSmithingTemplate(ItemIds.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, Rarity.UNCOMMON);
            this.registerDecoratedPotPattern(ItemIds.BRICK, Rarity.COMMON, DecoratedPotPatterns.BLANK);
            this.registerDecoratedPotPattern(ItemIds.ANGLER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.ANGLER);
            this.registerDecoratedPotPattern(ItemIds.ARCHER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.ARCHER);
            this.registerDecoratedPotPattern(ItemIds.ARMS_UP_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.ARMS_UP);
            this.registerDecoratedPotPattern(ItemIds.BLADE_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.BLADE);
            this.registerDecoratedPotPattern(ItemIds.BREWER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.BREWER);
            this.registerDecoratedPotPattern(ItemIds.BURN_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.BURN);
            this.registerDecoratedPotPattern(ItemIds.DANGER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.DANGER);
            this.registerDecoratedPotPattern(ItemIds.EXPLORER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.EXPLORER);
            this.registerDecoratedPotPattern(ItemIds.FLOW_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.FLOW);
            this.registerDecoratedPotPattern(ItemIds.FRIEND_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.FRIEND);
            this.registerDecoratedPotPattern(ItemIds.GUSTER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.GUSTER);
            this.registerDecoratedPotPattern(ItemIds.HEART_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.HEART);
            this.registerDecoratedPotPattern(ItemIds.HEARTBREAK_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.HEARTBREAK);
            this.registerDecoratedPotPattern(ItemIds.HOWL_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.HOWL);
            this.registerDecoratedPotPattern(ItemIds.MINER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.MINER);
            this.registerDecoratedPotPattern(ItemIds.MOURNER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.MOURNER);
            this.registerDecoratedPotPattern(ItemIds.PLENTY_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.PLENTY);
            this.registerDecoratedPotPattern(ItemIds.PRIZE_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.PRIZE);
            this.registerDecoratedPotPattern(ItemIds.SCRAPE_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.SCRAPE);
            this.registerDecoratedPotPattern(ItemIds.SHEAF_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.SHEAF);
            this.registerDecoratedPotPattern(ItemIds.SHELTER_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.SHELTER);
            this.registerDecoratedPotPattern(ItemIds.SKULL_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.SKULL);
            this.registerDecoratedPotPattern(ItemIds.SNORT_POTTERY_SHERD, Rarity.UNCOMMON, DecoratedPotPatterns.SNORT);
            this.builderForBlock(BlockItemIds.WHEAT_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builder(ItemIds.WHEAT)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.HAY_BLOCK)
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForConsumable(ItemIds.BREAD, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.BREAD))
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForBlock(BlockItemIds.CAKE, 1)
                .behavior(CompostableItemBehavior.of(CompostChances.GUARANTEED))
                .register();
            this.builderForConsumable(BlockItemIds.CARROT_CROP.item(), Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.CARROT))
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.CARROT_CROP.block())))
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForConsumable(ItemIds.GOLDEN_CARROT, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.GOLDEN_CARROT))
                .register();
            this.builderForConsumable(BlockItemIds.POTATO_CROP.item(), Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.POTATO))
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.POTATO_CROP.block())))
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForConsumable(ItemIds.BAKED_POTATO, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.BAKED_POTATO))
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForConsumable(ItemIds.POISONOUS_POTATO, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.POISONOUS_POTATO))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.6f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 100)
                        )
                    )
                )
                .register();
            this.builderForBlock(BlockItemIds.BEETROOT_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(ItemIds.BEETROOT, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.BEETROOT))
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForBlock(BlockItemIds.COCOA_CROP)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForConsumable(ItemIds.COOKIE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKIE))
                .behavior(CompostableItemBehavior.of(CompostChances.ALMOST_GUARANTEED))
                .register();
            this.builderForConsumable(BlockItemIds.SWEET_BERRY_CROP.item(), Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.SWEET_BERRIES))
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.SWEET_BERRY_CROP.block())))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(BlockItemIds.GLOW_BERRY_CROP.item(), Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.GLOW_BERRIES))
                .behavior(BlockItemBehavior.of(this.blocks.getOrThrow(BlockItemIds.GLOW_BERRY_CROP.block())))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(ItemIds.APPLE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.APPLE))
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.builderForConsumable(ItemIds.GOLDEN_APPLE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.GOLDEN_APPLE))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 100, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 2400)
                        )
                    )
                )
                .register();
            this.builderForConsumable(ItemIds.ENCHANTED_GOLDEN_APPLE, Consumables.DEFAULT_FOOD)
                .display(display -> display.rarity(Rarity.RARE).glint())
                .behavior(FoodItemBehavior.of(Foods.ENCHANTED_GOLDEN_APPLE))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.REGENERATION), 400, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.RESISTANCE), 6000),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.FIRE_RESISTANCE), 6000),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.ABSORPTION), 2400)
                        )
                    )
                )
                .register();
            this.builderForConsumable(ItemIds.DRIED_KELP, Consumables.DRIED_KELP)
                .behavior(FoodItemBehavior.of(Foods.DRIED_KELP))
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
            this.builderForConsumable(ItemIds.CHORUS_FRUIT, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.CHORUS_FRUIT))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(TeleportAction.of(16, LootContext.EntityTarget.THIS))
                )
                .register();
            this.registerItem(ItemIds.POPPED_CHORUS_FRUIT);
            this.builderForConsumable(ItemIds.PORKCHOP, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.PORKCHOP))
                .register();
            this.builderForConsumable(ItemIds.COOKED_PORKCHOP, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_PORKCHOP))
                .register();
            this.builderForConsumable(ItemIds.BEEF, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.BEEF))
                .register();
            this.builderForConsumable(ItemIds.COOKED_BEEF, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_BEEF))
                .register();
            this.builderForConsumable(ItemIds.CHICKEN, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.CHICKEN))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.3f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 600)
                        )
                    )
                )
                .register();
            this.builderForConsumable(ItemIds.COOKED_CHICKEN, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_CHICKEN))
                .register();
            this.registerThrowableEgg(ItemIds.EGG, ChickenVariants.TEMPERATE);
            this.registerThrowableEgg(ItemIds.BLUE_EGG, ChickenVariants.COLD);
            this.registerThrowableEgg(ItemIds.BROWN_EGG, ChickenVariants.WARM);
            this.builderForConsumable(ItemIds.MUTTON, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.MUTTON))
                .register();
            this.builderForConsumable(ItemIds.COOKED_MUTTON, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_MUTTON))
                .register();
            this.builderForConsumable(ItemIds.COD, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COD))
                .register();
            this.builderForConsumable(ItemIds.COOKED_COD, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_COD))
                .register();
            this.builderForConsumable(ItemIds.SALMON, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.SALMON))
                .register();
            this.builderForConsumable(ItemIds.COOKED_SALMON, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_SALMON))
                .register();
            this.builderForConsumable(ItemIds.TROPICAL_FISH, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.TROPICAL_FISH))
                .register();
            this.builderForConsumable(ItemIds.PUFFERFISH, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.PUFFERFISH))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 1200, 1),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.HUNGER), 300, 2),
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.NAUSEA), 300)
                        )
                    )
                )
                .register();
            this.registerFuel(ItemIds.BOWL, FuelTimes.SMALL_WOODEN_ITEM);
            this.builderForConsumableBowl(ItemIds.MUSHROOM_STEW)
                .behavior(FoodItemBehavior.of(Foods.MUSHROOM_STEW))
                .register();
            this.builderForConsumable(ItemIds.RABBIT, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.RABBIT))
                .register();
            this.builderForConsumable(ItemIds.COOKED_RABBIT, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.COOKED_RABBIT))
                .register();
            this.builderForConsumableBowl(ItemIds.RABBIT_STEW)
                .behavior(FoodItemBehavior.of(Foods.RABBIT_STEW))
                .register();
            this.registerItem(ItemIds.RABBIT_FOOT);
            this.registerItem(ItemIds.RABBIT_HIDE);
            this.builderForConsumableBowl(ItemIds.BEETROOT_SOUP)
                .behavior(FoodItemBehavior.of(Foods.BEETROOT_SOUP))
                .register();
            this.builderForConsumableBowl(ItemIds.SUSPICIOUS_STEW)
                .behavior(FoodItemBehavior.of(Foods.SUSPICIOUS_STEW))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(ApplySuspiciousStewEffectsFromItemAction.of(LootContext.EntityTarget.THIS))
                )
                .register();
            this.builderForConsumable(ItemIds.ROTTEN_FLESH, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.ROTTEN_FLESH))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        LootItemRandomChanceCondition.randomChance(0.8f),
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.HUNGER), 600)
                        )
                    )
                )
                .register();
            this.builderForConsumable(ItemIds.SPIDER_EYE, Consumables.DEFAULT_FOOD)
                .behavior(FoodItemBehavior.of(Foods.SPIDER_EYE))
                .event(
                    ItemEvent.CONSUME_ITEM,
                    ActionEntry.of(
                        AddStatusEffectsAction.of(
                            new MobEffectInstance(this.statusEffects.getOrThrow(MobEffectIds.POISON), 100)
                        )
                    )
                )
                .register();
            this.builder(ItemIds.BOOK)
                .behavior(EnchantableItemBehavior.ofTransforming(1, this.items.getOrThrow(ItemIds.ENCHANTED_BOOK)))
                .register();
            this.builder(ItemIds.WRITABLE_BOOK, 1)
                .behavior(WritableItemBehavior.of(this.items.getOrThrow(ItemIds.WRITTEN_BOOK)))
                .register();
            this.builder(ItemIds.WRITTEN_BOOK, 16)
                .display(ItemDisplay.Builder::glint)
                .behavior(TextHolderItemBehavior.INSTANCE)
                .register();
            this.builder(ItemIds.ENCHANTED_BOOK, 1)
                .display(display -> display.rarity(Rarity.UNCOMMON).glint())
                .behavior(EnchantmentHolderItemBehavior.of(this.items.getOrThrow(ItemIds.BOOK)))
                .register();
            this.builder(ItemIds.MAP)
                .behavior(MappableItemBehavior.of(this.items.getOrThrow(ItemIds.FILLED_MAP)))
                .register();
            this.builder(ItemIds.FILLED_MAP)
                .behavior(MapHolderItemBehavior.INSTANCE)
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(MarkBannerOnItemAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    )
                )
                .register();
            this.builder(ItemIds.LEAD)
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(AttachLeashedEntitiesOnBlockAction.of(PositionTarget.INTERACTED))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    )
                )
                .register();
            this.builder(ItemIds.NAME_TAG)
                .event(
                    ItemEvent.USE_ON_ENTITY,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(SetEntityNameFromItemAction.of(LootContext.EntityTarget.TARGET_ENTITY))
                            .add(DecrementItemAction.of(1))
                            .add(SwingHandAction.of(LootContext.EntityTarget.THIS))
                    )
                )
                .register();
            ColorCollection.zipApply(ItemIds.DYE, ColorCollection.VALUES, (dye, dyeColor) -> this.builder(dye)
                .behavior(DyeItemBehavior.of(dyeColor))
                .register()
            );
            this.registerBundle(ItemIds.BUNDLE);
            ItemIds.DYED_BUNDLE.forEach(this::registerBundle);
            this.registerFuel(ItemIds.COAL, FuelTimes.COAL);
            this.registerFuel(ItemIds.CHARCOAL, FuelTimes.COAL);
            this.registerTrimMaterialProvider(ItemIds.IRON_INGOT, TrimMaterials.IRON);
            this.registerItem(ItemIds.RAW_IRON);
            this.registerItem(ItemIds.IRON_NUGGET);
            this.registerTrimMaterialProvider(ItemIds.COPPER_INGOT, TrimMaterials.COPPER);
            this.registerItem(ItemIds.RAW_COPPER);
            this.registerItem(ItemIds.COPPER_NUGGET);
            this.registerTrimMaterialProvider(ItemIds.GOLD_INGOT, TrimMaterials.GOLD);
            this.registerItem(ItemIds.RAW_GOLD);
            this.registerItem(ItemIds.GOLD_NUGGET);
            this.registerTrimMaterialProvider(ItemIds.DIAMOND, TrimMaterials.DIAMOND);
            this.builder(ItemIds.NETHERITE_INGOT)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .behavior(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(TrimMaterials.NETHERITE)))
                .register();
            this.builder(ItemIds.NETHERITE_SCRAP)
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_FIRE)))
                .register();
            this.registerTrimMaterialProvider(ItemIds.EMERALD, TrimMaterials.EMERALD);
            this.registerTrimMaterialProvider(ItemIds.LAPIS_LAZULI, TrimMaterials.LAPIS);
            this.registerTrimMaterialProvider(ItemIds.QUARTZ, TrimMaterials.QUARTZ);
            this.registerTrimMaterialProvider(ItemIds.AMETHYST_SHARD, TrimMaterials.AMETHYST);
            this.registerTrimMaterialProvider(ItemIds.RESIN_BRICK, TrimMaterials.RESIN);
            this.builder(ItemIds.NETHER_STAR)
                .display(display -> display.rarity(Rarity.RARE).glint())
                .behavior(ImmuneToDamageItemBehavior.of(this.damageTypes.getOrThrow(DamageTypeTags.IS_EXPLOSION)))
                .register();
            this.registerFuel(ItemIds.STICK, FuelTimes.SMALL_WOODEN_ITEM);
            this.registerItem(ItemIds.FLINT);
            this.registerItem(ItemIds.BONE);
            this.builder(ItemIds.BONE_MEAL)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_ITEM_ON_BLOCK)))
                .event(
                    ItemEvent.USE_ON_BLOCK,
                    ActionEntry.of(
                        PassingSequenceHandler.builder()
                            .add(FertilizeAction.of(PositionTarget.INTERACTED))
                            .add(
                                InvokeGameEventAction.of(
                                    GameEvent.ITEM_INTERACT_FINISH,
                                    PositionTarget.ORIGIN,
                                    LootContext.EntityTarget.THIS
                                )
                            )
                            .add(DecrementItemAction.of(1))
                    )
                )
                .register();
            this.registerItem(ItemIds.FEATHER);
            this.registerItem(ItemIds.GUNPOWDER);
            this.builder(ItemIds.FIREWORK_STAR)
                .behavior(FireworkExplosionHolderItemBehavior.INSTANCE)
                .register();
            this.builderForProjectile(ItemIds.FIREWORK_ROCKET, 64, EntityTypeIds.FIREWORK_ROCKET)
                .behavior(FireworkItemBehavior.INSTANCE)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_FIREWORK_ROCKET)))
                .register();
            this.registerItem(ItemIds.LEATHER);
            this.registerItem(ItemIds.CLAY_BALL);
            this.registerItem(ItemIds.PAPER);
            this.registerItem(ItemIds.GLOWSTONE_DUST);
            this.builder(ItemIds.INK_SAC)
                .event(ItemEvent.USE_ON_BLOCK, Actions.glowSign(this.blocks, false))
                .register();
            this.builder(ItemIds.GLOW_INK_SAC)
                .event(ItemEvent.USE_ON_BLOCK, Actions.glowSign(this.blocks, true))
                .register();
            this.builderForBlock(BlockItemIds.NETHER_WART)
                .display(ItemDisplay.Builder::itemName)
                .behavior(CompostableItemBehavior.of(CompostChances.BIG))
                .register();
            this.registerFuel(ItemIds.BLAZE_ROD, FuelTimes.BLAZE_ROD);
            this.registerItem(ItemIds.BLAZE_POWDER);
            this.registerItem(ItemIds.SUGAR);
            this.registerItem(ItemIds.GHAST_TEAR);
            this.registerItem(ItemIds.FERMENTED_SPIDER_EYE);
            this.registerItem(ItemIds.MAGMA_CREAM);
            this.registerBlock(BlockItemIds.SLIME_BLOCK);
            this.registerItem(ItemIds.SLIME_BALL);
            this.registerItem(ItemIds.NETHER_BRICK);
            this.registerItem(ItemIds.PRISMARINE_SHARD);
            this.registerItem(ItemIds.PRISMARINE_CRYSTALS);
            this.builder(ItemIds.KNOWLEDGE_BOOK, 1)
                .display(display -> display.rarity(Rarity.EPIC))
                .behavior(UnlockRecipesItemBehavior.INSTANCE)
                .register();
            this.builder(ItemIds.DEBUG_STICK, 1)
                .display(display -> display.rarity(Rarity.EPIC).glint())
                .behavior(DebugStickItemBehavior.INSTANCE)
                .register();
        }

        private Builder builderForConsumable(ResourceKey<Item> item, Consumable consumable) {
            return this.builder(item)
                .behavior(UseableItemBehavior.of(consumable))
                .behavior(ConsumableItemBehavior.of(consumable));
        }

        private Builder builderForConsumable(ResourceKey<Item> item, Consumable consumable, ResourceKey<Item> remainder) {
            return this.builder(item, 1)
                .behavior(UseableItemBehavior.of(consumable, this.items.getOrThrow(remainder)))
                .behavior(ConsumableItemBehavior.of(consumable));
        }

        private Builder builderForConsumableBowl(ResourceKey<Item> item) {
            return this.builderForConsumable(item, Consumables.DEFAULT_FOOD, ItemIds.BOWL);
        }

        private void registerBucket(ResourceKey<Item> item, int maxStackSize, BucketItemBehavior bucket) {
            this.builderForBucket(item, maxStackSize, bucket).register();
        }

        private Builder builderForBucket(ResourceKey<Item> item, int maxStackSize, BucketItemBehavior bucket) {
            return this.builder(item, maxStackSize)
                .behavior(bucket)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.USE_BUCKET)));
        }

        private Builder builderForBucketWithFluid(ResourceKey<Item> item, ResourceKey<Fluid> fluid, ResourceKey<SoundEvent> placeSound) {
            return this.builderForBucket(
                item,
                1,
                BucketItemBehavior.placeFluid(
                    this.fluids.getOrThrow(fluid),
                    this.soundEvents.getOrThrow(placeSound),
                    this.items
                )
            );
        }

        private Builder builderForBucketWithEntity(ResourceKey<Item> item, ResourceKey<EntityType<?>> entity, ResourceKey<SoundEvent> placeSound) {
            return this.builderForBucket(
                item,
                1,
                BucketItemBehavior.placeFluidWithEntity(
                    this.fluids.getOrThrow(FluidIds.WATER),
                    this.entityTypes.getOrThrow(entity),
                    this.soundEvents.getOrThrow(placeSound),
                    this.items
                )
            );
        }

        private void registerSpawnEgg(ResourceKey<Item> item, ResourceKey<EntityType<?>> entity) {
            this.builder(item)
                .behavior(
                    EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(entity))
                            .allowItemData()
                            .build(),
                        true,
                        EntityItemBehavior.Pass.BLOCK,
                        EntityItemBehavior.Pass.FLUID
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)))
                .behavior(SpawnEggItemBehavior.INSTANCE)
                .register();
        }

        private void registerMusicDisc(ResourceKey<Item> item, Rarity rarity, ResourceKey<JukeboxSong> jukeboxSong) {
            this.builder(item, 1)
                .display(display -> display.rarity(rarity))
                .behavior(PlayableSongItemBehavior.of(this.jukeboxSongs.getOrThrow(jukeboxSong)))
                .register();
        }

        private void registerSuspiciousEffectIngredient(BlockItemId blockItem, float compostChance, ResourceKey<MobEffect> effect, int effectDuration, ResourceKey<Block> pottedBlock) {
            this.builderForBlock(blockItem)
                .behavior(CompostableItemBehavior.of(compostChance))
                .behavior(
                    SuspiciousEffectIngredientItemBehavior.of(
                        new SuspiciousStewEffects.Entry(this.statusEffects.getOrThrow(effect), effectDuration)
                    )
                )
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, pottedBlock))
                .register();
        }

        private void registerTrimSmithingTemplate(ResourceKey<Item> item, Rarity rarity) {
            this.registerSmithingTemplate(
                item,
                rarity,
                Tooltips.smithingTrimPattern(),
                SmithingTemplates.TRIM_PATTERN
            );
        }

        private void registerSmithingTemplate(ResourceKey<Item> item, Rarity rarity, Component[] tooltip, SmithingTemplate smithingTemplate) {
            this.builder(item)
                .display(display -> display.rarity(rarity).tooltip(tooltip))
                .behavior(SmithingTemplateProviderItemBehavior.of(smithingTemplate))
                .register();
        }

        private void registerBannerPattern(ResourceKey<Item> item, Rarity rarity, TagKey<BannerPattern> bannerPatterns) {
            this.builder(item, 1)
                .display(display -> display.rarity(rarity))
                .behavior(BannerPatternItemBehavior.of(this.bannerPatterns.getOrThrow(bannerPatterns)))
                .register();
        }

        private void registerDecoratedPotPattern(ResourceKey<Item> item, Rarity rarity, ResourceKey<DecoratedPotPattern> decoratedPotPattern) {
            this.builder(item)
                .display(display -> display.rarity(rarity))
                .behavior(DecoratedPotPatternItemBehavior.of(this.decoratedPotPatterns.getOrThrow(decoratedPotPattern)))
                .register();
        }

        private void registerTrimMaterialProvider(ResourceKey<Item> item, ResourceKey<TrimMaterial> trimMaterial) {
            this.builder(item)
                .behavior(TrimMaterialProviderItemBehavior.of(this.trimMaterials.getOrThrow(trimMaterial)))
                .register();
        }

        private void registerBundle(ResourceKey<Item> item) {
            this.builder(item, 1)
                .display(display -> display.itemBarStyle(ItemBarStyleIds.BUNDLE))
                .behavior(UseableItemBehavior.builder().useFor(BundleItemAccessor.useDuration()).build())
                .behavior(
                    ItemHolderItemBehavior.of(
                        Fraction.ONE,
                        ItemHolderRules.builder()
                            .rule(
                                RejectItemHolderRule.INSTANCE,
                                ItemPredicate.Builder.item()
                                    .itematic$items(this.items.getOrThrow(ItematicItemTags.BANNED_BUNDLE_ITEMS))
                                    .build()
                            )
                            .rule(
                                OccupancyHeldItemsWithPenaltyItemHolderRule.of(BundleContentsAccessor.nestedBundleOccupancy()),
                                ItemPredicate.Builder.item()
                                    .withComponents(DataComponentMatchers.Builder.components()
                                        .any(DataComponents.BUNDLE_CONTENTS)
                                        .build())
                                    .build()
                            )
                            .rule(
                                FractionItemHolderRule.of(Fraction.ONE),
                                ItemPredicate.Builder.item()
                                    .withComponents(DataComponentMatchers.Builder.components()
                                        .any(DataComponents.BEES)
                                        .build())
                                    .build()
                            )
                            .build(),
                        this.soundEvents.getOrThrow(SoundEventIds.BUNDLE_INSERT),
                        this.soundEvents.getOrThrow(SoundEventIds.BUNDLE_INSERT_FAIL),
                        this.soundEvents.getOrThrow(SoundEventIds.BUNDLE_REMOVE_ONE),
                        this.soundEvents.getOrThrow(SoundEventIds.BUNDLE_DROP_CONTENTS)
                    )
                )
                .register();
        }

        private void registerSkull(BlockItemId blockItem, Rarity rarity, ResourceKey<Block> wallSkull) {
            this.builderForBlockAttachedToSide(blockItem, wallSkull, Direction.DOWN)
                .display(display -> display.rarity(rarity))
                .attributeModifiers(AttributeModifiers::hideFromLocatorBar)
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.HEAD)
                            .setSwappable(false)
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY_HEAD)))
                .register();
        }

        private void registerFuel(ResourceKey<Item> item, int fuelTicks) {
            this.builder(item)
                .behavior(FuelItemBehavior.of(fuelTicks))
                .register();
        }

        private void registerSword(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            this.builderForSword(item, material, repairItems)
                .register();
        }

        private Builder builderForSword(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            return this.builder(item, 1)
                .behavior(DamageableItemBehavior.of(material.durability()))
                .behavior(
                    ToolItemBehavior.builder(2)
                        .preventCreativeDestruction()
                        .rule(Tool.Rule.minesAndDrops(HolderSet.direct(this.blocks.getOrThrow(BlockItemIds.COBWEB.block())), 15.0f))
                        .rule(Tool.Rule.overrideSpeed(this.blocks.getOrThrow(BlockTags.SWORD_EFFICIENT), 1.5f))
                        .build()
                )
                .behavior(
                    WeaponItemBehavior.builder(1, 4.0d + material.attackDamageBonus(), 0.4d)
                        .build()
                )
                .behavior(EnchantableItemBehavior.of(material))
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(repairItems)));
        }

        private void registerSpear(ResourceKey<Item> item, ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountSpeedThreshold, float knockbackTime, float damageTime, TagKey<Item> repairItems) {
            this.builderForSpear(item, material, attackDuration, damageMultiplier, delay, dismountTime, dismountSpeedThreshold, knockbackTime, damageTime, repairItems)
                .register();
        }

        private Builder builderForSpear(ResourceKey<Item> item, ToolMaterial material, float attackDuration, float damageMultiplier, float delay, float dismountTime, float dismountSpeedThreshold, float knockbackTime, float damageTime, TagKey<Item> repairItems) {
            return this.builder(item, 1)
                .behavior(DamageableItemBehavior.of(material.durability()))
                .behavior(
                    UseableItemBehavior.builder()
                        .useIndefinitely()
                        .animation(ItemUseAnimation.SPEAR)
                        .effects(new UseEffects(
                            true,
                            false,
                            1.0f
                        ))
                        .build()
                )
                .behavior(
                    WeaponItemBehavior.builder(1, material.attackDamageBonus(), 1 / (4 * attackDuration))
                        .type(
                            MeleeWeaponComponents.KINETIC,
                            KineticMeleeWeapon.of(
                                new KineticWeapon(
                                    10,
                                    (int)(delay * SharedConstants.TICKS_PER_SECOND),
                                    KineticWeapon.Condition.ofAttackerSpeed(
                                        (int)(dismountTime * SharedConstants.TICKS_PER_SECOND),
                                        dismountSpeedThreshold
                                    ),
                                    KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackTime * SharedConstants.TICKS_PER_SECOND), 5.1f),
                                    KineticWeapon.Condition.ofRelativeSpeed((int)(damageTime * SharedConstants.TICKS_PER_SECOND), 4.6f),
                                    0.38f,
                                    damageMultiplier,
                                    Optional.of(
                                        material == ToolMaterial.WOOD
                                            ? this.soundEvents.getOrThrow(SoundEventIds.SPEAR_WOOD_USE)
                                            : this.soundEvents.getOrThrow(SoundEventIds.SPEAR_USE)
                                    ),
                                    Optional.of(
                                        material == ToolMaterial.WOOD
                                            ? this.soundEvents.getOrThrow(SoundEventIds.SPEAR_WOOD_HIT)
                                            : this.soundEvents.getOrThrow(SoundEventIds.SPEAR_HIT)
                                    )
                                )
                            )
                        )
                        .type(
                            MeleeWeaponComponents.PIERCING,
                            PiercingMeleeWeapon.of(
                                new PiercingWeapon(
                                    true,
                                    false,
                                    Optional.of(material == ToolMaterial.WOOD
                                        ? this.soundEvents.getOrThrow(SoundEventIds.SPEAR_WOOD_ATTACK)
                                        : this.soundEvents.getOrThrow(SoundEventIds.SPEAR_ATTACK)
                                    ),
                                    Optional.of(material == ToolMaterial.WOOD
                                        ? this.soundEvents.getOrThrow(SoundEventIds.SPEAR_WOOD_HIT)
                                        : this.soundEvents.getOrThrow(SoundEventIds.SPEAR_HIT)
                                    )
                                )
                            )
                        )
                        .damageType(this.damageTypes.getOrThrow(DamageTypes.SPEAR))
                        .swingAnimation(
                            new SwingAnimation(
                                SwingAnimationType.STAB,
                                (int)(attackDuration * SharedConstants.TICKS_PER_SECOND)
                            )
                        )
                        .attackRange(
                            new AttackRange(
                                2.0f,
                                4.5f,
                                2.0f,
                                6.5f,
                                0.125f,
                                0.5f
                            )
                        )
                        .minimumAttackCharge(1.0f)
                        .build()
                )
                .behavior(EnchantableItemBehavior.of(material))
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(repairItems)));
        }

        private Builder builderForTool(ResourceKey<Item> item, ToolMaterial material, float disableBlockingForSeconds, double baseAttackDamage, double attackSpeed, TagKey<Block> mineableBlocks, TagKey<Item> repairItems) {
            return this.builder(item, 1)
                .behavior(DamageableItemBehavior.of(material.durability()))
                .behavior(ToolItemBehavior.of(this.blocks, material, mineableBlocks))
                .behavior(
                    WeaponItemBehavior.builder(2, baseAttackDamage + material.attackDamageBonus(), attackSpeed)
                        .disableBlockingForSeconds(disableBlockingForSeconds)
                        .build()
                )
                .behavior(EnchantableItemBehavior.of(material))
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(repairItems)));
        }

        private void registerShovel(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            this.builderForShovel(item, material, repairItems)
                .register();
        }

        private Builder builderForShovel(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            return this.builderForTool(item, material, 0.0f, 2.5d, 0.25d, BlockTags.MINEABLE_WITH_SHOVEL, repairItems)
                .event(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_SHOVEL_ON_BLOCK));
        }

        private void registerPickaxe(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            this.builderForPickaxe(item, material, repairItems)
                .register();
        }

        private Builder builderForPickaxe(ResourceKey<Item> item, ToolMaterial material, TagKey<Item> repairItems) {
            return this.builderForTool(item, material, 0.0f, 2.0d, 0.3d, BlockTags.MINEABLE_WITH_PICKAXE, repairItems);
        }

        private void registerAxe(ResourceKey<Item> item, ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItems) {
            this.builderForAxe(item, material, attackDamage, attackSpeed, repairItems)
                .register();
        }

        private Builder builderForAxe(ResourceKey<Item> item, ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItems) {
            return this.builderForTool(item, material, 5.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_AXE, repairItems);
        }

        private void registerHoe(ResourceKey<Item> item, ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItems) {
            this.builderForHoe(item, material, attackDamage, attackSpeed, repairItems)
                .register();
        }

        private Builder builderForHoe(ResourceKey<Item> item, ToolMaterial material, double attackDamage, double attackSpeed, TagKey<Item> repairItems) {
            return this.builderForTool(item, material, 0.0f, attackDamage, attackSpeed, BlockTags.MINEABLE_WITH_HOE, repairItems)
                .event(ItemEvent.USE_ON_BLOCK, this.actions.getOrThrow(Actions.USE_HOE_ON_BLOCK));
        }

        private void registerArmor(ResourceKey<Item> item, ArmorMaterial material, ArmorType type, TagKey<Item> repairItems) {
            this.builderForArmor(item, material, type, repairItems)
                .register();
        }

        private Builder builderForArmor(ResourceKey<Item> item, ArmorMaterial material, ArmorType type, TagKey<Item> repairItems) {
            return this.builder(item, 1)
                .attributeModifiers(builder -> AttributeModifiers.armor(builder, material, type))
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(type.getSlot())
                            .setSwappable(true)
                            .setEquipSound(material.equipSound())
                            .setAsset(material.assetId())
                            .build()
                    )
                )
                .behavior(DamageableItemBehavior.of(type.getDurability(material.durability())))
                .behavior(EnchantableItemBehavior.of(material))
                .behavior(RepairableItemBehavior.of(this.items.getOrThrow(repairItems)))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)));
        }

        private void registerHorseArmor(ResourceKey<Item> item, ArmorMaterial material) {
            this.builderForHorseArmor(item, material)
                .register();
        }

        private Builder builderForHorseArmor(ResourceKey<Item> item, ArmorMaterial material) {
            return this.builder(item, 1)
                .attributeModifiers(builder -> AttributeModifiers.armor(builder, material, ArmorType.BODY))
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.BODY)
                            .setEquipSound(this.soundEvents.getOrThrow(SoundEventIds.HORSE_ARMOR))
                            .setAsset(material.assetId())
                            .setAllowedEntities(this.entityTypes.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
                            .setDamageOnHurt(false)
                            .setCanBeSheared(true)
                            .setShearingSound(this.soundEvents.getOrThrow(SoundEventIds.HORSE_ARMOR_UNEQUIP))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)));
        }

        private void registerNautilusArmor(ResourceKey<Item> item, ArmorMaterial material) {
            this.builderForNautilusArmor(item, material)
                .register();
        }

        private Builder builderForNautilusArmor(ResourceKey<Item> item, ArmorMaterial material) {
            return this.builder(item, 1)
                .attributeModifiers(builder -> AttributeModifiers.armor(builder, material, ArmorType.BODY))
                .behavior(
                    EquipmentItemBehavior.of(
                        Equippable.builder(EquipmentSlot.BODY)
                            .setEquipSound(soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_NAUTILUS))
                            .setAsset(material.assetId())
                            .setAllowedEntities(entityTypes.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
                            .setDamageOnHurt(false)
                            .setEquipOnInteract(true)
                            .setCanBeSheared(true)
                            .setShearingSound(soundEvents.getOrThrow(SoundEventIds.ARMOR_UNEQUIP_NAUTILUS))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY)));
        }

        private void registerMinecart(ResourceKey<Item> item, ResourceKey<EntityType<?>> entity) {
            this.builderForMinecart(item, entity)
                .register();
        }

        private Builder builderForMinecart(ResourceKey<Item> item, ResourceKey<EntityType<?>> entity) {
            return this.builder(item, 1)
                .behavior(
                    EntityItemBehavior.of(
                        EntitySpawner.builder(this.entityTypes.getOrThrow(entity))
                            .spawnRule(
                                DiscardEntitySpawnRule.INSTANCE,
                                InvertedLootItemCondition.invert(
                                    LocationCheckPredicates.builder(
                                        PositionTarget.INTERACTED,
                                        LocationPredicate.Builder.location()
                                            .setBlock(BlockPredicate.Builder.block()
                                                .of(blocks, BlockTags.RAILS))
                                    )
                                )
                            )
                            .spawnRule(OffsetSpawnPositionEntitySpawnRule.of(new Vec3(0.0d, 0.0625d, 0.0d)))
                            .spawnRule(
                                OffsetSpawnPositionEntitySpawnRule.of(new Vec3(0.0d, 0.5d, 0.0d)),
                                LocationCheckPredicates.builder(
                                    PositionTarget.INTERACTED,
                                    LocationPredicate.Builder.location()
                                        .setBlock(BlockPredicate.Builder.block()
                                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                                .itematic$range(BlockStateProperties.RAIL_SHAPE, RailShape.ASCENDING_EAST, RailShape.ASCENDING_SOUTH)))
                                )
                            )
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)));
        }

        private void registerBoat(ResourceKey<Item> item, ResourceKey<EntityType<?>> entity) {
            this.builder(item, 1)
                .behavior(EntityItemBehavior.of(EntitySpawner.of(this.entityTypes.getOrThrow(entity))))
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SPAWN_ENTITY_FROM_ITEM)))
                .behavior(FuelItemBehavior.of(FuelTimes.BOAT))
                .register();
        }

        private Builder builderForProjectile(ResourceKey<Item> item, int maxStackSize, ResourceKey<EntityType<?>> entity) {
            return this.builder(item, maxStackSize)
                .behavior(ProjectileItemBehavior.of(this.entityTypes.getOrThrow(entity)));
        }

        private void registerThrowableEgg(ResourceKey<Item> item, ResourceKey<ChickenVariant> chickenVariant) {
            this.builder(item, 16)
                .behavior(ThrowableItemBehavior.of(1.5f))
                .behavior(
                    ProjectileItemBehavior.of(
                        this.entityTypes.getOrThrow(EntityTypeIds.EGG),
                        DataComponentPatch.builder()
                            .set(DataComponents.CHICKEN_VARIANT, this.chickenVariants.getOrThrow(chickenVariant))
                            .build()
                    )
                )
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.SHOOT_PROJECTILE)))
                .register();
        }

        public void registerShulkerBox(BlockItemId blockItem) {
            this.builderForBlock(blockItem, 1)
                .behavior(DispensableItemBehavior.of(this.dispenseBehaviors.getOrThrow(DispenseBehaviors.PLACE_BLOCK_FROM_ITEM)))
                .register();
        }

        public void registerBurningWoodBlock(BlockItemId blockItem) {
            this.builderForBlock(blockItem)
                .behavior(FuelItemBehavior.of(FuelTimes.WOOD))
                .register();
        }

        public void registerBurningSlab(BlockItemId blockItem) {
            this.builderForBlock(blockItem)
                .behavior(FuelItemBehavior.of(FuelTimes.SLAB))
                .register();
        }

        public Consumer<BlockItemId> registerBurningBlock(int fuelTicks) {
            return blockItem -> this.builderForBlock(blockItem)
                .behavior(FuelItemBehavior.of(fuelTicks))
                .register();
        }

        public void registerPottableSapling(BlockItemId blockItem, ResourceKey<Block> pottedBlock, int fuelTicks, float compostChance) {
            this.builderForBlock(blockItem)
                .behavior(compostChance > 0.0f, CompostableItemBehavior.of(compostChance))
                .behavior(fuelTicks > 0, FuelItemBehavior.of(fuelTicks))
                .cancellableEvent(ItemEvent.BEFORE_USE_ON_BLOCK, Actions.potBlock(this.blocks, pottedBlock))
                .register();
        }

        public void registerCompostableLeaves(BlockItemId blockItem) {
            this.builderForBlock(blockItem)
                .behavior(CompostableItemBehavior.of(CompostChances.SMALL))
                .register();
        }

        public void registerBlockAttachedToSide(BlockItemId blockItem, ResourceKey<Block> otherBlock, Direction direction) {
            this.builderForBlockAttachedToSide(blockItem, otherBlock, direction)
                .register();
        }
    }
}
