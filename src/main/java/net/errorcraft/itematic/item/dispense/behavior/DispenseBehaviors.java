package net.errorcraft.itematic.item.dispense.behavior;

import net.errorcraft.itematic.block.BlockKeys;
import net.errorcraft.itematic.entity.EntityTypeKeys;
import net.errorcraft.itematic.item.ItemKeys;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.loot.condition.LocationCheckLootConditionUtil;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.world.action.Action;
import net.errorcraft.itematic.world.action.ActionEntry;
import net.errorcraft.itematic.world.action.actions.*;
import net.errorcraft.itematic.world.action.context.PositionTarget;
import net.errorcraft.itematic.world.action.sequence.handler.SequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToPassRequirementsSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.FirstToSucceedSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.PassingSequenceHandler;
import net.errorcraft.itematic.world.action.sequence.handler.handlers.UncheckedSequenceHandler;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.Block;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class DispenseBehaviors {
    public static final DispenserBehavior FALLBACK = new ItemDispenserBehavior();

    public static final RegistryKey<DispenseBehavior> BRUSH = of("brush");
    public static final RegistryKey<DispenseBehavior> CHARGE_RESPAWN_ANCHOR = of("charge_respawn_anchor");
    public static final RegistryKey<DispenseBehavior> EQUIP_CHEST = of("equip_chest");
    public static final RegistryKey<DispenseBehavior> EQUIP_ENTITY = of("equip_entity");
    public static final RegistryKey<DispenseBehavior> EQUIP_ENTITY_HEAD = of("equip_entity_head");
    public static final RegistryKey<DispenseBehavior> GLASS_BOTTLE = of("glass_bottle");
    public static final RegistryKey<DispenseBehavior> PLACE_BLOCK_FROM_ITEM = of("place_block_from_item");
    public static final RegistryKey<DispenseBehavior> PLACE_CARVED_PUMPKIN = of("place_carved_pumpkin");
    public static final RegistryKey<DispenseBehavior> SADDLE = of("saddle");
    public static final RegistryKey<DispenseBehavior> SHEAR = of("shear");
    public static final RegistryKey<DispenseBehavior> SHOOT_BOTTLE = of("shoot_bottle");
    public static final RegistryKey<DispenseBehavior> SHOOT_CHARGE = of("shoot_charge");
    public static final RegistryKey<DispenseBehavior> SHOOT_FIREWORK_ROCKET = of("shoot_firework_rocket");
    public static final RegistryKey<DispenseBehavior> SHOOT_PROJECTILE = of("shoot_projectile");
    public static final RegistryKey<DispenseBehavior> SPAWN_ENTITY_FROM_ITEM = of("spawn_entity_from_item");
    public static final RegistryKey<DispenseBehavior> SPAWN_TNT = of("spawn_tnt");
    public static final RegistryKey<DispenseBehavior> USE_BUCKET = of("use_bucket");
    public static final RegistryKey<DispenseBehavior> USE_ITEM_ON_BLOCK = of("use_item_on_block");
    public static final RegistryKey<DispenseBehavior> USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM = of("use_item_on_block_or_dispense_item");
    public static final RegistryKey<DispenseBehavior> WAX_BLOCK = of("wax_block");

    private DispenseBehaviors() {}

    public static void bootstrap(Registerable<DispenseBehavior> registerable) {
        RegistryEntryLookup<Block> blocks = registerable.getRegistryLookup(RegistryKeys.BLOCK);
        RegistryEntryLookup<Item> items = registerable.getRegistryLookup(RegistryKeys.ITEM);
        RegistryEntryLookup<SoundEvent> soundEvents = registerable.getRegistryLookup(RegistryKeys.SOUND_EVENT);
        RegistryEntryLookup<EntityType<?>> entityTypes = registerable.getRegistryLookup(RegistryKeys.ENTITY_TYPE);

        registerable.register(BRUSH, DispenseBehavior.builder(
            PassingSequenceHandler.builder()
                .add(BrushArmadilloAtPositionAction.of(PositionTarget.INTERACTED))
                .add(DamageItemAction.of(16))
        ).doNotDispenseOnFailure().build());
        registerable.register(CHARGE_RESPAWN_ANCHOR, DispenseBehavior.builder(
            ActionEntry.of(
                LocationCheckLootConditionUtil.builder(
                    PositionTarget.INTERACTED,
                    LocationPredicate.Builder.create()
                        .block(BlockPredicate.Builder.create()
                            .blocks(blocks, blocks.getOrThrow(BlockKeys.RESPAWN_ANCHOR).value()))
                ),
                decrement(ChargeRespawnAnchorAction.of(PositionTarget.INTERACTED)))
        ).doNotDispenseOnFailure().build());
        registerable.register(EQUIP_CHEST, DispenseBehavior.builder(
            decrement(EquipHorseWithChestAtPositionAction.of(PositionTarget.INTERACTED))
        ).build());
        registerable.register(EQUIP_ENTITY, DispenseBehavior.builder(
            decrement(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED))
        ).build());
        registerable.register(EQUIP_ENTITY_HEAD, DispenseBehavior.builder(
            decrement(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED))
        ).doNotDispenseOnFailure().build());
        registerable.register(GLASS_BOTTLE, DispenseBehavior.builder(
            FirstToPassRequirementsSequenceHandler.builder()
                .add(
                    LocationCheckLootConditionUtil.builder(
                        PositionTarget.INTERACTED,
                        LocationPredicate.Builder.create()
                            .block(BlockPredicate.Builder.create()
                                .state(StatePredicate.Builder.create()
                                    .exactMatch(BeehiveBlock.HONEY_LEVEL, BeehiveBlock.FULL_HONEY_LEVEL)))
                    ),
                    UncheckedSequenceHandler.builder()
                        .add(TakeHoneyAction.of(PositionTarget.INTERACTED))
                        .add(ExchangeItemAction.of(items.getOrThrow(ItemKeys.HONEY_BOTTLE)))
                )
                .add(InvokeItemEventAction.of(ItemEvents.USE_ON_BLOCK))
        ).build());
        registerable.register(PLACE_BLOCK_FROM_ITEM, DispenseBehavior.builder(
            PlaceBlockFromItemAction.of(PositionTarget.INTERACTED, true)
        ).doNotDispenseOnFailure().build());
        registerable.register(PLACE_CARVED_PUMPKIN, DispenseBehavior.builder(
            decrement(FirstToSucceedSequenceHandler.builder()
                .add(PlaceCarvedPumpkinAction.of(PositionTarget.INTERACTED))
                .add(EquipEntityAtPositionAction.of(PositionTarget.INTERACTED)))
        ).doNotDispenseOnFailure().build());
        registerable.register(SHEAR, DispenseBehavior.builder(
            PassingSequenceHandler.builder()
                .add(ShearAtPositionAction.of(PositionTarget.INTERACTED))
                .add(DamageItemAction.of(1))
        ).doNotDispenseOnFailure().build());
        registerable.register(SHOOT_BOTTLE, DispenseBehavior.builder(
            shootProjectile(1.1f * 1.25f, 6.0f * 0.5f)
        ).offset(DispenseBehavior.Offset.of(0.7d, 0.0d, 0.1d, 0.0d)).build());
        registerable.register(SHOOT_CHARGE, DispenseBehavior.builder(
            shootProjectile(1.0f, 20.0f / 3.0f)
        ).build());
        registerable.register(SHOOT_FIREWORK_ROCKET, DispenseBehavior.builder(
            shootProjectile(1.0f, 0.5f)
        ).offset(DispenseBehavior.Offset.ofSide(
            0.5d - EntityType.FIREWORK_ROCKET.getWidth() * 0.5d,
            -EntityType.FIREWORK_ROCKET.getHeight() + 0.5d,
            0.5d - EntityType.FIREWORK_ROCKET.getWidth() * 0.5d
        )).build());
        registerable.register(SHOOT_PROJECTILE, DispenseBehavior.builder(
            shootProjectile(1.1f, 6.0f)
        ).offset(DispenseBehavior.Offset.of(0.7d, 0.0d, 0.1d, 0.0d)).build());
        registerable.register(SPAWN_ENTITY_FROM_ITEM, DispenseBehavior.builder(
            PassingSequenceHandler.builder()
                .add(SpawnEntityFromItemAction.of(PositionTarget.INTERACTED))
                .add(DecrementItemAction.of(1))
        ).build());
        registerable.register(SPAWN_TNT, DispenseBehavior.builder(
            PassingSequenceHandler.builder()
                .add(SpawnEntityAction.of(
                    entityTypes.getOrThrow(EntityTypeKeys.TNT),
                    PositionTarget.INTERACTED
                ))
                .add(DecrementItemAction.of(1))
                .add(PlaySoundAction.of(
                    PositionTarget.INTERACTED,
                    soundEvents.getOrThrow(SoundEventKeys.TNT_PRIMED),
                    SoundCategory.BLOCKS
                ))
        ).build());
        registerable.register(USE_BUCKET, DispenseBehavior.builder(
            UseBucketAction.of(PositionTarget.INTERACTED)
        ).build());
        registerable.register(USE_ITEM_ON_BLOCK, DispenseBehavior.builder(
            InvokeItemEventAction.of(ItemEvents.USE_ON_BLOCK)
        ).doNotDispenseOnFailure().build());
        registerable.register(USE_ITEM_ON_BLOCK_OR_DISPENSE_ITEM, DispenseBehavior.builder(
            InvokeItemEventAction.of(ItemEvents.USE_ON_BLOCK)
        ).build());
        registerable.register(WAX_BLOCK, DispenseBehavior.builder(
            decrement(WaxBlockAction.of(PositionTarget.INTERACTED))
        ).build());
    }

    private static RegistryKey<DispenseBehavior> of(String id) {
        return RegistryKey.of(ItematicRegistryKeys.DISPENSE_BEHAVIOR, Identifier.ofVanilla(id));
    }
    
    private static PassingSequenceHandler.Builder shootProjectile(float power, float uncertainty) {
        return decrement(ShootProjectileFromItemAction.of(PositionTarget.INTERACTED, power, uncertainty));
    }

    private static PassingSequenceHandler.Builder decrement(Action<?> action) {
        return PassingSequenceHandler.builder()
            .add(action)
            .add(DecrementItemAction.of(1));
    }

    private static PassingSequenceHandler.Builder decrement(SequenceHandler.Builder<?, ?> builder) {
        return decrement(SequenceAction.of(builder));
    }
}
