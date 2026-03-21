package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import net.errorcraft.itematic.item.ItemResult;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.util.context.ItematicContextParameters;
import net.errorcraft.itematic.world.action.context.ItemStackExchanger;
import net.errorcraft.itematic.world.action.context.NewActionContext;
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
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record EquipmentItemComponent(EquippableComponent equippable) implements ItemComponent<EquipmentItemComponent> {
    public static final Codec<EquipmentItemComponent> CODEC = EquippableComponent.CODEC.xmap(EquipmentItemComponent::new, EquipmentItemComponent::equippable);

    public static EquipmentItemComponent of(EquippableComponent equippable) {
        return new EquipmentItemComponent(equippable);
    }

    private static EquipmentItemComponent of(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new EquipmentItemComponent(EquippableComponent.builder(type.getEquipmentSlot())
            .swappable(true)
            .equipSound(material.equipSound())
            .model(material.assetId())
            .allowedEntities(animalType.itematic$allowedEntities())
            .build());
    }

    public static ItemComponent<?>[] from(ArmorMaterial material, EquipmentType type) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            of(EquippableComponent.builder(type.getEquipmentSlot())
                .swappable(true)
                .equipSound(material.equipSound())
                .model(material.assetId())
                .build()),
            DamageableItemComponent.of(type.getMaxDamage(material.durability())),
        };
    }

    public static ItemComponent<?>[] from(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            of(material, type, animalType)
        };
    }

    public static ItemComponent<?>[] fromDamageable(ArmorMaterial material, EquipmentType type, AnimalArmorItem.Type animalType) {
        return new ItemComponent<?>[] {
            StackableItemComponent.of(1),
            DamageableItemComponent.of(type.getMaxDamage(material.durability()), animalType.itematic$breakSound()),
            of(material, type, animalType)
        };
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
    public ItemResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackExchanger stackExchanger) {
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
            stackExchanger.exchange(success.getNewHandStack());
        }

        if (world instanceof ServerWorld serverWorld) {
            NewActionContext context = NewActionContext.builder(serverWorld)
                .stackExchanger(stackExchanger)
                .add(LootContextParameters.THIS_ENTITY, user)
                .add(LootContextParameters.ORIGIN, user.getPos())
                .add(LootContextParameters.TOOL, stack)
                .add(ItematicContextParameters.HAND, hand)
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
