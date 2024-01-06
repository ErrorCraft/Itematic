package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.ItemStackConsumer;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.item.event.ItemEvents;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.errorcraft.itematic.world.action.context.ActionContext;
import net.errorcraft.itematic.world.action.context.MutableActionContext;
import net.errorcraft.itematic.world.action.context.parameter.ActionContextParameter;
import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public record EquipmentItemComponent(EquipmentSlot slot, boolean swappable, RegistryEntry<SoundEvent> equipSound) implements ItemComponent, Equipment {
    public static final Codec<EquipmentItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        StringIdentifiable.createCodec(EquipmentSlot::values).fieldOf("slot").forGetter(EquipmentItemComponent::slot),
        Codec.BOOL.optionalFieldOf("swappable", false).forGetter(EquipmentItemComponent::swappable),
        SoundEvent.ENTRY_CODEC.fieldOf("equip_sound").forGetter(EquipmentItemComponent::equipSound)
    ).apply(instance, EquipmentItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.EQUIPMENT;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
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
            ActionContext context = MutableActionContext.stackUsage(serverWorld, stack, resultStackConsumer, hand)
                .entityPosition(ActionContextParameter.THIS, user);
            stack.itematic$invokeEvent(ItemEvents.EQUIP_ITEM, context);
        }
        if (result.getResult() == ActionResult.FAIL) {
            return ActionResult.PASS;
        }
        return result.getResult();
    }

    @Override
    public EquipmentSlot getSlotType() {
        return this.slot;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound.value();
    }

    public static ItemComponent[] skull(RegistryEntry<Block> block, RegistryEntryLookup<SoundEvent> soundEvents) {
        return new ItemComponent[] {
            BlockItemComponent.of(block),
            new EquipmentItemComponent(EquipmentSlot.HEAD, false, soundEvents.getOrThrow(SoundEventKeys.ARMOR_EQUIP_GENERIC)),
            new FireworkShapeModifierItemComponent(FireworkRocketItem.Type.CREEPER)
        };
    }
}
