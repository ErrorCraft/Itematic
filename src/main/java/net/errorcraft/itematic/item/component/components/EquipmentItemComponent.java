package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.sound.SoundEventKeys;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Equipment;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
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
    public ItemComponentType<?> getType() {
        return ItemComponentTypes.EQUIPMENT;
    }

    @Override
    public Codec<? extends ItemComponent> getCodec() {
        return CODEC;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (!this.swappable) {
            return TypedActionResult.pass(stack);
        }
        ItemStack currentStack = user.getEquippedStack(this.slot);
        if (EnchantmentHelper.hasBindingCurse(currentStack) || ItemStack.areEqual(stack, currentStack)) {
            return TypedActionResult.pass(stack);
        }
        user.equipStack(this.slot, stack.copy());
        if (!world.isClient()) {
            user.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
        }
        if (currentStack.isEmpty()) {
            stack.setCount(0);
        } else {
            user.setStackInHand(hand, currentStack.copy());
        }
        return TypedActionResult.success(stack, world.isClient());
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
            new BlockItemComponent(block),
            new EquipmentItemComponent(EquipmentSlot.HEAD, false, soundEvents.getOrThrow(SoundEventKeys.ITEM_ARMOR_EQUIP_GENERIC)),
            new FireworkShapeModifierItemComponent(FireworkRocketItem.Type.CREEPER)
        };
    }
}
