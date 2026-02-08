package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record EquipmentItemComponent(EquippableComponent equippable) implements ItemComponent<EquipmentItemComponent> {
    public static final Codec<EquipmentItemComponent> CODEC = EquippableComponent.CODEC.xmap(EquipmentItemComponent::new, EquipmentItemComponent::equippable);

    public static EquipmentItemComponent of(ArmorMaterial material, EquipmentType type) {
        return new EquipmentItemComponent(EquippableComponent.builder(type.getEquipmentSlot())
            .swappable(true)
            .equipSound(material.equipSound())
            .model(material.modelId())
            .build());
    }

    public static EquipmentItemComponent of(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new EquipmentItemComponent(EquippableComponent.builder(type.getEquipmentSlot())
            .swappable(true)
            .equipSound(material.equipSound())
            .model(material.modelId())
            .allowedEntities(animalType.itematic$allowedEntities())
            .build());
    }

    public static EquipmentItemComponent of(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound) {
        return new EquipmentItemComponent(EquippableComponent.builder(slot)
            .swappable(swappable)
            .equipSound(equipSound)
            .build());
    }

    public static EquipmentItemComponent of(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound, Identifier model) {
        return new EquipmentItemComponent(EquippableComponent.builder(slot)
            .swappable(swappable)
            .equipSound(equipSound)
            .model(model)
            .build());
    }

    public static EquipmentItemComponent of(EquippableComponent equippable) {
        return new EquipmentItemComponent(equippable);
    }

    public static ItemComponent<?>[] skull(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            BlockItemComponent.attachedToSide(attachedBlock, otherBlock, Direction.DOWN),
            new EquipmentItemComponent(EquippableComponent.builder(EquipmentSlot.HEAD)
                .swappable(false)
                .build()),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY_HEAD)),
            FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.CREEPER)
        };
    }

    @Override
    public ItemComponentType<EquipmentItemComponent> type() {
        return ItemComponentTypes.EQUIPMENT;
    }

    @Override
    public Codec<EquipmentItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippable == null) {
            return ItemResult.PASS;
        }

        if (!equippable.swappable()) {
            return ItemResult.PASS;
        }

        ActionResult result = equippable.equip(stack, user);
        if (result == ActionResult.FAIL) {
            return ItemResult.PASS;
        }

        if (result instanceof ActionResult.Success success) {
            resultStackConsumer.set(success.getNewHandStack());
        }

        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            stack.itematic$invokeEvent(ItemEvents.EQUIP_ITEM, context);
        }

        return result.isAccepted() ? ItemResult.SUCCEED : ItemResult.PASS;
    }

    @Override
    public void addComponents(ComponentMap.Builder builder) {
        builder.add(DataComponentTypes.EQUIPPABLE, this.equippable);
    }
}
