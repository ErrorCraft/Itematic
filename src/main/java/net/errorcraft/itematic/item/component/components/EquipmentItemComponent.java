package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehavior;
import net.errorcraft.itematic.item.dispense.behavior.DispenseBehaviors;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record EquipmentItemComponent(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound) implements ItemComponent<EquipmentItemComponent>, Equipment {
    public static final Codec<EquipmentItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        EquipmentSlot.CODEC.fieldOf("slot").forGetter(EquipmentItemComponent::slot),
        Codec.BOOL.optionalFieldOf("swappable", false).forGetter(EquipmentItemComponent::swappable),
        SoundEvent.ENTRY_CODEC.fieldOf("equip_sound").forGetter(EquipmentItemComponent::equipSound)
    ).apply(instance, EquipmentItemComponent::new));

    @Override
    public ItemComponentType<EquipmentItemComponent> type() {
        return ItemComponentTypes.EQUIPMENT;
    }

    @Override
    public Codec<EquipmentItemComponent> codec() {
        return CODEC;
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand, ItemStack stack, ItemStackConsumer resultStackConsumer) {
        if (!this.swappable) {
            return ActionResult.PASS;
        }
        TypedActionResult<ItemStack> result = this.equipAndSwap(stack.getItem(), world, user, hand);
        resultStackConsumer.set(result.getValue());
        if (world instanceof ServerWorld serverWorld) {
            ActionContext context = ActionContext.builder(serverWorld, stack, resultStackConsumer, hand)
                .entityPosition(ActionContextParameter.THIS, user)
                .build();
            stack.itematic$invokeEvent(ItemEvents.EQUIP_ITEM, context);
        }
        if (result.getResult() == ActionResult.FAIL) {
            return ActionResult.PASS;
        }
        if (result.getResult().isAccepted() && !world.isClient()) {
            user.incrementStat(Stats.USED.itematic$getOrCreateStat(stack.getRegistryEntry()));
        }
        return result.getResult();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.slot;
    }

    @Override
    public RegistryEntry<SoundEvent> getEquipSound() {
        return this.equipSound;
    }

    public static EquipmentItemComponent of(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound) {
        return new EquipmentItemComponent(slot, swappable, equipSound);
    }

    public static ItemComponent<?>[] skull(RegistryEntry<Block> attachedBlock, RegistryEntry<Block> otherBlock, RegistryEntryLookup<SoundEvent> soundEvents, RegistryEntryLookup<DispenseBehavior> dispenseBehaviors) {
        return new ItemComponent<?>[] {
            RarityItemComponent.of(Rarity.UNCOMMON),
            BlockItemComponent.attachedToSide(attachedBlock, otherBlock, Direction.DOWN),
            of(EquipmentSlot.HEAD, false, soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC)),
            DispensableItemComponent.of(dispenseBehaviors.getOrThrow(DispenseBehaviors.EQUIP_ENTITY_HEAD)),
            FireworkShapeModifierItemComponent.of(FireworkExplosionComponent.Type.CREEPER)
        };
    }
}
