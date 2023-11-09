package net.errorcraft.itematic.item.component.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.item.component.ItemComponent;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.item.component.ItemComponentTypes;
import net.errorcraft.itematic.registry.ItematicRegistryKeys;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;

public record DispensableItemComponent(RegistryEntry<DispenserBehavior> behavior) implements ItemComponent {
    public static final Codec<DispensableItemComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFixedCodec.of(ItematicRegistryKeys.DISPENSE_BEHAVIOR).fieldOf("behavior").forGetter(DispensableItemComponent::behavior)
    ).apply(instance, DispensableItemComponent::new));

    @Override
    public ItemComponentType<?> type() {
        return ItemComponentTypes.DISPENSABLE;
    }

    @Override
    public Codec<? extends ItemComponent> codec() {
        return CODEC;
    }
}
