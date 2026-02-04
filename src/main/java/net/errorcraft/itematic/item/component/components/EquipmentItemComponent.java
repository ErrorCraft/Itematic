package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record EquipmentItemComponent(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound, Optional<Identifier> model, Optional<RegistryEntryList<EntityType<?>>> allowedEntities) implements ItemComponent<EquipmentItemComponent> {
    public static final Codec<EquipmentItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EquipmentSlot.CODEC.fieldOf("slot").forGetter(EquipmentItemComponent::slot),
        Codec.BOOL.optionalFieldOf("swappable", false).forGetter(EquipmentItemComponent::swappable),
        SoundEvent.ENTRY_CODEC.optionalFieldOf("equip_sound", SoundEvents.ITEM_ARMOR_EQUIP_GENERIC).forGetter(EquipmentItemComponent::equipSound),
        Identifier.CODEC.optionalFieldOf("model").forGetter(EquipmentItemComponent::model),
        RegistryCodecs.entryList(RegistryKeys.ENTITY_TYPE).optionalFieldOf("allowed_entities").forGetter(EquipmentItemComponent::allowedEntities)
    ).apply(instance, EquipmentItemComponent::new));

    public static EquipmentItemComponent of(ArmorMaterial material, EquipmentType type, @Nullable AnimalArmorItem.Type animalType) {
        return new EquipmentItemComponent(
            type.getEquipmentSlot(),
            true,
            material.equipSound(),
            Optional.of(material.modelId()),
            Optional.ofNullable(animalType).map(AnimalArmorItem.Type::itematic$allowedEntities)
        );
    }

    public static EquipmentItemComponent of(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound) {
        return new EquipmentItemComponent(
            slot,
            swappable,
            equipSound,
            Optional.empty(),
            Optional.empty()
        );
    }

    public static EquipmentItemComponent of(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound, Identifier model) {
        return new EquipmentItemComponent(
            slot,
            swappable,
            equipSound,
            Optional.of(model),
            Optional.empty()
        );
    }

    public static EquipmentItemComponent of(EquippableComponent equippable) {
        return new EquipmentItemComponent(
            equippable.slot(),
            true,
            equippable.equipSound(),
            equippable.model(),
            equippable.allowedEntities()
        );
    }

    public static EquipmentItemComponent ofStatic(EquipmentSlot slot) {
        return new EquipmentItemComponent(
            slot,
            false,
            SoundEvents.ITEM_ARMOR_EQUIP_GENERIC,
            Optional.empty(),
            Optional.empty()
        );
    }

    public static ItemComponent<?>[] skull(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            BlockItemComponent.attachedToSide(attachedBlock, otherBlock, Direction.DOWN),
            ofStatic(EquipmentSlot.HEAD),
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
        if (!this.swappable) {
            return ItemResult.PASS;
        }

        EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippable == null) {
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
        builder.add(DataComponentTypes.EQUIPPABLE, new EquippableComponent(this.slot, this.equipSound, this.model, this.allowedEntities, false));
    }
}
