package net.errorcraft.itematic.core.dispenser.behavior;

import net.errorcraft.itematic.core.registries.ItematicRegistries;
import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.Actions;
import net.errorcraft.itematic.world.action.actions.BrushArmadilloAtPositionAction;
import net.errorcraft.itematic.world.action.actions.ChargeRespawnAnchorAction;
import net.errorcraft.itematic.world.action.actions.DamageItemAction;
import net.errorcraft.itematic.world.action.actions.DecrementItemAction;
import net.errorcraft.itematic.world.action.actions.EquipEntityAtPositionAction;
import net.errorcraft.itematic.world.action.actions.EquipHorseWithChestAtPositionAction;
import net.errorcraft.itematic.world.action.actions.ExchangeItemAction;
import net.errorcraft.itematic.world.action.actions.InvokeItemEventAction;
import net.errorcraft.itematic.world.action.actions.PlaceBlockFromItemAction;
import net.errorcraft.itematic.world.action.actions.PlaceCarvedPumpkinAction;
import net.errorcraft.itematic.world.action.actions.PlaySoundAction;
import net.errorcraft.itematic.world.action.actions.ShearAtPositionAction;
import net.errorcraft.itematic.world.action.actions.ShootProjectileFromItemAction;
import net.errorcraft.itematic.world.action.actions.SpawnEntityAction;
import net.errorcraft.itematic.world.action.actions.SpawnEntityFromItemAction;
import net.errorcraft.itematic.world.action.actions.TakeHoneyAction;
import net.errorcraft.itematic.world.action.actions.UseBucketAction;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToSucceedSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.level.storage.loot.predicates.LocationCheckPredicates;
import net.minecraft.advancements.predicates.BlockPredicate;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypeIds;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;

public class DispenseBehaviors {
    public static final ResourceKey<DispenseBehavior> BRUSH = createKey("brush");
    public static final ResourceKey<DispenseBehavior> CHARGE_RESPAWN_ANCHOR = createKey("charge_respawn_anchor");
    public static final ResourceKey<DispenseBehavior> EQUIP_CHEST = createKey("equip_chest");
    public static final ResourceKey<DispenseBehavior> EQUIP_ENTITY = createKey("equip_entity");
    public static final ResourceKey<DispenseBehavior> EQUIP_ENTITY_HEAD = createKey("equip_entity_head");
    public static final ResourceKey<DispenseBehavior> GLASS_BOTTLE = createKey("glass_bottle");
    public static final ResourceKey<DispenseBehavior> PLACE_BLOCK_FROM_ITEM = createKey("place_block_from_item");
    public static final ResourceKey<DispenseBehavior> PLACE_CARVED_PUMPKIN = createKey("place_carved_pumpkin");
    public static final ResourceKey<DispenseBehavior> SHEAR = createKey("shear");
    public static final ResourceKey<DispenseBehavior> SHOOT_BOTTLE = createKey("shoot_bottle");
    public static final ResourceKey<DispenseBehavior> SHOOT_CHARGE = createKey("shoot_charge");
    public static final ResourceKey<DispenseBehavior> SHOOT_FIREWORK_ROCKET = createKey("shoot_firework_rocket");
    public static final ResourceKey<DispenseBehavior> SHOOT_PROJECTILE = createKey("shoot_projectile");
    public static final ResourceKey<DispenseBehavior> SPAWN_ENTITY_FROM_ITEM = createKey("spawn_entity_from_item");
    public static final ResourceKey<DispenseBehavior> SPAWN_TNT = createKey("spawn_tnt");
    public static final ResourceKey<DispenseBehavior> USE_BUCKET = createKey("use_bucket");
    public static final ResourceKey<DispenseBehavior> USE_ITEM_ON_BLOCK = createKey("use_item_on_block");
    public static final ResourceKey<DispenseBehavior> USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM = createKey("use_item_on_block_or_dispense_item");
    public static final ResourceKey<DispenseBehavior> WAX_BLOCK = createKey("wax_block");

    private DispenseBehaviors() {}

    private static ResourceKey<DispenseBehavior> createKey(String id) {
        return ResourceKey.create(ItematicRegistries.DISPENSE_BEHAVIOR, Identifier.withDefaultNamespace(id));
    }

    public static void bootstrap(BootstrapContext<DispenseBehavior> registerable) {
        HolderGetter<Block> blocks = registerable.lookup(Registries.BLOCK);
        HolderGetter<Item> items = registerable.lookup(Registries.ITEM);
        HolderGetter<SoundEvent> soundEvents = registerable.lookup(Registries.SOUND_EVENT);
        HolderGetter<EntityType<?>> entityTypes = registerable.lookup(Registries.ENTITY_TYPE);
        HolderGetter<ActionEntry> actions = registerable.lookup(ItematicRegistries.ACTION);

        registerable.register(
            BRUSH,
            DispenseBehavior
                .builder(
                    PassingSequenceHandler.builder()
                        .add(BrushArmadilloAtPositionAction.of(PositionTarget.INTERACTED))
                        .add(DamageItemAction.of(16))
                )
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            CHARGE_RESPAWN_ANCHOR,
            DispenseBehavior
                .builder(
                    ActionEntry.of(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(
                                    BlockPredicate.Builder.block()
                                        .of(
                                            blocks,
                                            blocks.getOrThrow(BlockItemIds.RESPAWN_ANCHOR.block()).value()
                                        )
                                )
                        ),
                        PassingSequenceHandler.builder()
                            .add(ChargeRespawnAnchorAction.of(PositionTarget.INTERACTED))
                            .add(DecrementItemAction.of(1))
                    )
                )
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            EQUIP_CHEST,
            DispenseBehavior.of(
                PassingSequenceHandler.builder()
                    .add(EquipHorseWithChestAtPositionAction.of(PositionTarget.INTERACTED))
                    .add(DecrementItemAction.of(1))
            )
        );
        registerable.register(
            EQUIP_ENTITY,
            DispenseBehavior.of(
                PassingSequenceHandler.builder()
                    .add(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED))
                    .add(DecrementItemAction.of(1))
            )
        );
        registerable.register(
            EQUIP_ENTITY_HEAD,
            DispenseBehavior
                .builder(
                    PassingSequenceHandler.builder()
                        .add(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED))
                        .add(DecrementItemAction.of(1))
                )
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            GLASS_BOTTLE,
            DispenseBehavior.of(
                FirstToPassRequirementsSequenceHandler.builder()
                    .add(
                        LocationCheckPredicates.builder(
                            PositionTarget.INTERACTED,
                            LocationPredicate.Builder.location()
                                .setBlock(
                                    BlockPredicate.Builder.block()
                                        .setProperties(
                                            StatePropertiesPredicate.Builder.properties()
                                                .hasProperty(BeehiveBlock.HONEY_LEVEL, BeehiveBlock.MAX_HONEY_LEVELS)
                                        )
                                )
                        ),
                        UncheckedSequenceHandler.builder()
                            .add(TakeHoneyAction.of(PositionTarget.INTERACTED))
                            .add(ExchangeItemAction.of(items.getOrThrow(ItemIds.HONEY_BOTTLE)))
                    )
                    .add(InvokeItemEventAction.of(ItemEvent.USE_ON_BLOCK))
            )
        );
        registerable.register(
            PLACE_BLOCK_FROM_ITEM,
            DispenseBehavior
                .builder(PlaceBlockFromItemAction.of(PositionTarget.INTERACTED, true))
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            PLACE_CARVED_PUMPKIN,
            DispenseBehavior
                .builder(
                    PassingSequenceHandler.builder()
                        .add(
                            FirstToSucceedSequenceHandler.builder()
                                .add(PlaceCarvedPumpkinAction.of(PositionTarget.INTERACTED))
                                .add(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED))
                        )
                        .add(DecrementItemAction.of(1))
                )
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            SHEAR,
            DispenseBehavior
                .builder(
                    PassingSequenceHandler.builder()
                        .add(ShearAtPositionAction.of(PositionTarget.INTERACTED))
                        .add(DamageItemAction.of(1))
                )
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            SHOOT_BOTTLE,
            DispenseBehavior
                .builder(shootProjectile(1.1f * 1.25f, 6.0f * 0.5f))
                .offset(DispenseBehavior.Offset.of(0.7d, 0.0d, 0.1d, 0.0d))
                .build()
        );
        registerable.register(
            SHOOT_CHARGE,
            DispenseBehavior.of(shootProjectile(1.0f, 20.0f / 3.0f))
        );
        registerable.register(
            SHOOT_FIREWORK_ROCKET,
            DispenseBehavior
                .builder(shootProjectile(1.0f, 0.5f))
                .offset(
                    DispenseBehavior.Offset.ofSide(
                        0.5d - EntityTypes.FIREWORK_ROCKET.getWidth() * 0.5d,
                        -EntityTypes.FIREWORK_ROCKET.getHeight() + 0.5d,
                        0.5d - EntityTypes.FIREWORK_ROCKET.getWidth() * 0.5d
                    )
                )
                .build()
        );
        registerable.register(
            SHOOT_PROJECTILE,
            DispenseBehavior
                .builder(shootProjectile(1.1f, 6.0f))
                .offset(DispenseBehavior.Offset.of(0.7d, 0.0d, 0.1d, 0.0d))
                .build()
        );
        registerable.register(
            SPAWN_ENTITY_FROM_ITEM,
            DispenseBehavior.of(
                PassingSequenceHandler.builder()
                    .add(SpawnEntityFromItemAction.of(PositionTarget.INTERACTED))
                    .add(DecrementItemAction.of(1))
            )
        );
        registerable.register(
            SPAWN_TNT,
            DispenseBehavior.of(
                PassingSequenceHandler.builder()
                    .add(
                        SpawnEntityAction.of(
                            entityTypes.getOrThrow(EntityTypeIds.TNT),
                            PositionTarget.INTERACTED
                        )
                    )
                    .add(DecrementItemAction.of(1))
                    .add(
                        PlaySoundAction.of(
                            PositionTarget.INTERACTED,
                            soundEvents.getOrThrow(SoundEventIds.TNT_PRIMED),
                            SoundSource.BLOCKS
                        )
                    )
            )
        );
        registerable.register(
            USE_BUCKET,
            DispenseBehavior.of(UseBucketAction.of(PositionTarget.INTERACTED))
        );
        registerable.register(
            USE_ITEM_ON_BLOCK,
            DispenseBehavior
                .builder(InvokeItemEventAction.of(ItemEvent.USE_ON_BLOCK))
                .doNotDispenseOnFailure()
                .build()
        );
        registerable.register(
            USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM,
            DispenseBehavior.of(InvokeItemEventAction.of(ItemEvent.USE_ON_BLOCK))
        );
        registerable.register(
            WAX_BLOCK,
            DispenseBehavior.of(
                PassingSequenceHandler.builder()
                    .add(actions.getOrThrow(Actions.WAX_BLOCK))
                    .add(DecrementItemAction.of(1))
            )
        );
    }
    
    private static PassingSequenceHandler.Builder shootProjectile(float power, float uncertainty) {
        return PassingSequenceHandler.builder()
            .add(ShootProjectileFromItemAction.of(PositionTarget.INTERACTED, power, uncertainty))
            .add(DecrementItemAction.of(1));
    }
}
