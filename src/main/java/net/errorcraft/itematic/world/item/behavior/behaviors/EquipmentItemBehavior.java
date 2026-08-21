package net.errorcraft.itematic.world.item.behavior.behaviors;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehavior;
import net.errorcraft.itematic.core.dispenser.behavior.DispenseBehaviors;
import net.errorcraft.itematic.references.SoundEventIds;
import net.errorcraft.itematic.util.context.ItematicContextKeys;
import net.errorcraft.itematic.world.ItemResult;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.item.ItemEvent;
import net.errorcraft.itematic.world.item.behavior.ItemBehavior;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jspecify.annotations.Nullable;

public record EquipmentItemBehavior(Equippable equippable) implements ItemBehavior<EquipmentItemBehavior> {
    public static final Codec<EquipmentItemBehavior> CODEC = Equippable.CODEC.xmap(EquipmentItemBehavior::new, EquipmentItemBehavior::equippable);

    public static EquipmentItemBehavior of(Equippable equippable) {
        return new EquipmentItemBehavior(equippable);
    }

    public static EquipmentItemBehavior ofHorseArmor(ArmorMaterial material, HolderGetter<SoundEvent> soundEvents, HolderGetter<EntityType<?>> entityTypes) {
        return of(Equippable.builder(EquipmentSlot.BODY)
            .setEquipSound(soundEvents.getOrThrow(SoundEventIds.HORSE_ARMOR))
            .setAsset(material.assetId())
            .setAllowedEntities(entityTypes.getOrThrow(EntityTypeTags.CAN_WEAR_HORSE_ARMOR))
            .setDamageOnHurt(false)
            .setCanBeSheared(true)
            .setShearingSound(soundEvents.getOrThrow(SoundEventIds.HORSE_ARMOR_UNEQUIP))
            .build()
        );
    }

    public static ItemBehavior<?>[] ofHarness(DyeColor color, HolderGetter<SoundEvent> soundEvents, HolderGetter<EntityType<?>> entityTypes, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            of(Equippable.builder(EquipmentSlot.BODY)
                .setEquipSound(soundEvents.getOrThrow(SoundEventIds.HAPPY_GHAST_EQUIP))
                .setAsset(EquipmentAssets.HARNESSES.get(color))
                .setAllowedEntities(entityTypes.getOrThrow(EntityTypeTags.CAN_EQUIP_HARNESS))
                .setEquipOnInteract(true)
                .setCanBeSheared(true)
                .setShearingSound(soundEvents.getOrThrow(SoundEventIds.HAPPY_GHAST_UNEQUIP))
                .build()),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY))
        };
    }

    public static ItemBehavior<?>[] ofNautilusArmor(ArmorMaterial material, HolderGetter<SoundEvent> soundEvents, HolderGetter<EntityType<?>> entityTypes, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            of(Equippable.builder(EquipmentSlot.BODY)
                .setEquipSound(soundEvents.getOrThrow(SoundEventIds.ARMOR_EQUIP_NAUTILUS))
                .setAsset(material.assetId())
                .setAllowedEntities(entityTypes.getOrThrow(EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR))
                .setDamageOnHurt(false)
                .setEquipOnInteract(true)
                .setCanBeSheared(true)
                .setShearingSound(soundEvents.getOrThrow(SoundEventIds.ARMOR_UNEQUIP_NAUTILUS))
                .build()),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY))
        };
    }

    public static ItemBehavior<?>[] forArmor(ArmorMaterial material, ArmorType type) {
        return new ItemBehavior<?>[] {
            StackableItemBehavior.of(1),
            of(Equippable.builder(type.getSlot())
                .setSwappable(true)
                .setEquipSound(material.equipSound())
                .setAsset(material.assetId())
                .build()),
            DamageableItemBehavior.of(type.getDurability(material.durability())),
        };
    }

    public static ItemBehavior<?>[] forSkull(Holder<Block> attachedBlock, Holder<Block> otherBlock, HolderGetter<DispenseBehavior> dispenseBehaviors) {
        return new ItemBehavior<?>[] {
            BlockItemBehavior.attachedToSide(attachedBlock, otherBlock, Direction.DOWN),
            of(Equippable.builder(EquipmentSlot.HEAD)
                .setSwappable(false)
                .build()),
            DispensableItemBehavior.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY_HEAD)),
            FireworkShapeModifierItemBehavior.of(FireworkExplosion.Shape.CREEPER)
        };
    }

    @Override
    public ItemBehaviorType<EquipmentItemBehavior> type() {
        return ItemBehaviorType.EQUIPMENT;
    }

    @Override
    public ItemResult use(Level level, Player user, InteractionHand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null) {
            return ItemResult.PASS;
        }

        if (!equippable.swappable()) {
            return ItemResult.PASS;
        }

        InteractionResult result = equippable.swapWithEquipmentSlot(stack, user);
        if (result == InteractionResult.FAIL) {
            return ItemResult.PASS;
        }

        if (result instanceof InteractionResult.Success success) {
            this.tryExchangeResultStack(stackExchanger, success.heldItemTransformedTo());
        }

        if (level instanceof ServerLevel serverLevel) {
            ActionContext context = ActionContext.builder(serverLevel)
                .stackExchanger(stackExchanger)
                .add(LootContextParams.THIS_ENTITY, user)
                .add(LootContextParams.ORIGIN, user.position())
                .add(LootContextParams.TOOL, stack)
                .add(ItematicContextKeys.HAND, hand)
                .build();
            stack.itematic$invokeEvent(ItemEvent.EQUIP_ITEM, context);
        }

        return result.consumesAction() ? ItemResult.SUCCEED : ItemResult.PASS;
    }

    @Override
    public void addComponents(DataComponentMap.Builder builder) {
        builder.set(DataComponents.EQUIPPABLE, this.equippable);
    }

    private void tryExchangeResultStack(ItemStackExchanger stackExchanger, @Nullable ItemStack stack) {
        if (stack != null) {
            stackExchanger.exchange(stack);
        }
    }
}
